package com.example.elasticsearch.service;

import com.example.elasticsearch.document.Product;
import com.example.elasticsearch.dto.ProductResponse;
import com.example.elasticsearch.repository.ProductNativeQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private  final ProductNativeQueryRepository productNativeQueryRepository;

    public List<ProductResponse> findByAccommodationName(String accommodationName) {
        List<Product> productResponses = productNativeQueryRepository.findByAccommodationName(accommodationName);

        return productResponses
                .stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }
}
