package com.example.elasticsearch.repository;

import com.example.elasticsearch.document.Product;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.elasticsearch.constant.ElasticsearchConstants.ACCOMMODATION_NAME_NGRAM;
import static com.example.elasticsearch.constant.ElasticsearchConstants.NGRAM_ANALYZER;

@Repository
@RequiredArgsConstructor
public class ProductNativeQueryRepository {

    private final ElasticsearchOperations operations;

    public List<Product> findByAccommodationName(String accommodationName) {
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders
                        .matchQuery(ACCOMMODATION_NAME_NGRAM, accommodationName)
                        .analyzer(NGRAM_ANALYZER)
                )
                .build();
        SearchHits<Product> search = operations.search(query, Product.class);

        return search
                .stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }
}
