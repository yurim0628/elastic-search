package com.example.elasticsearch.document;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Getter
@Document(indexName = "accesslogs")
public class AccessLog {

    @Id
    private String id;

    private String keyword;
}
