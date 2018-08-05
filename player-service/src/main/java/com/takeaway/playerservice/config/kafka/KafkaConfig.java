package com.takeaway.playerservice.config.kafka;

import com.takeaway.gamechallenge.events.GameActionEvent;
import com.takeaway.gamechallenge.events.GameEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConfig {

    @Bean
    public KafkaTemplate<String, GameActionEvent> kafkaTemplate(@Value("${spring.kafka.bootstrap-servers}") String bootstrapServers) {
        return new KafkaTemplate<>(producerFactory(bootstrapServers));
    }

    private ProducerFactory<String, GameActionEvent> producerFactory(String bootstrapServers) {
        Map<String, Object> configProps = new HashMap<>();

        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, GameEvent> gameEventFactory(@Value("${spring.kafka.bootstrap-servers}") String bootstrapServers) {
        ConcurrentKafkaListenerContainerFactory<String, GameEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory(bootstrapServers, GameEvent.class));
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, GameActionEvent> gameActionEventFactory(@Value("${spring.kafka.bootstrap-servers}") String bootstrapServers) {
        ConcurrentKafkaListenerContainerFactory<String, GameActionEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory(bootstrapServers, GameActionEvent.class));

        return factory;
    }

    private <T> ConsumerFactory<String, T> consumerFactory(@Value("${spring.kafka.bootstrap-servers}") String bootstrapServers, Class<T> clazz) {
        Map<String, Object> props = new HashMap<>();

        props.put(ConsumerConfig.GROUP_ID_CONFIG, "takeaway");
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(clazz));
    }
}
