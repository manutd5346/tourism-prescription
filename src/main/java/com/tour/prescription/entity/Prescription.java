package com.tour.prescription.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 관광 처방 테이블
 * → DB 테이블명: prescriptions
 */
@Entity
@Table(name = "prescriptions")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 처방번호 (예: RX-2026-384710)
    @Column(name = "rx_no", unique = true, nullable = false, length = 30)
    private String rxNo;

    // 환자 (N:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    // 담당 의사
    @Column(name = "doctor_name", length = 100)
    private String doctorName;

    // 처방 기관
    @Column(name = "hospital_name", length = 100)
    private String hospitalName;

    // 처방 사유 (사회적 고립 / 정서적 회복 / 신체 재활 등)
    @Column(name = "prescription_reason", length = 100)
    private String prescriptionReason;

    // 처방 소견
    @Column(name = "clinical_note", columnDefinition = "TEXT")
    private String clinicalNote;

    // 선택 프로그램 (자연 힐링 여행 / 웰니스 케어 여행 등)
    @Column(name = "program_type", length = 100)
    private String programType;

    // TourAPI 관광지 contentId (선택한 관광지)
    @Column(name = "content_id", length = 30)
    private String contentId;

    // 관광지명
    @Column(name = "tour_place_name", length = 200)
    private String tourPlaceName;

    // 여행 기간 (당일 / 1박2일 / 2박3일 등)
    @Column(name = "tour_period", length = 30)
    private String tourPeriod;

    // 출발 희망일
    @Column(name = "departure_date")
    private LocalDate departureDate;

    // 동반 인원
    @Column(name = "companion_count")
    private Integer companionCount;

    // 지원 항목 (교통비, 숙박비 등 콤마 구분)
    @Column(name = "support_items", length = 200)
    private String supportItems;

    // 처방 상태 (DRAFT / ISSUED / CANCELLED)
    @Column(name = "status", length = 20)
    @Builder.Default
    private String status = "ISSUED";

    @CreationTimestamp
    @Column(name = "issued_at", updatable = false)
    private LocalDateTime issuedAt;
}
