package com.example.runway.service;

import com.example.runway.domain.User;
import com.example.runway.dto.user.UserReq;
import com.example.runway.dto.user.UserRes;
import com.example.runway.exception.BadRequestException;
import com.example.runway.exception.BaseException;
import com.example.runway.exception.ForbiddenException;
import com.example.runway.repository.UserRepository;
import com.google.gson.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.*;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.*;

import static com.example.runway.constants.CommonResponseStatus.*;
import static com.example.runway.constants.Constants.apple;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final LoginService loginService;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;

    @Value("${kakao.rest.api.key}")
    private String kakaoRestApiKey;

    @Value("${jwt.refresh-token-seconds}")
    private long refreshTime;

    @Override
    public String getKakaoAccessToken(String code) {
        String access_Token = "";
        String refresh_Token = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //POST 요청을 위해 기본값이 false인 setDoOutput을 true로
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            //POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id="+kakaoRestApiKey); // TODO REST_API_KEY 입력
            sb.append("&redirect_uri=http://localhost:9000/login/kakao"); // TODO 인가코드 받은 redirect_uri 입력
            sb.append("&code=" + code);
            bw.write(sb.toString());
            bw.flush();

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);
            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            //Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            access_Token = element.getAsJsonObject().get("access_token").getAsString();
            refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();

            System.out.println("access_token : " + access_Token);
            System.out.println("refresh_token : " + refresh_Token);

            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return access_Token;
    }

    @Override
    public UserRes.Token logInKakaoUser(UserReq.SocialLogin SocialLogin) throws ForbiddenException {
        String reqURL = "https://kapi.kakao.com/v2/user/me";

        //access_token을 이용하여 사용자 정보 조회
        //access_token을 이용하여 사용자 정보 조회
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + SocialLogin.getAccessToken()); //전송할 header 작성, access_token전송

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            //Gson 라이브러리로 JSON파싱
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            Long id = element.getAsJsonObject().get("id").getAsLong();


            String name = element.getAsJsonObject().get("properties").getAsJsonObject().get("nickname").getAsString();


            String profileImgUrl=element.getAsJsonObject().get("properties").getAsJsonObject().get("profile_image").getAsString();

            HashMap<String,String> kakao = new HashMap<String,String>();
            kakao.put("kakaoId",id.toString());
            kakao.put("profileImgUrl",profileImgUrl);

            //회원 존재 X -> 예외처리(카카오 id, 프로필 이미지)
            User user=userRepository.findByUsernameAndSocialAndStatus(String.valueOf(id), "KAKAO",true).orElseThrow(() ->
                    new BaseException(USER_NOT_FOUND, kakao));

            br.close();
            Long userId = user.getId();

            UserRes.GenerateToken generateToken = loginService.createToken(userId);


            return new UserRes.Token(userId, generateToken.getAccessToken(), generateToken.getRefreshToken());


        } catch (IOException e) {
            throw new ForbiddenException(KAKAO_SERVER_ERROR);
        }
    }




    //애플 공개키
    public PublicKey getPublicKey(JsonObject object) {
        String nStr = object.get("n").toString();
        String eStr = object.get("e").toString();

        byte[] nBytes = Base64.getUrlDecoder().decode(nStr.substring(1, nStr.length() - 1));
        byte[] eBytes = Base64.getUrlDecoder().decode(eStr.substring(1, eStr.length() - 1));

        BigInteger n = new BigInteger(1, nBytes);
        BigInteger e = new BigInteger(1, eBytes);

        try {
            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(publicKeySpec);
        } catch (Exception exception) {
            throw new BadRequestException(FAIL_TO_MAKE_APPLE_PUBLIC_KEY);
        }
    }

    public UserRes.AppleLogin appleLogin(UserReq.SocialLogin SocialLogin) {

        String token = SocialLogin.getAccessToken();
        String appleReqUrl = "https://appleid.apple.com/auth/keys";
        StringBuffer result = new StringBuffer();

        String appleId;

        // 애플 api로 토큰 검증용 공개키 요청
        try {
            URL url = new URL(appleReqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("charset", "utf-8");

            int responseCode = conn.getResponseCode();
            if (responseCode != HttpStatus.OK.value()) throw new ForbiddenException(APPLE_SERVER_ERROR);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";

            while ((line = br.readLine()) != null) {
                result.append(line);
            }

        } catch (Exception e) {
            throw new ForbiddenException(APPLE_SERVER_ERROR);
        }

        //Gson 라이브러리로 JSON파싱
        // 통신 결과에서 공개키 목록 가져오기
        JsonParser parser = new JsonParser();
        JsonObject keys = (JsonObject) parser.parse(result.toString());
        JsonArray publicKeys = (JsonArray) keys.get("keys");

        try {
            // 클라이언트로부터 입력받은 jwt(idToken)에서 공개키와 비교할 항목 추출하기
            String headerOfIdentityToken = token.substring(0, token.indexOf("."));
            String header = new String(Base64.getDecoder().decode(headerOfIdentityToken), "UTF-8");

            JsonObject parsedHeader = (new Gson()).fromJson(header, JsonObject.class);
            JsonElement kid = parsedHeader.get("kid");
            JsonElement alg = parsedHeader.get("alg");

            // 애플의 공개키 3개중 클라이언트 토큰과 kid, alg 일치하는 것 찾기
            JsonObject avaliableObject = null;
            for (int i = 0; i < publicKeys.size(); i++) {
                JsonObject appleObject = (JsonObject) publicKeys.get(i);
                JsonElement appleKid = appleObject.get("kid");
                JsonElement appleAlg = appleObject.get("alg");
                System.out.println(kid);
                System.out.println(alg);

                if (Objects.equals(appleKid, kid) && Objects.equals(appleAlg, alg)) {
                    avaliableObject = appleObject;
                    break;
                }
            }

            //일치하는 공개키 없음
            if (ObjectUtils.isEmpty(avaliableObject))
                throw new BadRequestException(APPLE_BAD_REQUEST);

            PublicKey publicKey = this.getPublicKey(avaliableObject);
            System.out.println(getPublicKey(avaliableObject));

            // 일치하는 키를 이용해 정보 확인 후, 사용자 정보 가져오기
            Claims userInfo = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token).getBody();
            JsonObject userInfoObject = (JsonObject) parser.parse(new Gson().toJson(userInfo));

            if (!Objects.equals(userInfoObject.get("iss").getAsString(), "https://appleid.apple.com"))
                throw new BadRequestException(APPLE_BAD_REQUEST);

            if (!Objects.equals(userInfoObject.get("aud").getAsString(), "com.fashionweek.Runway-iOS"))
                throw new BadRequestException(APPLE_BAD_REQUEST);

            appleId  = userInfoObject.get("sub").getAsString();


        } catch (Exception e) {
            throw new BadRequestException(APPLE_BAD_REQUEST);
        }


        if(!userRepository.existsByUsernameAndSocialAndStatus(appleId, "APPLE", true)){
            return new UserRes.AppleLogin(false,appleId,null,null,null);
        }

        Optional<User> user = userRepository.findByUsernameAndSocialAndStatus(appleId,apple,true);
        // 가입된 유저 확인 시 jwt, refreshToken 반환
        Long userId=user.get().getId();
        UserRes.GenerateToken generateToken = loginService.createToken(userId);


        return new UserRes.AppleLogin(true,null,userId, generateToken.getAccessToken(), generateToken.getRefreshToken());


    }


}

