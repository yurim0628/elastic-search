package com.example.elasticsearch.repository;

import com.example.elasticsearch.document.Product;
import com.example.elasticsearch.document.SearchLog;
import com.example.elasticsearch.dto.CustomSlice;
import com.example.elasticsearch.dto.KeywordResponse;
import com.example.elasticsearch.util.DateMapper;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchPhrasePrefixQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.elasticsearch.constant.ElasticsearchConstants.*;
import static java.time.temporal.ChronoUnit.HOURS;
import static org.elasticsearch.common.unit.Fuzziness.ONE;
import static org.elasticsearch.common.unit.Fuzziness.ZERO;

@Repository
@RequiredArgsConstructor
public class ProductRepository {

    private final ElasticsearchOperations operations;

    public CustomSlice<Product> findBySearch(
            String keyword, LocalDate checkInDate, LocalDate checkOutDate,
            Long cursorId, Pageable pageable
    ) {
        Fuzziness fuzziness = keyword.length() < MIN_KEYWORD_LENGTH ? ZERO : ONE;

        long startMillis = DateMapper.toMilliseconds(checkInDate.atStartOfDay());
        long endMillis = DateMapper.toMilliseconds(checkOutDate.atStartOfDay());

        // 1. Elasticsearch 쿼리 작성
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                // 1.1 다중 필드 검색 및 허용 가능한 일치 범위 설정
                .must(
                        QueryBuilders.multiMatchQuery(keyword)
                                .field(ACCOMMODATION_NAME_FIELD)
                                .field(ACCOMMODATION_NAME_NORI_FIELD)
                                .field(ACCOMMODATION_NAME_NGRAM_FIELD)
                                .fuzziness(fuzziness)
                )
                // 1.2 체크인 및 체크아웃 날짜 범위 설정
                .must(
                        QueryBuilders.rangeQuery(CHECK_IN_DATE_FIELD)
                                .gte(startMillis)
                                .lte(endMillis)
                )
                .must(
                        QueryBuilders.rangeQuery(CHECK_OUT_DATE_FIELD)
                                .gte(startMillis)
                                .lte(endMillis)
                )
                // 1.3 커서 아이디 기준으로 페이징
                .must(
                        QueryBuilders.rangeQuery(ID_FIELD)
                                .gt(cursorId)
                );

        // 3. Elasticsearch 쿼리 전송
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withPageable(
                        PageRequest.of(
                                pageable.getPageNumber(),
                                pageable.getPageSize() + PAGE_SIZE_INCREMENT,
                                pageable.getSort()
                        )
                )
                .withMinScore(MIN_SCORE)
                .build();

        SearchHits<Product> search = operations.search(query, Product.class);

        // 4. 검색 결과 필터링
        List<Product> content = new ArrayList<>();
        for (SearchHit<Product> searchHit : search.getSearchHits()) {
            content.add(searchHit.getContent());
        }

        boolean hasNext = search.getTotalHits() > pageable.getPageSize();
        if (hasNext) {
            content.remove(pageable.getPageSize());
        }

        return new CustomSlice<>(content, hasNext);
    }

    public List<KeywordResponse> getSearchRanking() {
        List<KeywordResponse> keywordResponseList = new ArrayList<>();

        long startMillis = DateMapper.toMilliseconds(LocalDateTime.now().truncatedTo(HOURS));
        long endMillis = DateMapper.toMilliseconds(LocalDateTime.now());

        QueryBuilder queryBuilder = QueryBuilders.rangeQuery(TIMESTAMP_FIELD)
                .gte(startMillis)
                .lte(endMillis);

        TermsAggregationBuilder aggregationBuilder = AggregationBuilders.terms(DEFAULT_AGGREGATION_NAME)
                .field(KEYWORD_FIELD)
                .size(MAX_AGGREGATION_SIZE);

        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .addAggregation(aggregationBuilder)
                .build();;

        SearchHits<SearchLog> search = operations.search(query, SearchLog.class);

        Terms terms = search.getAggregations() != null ? search.getAggregations().get(DEFAULT_AGGREGATION_NAME) : null;
        if(terms != null) {
            for (Terms.Bucket bucket : terms.getBuckets()) {
                String key = bucket.getKeyAsString();
                long docCount = bucket.getDocCount();

                KeywordResponse keywordResponse = KeywordResponse.from(key, docCount);
                keywordResponseList.add(keywordResponse);
            }
        }

        return keywordResponseList;
    }

    public List<KeywordResponse> autocomplete(String prefix) {
        List<KeywordResponse> keywordResponseList = new ArrayList<>();

        MatchPhrasePrefixQueryBuilder queryBuilder = QueryBuilders.matchPhrasePrefixQuery(KEYWORD_NAME, prefix);

        TermsAggregationBuilder aggregationBuilder = AggregationBuilders.terms(DEFAULT_AGGREGATION_NAME)
                .field(KEYWORD_FIELD)
                .size(MIN_AGGREGATION_SIZE);

        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .addAggregation(aggregationBuilder)
                .build();

        SearchHits<SearchLog> search = operations.search(query, SearchLog.class);

        Terms terms = search.getAggregations() != null ? search.getAggregations().get(DEFAULT_AGGREGATION_NAME) : null;
        if(terms != null) {
            for (Terms.Bucket bucket : terms.getBuckets()) {
                String key = bucket.getKeyAsString();
                long docCount = bucket.getDocCount();

                KeywordResponse keywordResponse = KeywordResponse.from(key, docCount);
                keywordResponseList.add(keywordResponse);
            }
        }

        return keywordResponseList;
    }
}
