package kopo.poly.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OcrDTO {
    private String filePath; // 저장된 이미지 파일의 파일 저장 경로
    private String fileName; // 저장된 이미지 파일 이름
    private String textFromImage; // 저장된 이미지로부터 읽은 글씨

    private String orgFileName; // 원래 파일 이름
    private String ext; // 확장자
    private String regId; // 등록자
    private String chgId; // 수정자
}
