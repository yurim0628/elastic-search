package com.example.elasticsearch.dto;

public record SearchRankResponse(
        String keyword,
        long count
) {
    public static SearchRankResponse from(String keyword, long count) {
        return new SearchRankResponse(keyword, count);
    }
}
