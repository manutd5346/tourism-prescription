package com.tour.prescription.dto.patient;

import com.tour.prescription.entity.Patient;
import lombok.Data;

@Data
public class PatientResponseDto {

    private Long id;
    private String patientNo;       // 등록번호 (P-yyyyMMdd-XXXX)
    private String name;
    private String birthDate;
    private String phone;
    private String areaCode;
    private String areaName;
    private String targetType;
    private String guardianName;
    private String diagnosisCode;
    private String diagnosisName;
    private String createdAt;

    // Entity → DTO 변환
    public static PatientResponseDto from(Patient p) {
        PatientResponseDto dto = new PatientResponseDto();
        dto.setId(p.getId());
        dto.setPatientNo(p.getPatientNo());
        dto.setName(p.getName());
        dto.setBirthDate(p.getBirthDate() != null ? p.getBirthDate().toString() : null);
        dto.setPhone(p.getPhone());
        dto.setAreaCode(p.getAreaCode());
        dto.setAreaName(p.getAreaName());
        dto.setTargetType(p.getTargetType());
        dto.setGuardianName(p.getGuardianName());
        dto.setDiagnosisCode(p.getDiagnosisCode());
        dto.setDiagnosisName(p.getDiagnosisName());
        dto.setCreatedAt(p.getCreatedAt() != null ? p.getCreatedAt().toString() : null);
        return dto;
    }
}
