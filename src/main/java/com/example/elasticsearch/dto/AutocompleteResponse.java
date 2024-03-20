package com.example.elasticsearch.dto;

import java.util.List;

public record AutocompleteResponse(
        List<KeywordResponse> logsResponses,
        List<KeywordResponse> productsResponses
) {
    public static AutocompleteResponse from(List<KeywordResponse> logsResponses, List<KeywordResponse> productsResponses){
        return new AutocompleteResponse(logsResponses, productsResponses);
    }
}
