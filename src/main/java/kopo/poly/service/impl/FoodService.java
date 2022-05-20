package kopo.poly.service.impl;

import kopo.poly.dto.FoodDTO;
import kopo.poly.persistance.mapper.IFoodMapper;
import kopo.poly.service.IFoodService;
import kopo.poly.util.CmmUtil;
import kopo.poly.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.Iterator;

@Slf4j
@Service("FoodService")
public class FoodService implements IFoodService {

    private final IFoodMapper foodMapper;

    public FoodService(IFoodMapper foodMapper) {
        this.foodMapper = foodMapper;
    }

    @Override
    public int getFoodInfoFromWEB() throws Exception {

        // 로그 찍기(추후 찍은 로그를 통해 이 함수에 접근했는지 파악하기 용이하다.)
        log.info(this.getClass().getName() + ".getFoodInfoFromWEB start!");

        int res = 0; //크롤링 결과 (0보다 크면 크롤링 성공)

        // 서울강서캠퍼스 식단 정보 가져올 사이트 주소
        String url = "http://www.kopo.ac.kr/kangseo/content.do?menu=262";

        // JSOUP 라이브러리를 통해 사이트 접속되면, 그 사이트의 전체 HTML소스 저장할 변수
        Document doc = null; //

        //사이트 접속(http프로토롱만 가능, https 프로토콜은 보안상 안됨)
        doc = Jsoup.connect(url).get();

        Elements element = doc.select("table.tbl_table tbody");

        // Iterator을 사용하여 영화 순위 정보를 가져오기
        Iterator<Element> foodList = element.select("tr").iterator(); //영화 순위

        FoodDTO pDTO = null;

        int idx = 0; //반복횟수를 월요일부터 금요일까지만 되도록함(5일동안만)
        //수집된 데이터 DB에 저장하기
        while (foodList.hasNext()) {

            //반복문 5번만 돌기(월요일부터 금요일까지만 넣기)
            if (idx > 4) {
                break;

            }

            pDTO = new FoodDTO(); //수집된 영화정보를 DTO에 저장하기 위해 메모리에 올리기
			
			/*
			 * 	수집되는 데이터 예)
			 	월요일 백미밥 , 물만두국 , 매콤바싹불고기*파채 , 떡볶이 , 숙주맛살무침 , 김치
				화요일 참치김치덮밥 , 다시마채무국 , 핫바*케첩 , 크루통샐러드 , 알타리김치 , .
				수요일 백미밥 , 소고기미역국 , 닭볶음탕 , 숯불바베큐바조림 , 오이양파생채 , 김치
				목요일 콩나물밥 , 건새우근대국 , 분홍소시지전 , 해물볶음우동 , 백김치 , .
				금요일 미트소스스파게티 , 옥수수스프 , 몬테크리스토토스트 , 피클 , 추가밥 , 추가김치
				토요일
				일요일

			 * */

            //수집시간을 기본키(pk)로 사용
            pDTO.setCollect_time(DateUtil.getDateTime("yyyyMMdd24hmmss"));

            //요일별 식단 정보들어옴
            String food = CmmUtil.nvl(foodList.next().text()).trim();

            //앞의 3글자가 요일이기 때문에 요일 저장
            pDTO.setDay(food.substring(0, 3));

            //식단 정보
            pDTO.setFood_nm(food.substring(4));

            //등록자
            pDTO.setReg_id("admin");

            //영화 한개씩 추가
            res += foodMapper.InsertFoodInfo(pDTO);

            idx++;
        }

        // 로그 찍기(추후 찍은 로그를 통해 이 함수에 접근했는지 파악하기 용이하다.)
        log.info(this.getClass().getName() + ".getFoodInfoFromWEB end!");

        return res;
    }

}
