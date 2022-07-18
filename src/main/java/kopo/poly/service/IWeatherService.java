package kopo.poly.service;

import kopo.poly.dto.WeatherDTO;

public interface IWeatherService {

    String apiURL = "https://api.openweathermap.org/data/3.0/onecall";
    String apiKey = "91d6edcc8a4c2a4548c37525dd9cd012";

    // 날씨 API를 호출하여 날씨 결과 받아오기
    WeatherDTO getWeather(WeatherDTO pDTO) throws Exception;
}
