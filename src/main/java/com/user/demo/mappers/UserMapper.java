package com.user.demo.mappers;

import com.user.demo.entites.Address;
import com.user.demo.entites.User;
import com.user.demo.entites.UserAddress;
import com.user.demo.model.dto.AddUserDto;
import com.user.demo.model.dto.AddressDto;
import com.user.demo.model.dto.UpdateUserDto;
import com.user.demo.model.dto.UserResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    User addUserDtoToUser(AddUserDto addUserDto);
    User updateUserDtoToUser(UpdateUserDto updateUserDto);
    default UserResponseDto toUserResponseDto(User user) {
        if (user == null) {
            return null;
        }
        List<AddressDto> addressDtos = user.getUserAddresses().stream().map(UserAddress::getAddress).map(this::toAddressDto).collect(Collectors.toList());
        return new UserResponseDto(user.getId(), user.getUsername(), user.getEmail(), addressDtos);
    }

    private AddressDto toAddressDto(Address address) {
        return new AddressDto(
                address.getId(),
                address.getStreet(),
                address.getCity(),
                address.getState(),
                address.getCountry(),
                address.getZip()
        );
    }
}