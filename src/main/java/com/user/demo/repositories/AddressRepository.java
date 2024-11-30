package com.user.demo.repositories;

import com.user.demo.entites.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}