package com.reporting.controllers;

import com.reporting.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("/report")
public class ReportController {
    @Autowired
    private ReportService reportService;

    @GetMapping("/publish")
    public ResponseEntity<?> getMessage(@RequestParam("topic") String topic){
        return ResponseEntity.status(HttpStatus.OK).body(reportService.consume(topic));

    }
}
