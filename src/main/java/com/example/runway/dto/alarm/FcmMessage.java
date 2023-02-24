package com.example.runway.dto.alarm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class FcmMessage {
    private final boolean validateOnly;
    private final Message message;

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Message {
        private final Notification notification;
        private final String token;
    }

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Notification {
        private final String title;
        private final String body;
        private final String image;
    }
}
