package com.user.demo.services;

import com.user.demo.entites.Address;
import com.user.demo.entites.User;
import com.user.demo.entites.UserAddress;
import com.user.demo.entites.UserAddressId;
import com.user.demo.kafka.KafkaAddressInfoResponder;
import com.user.demo.mappers.UserMapper;
import com.user.demo.model.dto.UserResponseDto;
import com.user.demo.repositories.AddressRepository;
import com.user.demo.repositories.UserAddressRepository;
import com.user.demo.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserAddressRepository userAddressRepository;
    private final AddressRepository addressRepository;
    @Value("${address.api.url}")
    private String addressApiUrl;
    private final RestTemplate restTemplate;
    private final UserMapper userMapper = UserMapper.INSTANCE;

    private final KafkaAddressInfoResponder kafkaAddressInfoResponder;

    public List<User> getAllUsers() {
        return userRepository.findAllWithAddressesAndDetails();
    }

    public UserResponseDto getUserById(Long id) {

        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.getUserAddresses().size(); // Принудительная инициализация коллекции
            return userMapper.toUserResponseDto(user);
        }
        return null;
    }

    public User getRestUserById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            List<Address> addresses = fetchAllAddressesFromApi();
            Map<Long, Address> addressMap = addresses.stream().collect(Collectors.toMap(Address::getId, Function.identity()));
        }
        return user;
    }

    public UserResponseDto getAddressUsingKafka(Long userId) {
        UserResponseDto user;
        try {
            user = kafkaAddressInfoResponder.getAddressInfoyUserId(userId,UserResponseDto.class);
            System.out.println(user);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
        return user;
    }

    public User createUser(User user, List<Long> addressesId) {
        User createdUser = userRepository.save(user);
        addUserAddresses(createdUser, addressesId);
        return createdUser;
    }

    public User updateUser(Long id, User userDetails, List<Long> addressesId) {
        User user = userRepository.findById(id).orElseThrow();
        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());
        User updatedUser = userRepository.save(user);
        updateUserAddresses(updatedUser, addressesId);
        return updatedUser;
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public void addUserAddresses(User user, List<Long> addressesId) {
        addressesId.forEach(addressId -> {
            Address address = addressRepository.findById(addressId).orElseThrow();
            UserAddress userAddress = new UserAddress(new UserAddressId(user.getId(), addressId), user, address);
            userAddressRepository.save(userAddress);
        });
    }

    public void updateUserAddresses(User user, List<Long> addressesId) {
        userAddressRepository.deleteAllById_UserId(user.getId());
        addUserAddresses(user, addressesId);
    }

    private List<Address> fetchAllAddressesFromApi() {
        ResponseEntity<List<Address>> response = restTemplate.exchange(
                addressApiUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Address>>() {
                }
        );
        return response.getBody();
    }
}
