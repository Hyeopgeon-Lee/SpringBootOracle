package kopo.poly.persistance.mapper;

import kopo.poly.dto.FoodDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IFoodMapper {

	//수집된 내용 DB에 등록
	int InsertFoodInfo(FoodDTO pDTO) throws Exception;
	
	
}

