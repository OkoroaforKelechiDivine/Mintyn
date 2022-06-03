package com.reporting.data.repositories;

import com.reporting.data.models.ReportModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<ReportModel, Long> {
}
