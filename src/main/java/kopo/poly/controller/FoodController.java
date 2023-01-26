package kopo.poly.controller;

import kopo.poly.dto.FoodDTO;
import kopo.poly.service.IFoodService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@RequestMapping(value = "/food")
@RequiredArgsConstructor
@Controller
public class FoodController {

    private final IFoodService foodService;

    /**
     * 서울강서캠퍼스 식단 수집을 위한 URL 호출
     */
    @RequestMapping(value = "toDayFood")
    public String collectFood(ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".collectFood Start!");

        List<FoodDTO> rList = foodService.toDayFood();

        //크롤링 결과를 넣어주기
        model.addAttribute("rList", rList);

        log.info(this.getClass().getName() + ".collectFood End!");

        return "/food/todayFood";
    }

}



