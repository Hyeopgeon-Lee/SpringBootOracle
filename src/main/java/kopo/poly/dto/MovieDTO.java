package kopo.poly.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieDTO {

    private String collect_time; // 수집시간
    private String seq; //수집된 데이터 순번
    private String movie_rank; //영화 순위
    private String movie_nm; //영화제목
    private String movie_reserve; // 예매율
    private String score; //평점
    private String open_day; //개봉일
    private String reg_id;
    private String reg_dt;
    private String chg_id;
    private String chg_dt;

}
