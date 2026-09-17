package com.tour.prescription.dto.patient;

import lombok.Data;

@Data
public class PatientRequestDto {

    private String name;            // 이름 (필수)
    private String birthDate;       // 생년월일 (yyyy-MM-dd)
    private String phone;           // 연락처
    private String areaCode;        // 거주 지역코드 (TourAPI 코드)
    private String areaName;        // 거주 지역명 (예: 강원도 원주시)
    private String targetType;      // 대상 구분 (저소득층, 장애인 등)
    private String guardianName;    // 보호자 이름
    private String diagnosisCode;   // 주진단 ICD 코드 (예: F32.2)
    private String diagnosisName;   // 주진단명
}
