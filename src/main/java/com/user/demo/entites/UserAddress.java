package com.user.demo.entites;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_address")
@Getter
@Setter
@NoArgsConstructor
@Builder
public class UserAddress {
    @EmbeddedId
    private UserAddressId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToOne
    @MapsId("addressId")
    @JoinColumn(name = "address_id")
    private Address address;

    public UserAddress(UserAddressId id, User user, Address address) {
        this.id = id;
        this.user = user;
        this.address = address;
    }
}
