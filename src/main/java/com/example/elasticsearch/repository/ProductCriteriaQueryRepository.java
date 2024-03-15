package com.example.elasticsearch.repository;

import com.example.elasticsearch.document.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.elasticsearch.constant.ElasticsearchConstants.ACCOMMODATION_NAME;

@Repository
@RequiredArgsConstructor
public class ProductCriteriaQueryRepository {

    private final ElasticsearchOperations operations;

    public List<Product> findByAccommodationName(String accommodationName) {
        Criteria criteria = Criteria.where(ACCOMMODATION_NAME).contains(accommodationName);
        CriteriaQuery query = new CriteriaQuery(criteria);

        SearchHits<Product> search = operations.search(query, Product.class);
        return search
                .stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }
}
