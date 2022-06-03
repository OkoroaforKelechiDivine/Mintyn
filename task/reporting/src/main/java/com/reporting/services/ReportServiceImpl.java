package com.reporting.services;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ReportServiceImpl implements ReportService{

    @KafkaListener(topics = "order_details",
            groupId = "group_id")
    @Override
    public String consume(String message) {
        return("Order details\n"+message);
    }
}
