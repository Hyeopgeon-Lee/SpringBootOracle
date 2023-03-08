package kopo.poly.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class ChatDTO {
    private String name; // 이름
    private String msg; // 채팅 메시지
    private String date; // 발송날짜
    private String koMsg; // 한국어 채팅 메시지
    private String enMsg; // 영어 채팅 메시지

}


