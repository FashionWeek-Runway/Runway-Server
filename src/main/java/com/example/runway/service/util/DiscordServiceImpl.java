package com.example.runway.service.util;

import com.example.runway.convertor.StoreConvertor;
import com.example.runway.domain.Store;
import com.example.runway.dto.store.Message;
import com.example.runway.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class DiscordServiceImpl implements DiscordService{
    private final StoreRepository storeRepository;
    @Value("${discord.webhook.alert.url}")
    String webhookUrl;

    public void sendMsg(Store store, String storeInfoReport){

        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Content-Type", "application/json; utf-8");
            HttpEntity<Message> messageEntity = new HttpEntity<>(StoreConvertor.MessageBuilder(store, storeInfoReport), httpHeaders);

            RestTemplate template = new RestTemplate();
            ResponseEntity<String> response = template.exchange(
                    webhookUrl,
                    HttpMethod.POST,
                    messageEntity,
                    String.class
            );

            System.out.println(response);

        } catch (Exception e) {
            log.info("에러" + e);
        }
    }
}
