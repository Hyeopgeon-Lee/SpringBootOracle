package kopo.poly.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieDTO {

    private String collectTime; // 수집시간
    private String seq; //수집된 데이터 순번
    private String movieRank; //영화 순위
    private String movieNm; //영화제목
    private String movieReserve; // 예매율
    private String score; //평점
    private String openDay; //개봉일
    private String regId;
    private String regDt;
    private String chgId;
    private String chgDt;

}
