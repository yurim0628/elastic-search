package com.example.elasticsearch.service;

import com.example.elasticsearch.document.Product;
import com.example.elasticsearch.document.SearchLog;
import com.example.elasticsearch.dto.AutocompleteResponse;
import com.example.elasticsearch.dto.ProductResponse;
import com.example.elasticsearch.dto.CustomSlice;
import com.example.elasticsearch.dto.KeywordResponse;
import com.example.elasticsearch.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.elasticsearch.constant.ElasticsearchConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public CustomSlice<List<ProductResponse>> findByKeyword(
            String keyword, LocalDate checkInDate, LocalDate checkOutDate,
            Long cursorId, Pageable pageable
    ) {
        log.info(keyword);

        cursorId = (cursorId == null) ? DEFAULT_CURSOR_ID : cursorId;
        CustomSlice<Product> productSlice = productRepository.findByKeyword(keyword, checkInDate, checkOutDate, cursorId, pageable);
        List<ProductResponse> productResponseList = productSlice.content()
                .stream()
                .map(ProductResponse::from)
                .toList();

        return new CustomSlice<>(
                Collections.singletonList(productResponseList),
                productSlice.hasNext()
        );
    }

    public List<KeywordResponse> getSearchRanking() {
        List<KeywordResponse> keywordResponseList = new ArrayList<>();

        SearchHits<SearchLog> search = productRepository.getSearchRanking();
        Terms terms = search.getAggregations().get(AGGREGATION_NAME);
        for (Terms.Bucket bucket : terms.getBuckets()) {
            String key = bucket.getKeyAsString();
            long docCount = bucket.getDocCount();

            KeywordResponse keywordResponse = KeywordResponse.from(key, docCount);
            keywordResponseList.add(keywordResponse);
        }

        return keywordResponseList;
    }

    public AutocompleteResponse autocomplete(String prefix) {
        List<KeywordResponse> logsResponses = new ArrayList<>();
        List<KeywordResponse> productsResponses = new ArrayList<>();

        SearchHits<SearchLog> searchLogs = productRepository.autocomplete(prefix);
        Terms terms = searchLogs.getAggregations().get(AGGREGATION_NAME);
        for (Terms.Bucket bucket : terms.getBuckets()) {
            String key = bucket.getKeyAsString();
            long docCount = bucket.getDocCount();

            logsResponses.add(KeywordResponse.from(key, docCount));
        }

        SearchHits<Product> searchProducts = productRepository.findByPrefix(prefix);
        for(SearchHit<Product> searchHit : searchProducts.getSearchHits()){
            String key = searchHit.getContent().getAccommodationName();

            productsResponses.add(KeywordResponse.from(key, DEFAULT_COUNT));
        }

        return AutocompleteResponse.from(logsResponses, productsResponses);
    }
}
