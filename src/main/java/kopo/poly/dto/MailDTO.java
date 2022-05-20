package kopo.poly.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailDTO {

    String toMail; // 받는 사람
    String title; // 보내는 메일 제목
    String contents; // 보내는 메일 내용
}
