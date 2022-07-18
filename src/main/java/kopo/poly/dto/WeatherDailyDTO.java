package kopo.poly.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class WeatherDailyDTO {

    private String day; // 기준 일자
    private String sunrise; // 해뜨는 시간
    private String sunset; // 해지는 시간
    private String moonrise; // 달뜨는 시간
    private String moonset; // 달지는 시간
    private String dayTemp; // 평균 기온
    private String dayTempMax; // 최고 기온
    private String dayTempMin; // 최저 기온
}

