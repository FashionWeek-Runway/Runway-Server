package com.example.runway.service;


import java.time.Duration;

public interface RedisService {
    void setValues(String key, String data) ;

    void setValues(String key, String data, Duration duration) ;

    String getValues(String key) ;

    void deleteValues(String key) ;

    void saveToken(String userId, String refreshToken, long time);
}
