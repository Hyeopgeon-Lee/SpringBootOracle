package kopo.poly.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class PapagoDTO {

    private String langCode; // 원문의 언어(한국어 : ko / 영어 :en)
    private String text; // 분석을 위한 문장

    private String translatedText; // 번역된 문장
    private String scrLangType; // 번역전 문장 사용 언어
    private String tarLangType; // 번연된 문장 사용 언어


}

