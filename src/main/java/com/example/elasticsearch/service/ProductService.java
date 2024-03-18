package com.example.elasticsearch.service;

import com.example.elasticsearch.document.Product;
import com.example.elasticsearch.dto.ProductResponse;
import com.example.elasticsearch.dto.CustomSlice;
import com.example.elasticsearch.dto.KeywordResponse;
import com.example.elasticsearch.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static com.example.elasticsearch.constant.ElasticsearchConstants.DEFAULT_CURSOR_ID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public CustomSlice<List<ProductResponse>> findBySearch(
            String keyword,
            LocalDate checkInDate,
            LocalDate checkOutDate,
            Long cursorId,
            Pageable pageable
    ) {
        // LOGSTASH appender를 통해 로그를 Logstash로 전송한다.
        log.info(keyword);

        if (cursorId == null) {
            cursorId = DEFAULT_CURSOR_ID;
        }
        CustomSlice<Product> productSlice = productRepository.findBySearch(keyword, checkInDate, checkOutDate, cursorId, pageable);
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
        return productRepository.getSearchRanking();
    }

    public List<KeywordResponse> autocomplete(String prefix) {
        return productRepository.autocomplete(prefix);
    }
}
