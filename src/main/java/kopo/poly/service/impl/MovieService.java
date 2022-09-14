package kopo.poly.service.impl;

import kopo.poly.dto.MovieDTO;
import kopo.poly.persistance.mapper.IMovieMapper;
import kopo.poly.service.IMovieService;
import kopo.poly.util.CmmUtil;
import kopo.poly.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service("MovieService")
public class MovieService implements IMovieService {

    // RequiredArgsConstructor 어노테이션으로 생성자를 자동 생성함
    // movieMapper 변수에 이미 메모리에 올라간 Mapper 객체를 넣어줌
    // 예전에는 autowired 어노테이션를 통해 설정했었지만, 이젠 생성자를 통해 객체 주입함
    private final IMovieMapper movieMapper;

    /**
     * JSOUP 라이브러리를 통한 CGV 영화 순위 정보가져오기
     */
    @Transactional
    @Override
//    @Scheduled(cron = "5 * * * * *")
    public int collectMovieRank() throws Exception {

        // 로그 찍기(추후 찍은 로그를 통해 이 함수에 접근했는지 파악하기 용이하다.)
        log.info(this.getClass().getName() + ".collectMovieRank Start!");

        int res = 0; //크롤링 결과 (0보다 크면 크롤링 성공)

        // CGV 영화 순위 정보 가져올 사이트 주소
        String url = "http://www.cgv.co.kr/movies/";

        // JSOUP 라이브러리를 통해 사이트 접속되면, 그 사이트의 전체 HTML소스 저장할 변수
        Document doc = null; //

        //사이트 접속(http프로토롱만 가능, https 프로토콜은 보안상 안됨)
        doc = Jsoup.connect(url).get();

        // CGV 웹페이지의 전체 소소 중 일부 태그를 선택하기 위해 사용
        // <div class="sect-movie-chart"> 이 태그 내에서 있는 HTML소스만 element에 저장됨
        Elements element = doc.select("div.sect-movie-chart");

        // Iterator을 사용하여 영화 순위 정보를 가져오기
        // 영화순위는 기본적으로 1개 이상의 영화가 존재하기 때문에 태그의 반복이 존재할 수 밖에 없음
        Iterator<Element> movie_rank = element.select("strong.rank").iterator(); //영화 순위
        Iterator<Element> movie_name = element.select("strong.title").iterator(); //영화 이름
        Iterator<Element> movie_reserve = element.select("strong.percent span").iterator(); //영화 예매율
        Iterator<Element> score = element.select("span.percent").iterator(); //점수
        Iterator<Element> open_day = element.select("span.txt-info").iterator(); //개봉일

        MovieDTO pDTO = null;

        //수집된 데이터 DB에 저장하기
        while (movie_rank.hasNext()) {

            pDTO = new MovieDTO(); //수집된 영화정보를 DTO에 저장하기 위해 메모리에 올리기

            //수집시간을 기본키(pk)로 사용
            pDTO.setCollect_time(DateUtil.getDateTime("yyyyMMddhhmmss"));

            //영화 순위(trim 함수 추가 이유 : trim 함수는 글자의 앞뒤 공백 삭제 역할을 수행하여,데이터 수집시,
            // 홈페이지 개발자들을 앞뒤 공백 집어넣을 수 있어서 추가)
            String rank = CmmUtil.nvl(movie_rank.next().text()).trim();  //No.1 들어옴
            pDTO.setMovie_rank(rank.substring(3, rank.length()));

            //영화 제목
            pDTO.setMovie_nm(CmmUtil.nvl(movie_name.next().text()).trim());

            //영화 예매율
            pDTO.setMovie_reserve(CmmUtil.nvl(movie_reserve.next().text()).trim());

            //영화 점수
            pDTO.setScore(CmmUtil.nvl(score.next().text()).trim());

            //수집되는 데이터가 '2019.10.23 개봉'이기 때문에 앞에 10자리(2019.10.23)만 저장
            pDTO.setOpen_day(CmmUtil.nvl(open_day.next().text()).trim().substring(0, 10));

            //등록자
            pDTO.setReg_id("admin");

            //영화 한개씩 추가
            res += movieMapper.InsertMovieInfo(pDTO);

        }

        // 로그 찍기(추후 찍은 로그를 통해 이 함수에 접근했는지 파악하기 용이하다.)
        log.info(this.getClass().getName() + ".collectMovieRank End!");

        return res;
    }

    @Override
    public List<MovieDTO> getMovieInfo(MovieDTO pDTO) throws Exception {

        // 로그 찍기(추후 찍은 로그를 통해 이 함수에 접근했는지 파악하기 용이하다.)
        log.info(this.getClass().getName() + ".getMovieInfo Start!");

        // DB에서 조회하기
        List<MovieDTO> rList = movieMapper.getMovieInfo(pDTO);

        // DB 조회 결과가 없다면, NullPointer 에러 방지를 위해
        // 데이터가 존재하는 않는 객체로 메모리에 올리기
        if (rList == null) {
            rList = new ArrayList<>();
        }

        // 로그 찍기(추후 찍은 로그를 통해 이 함수에 접근했는지 파악하기 용이하다.)
        log.info(this.getClass().getName() + ".getMovieInfo End!");

        return rList;
    }
}
