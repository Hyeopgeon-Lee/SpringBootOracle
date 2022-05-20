package kopo.poly.controller;


import kopo.poly.service.IFoodService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Slf4j
@Controller
public class FoodController {

    @Resource(name = "FoodService")
    private IFoodService foodService;

    /**
     * 서울강서캠퍼스 식단 수집을 위한 URL 호출
     */
    @RequestMapping(value = "food/getFoodInfoFromWEB")
    public String getFoodInfoFromWEB(ModelMap model)
            throws Exception {

        log.info(this.getClass().getName() + ".getFoodInfoFromWEB start!");

        int res = foodService.getFoodInfoFromWEB();

        //크롤링 결과를 넣어주기
        model.addAttribute("res", String.valueOf(res));

        log.info(this.getClass().getName() + ".getFoodInfoFromWEB end!");

        return "/food/FoodForWEB";
    }

}



