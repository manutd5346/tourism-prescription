package com.tour.prescription.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 환자 상병 이력
 */
@Entity
@Table(name = "diseases")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Disease {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(name = "icd_code", length = 20, nullable = false)
    private String icdCode;          // F32.2

    @Column(name = "disease_name", length = 200, nullable = false)
    private String diseaseName;      // 중증 우울 삽화

    @Column(name = "diagnosed_date")
    private LocalDate diagnosedDate; // 진단일

    @Column(name = "doctor_name", length = 50)
    private String doctorName;       // 진단 의사

    @Column(name = "status", length = 20)
    @Builder.Default
    private String status = "ACTIVE"; // ACTIVE(치료중) / OBSERVING(관찰중)

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
