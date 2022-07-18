package kopo.poly.controller;

import kopo.poly.dto.OcrDTO;
import kopo.poly.service.IOcrService;
import kopo.poly.util.CmmUtil;
import kopo.poly.util.DateUtil;
import kopo.poly.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;

@Slf4j
@RequestMapping(value="/ocr")
@Controller
public class OcrController {

    /*
     * 비즈니스 로직(중요 로직을 수행하기 위해 사용되는 서비스를 메모리에 적재(싱글톤패턴 적용됨)
     */
    @Resource(name = "OcrService")
    private IOcrService ocrService;

    // 업로드되는 파일이 저장되는 기본 폴더 설정(자바에서 경로는 /로 표현함)
    final private String FILE_UPLOAD_SAVE_PATH = "c:/upload"; // C:\\upload 폴더에 저장


    /**
     * 이미지 인식을 위한 파일업로드 화면 호출
     */
    @RequestMapping(value="uploadImage")
    public String uploadImage() {
        log.info(this.getClass().getName() + ".uploadImage!");

        return "/ocr/uploadImage";
    }

    /**
     * 파일업로드 및 이미지 인식
     */
    @RequestMapping(value = "readImage")
    public String readImage(ModelMap model, @RequestParam(value = "fileUpload") MultipartFile mf)
            throws Exception {

        log.info(this.getClass().getName() + ".readImage Start!");

        // OCR 실행 결과
        String res = "";

        // 업로드하는 실제 파일명
        // 다운로드 기능 구현시, 임의로 정의된 파일명을 원래대로 만들어주기 위한 목적
        String originalFileName = mf.getOriginalFilename();

        // 파일 확장자 가져오기(파일 확장자를 포함한 전체 이름(myimage.jpg)에서 뒤쪽부터 .이 존재하는 위치 찾기
        String ext = originalFileName.substring(originalFileName.lastIndexOf(".") + 1,
                originalFileName.length()).toLowerCase();

        // 이미지 파일만 실행되도록 함
        if (ext.equals("jpeg") || ext.equals("jpg") || ext.equals("gif") || ext.equals("png")) {

            // 웹서버에 저장되는 파일 이름
            // 업로드하는 파일 이름에 한글, 특수 문자들이 저장될 수 있기 때문에 강제로 영어와 숫자로 구성된 파일명으로 변경해서 저장한다.
            // 리눅스나 유닉스 등 운영체제는 다국어 지원에 취약하기 때문이다.
            String saveFileName = DateUtil.getDateTime("HHmmss") + "." + ext;

            // 웹서버에 업로드한 파일 저장하는 물리적 경로
            String saveFilePath = FileUtil.mkdirForDate(FILE_UPLOAD_SAVE_PATH);

            String fullFileInfo = saveFilePath + "/"+ saveFileName;

            // 정상적으로 값이 생성되었는지 로그 찍어서 확인
            log.info("ext : " + ext);
            log.info("saveFileName : " + saveFileName);
            log.info("saveFilePath : " + saveFilePath);
            log.info("fullFileInfo : " + fullFileInfo);

            // 업로드 되는 파일을 서버에 저장
            mf.transferTo(new File(fullFileInfo));

            OcrDTO pDTO = new OcrDTO();

            pDTO.setFileName(saveFileName); // 저장되는 파일명
            pDTO.setFilePath(saveFilePath); // 저장되는 경로
            pDTO.setExt(ext); // 확장자
            pDTO.setOrg_file_name(originalFileName);; // 원래이름
            pDTO.setReg_id("admin");

            OcrDTO rDTO = ocrService.getReadforImageText(pDTO);

            if (rDTO == null) {
                rDTO = new OcrDTO();
            }

            res = CmmUtil.nvl(rDTO.getTextFromImage());

            rDTO = null;
            pDTO = null;

        }else {
            res = "이미지 파일이 아니라서 인식이 불가능합니다.";

        }

        // 이미지로부터 인식된 문자를 JSP에 전달하기
        model.addAttribute("res", res);

        log.info(this.getClass().getName() + ".readImage End!");

        return "/ocr/readImage";
    }
}

