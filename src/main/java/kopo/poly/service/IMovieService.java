package kopo.poly.service;

import kopo.poly.dto.MovieDTO;

import java.util.List;

public interface IMovieService {

    // 웹상(CGV 홈페이지)에서 영화 순위정보 가져오기
    int collectMovieRank() throws Exception;

    // 수집된 내용을 조회하기
    List<MovieDTO> getMovieInfo() throws Exception;

}

