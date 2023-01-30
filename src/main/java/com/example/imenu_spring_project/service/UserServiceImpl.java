package com.example.imenu_spring_project.service;

import com.example.imenu_spring_project.common.CommonException;
import com.example.imenu_spring_project.convertor.UserConvertor;
import com.example.imenu_spring_project.domain.Authority;
import com.example.imenu_spring_project.domain.User;
import com.example.imenu_spring_project.dto.UserReq;
import com.example.imenu_spring_project.dto.UserRes;
import com.example.imenu_spring_project.jwt.JwtFilter;
import com.example.imenu_spring_project.jwt.TokenProvider;
import com.example.imenu_spring_project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.imenu_spring_project.common.CommonResponseStatus.NOT_CORRECT_PASSWORD;
import static com.example.imenu_spring_project.common.CommonResponseStatus.NOT_EXIST_USER;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Value("${jwt.refresh-token-seconds}")
    private long refreshTime;


    @Override
    public UserRes.Token logIn(UserReq.LoginUserInfo loginUserInfo) throws CommonException {

        if(!checkUserId(loginUserInfo.getUsername())){
            throw new CommonException(NOT_EXIST_USER);
        }

        User user=userRepository.findByUsername(loginUserInfo.getUsername());
        Long userId = user.getId();

        if(!passwordEncoder.matches(loginUserInfo.getPassword(),user.getPassword())){
            throw new CommonException(NOT_CORRECT_PASSWORD);
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUserInfo.getUsername(), loginUserInfo.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserRes.GenerateToken generateToken=createToken(userId);


        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + generateToken.getAccessToken());


        //반환 값 아이디 추가
        return new UserRes.Token(userId,generateToken.getAccessToken(),generateToken.getRefreshToken());
    }

    private UserRes.GenerateToken createToken(Long userId) {
        String accessToken=tokenProvider.createToken(userId);
        String refreshToken=tokenProvider.createRefreshToken(userId);

        redisService.saveToken(String.valueOf(userId),refreshToken, (System.currentTimeMillis()+ refreshTime*1000));

        return new UserRes.GenerateToken(accessToken,refreshToken);
    }

    @Override
    public UserRes.Token signUp(UserReq.SignupUser signupUser) {
        Authority authority = UserConvertor.PostAuthroity();
        String passwordEncoded=passwordEncoder.encode(signupUser.getPassword());
        User user = UserConvertor.SignUpUser(signupUser,authority,passwordEncoded);

        Long userId=userRepository.save(user).getId();

        UserRes.GenerateToken token = createToken(userId);

        return new UserRes.Token(userId,token.getAccessToken(),token.getRefreshToken());
    }

    @Override
    public boolean checkUserId(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean validationPassword(String password) {
        // 비밀번호 포맷 확인(영문, 특수문자, 숫자 포함 8자 이상)
        Pattern pattern = Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$");
        Matcher matcher = pattern.matcher(password);

        return matcher.find();
    }

    @Override
    public boolean checkNickName(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    @Override
    public boolean validationPhoneNumber(String phone) {
        Pattern pattern = Pattern.compile("\\d{11}");
        Matcher matcher = pattern.matcher(phone);

        return matcher.matches();
    }

    @Override
    public void updateFcmToken(Long userId, String token) {
        Optional<User> user = userRepository.findById(userId);

        user.get().updateToken(token);

        userRepository.save(user.get());
    }
}
