package com.tour.prescription.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 환자 임상 평가 점수 (PHQ-9, GAD-7, SF-8 등)
 */
@Entity
@Table(name = "clinical_scores")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClinicalScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(name = "score_type", length = 20, nullable = false)
    private String scoreType;     // PHQ9 / GAD7 / SF8

    @Column(name = "score_value", nullable = false)
    private Integer scoreValue;   // 19

    @Column(name = "score_level", length = 20)
    private String scoreLevel;    // 중증 / 중등도 / 심각

    @Column(name = "measured_date")
    private LocalDate measuredDate;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
