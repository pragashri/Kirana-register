package com.example.springboot.feature_report.kafka;

import com.example.springboot.feature_report.dto.KafkaMessage;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Service responsible for producing Kafka messages related to report generation. This class sends
 * messages to the "generate-report" topic to request report generation.
 */
@Service
public class KafkaProducerService {
    private final KafkaTemplate<String, KafkaMessage> kafkaTemplate;

    /**
     * Constructor-based dependency injection for KafkaTemplate.
     *
     * @param kafkaTemplate Kafka template used to send messages to Kafka topics.
     */
    public KafkaProducerService(KafkaTemplate<String, KafkaMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Sends a report generation request to the "generate-report" Kafka topic.
     *
     * @param kafkaMessage The Kafka message containing the report type and transaction data.
     */
    public void sendReportRequest(KafkaMessage kafkaMessage) {
        kafkaTemplate.send("generate-report", kafkaMessage);
    }
}
