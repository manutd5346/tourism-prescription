package com.tour.prescription.service;

import com.tour.prescription.dto.durunubi.DurunubiCourse;
import com.tour.prescription.dto.durunubi.DurunubiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;

/**
 * 두루누비(코리아둘레길) 코스 정보 호출
 *
 * 주의: 실제 파라미터/필드명은 승인 후 브라우저 직접 호출로 재확인 필요
 * (웰니스 API처럼 매뉴얼과 실제 응답이 다를 수 있음)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DurunubiApiService {

    private final WebClient tourApiWebClient;

    @Value("${tourapi.service-key}")
    private String serviceKey;

    @Value("${tourapi.durunubi.base-url}")
    private String baseUrl;

    @Value("${tourapi.durunubi.endpoint.course}")
    private String courseEndpoint;

    @Value("${tourapi.mobile-os}")
    private String mobileOs;

    @Value("${tourapi.mobile-app}")
    private String mobileApp;

    /**
     * 코스 목록 조회
     */
    public List<DurunubiCourse> getCourseList(int pageNo, int numOfRows) {

        URI uri = UriComponentsBuilder
                .fromHttpUrl(baseUrl + courseEndpoint)
                .queryParam("serviceKey", serviceKey)
                .queryParam("numOfRows", numOfRows)
                .queryParam("pageNo", pageNo)
                .queryParam("MobileOS", mobileOs)
                .queryParam("MobileApp", mobileApp)
                .queryParam("_type", "json")
                .build(false)
                .toUri();

        log.info("[두루누비] 실제 호출 URL: {}", uri.toString());

        try {
            DurunubiResponse response = tourApiWebClient
                    .get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(DurunubiResponse.class)
                    .block();

            if (response == null
                    || response.getResponse() == null
                    || response.getResponse().getBody() == null
                    || response.getResponse().getBody().getItems() == null
                    || response.getResponse().getBody().getItems().getItem() == null) {
                log.warn("[두루누비] 응답 데이터 없음");
                return Collections.emptyList();
            }

            String resultCode = response.getResponse().getHeader().getResultCode();
            if (!"0000".equals(resultCode)) {
                log.error("[두루누비] 오류 코드={}, 메시지={}",
                        resultCode, response.getResponse().getHeader().getResultMsg());
                return Collections.emptyList();
            }

            List<DurunubiCourse> items = response.getResponse().getBody().getItems().getItem();
            log.info("[두루누비] 조회 성공 → {}건", items.size());
            return items;

        } catch (Exception e) {
            log.error("[두루누비] 호출 오류: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }
}
