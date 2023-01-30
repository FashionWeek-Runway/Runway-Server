package com.example.imenu_spring_project.service;


import java.time.Duration;

public interface RedisService {
    public void setValues(String key, String data) ;

    public void setValues(String key, String data, Duration duration) ;

    public String getValues(String key) ;

    public void deleteValues(String key) ;

    public void saveToken(String userId, String refreshToken, long time);
}
