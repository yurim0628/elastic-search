package com.example.elasticsearch.document;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Getter
@Document(indexName = "searchlogs")
public class SearchLog {

    @Id
    private String id;

    private String keyword;
}
