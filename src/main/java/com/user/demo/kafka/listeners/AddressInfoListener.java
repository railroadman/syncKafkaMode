package com.user.demo.kafka.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.demo.entites.User;
import com.user.demo.mappers.UserMapper;
import com.user.demo.model.dto.UserResponseDto;
import com.user.demo.services.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressInfoListener {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final UserService userService;
    private final ObjectMapper objectMapper;
    private final UserMapper userMapper = UserMapper.INSTANCE;

    @Value("${spring.kafka.address-info-response-topic}")
    private String addressResponseTopic;


    @KafkaListener(topics = "#{'${spring.kafka.address-info-request-topic}'}", groupId = "address-info-group")
    public void listenAddressInfoRequests(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
        String requestKey = record.key();
        var userId = Long.parseLong(record.value());
        long startTime = System.currentTimeMillis();
        respondToAddressInfoRequest(requestKey, userService.getUserById(userId));
        acknowledgment.acknowledge();
        long endTime = System.currentTimeMillis();
        double elapsedTime = endTime - startTime;
        System.out.println("Kafka + DB query: " + elapsedTime + "ms");
    }

    private void respondToAddressInfoRequest(String requestKey, UserResponseDto user) {
        try {
            kafkaTemplate.send(addressResponseTopic, requestKey, objectMapper.writeValueAsString(user));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
