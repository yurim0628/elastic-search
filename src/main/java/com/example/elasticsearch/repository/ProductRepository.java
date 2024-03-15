package com.example.elasticsearch.repository;

import com.example.elasticsearch.document.Product;
import com.example.elasticsearch.dto.ProductResponse;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ProductRepository extends ElasticsearchRepository<Product, Long> {
    List<ProductResponse> findByAccommodationName(String accommodationName);
}
