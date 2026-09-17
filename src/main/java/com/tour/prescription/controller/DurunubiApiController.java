package com.tour.prescription.controller;

import com.tour.prescription.dto.durunubi.DurunubiCourse;
import com.tour.prescription.service.DurunubiApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 두루누비 코스 정보 컨트롤러
 *
 * GET /api/durunubi/courses
 */
@RestController
@RequestMapping("/api/durunubi")
@RequiredArgsConstructor
public class DurunubiApiController {

    private final DurunubiApiService durunubiApiService;

    @GetMapping("/courses")
    public ResponseEntity<?> getCourses(
            @RequestParam(defaultValue = "1")  int pageNo,
            @RequestParam(defaultValue = "10") int numOfRows) {

        List<DurunubiCourse> items = durunubiApiService.getCourseList(pageNo, numOfRows);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "pageNo", pageNo,
                "count", items.size(),
                "items", items
        ));
    }
}
