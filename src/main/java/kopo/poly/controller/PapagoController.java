package kopo.poly.controller;

import kopo.poly.dto.PapagoDTO;
import kopo.poly.service.IPapagoService;
import kopo.poly.util.CmmUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RequestMapping(value = "papago")
@RestController
public class PapagoController {

    @Resource(name = "PapagoService")
    private IPapagoService papagoService;

    @GetMapping(value = "detectLangs")
    public PapagoDTO detectLangs(HttpServletRequest request)
            throws Exception {

        // 로그 찍기(추후 찍은 로그를 통해 이 함수에 접근했는지 파악하기 용이하다.)
        log.info(this.getClass().getName() + ".detectLangs Start!");

        // 분석할 문장
        String text = CmmUtil.nvl(request.getParameter("text"));

        log.info("text : " + text);

        PapagoDTO pDTO = new PapagoDTO(); // 서비스에 전달할 파라미터 객체
        pDTO.setText(text);

        // 입력된 문장의 언어 감지를 위해 서비스 호출하여 결과 받기
        PapagoDTO rDTO = papagoService.detectLangs(pDTO);

        if (rDTO == null) {
            rDTO = new PapagoDTO();

        }

        // 로그 찍기(추후 찍은 로그를 통해 이 함수 호출이 끝났는지 파악하기 용이하다.)
        log.info(this.getClass().getName() + ".detectLangs End!");

        return rDTO;
    }

    @GetMapping(value = "translate")
    public PapagoDTO translate(HttpServletRequest request)
            throws Exception {

        // 로그 찍기(추후 찍은 로그를 통해 이 함수에 접근했는지 파악하기 용이하다.)
        log.info(this.getClass().getName() + ".translate Start!");

        String text = CmmUtil.nvl(request.getParameter("text"));

        log.info("text : " + text);

        PapagoDTO pDTO = new PapagoDTO();
        pDTO.setText(text);

        // 입력된 문장을 번역하는 서비스 호출하여 결과 받기
        PapagoDTO rDTO = papagoService.translate(pDTO);

        if (rDTO == null) {
            rDTO = new PapagoDTO();

        }

        // 로그 찍기(추후 찍은 로그를 통해 이 함수 호출이 끝났는지 파악하기 용이하다.)
        log.info(this.getClass().getName() + ".translate End!");

        return rDTO;
    }


}
