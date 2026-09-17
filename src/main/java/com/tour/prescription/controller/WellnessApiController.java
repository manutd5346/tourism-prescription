package com.tour.prescription.controller;

import com.tour.prescription.dto.wellness.WellnessItem;
import com.tour.prescription.service.WellnessApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/wellness")
@RequiredArgsConstructor
public class WellnessApiController {

    private final WellnessApiService wellnessApiService;

    private static final Map<String, String> AREA_TO_LDONG = Map.ofEntries(
            Map.entry("1",  "11"),  // 서울
            Map.entry("2",  "28"),  // 인천
            Map.entry("3",  "30"),  // 대전
            Map.entry("4",  "27"),  // 대구
            Map.entry("5",  "29"),  // 광주
            Map.entry("6",  "26"),  // 부산
            Map.entry("7",  "31"),  // 울산
            Map.entry("31", "41"),  // 경기
            Map.entry("32", "51"),  // 강원
            Map.entry("33", "43"),  // 충북
            Map.entry("34", "44"),  // 충남
            Map.entry("35", "47"),  // 경북
            Map.entry("36", "48"),  // 경남
            Map.entry("37", "45"),  // 전북
            Map.entry("38", "46"),  // 전남
            Map.entry("39", "50")   // 제주
    );

    @GetMapping("/list")
    public ResponseEntity<?> getList(
            @RequestParam(defaultValue = "1")  int pageNo,
            @RequestParam(defaultValue = "10") int numOfRows) {

        List<WellnessItem> items = wellnessApiService.getWellnessList("", pageNo, numOfRows);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "pageNo", pageNo,
                "count", items.size(),
                "items", items
        ));
    }

    @GetMapping("/list/{areaCode}")
    public ResponseEntity<?> getListByArea(
            @PathVariable String areaCode,
            @RequestParam(defaultValue = "1")  int pageNo,
            @RequestParam(defaultValue = "10") int numOfRows) {

        String lDongRegnCd = AREA_TO_LDONG.getOrDefault(areaCode, areaCode);

        List<WellnessItem> items = wellnessApiService.getWellnessList(lDongRegnCd, pageNo, numOfRows);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "areaCode", areaCode,
                "pageNo", pageNo,
                "count", items.size(),
                "items", items
        ));
    }
}