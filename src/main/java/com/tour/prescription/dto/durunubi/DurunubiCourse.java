package com.tour.prescription.dto.durunubi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 두루누비(코리아둘레길) 코스 정보
 * 실제 응답 필드명은 승인 후 매뉴얼/실제 호출로 재확인 필요
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DurunubiCourse {

    @JsonProperty("crsIdx")
    private String crsIdx;          // 코스 ID

    @JsonProperty("crsKorNm")
    private String crsKorNm;        // 코스명(국문)

    @JsonProperty("crsLevel")
    private String crsLevel;        // 난이도

    @JsonProperty("crsDstnc")
    private String crsDstnc;        // 코스 거리(km)

    @JsonProperty("crsTotlRqrmHour")
    private String crsTotlRqrmHour; // 총 소요시간

    @JsonProperty("crsSummary")
    private String crsSummary;      // 코스 요약 설명

    @JsonProperty("crsContents")
    private String crsContents;     // 코스 상세 설명

    @JsonProperty("sigun")
    private String sigun;           // 시군

    @JsonProperty("lat")
    private String lat;             // 위도

    @JsonProperty("lon")
    private String lon;             // 경도
}
