package com.example.elasticsearch.repository;

import com.example.elasticsearch.document.Product;
import com.example.elasticsearch.dto.CustomSlice;
import com.example.elasticsearch.util.DateMapper;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.elasticsearch.constant.ElasticsearchConstants.*;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

@Repository
@RequiredArgsConstructor
public class ProductRepository {

    private final ElasticsearchOperations operations;

    public CustomSlice<Product> findBySearch(
            String keyword,
            LocalDate checkInDate,
            LocalDate checkOutDate,
            Long cursorId,
            Pageable pageable
    ) {
        String checkInDateTime = DateMapper.formatLocalDate(checkInDate);
        String checkOutDateTime = DateMapper.formatLocalDate(checkOutDate);

        BoolQueryBuilder boolQueryBuilder = boolQuery()
                .must(
                        matchQuery(ACCOMMODATION_NAME_NGRAM, keyword)
                                .analyzer(NGRAM_ANALYZER)
                )
                .filter(
                        QueryBuilders.rangeQuery(CHECK_IN_DATE_NAME)
                                .gte(checkInDateTime)
                                .lte(checkOutDateTime)
                ).filter(
                        QueryBuilders.rangeQuery(CHECK_OUT_DATE_NAME)
                                .gte(checkInDateTime)
                                .lte(checkOutDateTime)
                );
        if (cursorId != null) {
            boolQueryBuilder.filter(
                    QueryBuilders.rangeQuery(ID_NAME)
                            .gt(cursorId)
            );
        }

        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withPageable(
                        PageRequest.of(
                                pageable.getPageNumber(),
                                pageable.getPageSize() + ADDITIONAL_PAGE_SIZE
                        )
                )
                .withQuery(boolQueryBuilder)
                .build();

        SearchHits<Product> search = operations.search(query, Product.class);
        List<Product> content = search
                .stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());

        boolean hasNext = search.getTotalHits() > pageable.getPageSize();
        if (hasNext) {
            content.remove(pageable.getPageSize());
        }

        return new CustomSlice<>(content, hasNext);
    }
}
