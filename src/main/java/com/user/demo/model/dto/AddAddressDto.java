package com.user.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AddAddressDto {
    private String street;
    private String city;
    private String state;
    private String zip;
    private String country;

    // Getters and Setters
}