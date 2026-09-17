package com.tour.prescription.service;

import com.tour.prescription.dto.patient.*;
import com.tour.prescription.entity.ClinicalScore;
import com.tour.prescription.entity.Disease;
import com.tour.prescription.entity.Patient;
import com.tour.prescription.repository.ClinicalScoreRepository;
import com.tour.prescription.repository.DiseaseRepository;
import com.tour.prescription.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final DiseaseRepository diseaseRepository;
    private final ClinicalScoreRepository clinicalScoreRepository;

    @Transactional
    public PatientResponseDto registerPatient(PatientRequestDto dto) {

        String patientNo = generatePatientNo();

        Patient patient = Patient.builder()
                .patientNo(patientNo)
                .name(dto.getName())
                .birthDate(dto.getBirthDate() != null
                        ? LocalDate.parse(dto.getBirthDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                        : null)
                .phone(dto.getPhone())
                .areaCode(dto.getAreaCode())
                .areaName(dto.getAreaName())
                .targetType(dto.getTargetType())
                .guardianName(dto.getGuardianName())
                .diagnosisCode(dto.getDiagnosisCode())
                .diagnosisName(dto.getDiagnosisName())
                .build();

        Patient saved = patientRepository.save(patient);
        log.info("[환자등록] patientNo={}, name={}", patientNo, dto.getName());

        return PatientResponseDto.from(saved);
    }

    @Transactional(readOnly = true)
    public List<PatientResponseDto> getAllPatients() {
        return patientRepository.findAll()
                .stream()
                .map(PatientResponseDto::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PatientResponseDto getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("환자를 찾을 수 없습니다. id=" + id));
        return PatientResponseDto.from(patient);
    }

    /**
     * 처방 화면용 환자 상세 조회 (인적정보 + 상병이력 + 임상점수)
     */
    @Transactional(readOnly = true)
    public PatientDetailDto getPatientDetail(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("환자를 찾을 수 없습니다. id=" + id));

        List<Disease> diseases = diseaseRepository.findByPatientIdOrderByDiagnosedDateDesc(id);
        List<ClinicalScore> scores = clinicalScoreRepository.findByPatientIdOrderByMeasuredDateDesc(id);

        return PatientDetailDto.from(patient, diseases, scores);
    }

    /**
     * 상병 이력 추가
     */
    @Transactional
    public void addDisease(DiseaseRequestDto dto) {
        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("환자를 찾을 수 없습니다. id=" + dto.getPatientId()));

        Disease disease = Disease.builder()
                .patient(patient)
                .icdCode(dto.getIcdCode())
                .diseaseName(dto.getDiseaseName())
                .diagnosedDate(dto.getDiagnosedDate() != null
                        ? LocalDate.parse(dto.getDiagnosedDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                        : null)
                .doctorName(dto.getDoctorName())
                .status(dto.getStatus() != null ? dto.getStatus() : "ACTIVE")
                .build();

        diseaseRepository.save(disease);
        log.info("[상병등록] patientId={}, icdCode={}", dto.getPatientId(), dto.getIcdCode());
    }

    /**
     * 임상 평가 점수 추가
     */
    @Transactional
    public void addClinicalScore(ClinicalScoreRequestDto dto) {
        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("환자를 찾을 수 없습니다. id=" + dto.getPatientId()));

        ClinicalScore score = ClinicalScore.builder()
                .patient(patient)
                .scoreType(dto.getScoreType())
                .scoreValue(dto.getScoreValue())
                .scoreLevel(dto.getScoreLevel())
                .measuredDate(dto.getMeasuredDate() != null
                        ? LocalDate.parse(dto.getMeasuredDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                        : LocalDate.now())
                .build();

        clinicalScoreRepository.save(score);
        log.info("[임상점수등록] patientId={}, type={}, value={}", dto.getPatientId(), dto.getScoreType(), dto.getScoreValue());
    }

    private String generatePatientNo() {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String seq  = String.format("%04d", (int)(Math.random() * 9000) + 1000);
        String no   = "P-" + date + "-" + seq;

        while (patientRepository.existsByPatientNo(no)) {
            seq = String.format("%04d", (int)(Math.random() * 9000) + 1000);
            no  = "P-" + date + "-" + seq;
        }
        return no;
    }
}
