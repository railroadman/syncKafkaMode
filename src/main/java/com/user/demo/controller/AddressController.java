package com.user.demo.controller;

import com.user.demo.entites.Address;
import com.user.demo.mappers.AddressMapper;
import com.user.demo.model.dto.AddAddressDto;
import com.user.demo.model.dto.UpdateAddressDto;
import com.user.demo.services.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;
    private final AddressMapper addressMapper = AddressMapper.INSTANCE;

    @Operation(summary = "Get all addresses")
    @GetMapping
    public List<Address> getAllAddresses() {
        return addressService.getAllAddresses();
    }

    @Operation(summary = "Get address by ID")
    @GetMapping("/{id}")
    public Address getAddressById(@PathVariable Long id) {
        return addressService.getAddressById(id);
    }

    @Operation(summary = "Create a new address")
    @PostMapping
    public Address createAddress(@RequestBody AddAddressDto addAddressDto) {
        Address address = addressMapper.addAddressDtoToAddress(addAddressDto);
        return addressService.createAddress(address);
    }

    @Operation(summary = "Update address by ID")
    @PutMapping("/{id}")
    public Address updateAddress(@PathVariable Long id, @RequestBody UpdateAddressDto updateAddressDto) {
        Address address = addressMapper.updateAddressDtoToAddress(updateAddressDto);
        return addressService.updateAddress(id, address);
    }

    @Operation(summary = "Delete address by ID")
    @DeleteMapping("/{id}")
    public void deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
    }
}
