package com.example.elasticsearch.controller;

import com.example.elasticsearch.dto.CustomSlice;
import com.example.elasticsearch.dto.ProductResponse;
import com.example.elasticsearch.dto.SearchRankResponse;
import com.example.elasticsearch.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

import static com.example.elasticsearch.constant.ElasticsearchConstants.*;
import static org.springframework.data.domain.Sort.Direction.ASC;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<CustomSlice<List<ProductResponse>>> findBySearch(
            @RequestParam String keyword,
            @RequestParam LocalDate checkInDate,
            @RequestParam LocalDate checkOutDate,
            @RequestParam(required = false) Long cursorId,
            @PageableDefault(
                    page = DEFAULT_PAGE,
                    size = DEFAULT_PAGE_SIZE,
                    sort = DEFAULT_SORT_FIELD,
                    direction = ASC
            ) Pageable pageable
    ) {
        return ResponseEntity.ok(productService.findBySearch(keyword, checkInDate, checkOutDate, cursorId, pageable));
    }

    @GetMapping("/search-rank")
    public ResponseEntity<List<SearchRankResponse>> getSearchRanking()  {
        return ResponseEntity.ok(productService.getSearchRanking());
    }
}
