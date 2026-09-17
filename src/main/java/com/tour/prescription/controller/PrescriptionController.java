package com.tour.prescription.controller;

import com.tour.prescription.dto.prescription.PrescriptionRequestDto;
import com.tour.prescription.dto.prescription.PrescriptionResponseDto;
import com.tour.prescription.entity.Prescription;
import com.tour.prescription.repository.PrescriptionRepository;
import com.tour.prescription.service.PrescriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 관광 처방 오더 컨트롤러
 *
 * POST /api/prescription/issue              처방 발행
 * GET  /api/prescription/patient/{id}       환자별 처방 이력
 * GET  /api/prescription/rx/{rxNo}          처방번호로 단건 조회
 */
@RestController
@RequestMapping("/api/prescription")
@RequiredArgsConstructor
public class PrescriptionController {

    private final PrescriptionService prescriptionService;
    private final PrescriptionRepository prescriptionRepository;

    // 처방 오더 발행
    @PostMapping("/issue")
    public ResponseEntity<?> issue(@RequestBody PrescriptionRequestDto dto) {
        PrescriptionResponseDto result = prescriptionService.issuePrescription(dto);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "처방 오더가 발행되었습니다.",
                "prescription", result
        ));
    }

    // 환자별 처방 이력 조회
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<?> getByPatient(@PathVariable Long patientId) {
        List<PrescriptionResponseDto> list = prescriptionService.getPrescriptionsByPatient(patientId);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "count", list.size(),
                "prescriptions", list
        ));
    }

    // 처방번호로 단건 조회
    @GetMapping("/rx/{rxNo}")
    public ResponseEntity<?> getByRxNo(@PathVariable String rxNo) {
        PrescriptionResponseDto result = prescriptionService.getByRxNo(rxNo);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "prescription", result
        ));
    }

    // 전체 처방 목록 (최신순)
    @GetMapping("/list")
    public ResponseEntity<?> getAllPrescriptions(
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "20") int numOfRows) {

        List<Prescription> all = prescriptionRepository
                .findAll(org.springframework.data.domain.Sort.by(
                        org.springframework.data.domain.Sort.Direction.DESC, "issuedAt"));

        List<PrescriptionResponseDto> list = all.stream()
                .map(PrescriptionResponseDto::from)
                .collect(java.util.stream.Collectors.toList());

        return ResponseEntity.ok(Map.of(
                "success", true,
                "count", list.size(),
                "prescriptions", list
        ));
    }

    // 처방 취소
    @PatchMapping("/cancel/{rxNo}")
    public ResponseEntity<?> cancelPrescription(@PathVariable String rxNo) {
        Prescription prescription = prescriptionRepository.findByRxNo(rxNo)
                .orElseThrow(() -> new IllegalArgumentException("처방을 찾을 수 없습니다. rxNo=" + rxNo));

        if ("CANCELLED".equals(prescription.getStatus())) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "이미 취소된 처방입니다."
            ));
        }

        prescription.setStatus("CANCELLED");
        prescriptionRepository.save(prescription);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "처방이 취소되었습니다.",
                "rxNo", rxNo
        ));
    }

}
