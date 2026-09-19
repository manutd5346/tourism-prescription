package com.tour.prescription.dto.korservice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 국문 관광정보 서비스(KorService) 응답 항목
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class KorServiceItem {

    @JsonProperty("contentid")
    private String contentId;

    @JsonProperty("contenttypeid")
    private String contentTypeId;      // 12=관광지,14=문화시설,15=축제,25=여행코스,28=레포츠,32=숙박,38=쇼핑,39=음식점

    @JsonProperty("title")
    private String title;

    @JsonProperty("addr1")
    private String addr1;

    @JsonProperty("addr2")
    private String addr2;

    @JsonProperty("areacode")
    private String areaCode;

    @JsonProperty("sigungucode")
    private String sigunguCode;

    @JsonProperty("cat1")
    private String cat1;

    @JsonProperty("cat2")
    private String cat2;

    @JsonProperty("cat3")
    private String cat3;

    @JsonProperty("mapx")
    private String mapX;

    @JsonProperty("mapy")
    private String mapY;

    @JsonProperty("firstimage")
    private String firstImage;

    @JsonProperty("firstimage2")
    private String firstImage2;

    @JsonProperty("tel")
    private String tel;

    @JsonProperty("createdtime")
    private String createdTime;

    @JsonProperty("modifiedtime")
    private String modifiedTime;

    @JsonProperty("overview")
    private String overview;        // 개요

    @JsonProperty("homepage")
    private String homepage;        // 홈페이지
}
