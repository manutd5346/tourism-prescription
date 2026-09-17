package com.tour.prescription.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 환자(대상자) 테이블
 */
@Entity
@Table(name = "patients")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(length = 20)
    private String phone;

    @Column(name = "area_code", length = 10)
    private String areaCode;

    @Column(name = "area_name", length = 50)
    private String areaName;

    @Column(name = "target_type", length = 100)
    private String targetType;

    @Column(name = "guardian_name", length = 50)
    private String guardianName;

    @Column(name = "patient_no", unique = true, length = 30)
    private String patientNo;

    @Column(name = "diagnosis_code", length = 20)
    private String diagnosisCode;

    @Column(name = "diagnosis_name", length = 200)
    private String diagnosisName;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Prescription> prescriptions = new ArrayList<>();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Disease> diseases = new ArrayList<>();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ClinicalScore> clinicalScores = new ArrayList<>();
}
