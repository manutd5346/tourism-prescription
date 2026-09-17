package com.tour.prescription.dto.prescription;

import lombok.Data;

@Data
public class PrescriptionRequestDto {

    private Long patientId;           // 환자 ID
    private String doctorName;        // 처방 의사
    private String hospitalName;      // 처방 기관
    private String prescriptionReason;// 처방 사유
    private String clinicalNote;      // 임상 소견
    private String programType;       // 선택 프로그램
    private String contentId;         // TourAPI contentId
    private String tourPlaceName;     // 관광지명
    private String tourPeriod;        // 여행 기간
    private String departureDate;     // 출발 희망일 (yyyy-MM-dd)
    private Integer companionCount;   // 동반 인원
    private String supportItems;      // 지원 항목 (콤마 구분)
}
