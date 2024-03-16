package com.example.elasticsearch.dto;

import java.util.List;

public record CustomSlice<T>(
        List<T> content,
        boolean hasNext
) {

    public boolean hasContent() {
        return !content.isEmpty();
    }

    public boolean isLast() {
        return !hasNext;
    }
}
