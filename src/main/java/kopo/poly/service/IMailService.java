package kopo.poly.service;

import kopo.poly.dto.MailDTO;

public interface IMailService {

    //메일 발송
    int doSendMail(MailDTO pDTO);
}
