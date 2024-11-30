package com.user.demo.repositories;

import com.user.demo.entites.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.userAddresses ua LEFT JOIN FETCH ua.address")
    List<User> findAllWithAddressesAndDetails();
}
