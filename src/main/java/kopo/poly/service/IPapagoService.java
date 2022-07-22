package kopo.poly.service;

import kopo.poly.dto.PapagoDTO;

public interface IPapagoService {

    // Papago 언어감지API
    String detectLangsApiURL = "https://openapi.naver.com/v1/papago/detectLangs";

    // Papago 번역API
    String translateApiURL = "https://openapi.naver.com/v1/papago/n2mt";

    // 네이버 파파고 API를 호출하여 입력된 언어가 어느 나라 언어인지 찾기
    PapagoDTO detectLangs(PapagoDTO pDTO) throws Exception;

    // 네이버 Papago API를 호출하여 언어감지 후, 번역하기
    PapagoDTO translate(PapagoDTO pDTO) throws Exception;

}

