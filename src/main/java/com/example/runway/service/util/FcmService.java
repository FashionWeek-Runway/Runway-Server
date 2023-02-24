package com.example.runway.service.util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;

public interface FcmService {

    void sendMessageTo(String targetToken, String title, String body) throws IOException;


}
