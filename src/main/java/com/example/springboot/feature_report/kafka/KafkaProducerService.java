package com.example.springboot.feature_report.kafka;

import com.example.springboot.feature_report.dto.KafkaMessage;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {
    private final KafkaTemplate<String, KafkaMessage> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, KafkaMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendReportRequest(KafkaMessage kafkaMessage) {
        kafkaTemplate.send("generate-report", kafkaMessage);
    }
}
