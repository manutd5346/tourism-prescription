package com.tour.prescription.service;

import com.tour.prescription.dto.korservice.KorServiceItem;
import com.tour.prescription.dto.korservice.KorServiceResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
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
 * 국문 관광정보 서비스(KorService) 호출
 *
 * contentTypeId 코드표
 *   12 관광지   14 문화시설   15 축제/공연/행사
 *   25 여행코스 28 레포츠     32 숙박
 *   38 쇼핑     39 음식점
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KorServiceApiService {

    private final WebClient tourApiWebClient;

    @Value("${tourapi.service-key}")
    private String serviceKey;

    @Value("${tourapi.kor.base-url}")
    private String baseUrl;

    @Value("${tourapi.kor.endpoint.area}")
    private String areaEndpoint;

    @Value("${tourapi.mobile-os}")
    private String mobileOs;

    @Value("${tourapi.mobile-app}")
    private String mobileApp;

    /**
     * 지역기반 관광정보 조회
     *
     * @param areaCode      지역코드 (일반 시도코드, 서울=1, 강원=32 등 - KorService는 이 코드를 그대로 사용)
     * @param contentTypeId 콘텐츠 타입 (12,14,15,25,28,32,38,39 / 빈값이면 전체)
     */
    public List<KorServiceItem> getAreaBasedList(String areaCode, String contentTypeId, int pageNo, int numOfRows) {

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(baseUrl + areaEndpoint)
                .queryParam("serviceKey", serviceKey)
                .queryParam("numOfRows", numOfRows)
                .queryParam("pageNo", pageNo)
                .queryParam("MobileOS", mobileOs)
                .queryParam("MobileApp", mobileApp)
                .queryParam("_type", "json")
                .queryParam("arrange", "C");

        if (areaCode != null && !areaCode.isBlank()) {
            builder.queryParam("areaCode", areaCode);
        }
        if (contentTypeId != null && !contentTypeId.isBlank()) {
            builder.queryParam("contentTypeId", contentTypeId);
        }

        URI uri = builder.build(false).toUri();
        log.info("[KorService] 실제 호출 URL: {}", uri.toString());

        try {
            KorServiceResponse response = tourApiWebClient
                    .get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(KorServiceResponse.class)
                    .block();

            if (response == null
                    || response.getResponse() == null
                    || response.getResponse().getBody() == null
                    || response.getResponse().getBody().getItems() == null
                    || response.getResponse().getBody().getItems().getItem() == null) {
                log.warn("[KorService] 응답 데이터 없음");
                return Collections.emptyList();
            }

            String resultCode = response.getResponse().getHeader().getResultCode();
            if (!"0000".equals(resultCode)) {
                log.error("[KorService] 오류 코드={}, 메시지={}",
                        resultCode, response.getResponse().getHeader().getResultMsg());
                return Collections.emptyList();
            }

            List<KorServiceItem> items = response.getResponse().getBody().getItems().getItem();
            log.info("[KorService] 조회 성공 → {}건", items.size());
            return items;

        } catch (Exception e) {
            log.error("[KorService] 호출 오류: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    /**
     * 생태관광 목록 조회 (자연 힐링 여행 카드용)
     * cat3=A01010700 (생태관광지)
     */
    public List<KorServiceItem> getEcoTourList(String areaCode, int pageNo, int numOfRows) {

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(baseUrl + areaEndpoint)
                .queryParam("serviceKey", serviceKey)
                .queryParam("numOfRows", numOfRows)
                .queryParam("pageNo", pageNo)
                .queryParam("MobileOS", mobileOs)
                .queryParam("MobileApp", mobileApp)
                .queryParam("_type", "json")
                .queryParam("arrange", "C")
                .queryParam("contentTypeId", "12")   // 관광지
                .queryParam("cat1", "A01")            // 자연
                .queryParam("cat2", "A0101")          // 자연관광지
                .queryParam("cat3", "A01010700");     // 생태관광지

        if (areaCode != null && !areaCode.isBlank()) {
            builder.queryParam("areaCode", areaCode);
        }

        URI uri = builder.build(false).toUri();
        log.info("[KorService-생태관광] 호출 URL: {}", uri.toString());

        try {
            KorServiceResponse response = tourApiWebClient
                    .get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(KorServiceResponse.class)
                    .block();

            if (response == null
                    || response.getResponse() == null
                    || response.getResponse().getBody() == null
                    || response.getResponse().getBody().getItems() == null
                    || response.getResponse().getBody().getItems().getItem() == null) {
                log.warn("[KorService-생태관광] 응답 데이터 없음");
                return Collections.emptyList();
            }

            String resultCode = response.getResponse().getHeader().getResultCode();
            if (!"0000".equals(resultCode)) {
                log.error("[KorService-생태관광] 오류 코드={}", resultCode);
                return Collections.emptyList();
            }

            List<KorServiceItem> items = response.getResponse().getBody().getItems().getItem();
            log.info("[KorService-생태관광] 조회 성공 → {}건", items.size());
            return items;

        } catch (Exception e) {
            log.error("[KorService-생태관광] 호출 오류: {}", e.getMessage(), e);
            return Collections.emptyList();
        }


    }

    /**
     * 관광지 공통 상세 정보 조회
     */
    public String getDetailCommon(String contentId, String contentTypeId) {

        URI uri = UriComponentsBuilder
                .fromHttpUrl(baseUrl + "/detailCommon2")
                .queryParam("serviceKey", serviceKey)
                .queryParam("MobileOS", mobileOs)
                .queryParam("MobileApp", mobileApp)
                .queryParam("_type", "json")
                .queryParam("contentId", contentId)
                .build(false)
                .toUri();

        log.info("[KorService-상세] contentId={}", contentId);

        try {
            KorServiceResponse response = tourApiWebClient
                    .get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(KorServiceResponse.class)
                    .block();

            if (response == null
                    || response.getResponse() == null
                    || response.getResponse().getBody() == null
                    || response.getResponse().getBody().getItems() == null
                    || response.getResponse().getBody().getItems().getItem() == null
                    || response.getResponse().getBody().getItems().getItem().isEmpty()) {
                return null;
            }

            KorServiceItem item = response.getResponse().getBody().getItems().getItem().get(0);
            return new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(item);

        } catch (Exception e) {
            log.error("[KorService-상세] 오류: {}", e.getMessage());
            return null;
        }
    }


}
