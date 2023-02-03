package com.example.runway.service;

import com.example.runway.domain.UserCategory;
import com.example.runway.exception.BaseException;
import com.example.runway.convertor.UserConvertor;
import com.example.runway.domain.Authority;
import com.example.runway.domain.User;
import com.example.runway.dto.user.UserReq;
import com.example.runway.dto.user.UserRes;
import com.example.runway.jwt.TokenProvider;
import com.example.runway.repository.UserCategoryRepository;
import com.example.runway.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.runway.common.CommonResponseStatus.NOT_CORRECT_PASSWORD;
import static com.example.runway.common.CommonResponseStatus.NOT_EXIST_USER;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {
    private final UserRepository userRepository;
    private final UserCategoryRepository userCategoryRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Value("${jwt.refresh-token-seconds}")
    private long refreshTime;


    @Override
    public UserRes.Token logIn(UserReq.LoginUserInfo loginUserInfo) throws BaseException {
        Optional<User> user=userRepository.findByUsername(loginUserInfo.getPhone());
        Long userId = user.get().getId();

        if(!checkuserId(loginUserInfo.getPhone())){
            throw new BaseException(NOT_EXIST_USER);
        }



        if(!passwordEncoder.matches(loginUserInfo.getPassword(),user.get().getPassword())){
            throw new BaseException(NOT_CORRECT_PASSWORD);
        }

        user.get().updateLogInDate(LocalDateTime.now());

        userRepository.save(user.get());

        UserRes.GenerateToken generateToken=createToken(userId);

        //반환 값 아이디 추가
        return new UserRes.Token(userId,generateToken.getAccessToken(),generateToken.getRefreshToken());
    }

    private UserRes.GenerateToken createToken(Long userId) throws BaseException {
        String accessToken=tokenProvider.createToken(userId);
        String refreshToken=tokenProvider.createRefreshToken(userId);

        redisService.saveToken(String.valueOf(userId),refreshToken, (System.currentTimeMillis()+ refreshTime*1000));

        return new UserRes.GenerateToken(accessToken,refreshToken);
    }

    @Override
    @Transactional(rollbackFor = SQLException.class)
    public UserRes.Token signUp(UserReq.SignupUser signupUser) throws BaseException {

        Authority authority = UserConvertor.PostAuthroity();
        String passwordEncoded=passwordEncoder.encode(signupUser.getPassword());
        User user = UserConvertor.SignUpUser(signupUser,authority,passwordEncoded);

        Long userId=userRepository.save(user).getId();



        for(int i=0;i<signupUser.getCategoryList().size();i++){
            UserCategory userCategory=UserConvertor.PostUserCategory(userId,signupUser.getCategoryList().get(i));
            userCategoryRepository.save(userCategory);
        }




        UserRes.GenerateToken token = createToken(userId);

        return new UserRes.Token(userId,token.getAccessToken(),token.getRefreshToken());
    }

    @Override
    public boolean checkuserId(String phone) {
        return userRepository.existsByUsername(phone);
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
