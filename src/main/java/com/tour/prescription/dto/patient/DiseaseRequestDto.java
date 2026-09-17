package com.tour.prescription.dto.patient;

import lombok.Data;

@Data
public class DiseaseRequestDto {
    private Long patientId;
    private String icdCode;
    private String diseaseName;
    private String diagnosedDate;   // yyyy-MM-dd
    private String doctorName;
    private String status;          // ACTIVE / OBSERVING
}
