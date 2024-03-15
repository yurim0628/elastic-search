package com.example.elasticsearch.dto;

import com.example.elasticsearch.document.Product;

import java.time.LocalDate;
import java.time.LocalTime;

public record ProductResponse(
        Long id,
        String accommodationAddress,
        String accommodationImage,
        String accommodationName,
        String areaCode,
        LocalDate checkInDate,
        LocalTime checkInTime,
        LocalDate checkOutDate,
        LocalTime checkOutTime,
        Long standardNumber,
        Long maximumNumber,
        Long originPrice,
        String reservationType,
        String roomName
) {

    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getAccommodationAddress(),
                product.getAccommodationImage(),
                product.getAccommodationName(),
                product.getAreaCode(),
                product.getCheckInDate(),
                product.getCheckInTime(),
                product.getCheckOutDate(),
                product.getCheckOutTime(),
                product.getStandardNumber(),
                product.getMaximumNumber(),
                product.getOriginPrice(),
                product.getReservationType(),
                product.getRoomName()
        );
    }
}
