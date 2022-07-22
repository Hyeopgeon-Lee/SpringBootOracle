package kopo.poly.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import kopo.poly.dto.WeatherDTO;
import kopo.poly.dto.WeatherDailyDTO;
import kopo.poly.service.IWeatherService;
import kopo.poly.util.CmmUtil;
import kopo.poly.util.DateUtil;
import kopo.poly.util.NetworkUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service("WeatherService")
public class WeatherService implements IWeatherService {

    @Value("${weather.api.key}")
    private String apiKey;

    @Override
    public WeatherDTO getWeather(WeatherDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".getWeather Start!");

        String lat = CmmUtil.nvl(pDTO.getLat());
        String lon = CmmUtil.nvl(pDTO.getLon());

        String apiParam = "?lat=" + lat + "&lon=" + lon + "&appid=" + apiKey + "&units=metric";
        log.info("apiParam " + apiParam);

        String json = NetworkUtil.get(IWeatherService.apiURL + apiParam);
        log.info("json " + json);

        // JSON 구조를 Map 데이터 구조로 변경하기
        // 키와 값 구조의 JSON구조로부터 데이터를 쉽게 가져오기 위해 Map 데이터구조로 변경함
        Map<String, Object> rMap = new ObjectMapper().readValue(json, LinkedHashMap.class);

        // 현재 날씨 정보를 가지고 있는 current 키의 값 가져오기
        Map<String, Double> current = (Map<String, Double>) rMap.get("current");

        double currnetTemp = current.get("temp"); // 현재 기온
        log.info("현재 기온 : " + currnetTemp);


        // 일별 날씨 조회(OpenAPI가 현재 날짜 기준으로 최대 7일까지 제공)
        List<Map<String, Object>> dailyList = (List<Map<String, Object>>) rMap.get("daily");

        // 7일 동안의 날씨 정보를 저장할 데이터
        // OpenAPI로부터 필요한 정보만 가져와서, 처리하기 쉬운 JSON 구조로 변경에 활용
        List<WeatherDailyDTO> pList = new LinkedList<>();

        for (Map<String, Object> dailyMap : dailyList) {

            String day = DateUtil.getLongDateTime(dailyMap.get("dt"), "yyyy-MM-dd"); // 기준 날짜
            String sunrise = DateUtil.getLongDateTime(dailyMap.get("sunrise")); // 해뜨는 시간
            String sunset = DateUtil.getLongDateTime(dailyMap.get("sunset")); // 해지는 시간
            String moonrise = DateUtil.getLongDateTime(dailyMap.get("moonrise")); // 달뜨는 시간
            String moonset = DateUtil.getLongDateTime(dailyMap.get("moonset")); // 달지는 시간

            log.info("-----------------------------------------");
            log.info("today : " + day);
            log.info("해뜨는 시간 : " + sunrise);
            log.info("해지는 시간 : " + sunset);
            log.info("달뜨는 시간 : " + moonrise);
            log.info("달지는 시간 : " + moonset);

            Map<String, Double> dailyTemp = (Map<String, Double>) dailyMap.get("temp");

            // 숫자형태보다 문자열 형태가 데이터처리하기 쉽기 때문에 Double형태를 문자열로 변경함
            String dayTemp = String.valueOf(dailyTemp.get("day")); // 평균 기온
            String dayTempMax = String.valueOf(dailyTemp.get("max")); // 최대 기온
            String dayTempMin = String.valueOf(dailyTemp.get("min")); // 최저 기온

            log.info("평균 기온 : " + dayTemp);
            log.info("최고 기온 : " + dayTempMax);
            log.info("최저 기온 : " + dayTempMin);

            WeatherDailyDTO wdDTO = new WeatherDailyDTO();

            wdDTO.setDay(day);
            wdDTO.setSunrise(sunrise);
            wdDTO.setSunset(sunset);
            wdDTO.setMoonrise(moonrise);
            wdDTO.setMoonset(moonset);
            wdDTO.setDayTemp(dayTemp);
            wdDTO.setDayTempMax(dayTempMax);
            wdDTO.setDayTempMin(dayTempMin);

            pList.add(wdDTO); // 일별 날씨 정보를 List에 추가하기

            wdDTO = null;
        }

        WeatherDTO rDTO = new WeatherDTO();

        rDTO.setLat(lat);
        rDTO.setLon(lon);
        rDTO.setCurrentTemp(currnetTemp);
        rDTO.setDailyList(pList);

        log.info(this.getClass().getName() + ".getWeather End!");

        return rDTO;
    }
}

