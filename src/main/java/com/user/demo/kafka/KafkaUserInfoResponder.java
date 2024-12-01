package com.user.demo.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class KafkaUserInfoResponder {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ConsumerFactory<String, String> consumerFactory;
    private final ObjectMapper objectMapper;

    @Value("${spring.kafka.address-info-response-topic}")
    private String addressResponseTopic;
    @Value("${spring.kafka.address-info-request-topic}")
    private String addressRequestTopic;
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;
    private KafkaConsumer<String, String> kafkaConsumer;

    public KafkaUserInfoResponder(KafkaTemplate<String, String> kafkaTemplate, ConsumerFactory<String, String> consumerFactory, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.consumerFactory = consumerFactory;
        this.objectMapper = objectMapper;

    }

    @PostConstruct
    public void init() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "address-info-group");
        this.kafkaConsumer = new KafkaConsumer<>(properties);
        this.kafkaConsumer.subscribe(Collections.singletonList(addressResponseTopic));
    }

    @PreDestroy
    public void cleanup() {
        if (kafkaConsumer != null) {
            kafkaConsumer.close();
        }
    }

    public <T> T getInfoByUserId(Long userId, Class<T> responseType) throws InterruptedException, ExecutionException {
        long startTime = System.currentTimeMillis();
        var requestKey = UUID.randomUUID().toString();
        kafkaTemplate.send(addressRequestTopic, requestKey, userId.toString()).get();

        ConsumerRecord<String, String> responseRecord = null;
        long startPolling = System.currentTimeMillis();
        while (responseRecord == null && (System.currentTimeMillis() - startPolling) < 5000) {
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
        log.info(" Duration Time: {} ms", duration);
        if (responseRecord != null) {
            try {
                return objectMapper.readValue(responseRecord.value(), responseType);
            } catch (Exception e) {
                throw new RuntimeException("Failed to deserialize response", e);
            }
        } else {
            return null;
        }
    }
}
