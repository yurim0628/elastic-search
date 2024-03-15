package com.example.elasticsearch.document;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.example.elasticsearch.constant.ElasticsearchConstants.DATE_TIME_PATTERN;

@Getter
@Document(indexName = "products")
public class Product {

    @Id
    private Long id;

    @Field(name = "area_code")
    private String areaCode;
    @Field(name = "accommodation_image")
    private String accommodationImage;
    @Field(name = "accommodation_name")
    private String accommodationName;
    @Field(name = "accommodation_address")
    private String accommodationAddress;

    @Field(
            name = "check_in_date",
            type = FieldType.Date,
            pattern = DATE_TIME_PATTERN
    )
    private LocalDate checkInDate;
    @Field(
            name = "check_in_time",
            type = FieldType.Date,
            pattern = DATE_TIME_PATTERN
    )
    private LocalTime checkInTime;
    @Field(
            name = "check_out_date",
            type = FieldType.Date,
            pattern = DATE_TIME_PATTERN
    )
    private LocalDate checkOutDate;
    @Field(
            name = "check_out_date",
            type = FieldType.Date,
            pattern = DATE_TIME_PATTERN
    )
    private LocalTime checkOutTime;

    @Field(name = "standard_number")
    private Long standardNumber;
    @Field(name = "maximum_number")
    private Long maximumNumber;
    @Field(name = "origin_price")
    private Long originPrice;
    @Field(name = "reservation_type")
    private String reservationType;
    @Field(name = "room_name")
    private String roomName;
}
