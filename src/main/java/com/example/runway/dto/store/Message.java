package com.example.runway.dto.store;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@Getter
@Builder
@Setter
@NoArgsConstructor
public class Message{
    private String content;

    private boolean tts;

    private List<Embeds> embeds;

    @AllArgsConstructor
    @Getter
    @Builder
    @Setter
    @NoArgsConstructor
    public static class Embeds{
        private String title;

        private String description;
    }
}
