package kopo.poly.persistance.mapper;

import kopo.poly.dto.MovieDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IMovieMapper {

	// 수집된 내용 DB에 등록
	int InsertMovieInfo(MovieDTO pDTO) throws Exception;

	// 수집된 내용을 조회하기
	List<MovieDTO> getMovieInfo(MovieDTO pDTO) throws Exception;
}

