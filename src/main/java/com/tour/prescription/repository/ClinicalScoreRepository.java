package com.tour.prescription.repository;

import com.tour.prescription.entity.ClinicalScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClinicalScoreRepository extends JpaRepository<ClinicalScore, Long> {
    List<ClinicalScore> findByPatientIdOrderByMeasuredDateDesc(Long patientId);
}
