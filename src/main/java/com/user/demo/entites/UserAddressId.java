package com.user.demo.entites;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAddressId implements Serializable {
    private Long userId;
    private Long addressId;

    // Getters, Setters, hashCode, and equals
}