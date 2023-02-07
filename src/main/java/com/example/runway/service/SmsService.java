package com.example.runway.service;

import com.example.runway.dto.user.UserReq;
import com.example.runway.dto.user.UserRes;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface SmsService {
    UserRes.SmsResponse sendSms(UserReq.Message message) throws URISyntaxException, InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException, JsonProcessingException;
}
