package kopo.poly.controller;

import kopo.poly.dto.WeatherDTO;
import kopo.poly.service.IWeatherService;
import kopo.poly.util.CmmUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
public class WeatherController {

    @Resource(name = "WeatherService")
    private IWeatherService weatherService;

    @GetMapping(value = "weather/getWeather")
    public WeatherDTO getWeather(HttpServletRequest request, ModelMap model)
            throws Exception {

        // 로그 찍기(추후 찍은 로그를 통해 이 함수에 접근했는지 파악하기 용이하다.)
        log.info(this.getClass().getName() + ".getWeather start!");

        String lat = CmmUtil.nvl(request.getParameter("lat"));
        String lon = CmmUtil.nvl(request.getParameter("lon"));


        WeatherDTO pDTO = new WeatherDTO();

        pDTO.setLat(lat);
        pDTO.setLon(lon);

        // 공지사항 리스트 가져오기
        WeatherDTO rDTO = weatherService.getWeather(pDTO);

        if (rDTO == null) {
            rDTO = new WeatherDTO();

        }
        // 로그 찍기(추후 찍은 로그를 통해 이 함수 호출이 끝났는지 파악하기 용이하다.)
        log.info(this.getClass().getName() + ".getWeather end!");

        // 함수 처리가 끝나고 보여줄 JSP 파일명(/WEB-INF/view/notice/NoticeList.jsp)
        return rDTO;

    }

}
