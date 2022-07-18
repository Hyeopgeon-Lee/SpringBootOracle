package kopo.poly.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class WeatherDTO {

    private String lat; // 위도
    private String lon; //경도
    private double currentTemp; // 현재 기온

    // 현재로부터 7일동안 일자별 날씨
    private List<WeatherDailyDTO> dailyList;

}

