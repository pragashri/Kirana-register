package com.example.springboot.config;

import static com.example.springboot.config.constants.ConfigConstants.KAFKA_BOOTSTRAP_SERVERS;

import com.example.springboot.config.constants.ConfigConstants;
import com.example.springboot.feature_report.dto.KafkaMessage;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

/** Configuration class for setting up Kafka producer, consumer, and topic. */
@Configuration
public class KafkaConfig {

    private static final String KAFKA_SERVER = KAFKA_BOOTSTRAP_SERVERS;
    private static final String GROUP_ID = ConfigConstants.GROUP_ID;

    /**
     * Configures the Kafka producer factory, which is responsible for creating producer instances.
     *
     * @return a ProducerFactory that serializes keys as Strings and values as KafkaMessage objects.
     */
    @Bean
    public ProducerFactory<String, KafkaMessage> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_SERVER);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    /**
     * Creates a KafkaTemplate, which is used for sending messages to Kafka topics.
     *
     * @return a KafkaTemplate configured with the producer factory.
     */
    @Bean
    public KafkaTemplate<String, KafkaMessage> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    /**
     * Configures the Kafka consumer factory, which is responsible for creating consumer instances.
     *
     * @return a ConsumerFactory that deserializes messages as KafkaMessage objects.
     */
    @Bean
    public ConsumerFactory<String, KafkaMessage> consumerFactory() {
        JsonDeserializer<KafkaMessage> deserializer = new JsonDeserializer<>(KafkaMessage.class);
        deserializer.addTrustedPackages("*");

        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_SERVER);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);

        return new DefaultKafkaConsumerFactory<>(
                configProps, new StringDeserializer(), deserializer);
    }

    /**
     * Creates a Kafka listener container factory, which is used to manage Kafka listeners.
     *
     * @return a ConcurrentKafkaListenerContainerFactory configured with the consumer factory.
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, KafkaMessage>
            kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, KafkaMessage> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    /**
     * Defines a new Kafka topic named "generate-report" with one partition and one replica.
     *
     * @return a NewTopic instance representing the Kafka topic.
     */
    @Bean
    public NewTopic reportTopic() {
        return new NewTopic("generate-report", 1, (short) 1);
    }
}
