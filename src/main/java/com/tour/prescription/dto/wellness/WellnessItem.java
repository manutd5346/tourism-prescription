package com.tour.prescription.dto.wellness;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WellnessItem {

    @JsonProperty("contentid")
    private String contentId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("addr1")
    private String addr1;

    @JsonProperty("addr2")
    private String addr2;

    @JsonProperty("areacode")
    private String areaCode;

    @JsonProperty("mapx")
    private String mapX;        // 경도

    @JsonProperty("mapy")
    private String mapY;        // 위도

    @JsonProperty("firstimage")
    private String firstImage;

    @JsonProperty("tel")
    private String tel;

    @JsonProperty("overview")
    private String overview;
}
