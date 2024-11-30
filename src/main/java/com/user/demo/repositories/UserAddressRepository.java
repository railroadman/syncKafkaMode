package com.user.demo.repositories;

import com.user.demo.entites.UserAddress;
import com.user.demo.entites.UserAddressId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, UserAddressId> {
    void deleteAllById_UserId(Long userId);
}