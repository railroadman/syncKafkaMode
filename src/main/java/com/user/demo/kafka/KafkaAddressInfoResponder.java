package com.user.demo.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class KafkaAddressInfoResponder {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${spring.kafka.address-info-response-topic}")
    private String addressResponseTopic;
    @Value("${spring.kafka.address-info-request-topic}")
    private String addressRequestTopic;
    private final ObjectMapper objectMapper;

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    public <T> T getAddressInfoyUserId(Long userId, Class<T> responseType) throws InterruptedException, ExecutionException {
        long startTime = System.currentTimeMillis();
        var requestKey = UUID.randomUUID().toString();
        kafkaTemplate.send(addressRequestTopic, requestKey, userId.toString()).get();
        Map<String, Object> properties = new HashMap<>();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "address-info-group");
        long startKafkaCreation = System.currentTimeMillis();
        try (KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties)) {
            kafkaConsumer.subscribe(Collections.singletonList(addressResponseTopic));
            ConsumerRecord<String, String> responseRecord = null;
            long endKafkaCreation = System.currentTimeMillis();
            long durationCondumer = endKafkaCreation - startKafkaCreation;
            System.out.println("Время создания консюмера: " + durationCondumer + " мс");
            while (responseRecord == null) {
                var records = kafkaConsumer.poll(Duration.ofSeconds(1));
                for (ConsumerRecord<String, String> recordResponse : records) {
                    if (recordResponse.key().equals(requestKey)) {
                        responseRecord = recordResponse;
                        break;
                    }
                }
            }
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("Время выполнения: " + duration + " мс");
            if (responseRecord != null) {
                return objectMapper.readValue(responseRecord.value(), responseType);
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize response", e);
        }

    }

    public void respondToAddressInfoRequest(String requestKey, String responseMessage) {
        kafkaTemplate.send(addressResponseTopic, requestKey, responseMessage);
    }
}