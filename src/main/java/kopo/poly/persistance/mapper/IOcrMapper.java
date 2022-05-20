package kopo.poly.persistance.mapper;


import kopo.poly.dto.OcrDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IOcrMapper {

	//이미지로부터 인식된 텍스트 내용 DB에 등록
	int InsertOcrInfo(OcrDTO pDTO) throws Exception;
	
	
}

