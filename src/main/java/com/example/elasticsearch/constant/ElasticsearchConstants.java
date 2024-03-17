package com.example.elasticsearch.constant;

public class ElasticsearchConstants {
    public static final int DEFAULT_PAGE = 0;
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final String DEFAULT_SORT_FIELD = "id";
    public static final int ADDITIONAL_PAGE_SIZE = 1;
    public static final long DEFAULT_CURSOR_ID = 0L;
    public static final String DATE_TIME_PATTERN = "uuuu-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String NGRAM_ANALYZER = "ngram_analyzer";
    public static final String ACCOMMODATION_NAME_NGRAM = "accommodation_name.ngram";
    public static final String CURSOR_ID_FIELD = "id";
    public static final String CHECK_IN_DATE_FIELD = "check_in_date";
    public static final String CHECK_OUT_DATE_FIELD = "check_out_date";
    public static final String TIMESTAMP_FIELD = "@timestamp";
    public static final String DEFAULT_AGGREGATION_NAME = "keyword";
    public static final String KEYWORD_FIELD = "keyword.keyword";
    public static final int DEFAULT_AGGREGATION_SIZE = 10;
}
