package com.user.demo.mappers;

import com.user.demo.entites.Address;
import com.user.demo.model.dto.AddAddressDto;
import com.user.demo.model.dto.UpdateAddressDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AddressMapper {
    AddressMapper INSTANCE = Mappers.getMapper(AddressMapper.class);

    Address addAddressDtoToAddress(AddAddressDto addAddressDto);
    Address updateAddressDtoToAddress(UpdateAddressDto updateAddressDto);
}