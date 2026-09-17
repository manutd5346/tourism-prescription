package com.tour.prescription.dto.patient;

import lombok.Data;

@Data
public class ClinicalScoreRequestDto {
    private Long patientId;
    private String scoreType;       // PHQ9 / GAD7 / SF8
    private Integer scoreValue;
    private String scoreLevel;      // 중증 / 중등도 / 심각
    private String measuredDate;    // yyyy-MM-dd
}
