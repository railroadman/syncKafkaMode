package com.user.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
@Builder
public class AddUserDto {
    private String username;
    private String email;
    private List<Long> addressesId;

    // Getters and Setters
}