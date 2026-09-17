package com.tour.prescription.repository;

import com.tour.prescription.entity.Disease;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiseaseRepository extends JpaRepository<Disease, Long> {
    List<Disease> findByPatientIdOrderByDiagnosedDateDesc(Long patientId);
}
