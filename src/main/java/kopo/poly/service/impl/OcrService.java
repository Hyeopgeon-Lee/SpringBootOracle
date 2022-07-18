package kopo.poly.service.impl;

import kopo.poly.dto.OcrDTO;
import kopo.poly.service.IOcrService;
import kopo.poly.util.CmmUtil;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import org.springframework.stereotype.Service;

import java.io.File;

@Slf4j
@Service("OcrService")
public class OcrService implements IOcrService {

    /**
     * 이미지 파일로부터 문자 읽어 오기
     *
     * @param pDTO 이미지 파일 정보
     * @return pDTO 이미지로부터 읽은 문자열
     */
    @Override
    public OcrDTO getReadforImageText(OcrDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".getFoodInfoFromWEB start!");

        File imageFile = new File(CmmUtil.nvl(pDTO.getFilePath()) + "//" + CmmUtil.nvl(pDTO.getFileName()));

        // OCR 기술 사용을 위한 Tesseract 플랫폼 객체 생성
        ITesseract instance = new Tesseract();

        // OCR 분석에 필요한 기준 데이터(이미 각 나라의 언어별로 학습시킨 데이터 위치 폴더)
        // 저장 경로는 물리경로를 사용함(전체 경로)
        instance.setDatapath("C:/model/tessdata");

        // 한국어 학습 데이터 선택(기본 값은 영어)
        instance.setLanguage("kor"); //한국어 설정
//        instance.setLanguage("eng"); //영어 설정

        // 이미지 파일로부터 텍스트 읽기
        String result = instance.doOCR(imageFile);

        // 읽은 글자를 DTO에 저장하기
        pDTO.setTextFromImage(result);

        log.info("result : " + result);

        log.info(this.getClass().getName() + ".getFoodInfoFromWEB End!");

        return pDTO;
    }
}
