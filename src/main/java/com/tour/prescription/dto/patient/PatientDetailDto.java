package com.tour.prescription.dto.patient;

import com.tour.prescription.entity.ClinicalScore;
import com.tour.prescription.entity.Disease;
import com.tour.prescription.entity.Patient;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 처방 화면에서 사용할 환자 상세 정보 (인적정보 + 상병이력 + 임상점수 통합)
 */
@Data
public class PatientDetailDto {

    private Long id;
    private String patientNo;
    private String name;
    private String birthDate;
    private String gender;          // 추후 확장용 (현재는 미사용, null 가능)
    private String phone;
    private String areaCode;
    private String areaName;
    private String targetType;
    private String guardianName;
    private String diagnosisCode;
    private String diagnosisName;

    private List<DiseaseDto> diseases;
    private List<ClinicalScoreDto> clinicalScores;

    @Data
    public static class DiseaseDto {
        private String icdCode;
        private String diseaseName;
        private String diagnosedDate;
        private String doctorName;
        private String status;

        public static DiseaseDto from(Disease d) {
            DiseaseDto dto = new DiseaseDto();
            dto.setIcdCode(d.getIcdCode());
            dto.setDiseaseName(d.getDiseaseName());
            dto.setDiagnosedDate(d.getDiagnosedDate() != null ? d.getDiagnosedDate().toString() : null);
            dto.setDoctorName(d.getDoctorName());
            dto.setStatus(d.getStatus());
            return dto;
        }
    }

    @Data
    public static class ClinicalScoreDto {
        private String scoreType;
        private Integer scoreValue;
        private String scoreLevel;
        private String measuredDate;

        public static ClinicalScoreDto from(ClinicalScore c) {
            ClinicalScoreDto dto = new ClinicalScoreDto();
            dto.setScoreType(c.getScoreType());
            dto.setScoreValue(c.getScoreValue());
            dto.setScoreLevel(c.getScoreLevel());
            dto.setMeasuredDate(c.getMeasuredDate() != null ? c.getMeasuredDate().toString() : null);
            return dto;
        }
    }

    public static PatientDetailDto from(Patient p, List<Disease> diseases, List<ClinicalScore> scores) {
        PatientDetailDto dto = new PatientDetailDto();
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
        dto.setDiseases(diseases.stream().map(DiseaseDto::from).collect(Collectors.toList()));
        dto.setClinicalScores(scores.stream().map(ClinicalScoreDto::from).collect(Collectors.toList()));
        return dto;
    }
}
