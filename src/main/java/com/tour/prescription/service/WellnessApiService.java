package com.tour.prescription.service;

import com.tour.prescription.dto.wellness.WellnessItem;
import com.tour.prescription.dto.wellness.WellnessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WellnessApiService {

    private final WebClient tourApiWebClient;

    @Value("${tourapi.service-key}")
    private String serviceKey;

    @Value("${tourapi.wellness.base-url}")
    private String baseUrl;

    @Value("${tourapi.wellness.endpoint}")
    private String endpoint;

    @Value("${tourapi.mobile-os}")
    private String mobileOs;

    @Value("${tourapi.mobile-app}")
    private String mobileApp;

    /**
     * 웰니스 관광지 목록 조회
     *
     * @param lDongRegnCd  지역코드 (빈값 = 전국)
     * @param pageNo    페이지 번호
     * @param numOfRows 페이지당 결과 수
     */
    public List<WellnessItem> getWellnessList(String lDongRegnCd, int pageNo, int numOfRows) {

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(baseUrl + endpoint)
                .queryParam("serviceKey", serviceKey)
                .queryParam("numOfRows", numOfRows)
                .queryParam("pageNo", pageNo)
                .queryParam("MobileOS", mobileOs)
                .queryParam("MobileApp", mobileApp)
                .queryParam("_type", "json")
                .queryParam("langDivCd", "KOR");   // ← 필수 파라미터

        // lDongRegnCd 있을 때만 추가
        if (lDongRegnCd != null && !lDongRegnCd.isBlank()) {
            builder.queryParam("lDongRegnCd", lDongRegnCd);  // ← areaCode → lDongRegnCd
        }

        URI uri = builder.build(false).toUri();
        log.info("[TourAPI] 웰니스 API 호출 → areaCode={}, page={}, rows={}", lDongRegnCd, pageNo, numOfRows);

        try {
            WellnessResponse response = tourApiWebClient
                    .get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(WellnessResponse.class)
                    .block();

            if (response == null
                    || response.getResponse() == null
                    || response.getResponse().getBody() == null
                    || response.getResponse().getBody().getItems() == null
                    || response.getResponse().getBody().getItems().getItem() == null) {
                log.warn("[TourAPI] 응답 데이터 없음");
                return Collections.emptyList();
            }

            String resultCode = response.getResponse().getHeader().getResultCode();
            if (!"0000".equals(resultCode)) {
                log.error("[TourAPI] 오류 코드={}, 메시지={}",
                        resultCode, response.getResponse().getHeader().getResultMsg());
                return Collections.emptyList();
            }

            List<WellnessItem> items = response.getResponse().getBody().getItems().getItem();
            int total = response.getResponse().getBody().getTotalCount();
            log.info("[TourAPI] 조회 성공 → 전체 {}건 중 {}건", total, items.size());
            return items;

        } catch (Exception e) {
            log.error("[TourAPI] 호출 오류: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }
}
