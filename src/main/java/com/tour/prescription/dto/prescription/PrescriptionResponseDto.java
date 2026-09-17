package com.tour.prescription.dto.prescription;

import com.tour.prescription.entity.Prescription;
import lombok.Data;

@Data
public class PrescriptionResponseDto {

    private Long id;
    private String rxNo;               // 처방번호 (RX-2026-XXXXXX)
    private Long patientId;
    private String patientName;        // 환자 이름
    private String patientNo;          // 환자 등록번호
    private String doctorName;         // 처방 의사
    private String hospitalName;       // 처방 기관
    private String prescriptionReason; // 처방 사유
    private String clinicalNote;       // 임상 소견
    private String programType;        // 선택 프로그램
    private String contentId;          // TourAPI contentId
    private String tourPlaceName;      // 관광지명
    private String tourPeriod;         // 여행 기간
    private String departureDate;      // 출발 희망일
    private Integer companionCount;    // 동반 인원
    private String supportItems;       // 지원 항목
    private String status;             // 처방 상태
    private String issuedAt;           // 발행 일시

    // Entity → DTO 변환
    public static PrescriptionResponseDto from(Prescription p) {
        PrescriptionResponseDto dto = new PrescriptionResponseDto();
        dto.setId(p.getId());
        dto.setRxNo(p.getRxNo());
        dto.setPatientId(p.getPatient().getId());
        dto.setPatientName(p.getPatient().getName());
        dto.setPatientNo(p.getPatient().getPatientNo());
        dto.setDoctorName(p.getDoctorName());
        dto.setHospitalName(p.getHospitalName());
        dto.setPrescriptionReason(p.getPrescriptionReason());
        dto.setClinicalNote(p.getClinicalNote());
        dto.setProgramType(p.getProgramType());
        dto.setContentId(p.getContentId());
        dto.setTourPlaceName(p.getTourPlaceName());
        dto.setTourPeriod(p.getTourPeriod());
        dto.setDepartureDate(p.getDepartureDate() != null ? p.getDepartureDate().toString() : null);
        dto.setCompanionCount(p.getCompanionCount());
        dto.setSupportItems(p.getSupportItems());
        dto.setStatus(p.getStatus());
        dto.setIssuedAt(p.getIssuedAt() != null ? p.getIssuedAt().toString() : null);
        return dto;
    }
}
