package com.ceyloncab.authproxymgtservice.application.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaProducerConfig {

    @Value(value = "${kafka.producer.bootstrapServers}")
    private String bootstrapAddress;

    @Bean
    public ProducerFactory<String, Object> multiTypeProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        configProps.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        configProps.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        configProps.put(ProducerConfig.ACKS_CONFIG, "1");
//        configProps.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "lz4");
        configProps.put(ProducerConfig.RETRIES_CONFIG, 1);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        //TODO
        configProps.put(JsonSerializer.TYPE_MAPPINGS,
                "confirmPickup:com.ceyloncab.authproxymgtservice.domain.entity.dto.kafka.ConfirmPickup," +
                        "tripPriceForAllVehicles:com.ceyloncab.authproxymgtservice.domain.entity.dto.kafka.TripPricesForAllVehicles," +
                        "acceptTrip:com.ceyloncab.authproxymgtservice.domain.entity.dto.kafka.UpDateStatusOnTrip," +
                        "endTrip:com.ceyloncab.authproxymgtservice.domain.entity.dto.kafka.EndTrip");
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(multiTypeProducerFactory());
    }
}