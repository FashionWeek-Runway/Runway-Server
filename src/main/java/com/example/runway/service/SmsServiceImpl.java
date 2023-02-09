package com.example.runway.service;

import com.example.runway.dto.user.UserReq;
import com.example.runway.dto.user.UserRes;
import com.example.runway.repository.SmsUserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class SmsServiceImpl implements SmsService {
    @Value("${naver-cloud-sms.access.key}")
    private String accessKey;

    @Value("${naver-cloud-sms.secret.key}")
    private String secretKey;

    @Value("${naver-cloud-sms.service.id}")
    private String serviceId;

    @Value("${naver-cloud-sms.sender.phone}")
    private String phone;

    private final RedisService redisService;

    private final SmsUserRepository smsUserRepository;

    public String makeSignature(String time) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        String space = " ";
        String newLine = "\n";
        String method = "POST";
        String url = "/sms/v2/services/" + this.serviceId + "/messages";
        String accessKey = this.accessKey;
        String secretKey = this.secretKey;

        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(url)
                .append(newLine)
                .append(time)
                .append(newLine)
                .append(accessKey)
                .toString();

        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
        String encodeBase64String = Base64.encodeBase64String(rawHmac);

        return encodeBase64String;
    }

    public UserRes.SmsResponse sendSms(UserReq.Message messageDto) throws  URISyntaxException, InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException, JsonProcessingException {
        String time = Long.toString(System.currentTimeMillis());

        String smsConfirmNum = createSmsKey();

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-ncp-apigw-timestamp", time);
        headers.set("x-ncp-iam-access-key", accessKey);
        headers.set("x-ncp-apigw-signature-v2", makeSignature(time)); // signature 서명

        List<UserReq.Message> messages = new ArrayList<>();
        messages.add(messageDto);

        UserReq.SmsRequest request = UserReq.SmsRequest.builder()
                .type("SMS")
                .contentType("COMM")
                .countryCode("82")
                .from(phone)
                .content("[Runway] 인증번호 [" + smsConfirmNum + "]를 입력해주세요")
                .messages(messages)
                .build();

        //쌓은 바디를 json형태로 반환
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(request);
        // jsonBody와 헤더 조립
        HttpEntity<String> httpBody = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();

        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        //restTemplate로 post 요청 보내고 오류가 없으면 202코드 반환
        UserRes.SmsResponse smsResponseDto = restTemplate.postForObject(new URI("https://sens.apigw.ntruss.com/sms/v2/services/" + serviceId + "/messages"), httpBody, UserRes.SmsResponse.class);

        smsResponseDto.setSmsConfirmNum(smsConfirmNum);

        redisService.setValues(messageDto.getTo(),smsConfirmNum, Duration.ofSeconds(180));

        return smsResponseDto;
    }

    @Override
    public int checkLimitCertification(String to) {

        LocalDateTime todayLocalTime = LocalDateTime.now();
        LocalDateTime dateTime =todayLocalTime.minusMinutes(10);

        return smsUserRepository.countByPhoneAndCreatedAtGreaterThan(to,dateTime);
    }

    @Override
    public String getSmsConfirmNum(String to) {
        return redisService.getValues(to);
    }

    // 인증코드 만들기

    public static String createSmsKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 6; i++) { // 인증코드 6자리
            key.append((rnd.nextInt(10)));
        }
        return key.toString();
    }
}
