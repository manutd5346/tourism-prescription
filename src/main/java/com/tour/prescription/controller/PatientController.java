package com.tour.prescription.controller;

import com.tour.prescription.dto.patient.*;
import com.tour.prescription.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 환자 등록/조회 컨트롤러
 *
 * POST /api/patient/register             환자 등록
 * GET  /api/patient/list                 전체 목록 (드롭다운용)
 * GET  /api/patient/{id}                 단건 조회 (요약)
 * GET  /api/patient/{id}/detail          상세 조회 (상병이력+임상점수 포함, 처방화면용)
 * POST /api/patient/disease              상병 이력 추가
 * POST /api/patient/clinical-score       임상 평가 점수 추가
 */
@RestController
@RequestMapping("/api/patient")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody PatientRequestDto dto) {
        PatientResponseDto result = patientService.registerPatient(dto);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "환자가 등록되었습니다.",
                "patient", result
        ));
    }

    @GetMapping("/list")
    public ResponseEntity<?> list() {
        List<PatientResponseDto> patients = patientService.getAllPatients();
        return ResponseEntity.ok(Map.of(
                "success", true,
                "count", patients.size(),
                "patients", patients
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        PatientResponseDto patient = patientService.getPatientById(id);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "patient", patient
        ));
    }

    @GetMapping("/{id}/detail")
    public ResponseEntity<?> getDetail(@PathVariable Long id) {
        PatientDetailDto detail = patientService.getPatientDetail(id);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "patient", detail
        ));
    }

    @PostMapping("/disease")
    public ResponseEntity<?> addDisease(@RequestBody DiseaseRequestDto dto) {
        patientService.addDisease(dto);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "상병 이력이 등록되었습니다."
        ));
    }

    @PostMapping("/clinical-score")
    public ResponseEntity<?> addClinicalScore(@RequestBody ClinicalScoreRequestDto dto) {
        patientService.addClinicalScore(dto);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "임상 평가 점수가 등록되었습니다."
        ));
    }
}
