package com.example.runway.service.user;

import com.example.runway.constants.Constants;
import com.example.runway.convertor.UserConvertor;
import com.example.runway.domain.Social;
import com.example.runway.domain.User;
import com.example.runway.dto.user.AppleAuthTokenResponse;
import com.example.runway.dto.user.UserReq;
import com.example.runway.dto.user.UserRes;
import com.example.runway.exception.BadRequestException;
import com.example.runway.exception.BaseException;
import com.example.runway.exception.ForbiddenException;
import com.example.runway.repository.SocialRepository;
import com.example.runway.repository.UserRepository;
import com.google.gson.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static com.example.runway.constants.CommonResponseStatus.*;
import static com.example.runway.constants.Constants.apple;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final LoginService loginService;
    private final SocialRepository socialRepository;

    @Value("${kakao.rest.api.key}")
    private String kakaoRestApiKey;

    @Value("${apple.key.path}")
    private String appleSignKeyFilePath;

    @Value("${apple.sign.key}")
    private String appleSignKeyId;

    @Value("${apple.bundle.id}")
    private String appleBundleId;

    @Value("${apple.team.id}")
    private String appleTeamId;

    @Override
    public String getKakaoAccessToken(String code) {
        String access_Token="";
        String refresh_Token;
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //POST ????????? ?????? ???????????? false??? setDoOutput??? true???
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            //POST ????????? ????????? ???????????? ???????????? ???????????? ?????? ??????
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            String sb = "grant_type=authorization_code" +
                    "&client_id=" + kakaoRestApiKey + // TODO REST_API_KEY ??????
                    "&redirect_uri=http://localhost:9000/login/kakao" + // TODO ???????????? ?????? redirect_uri ??????
                    "&code=" + code;
            bw.write(sb);
            bw.flush();

            //?????? ????????? 200????????? ??????
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);
            //????????? ?????? ?????? JSON????????? Response ????????? ????????????
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuilder result = new StringBuilder();

            while ((line = br.readLine()) != null) {
                result.append(line);
            }
            System.out.println("response body : " + result);

            //Gson ?????????????????? ????????? ???????????? JSON?????? ?????? ??????
            JsonParser parser = new JsonParser();
            JsonElement element = JsonParser.parseString(result.toString());

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

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + SocialLogin.getAccessToken()); //????????? header ??????, access_token??????

            //?????? ????????? 200????????? ??????
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            //????????? ?????? ?????? JSON????????? Response ????????? ????????????
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line ;
            StringBuilder result = new StringBuilder();

            while ((line = br.readLine()) != null) {
                result.append(line);
            }
            System.out.println("response body : " + result);

            //Gson ?????????????????? JSON??????
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result.toString());

            Long id = element.getAsJsonObject().get("id").getAsLong();


            String name = element.getAsJsonObject().get("properties").getAsJsonObject().get("nickname").getAsString();


            String profileImgUrl=element.getAsJsonObject().get("properties").getAsJsonObject().get("profile_image").getAsString();

            HashMap<String,String> kakao = new HashMap<String,String>();
            kakao.put("kakaoId",id.toString());
            kakao.put("profileImgUrl",profileImgUrl);

            Optional<Social> social = socialRepository.findBySocialIdAndType(id.toString(), Constants.kakao);


            if(social.isPresent()){
                Optional<User> user = userRepository.findById(social.get().getId());

                checkUnactivatedUser(user);


                UserRes.GenerateToken generateToken = loginService.createToken(social.get().getUserId());
                return new UserRes.Token(social.get().getUserId(), generateToken.getAccessToken(), generateToken.getRefreshToken());
            }

            //?????? ?????? X -> ????????????(????????? id, ????????? ?????????)
            User user=userRepository.findByUsernameAndSocial(String.valueOf(id), "KAKAO").orElseThrow(() ->
                    new BaseException(USER_NOT_FOUND, kakao));

            br.close();
            Long userId = user.getId();
            checkUnactivatedUser(Optional.of(user));


            UserRes.GenerateToken generateToken = loginService.createToken(userId);


            return new UserRes.Token(userId, generateToken.getAccessToken(), generateToken.getRefreshToken());


        } catch (IOException e) {
            throw new ForbiddenException(KAKAO_SERVER_ERROR);
        }
    }

    private void checkUnactivatedUser(Optional<User> user) {
        if(!user.get().isStatus()){
            loginService.activeUser(user);
            loginService.activeReview(user);
        };
    }


    //?????? ?????????
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

        // ?????? api??? ?????? ????????? ????????? ??????
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

        //Gson ?????????????????? JSON??????
        // ?????? ???????????? ????????? ?????? ????????????
        JsonParser parser = new JsonParser();
        JsonObject keys = (JsonObject) parser.parse(result.toString());
        JsonArray publicKeys = (JsonArray) keys.get("keys");

        try {
            // ???????????????????????? ???????????? jwt(idToken)?????? ???????????? ????????? ?????? ????????????
            String headerOfIdentityToken = token.substring(0, token.indexOf("."));
            String header = new String(Base64.getDecoder().decode(headerOfIdentityToken), "UTF-8");

            JsonObject parsedHeader = (new Gson()).fromJson(header, JsonObject.class);
            JsonElement kid = parsedHeader.get("kid");
            JsonElement alg = parsedHeader.get("alg");

            // ????????? ????????? 3?????? ??????????????? ????????? kid, alg ???????????? ??? ??????
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

            //???????????? ????????? ??????
            if (ObjectUtils.isEmpty(avaliableObject))
                throw new BadRequestException(APPLE_BAD_REQUEST);

            PublicKey publicKey = this.getPublicKey(avaliableObject);
            System.out.println(getPublicKey(avaliableObject));

            // ???????????? ?????? ????????? ?????? ?????? ???, ????????? ?????? ????????????
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

        Optional<Social> social = socialRepository.findBySocialIdAndType(appleId,apple);


        if(social.isPresent()){
            Optional<User> user = userRepository.findById(social.get().getId());

            checkUnactivatedUser(user);
            UserRes.GenerateToken generateToken = loginService.createToken(social.get().getUserId());
            return new UserRes.AppleLogin(true,null,social.get().getUserId(), generateToken.getAccessToken(), generateToken.getRefreshToken());
        }
        if(!userRepository.existsByUsernameAndSocial(appleId, "APPLE")){
            return new UserRes.AppleLogin(false,appleId,null,null,null);
        }


        Optional<User> user = userRepository.findByUsernameAndSocial(appleId,apple);

        checkUnactivatedUser(user);

        Long userId=user.get().getId();

        UserRes.GenerateToken generateToken = loginService.createToken(userId);


        return new UserRes.AppleLogin(true,null,userId, generateToken.getAccessToken(), generateToken.getRefreshToken());


    }

    @Override
    public void syncKakaoUser(Long userId, String accessToken) {
        String reqURL = "https://kapi.kakao.com/v2/user/me";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + accessToken); //????????? header ??????, access_token??????

            //?????? ????????? 200????????? ??????
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            //????????? ?????? ?????? JSON????????? Response ????????? ????????????
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }

            //Gson ?????????????????? JSON??????
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            Long kakaoId = element.getAsJsonObject().get("id").getAsLong();

            if(socialRepository.existsBySocialIdAndUserIdNot(kakaoId.toString(),userId)) throw new BadRequestException(EXIST_DIFF_USER_SOCIAL);
            if(userRepository.existsByUsernameAndSocial(kakaoId.toString(),Constants.kakao)) throw new BadRequestException(EXIST_USER_SOCIAL);
            if(socialRepository.existsByUserIdAndType(userId,Constants.kakao))throw new BadRequestException(EXIST_SYNC_SOCIAL);

            Social social = UserConvertor.SyncSocial(String.valueOf(kakaoId),userId,Constants.kakao);


            socialRepository.save(social);

            br.close();


        } catch (IOException e) {
            throw new ForbiddenException(KAKAO_SERVER_ERROR);
        }
    }

    @Override
    public void syncAppleUser(Long userId, String accessToken) {
        String appleReqUrl = "https://appleid.apple.com/auth/keys";
        StringBuffer result = new StringBuffer();

        String appleId;

        // ?????? api??? ?????? ????????? ????????? ??????
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

        //Gson ?????????????????? JSON??????
        // ?????? ???????????? ????????? ?????? ????????????
        JsonParser parser = new JsonParser();
        JsonObject keys = (JsonObject) parser.parse(result.toString());
        JsonArray publicKeys = (JsonArray) keys.get("keys");

        try {
            // ???????????????????????? ???????????? jwt(idToken)?????? ???????????? ????????? ?????? ????????????
            String headerOfIdentityToken = accessToken.substring(0, accessToken.indexOf("."));
            String header = new String(Base64.getDecoder().decode(headerOfIdentityToken), "UTF-8");

            JsonObject parsedHeader = (new Gson()).fromJson(header, JsonObject.class);
            JsonElement kid = parsedHeader.get("kid");
            JsonElement alg = parsedHeader.get("alg");

            // ????????? ????????? 3?????? ??????????????? ????????? kid, alg ???????????? ??? ??????
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

            //???????????? ????????? ??????
            if (ObjectUtils.isEmpty(avaliableObject))
                throw new BadRequestException(APPLE_BAD_REQUEST);

            PublicKey publicKey = this.getPublicKey(avaliableObject);
            System.out.println(getPublicKey(avaliableObject));

            // ???????????? ?????? ????????? ?????? ?????? ???, ????????? ?????? ????????????
            Claims userInfo = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(accessToken).getBody();
            JsonObject userInfoObject = (JsonObject) parser.parse(new Gson().toJson(userInfo));

            if (!Objects.equals(userInfoObject.get("iss").getAsString(), "https://appleid.apple.com"))
                throw new BadRequestException(APPLE_BAD_REQUEST);

            if (!Objects.equals(userInfoObject.get("aud").getAsString(), "com.fashionweek.Runway-iOS"))
                throw new BadRequestException(APPLE_BAD_REQUEST);

            appleId  = userInfoObject.get("sub").getAsString();


        } catch (Exception e) {
            throw new BadRequestException(APPLE_BAD_REQUEST);
        }


        if(socialRepository.existsBySocialIdAndUserIdNot(appleId,userId)) throw new BadRequestException(EXIST_DIFF_USER_SOCIAL);
        if(userRepository.existsByUsernameAndSocial(appleId,Constants.apple)) throw new BadRequestException(EXIST_USER_SOCIAL);
        if(socialRepository.existsByUserIdAndType(userId,Constants.apple))throw new BadRequestException(EXIST_SYNC_SOCIAL);

        Social social = UserConvertor.SyncSocial(appleId,userId,Constants.kakao);


        socialRepository.save(social);



    }

    @Override
    public void unSyncSocial(Long userId, String social) {
        socialRepository.deleteByUserIdAndType(userId,social);
    }

    public void revoke(String code) throws IOException {

        String appleAuthToken = GenerateAuthToken(code);

        if (appleAuthToken != null) {
            System.out.println(appleAuthToken);
            RestTemplate restTemplate = new RestTemplateBuilder().build();
            String revokeUrl = "https://appleid.apple.com/auth/revoke";

            LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("client_id", appleBundleId);
            params.add("client_secret", createClientSecret());
            params.add("token", appleAuthToken);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

            restTemplate.postForEntity(revokeUrl, httpEntity, String.class);
        }

    }

    public String GenerateAuthToken(String code) throws IOException {
        RestTemplate restTemplate = new RestTemplateBuilder().build();
        String authUrl = "https://appleid.apple.com/auth/token";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", appleBundleId);
        params.add("client_secret", createClientSecret());
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<AppleAuthTokenResponse> response = restTemplate.postForEntity(authUrl, httpEntity, AppleAuthTokenResponse.class);
            System.out.println("????????????:"+response.getStatusCode());
            System.out.println("??????:"+response.getBody().getAccess_token());
            return response.getBody().getAccess_token();
        } catch (HttpClientErrorException e) {
            System.out.println(e.getCause());
            throw new IllegalArgumentException("Apple Auth Token Error");
        }
    }

    private String createClientSecret() throws IOException {
        Date expirationDate = Date.from(LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant());
        Map<String, Object> jwtHeader = new HashMap<>();
        jwtHeader.put("kid", appleSignKeyId);
        jwtHeader.put("alg", "ES256");

        System.out.println(Jwts.builder()
                .setHeaderParams(jwtHeader)
                .setIssuer(appleTeamId)
                .setIssuedAt(new Date(System.currentTimeMillis())) // ?????? ?????? - UNIX ??????
                .setExpiration(expirationDate) // ?????? ??????
                .setAudience("https://appleid.apple.com")
                .setSubject(appleBundleId)
                .signWith(SignatureAlgorithm.ES256, getPrivateKey())
                .compact());
        return Jwts.builder()
                .setHeaderParams(jwtHeader)
                .setIssuer(appleTeamId)
                .setIssuedAt(new Date(System.currentTimeMillis())) // ?????? ?????? - UNIX ??????
                .setExpiration(expirationDate) // ?????? ??????
                .setAudience("https://appleid.apple.com")
                .setSubject(appleBundleId)
                .signWith(SignatureAlgorithm.ES256, getPrivateKey())
                .compact();
    }

    private PrivateKey getPrivateKey() throws IOException {
        ClassPathResource resource = new ClassPathResource(appleSignKeyFilePath);
        String privateKey = new String(Files.readAllBytes(Paths.get(resource.getURI())));

        Reader pemReader = new StringReader(privateKey);
        PEMParser pemParser = new PEMParser(pemReader);
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
        PrivateKeyInfo object = (PrivateKeyInfo) pemParser.readObject();
        return converter.getPrivateKey(object);
    }



}

