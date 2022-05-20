package kopo.poly.controller;

import kopo.poly.service.IMovieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Slf4j
@Controller
public class MovieController {

    @Resource(name = "MovieService")
    private IMovieService movieService;

    /**
     * CGV 영화 수집을 위한 URL 호출
     */
    @GetMapping(value = "movie/getMovieInfoFromWEB")
    public String getMovieInfoFromWEB(HttpServletRequest request, HttpServletResponse response, ModelMap model)
            throws Exception {

        log.info(this.getClass().getName() + ".getMovieInfoFromWEB start!");

        int res = movieService.getMovieInfoFromWEB();

        //크롤링 결과를 넣어주기
        model.addAttribute("res", String.valueOf(res));

        log.info(this.getClass().getName() + ".getMovieInfoFromWEB end!");

        return "/movie/RankForWEB";
    }

}



