package com.example.elasticsearch.dto;

public record KeywordResponse(
        String keyword,
        long count
) {
    public static KeywordResponse from(String keyword, long count) {
        return new KeywordResponse(keyword, count);
    }
}
