package com.example.elasticsearch.controller;

import com.example.elasticsearch.dto.ProductResponse;
import com.example.elasticsearch.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    ResponseEntity<List<ProductResponse>> findByAccommodationName(@RequestParam String accommodationName){
        return ResponseEntity.ok(productService.findByAccommodationName(accommodationName));
    }
}
