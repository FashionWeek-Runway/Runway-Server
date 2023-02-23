package com.example.runway.service;

import com.example.runway.domain.SmsUser;
import com.example.runway.domain.UserCategory;
import com.example.runway.exception.BaseException;
import com.example.runway.convertor.UserConvertor;
import com.example.runway.domain.Authority;
import com.example.runway.domain.User;
import com.example.runway.dto.user.UserReq;
import com.example.runway.dto.user.UserRes;
import com.example.runway.exception.NotFoundException;
import com.example.runway.jwt.TokenProvider;
import com.example.runway.repository.SmsUserRepository;
import com.example.runway.repository.UserCategoryRepository;
import com.example.runway.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.runway.constants.CommonResponseStatus.NOT_CORRECT_PASSWORD;
import static com.example.runway.constants.CommonResponseStatus.NOT_EXIST_USER;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {
    private final UserRepository userRepository;
    private final UserCategoryRepository userCategoryRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final AwsS3Service awsS3Service;
    private final SmsUserRepository smsUserRepository;


    @Value("${jwt.refresh-token-seconds}")
    private long refreshTime;


    @Override
    public UserRes.Token logIn(UserReq.LoginUserInfo loginUserInfo) throws BaseException {

        Optional<User> user=userRepository.findByUsernameAndStatus(loginUserInfo.getPhone(),true);
        if(!checkuserId(loginUserInfo.getPhone())){
            throw new BaseException(NOT_EXIST_USER);
        }

        Long userId = user.get().getId();




        if(!passwordEncoder.matches(loginUserInfo.getPassword(),user.get().getPassword())){
            throw new NotFoundException(NOT_CORRECT_PASSWORD);
        }

        user.get().updateLogInDate(LocalDateTime.now());

        userRepository.save(user.get());

        UserRes.GenerateToken generateToken=createToken(userId);

        //반환 값 아이디 추가
        return new UserRes.Token(userId,generateToken.getAccessToken(),generateToken.getRefreshToken());
    }

    public UserRes.GenerateToken createToken(Long userId) {
        String accessToken=tokenProvider.createToken(userId);
        String refreshToken=tokenProvider.createRefreshToken(userId);

        redisService.setValues(String.valueOf(userId),refreshToken, Duration.ofSeconds(refreshTime));

        return new UserRes.GenerateToken(accessToken,refreshToken);
    }

    @Override
    public void countUserPhone(String phone) {
        SmsUser smsUser=SmsUser.builder().phone(phone).build();
        smsUserRepository.save(smsUser);
    }

    @Override
    public UserRes.SignUp signUpSocial(UserReq.SocialSignUp socialSignUp) throws IOException {
        Authority authority = UserConvertor.PostAuthroity();
        String passwordEncoded=passwordEncoder.encode(socialSignUp.getSocialId());
        String profileImgUrl;

        if(socialSignUp.getMultipartFile()==null){
            profileImgUrl = socialSignUp.getProfileImgUrl();
        }
        else {
            profileImgUrl = awsS3Service.upload(socialSignUp.getMultipartFile(), "profile");
        }

        User user = UserConvertor.SignUpSocialUser(socialSignUp,authority,passwordEncoded,profileImgUrl);


        Long userId=userRepository.save(user).getId();

        saveUserCategoryList(userId,socialSignUp.getCategoryList());

        List<String> categoryList = getCategoryNameList(socialSignUp.getCategoryList());


        UserRes.GenerateToken token = createToken(userId);

        return UserConvertor.SignUpUserRes(token,user,categoryList);
    }


    @Override
    @Transactional(rollbackFor = SQLException.class)
    public UserRes.SignUp signUp(MultipartFile multipartFile, UserReq.SignupUser signupUser) throws BaseException, IOException {



        Authority authority = UserConvertor.PostAuthroity();
        String passwordEncoded=passwordEncoder.encode(signupUser.getPassword());
        String profileImgUrl;

        if(multipartFile==null){
            profileImgUrl = null;
        }
        else {
            profileImgUrl = awsS3Service.upload(multipartFile, "profile");
        }

        User user = UserConvertor.SignUpUser(signupUser,authority,passwordEncoded,profileImgUrl);


        Long userId=userRepository.save(user).getId();


        saveUserCategoryList(userId,signupUser.getCategoryList());
        List<String> categoryList = getCategoryNameList(signupUser.getCategoryList());



        UserRes.GenerateToken token = createToken(userId);

        return UserConvertor.SignUpUserRes(token,user,categoryList);
    }

    @Override
    public boolean checkuserId(String phone) {
        return userRepository.existsByUsernameAndStatus(phone,true);
    }

    @Override
    public boolean validationPassword(String password) {
        // 비밀번호 포맷 확인(영문, 숫자 포함 8자 이상)
        Pattern pattern = Pattern.compile("^[A-Za-z[0-9]]{8,16}$");
        Matcher matcher = pattern.matcher(password);

        return matcher.find();
    }

    @Override
    public boolean checkNickName(String nickname) {
        return userRepository.existsByNicknameAndStatus(nickname,true);
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

    @Override
    public void modifyPassword(UserReq.PostPassword postPassword) {
        Optional<User> user = userRepository.findByUsernameAndStatus(postPassword.getPhone(),true);

        String passwordEncoded=passwordEncoder.encode(postPassword.getPassword());

        user.get().modifyPassword(passwordEncoded);

        userRepository.save(user.get());
    }


    public List<String> getCategoryNameList(List<Long> categoryList){
        List<String> categoryNameList=new ArrayList<>();

        for(int i=0;i<categoryList.size();i++){
            if(categoryList.get(i)==1){
                categoryNameList.add("미니멀");
            }
            else if(categoryList.get(i)==2){
                categoryNameList.add("캐주얼");
            }
            else if(categoryList.get(i)==3){
                categoryNameList.add("시티보이");
            }
            else if(categoryList.get(i)==4){
                categoryNameList.add("스트릿");
            }
            else if(categoryList.get(i)==5){
                categoryNameList.add("빈티지");
            }
            else if(categoryList.get(i)==6){
                categoryNameList.add("페미닌");
            }
        }
        return categoryNameList;

    }

    @Transactional(rollbackFor = SQLException.class)
    public void saveUserCategoryList(Long userId,List<Long> userCategoryList){
        List<UserCategory> userCategoryArrayList=new ArrayList<>();
        for(int i=1;i<7;i++){
            UserCategory userCategory=UserCategory.builder().userId(userId).categoryId(Long.valueOf(i)).status(false).build();
            userCategoryArrayList.add(userCategory);
        }
        userCategoryRepository.saveAll(userCategoryArrayList);

        for(Long categoryId : userCategoryList){
            Optional<UserCategory> userCategory=userCategoryRepository.findByUserIdAndCategoryId(userId,categoryId);
            userCategory.get().modifyCategoryStatus(true);
            userCategoryRepository.save(userCategory.get());
        }
    }

}
