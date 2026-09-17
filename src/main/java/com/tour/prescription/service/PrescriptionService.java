package com.tour.prescription.service;

import com.tour.prescription.dto.prescription.PrescriptionRequestDto;
import com.tour.prescription.dto.prescription.PrescriptionResponseDto;
import com.tour.prescription.entity.Patient;
import com.tour.prescription.entity.Prescription;
import com.tour.prescription.repository.PatientRepository;
import com.tour.prescription.repository.PrescriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final PatientRepository patientRepository;

    /**
     * 처방 오더 발행
     */
    @Transactional
    public PrescriptionResponseDto issuePrescription(PrescriptionRequestDto dto) {

        // 환자 조회
        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("환자를 찾을 수 없습니다. id=" + dto.getPatientId()));

        // 처방번호 생성
        String rxNo = generateRxNo();

        // 처방 저장
        Prescription prescription = Prescription.builder()
                .rxNo(rxNo)
                .patient(patient)
                .doctorName(dto.getDoctorName())
                .hospitalName(dto.getHospitalName())
                .prescriptionReason(dto.getPrescriptionReason())
                .clinicalNote(dto.getClinicalNote())
                .programType(dto.getProgramType())
                .contentId(dto.getContentId())
                .tourPlaceName(dto.getTourPlaceName())
                .tourPeriod(dto.getTourPeriod())
                .departureDate(dto.getDepartureDate() != null
                        ? LocalDate.parse(dto.getDepartureDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                        : null)
                .companionCount(dto.getCompanionCount())
                .supportItems(dto.getSupportItems())
                .status("ISSUED")
                .build();

        Prescription saved = prescriptionRepository.save(prescription);
        log.info("[처방발행] rxNo={}, 환자={}", rxNo, patient.getName());

        return PrescriptionResponseDto.from(saved);
    }

    /**
     * 환자별 처방 이력 조회
     */
    @Transactional(readOnly = true)
    public List<PrescriptionResponseDto> getPrescriptionsByPatient(Long patientId) {
        return prescriptionRepository.findByPatientIdOrderByIssuedAtDesc(patientId)
                .stream()
                .map(PrescriptionResponseDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 처방번호 단건 조회
     */
    @Transactional(readOnly = true)
    public PrescriptionResponseDto getByRxNo(String rxNo) {
        Prescription p = prescriptionRepository.findByRxNo(rxNo)
                .orElseThrow(() -> new IllegalArgumentException("처방을 찾을 수 없습니다. rxNo=" + rxNo));
        return PrescriptionResponseDto.from(p);
    }

    /**
     * 처방번호 생성: RX-yyyy-XXXXXX
     */
    private String generateRxNo() {
        String year = String.valueOf(LocalDate.now().getYear());
        String seq  = String.format("%06d", (int)(Math.random() * 900000) + 100000);
        String rxNo = "RX-" + year + "-" + seq;

        while (prescriptionRepository.existsByRxNo(rxNo)) {
            seq  = String.format("%06d", (int)(Math.random() * 900000) + 100000);
            rxNo = "RX-" + year + "-" + seq;
        }
        return rxNo;
    }
}
