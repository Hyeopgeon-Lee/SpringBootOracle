package kopo.poly.persistance.mapper;

import kopo.poly.dto.MovieDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IMovieMapper {

    // 수집된 영화 순위 DB에 등록하기
    int insertMovieInfo(MovieDTO pDTO) throws Exception;

    // DB에 저장된 영화 순위 삭제하기
    int deleteMovieInfo(MovieDTO pDTO) throws Exception;

    // 수집된 내용을 조회하기
    List<MovieDTO> getMovieInfo(MovieDTO pDTO) throws Exception;
}

