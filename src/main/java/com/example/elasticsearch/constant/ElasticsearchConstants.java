package com.example.elasticsearch.constant;

public class ElasticsearchConstants {
    public static final int DEFAULT_PAGE = 0;
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final Long DEFAULT_CURSOR_ID = 0L;
    public static final int PAGE_SIZE_INCREMENT = 1;
    public static final String DEFAULT_SORT_FIELD = "id";

    public static final String DATE_TIME_PATTERN = "uuuu-MM-dd'T'HH:mm:ss.SSS'Z'";

    public static final String ID_FIELD = "id";
    public static final String KEYWORD_FIELD = "keyword";
    public static final String CHECK_IN_DATE_FIELD = "check_in_date";
    public static final String CHECK_OUT_DATE_FIELD = "check_out_date";
    public static final String ACCOMMODATION_NAME_FIELD = "accommodation_name";
    public static final String ACCOMMODATION_NAME_NORI_FIELD = "accommodation_name.nori";
    public static final String ACCOMMODATION_NAME_NGRAM_FIELD = "accommodation_name.ngram";

    public static final int DEFAULT_TERMS_SIZE = 10;
    public static final int MIN_DOCUMENT_SIZE = 5;
    public static final String TIMESTAMP_FIELD = "@timestamp";
    public static final String AGGREGATION_NAME = "keywords";
    public static final String AGGREGATION_FIELD = "keyword.keyword";

    public static final Long DEFAULT_COUNT = 1L;
}
