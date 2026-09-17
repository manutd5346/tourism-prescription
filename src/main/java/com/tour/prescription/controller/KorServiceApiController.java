package com.tour.prescription.controller;

import com.tour.prescription.dto.korservice.KorServiceItem;
import com.tour.prescription.service.KorServiceApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 국문 관광정보 서비스 컨트롤러
 *
 * GET /api/korservice/list?areaCode=32&contentTypeId=12
 *
 * contentTypeId: 12=관광지 14=문화시설 15=축제/공연/행사 25=여행코스 28=레포츠 32=숙박 38=쇼핑 39=음식점
 */
@RestController
@RequestMapping("/api/korservice")
@RequiredArgsConstructor
public class KorServiceApiController {

    private final KorServiceApiService korServiceApiService;

    @GetMapping("/list")
    public ResponseEntity<?> getList(
            @RequestParam(required = false) String areaCode,
            @RequestParam(required = false) String contentTypeId,
            @RequestParam(defaultValue = "1")  int pageNo,
            @RequestParam(defaultValue = "10") int numOfRows) {

        List<KorServiceItem> items = korServiceApiService.getAreaBasedList(areaCode, contentTypeId, pageNo, numOfRows);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "areaCode", areaCode == null ? "" : areaCode,
                "contentTypeId", contentTypeId == null ? "" : contentTypeId,
                "pageNo", pageNo,
                "count", items.size(),
                "items", items
        ));
    }

    // 생태관광 목록
    @GetMapping("/eco")
    public ResponseEntity<?> getEcoList(
            @RequestParam(required = false) String areaCode,
            @RequestParam(defaultValue = "1")  int pageNo,
            @RequestParam(defaultValue = "9") int numOfRows) {

        List<KorServiceItem> items = korServiceApiService.getEcoTourList(areaCode, pageNo, numOfRows);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "areaCode", areaCode == null ? "" : areaCode,
                "count", items.size(),
                "items", items
        ));
    }
}
