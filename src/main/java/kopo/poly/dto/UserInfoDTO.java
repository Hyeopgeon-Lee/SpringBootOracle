package kopo.poly.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoDTO {

    private String user_id;
    private String user_name;
    private String password;
    private String email;
    private String addr1;
    private String addr2;
    private String reg_id;
    private String reg_dt;
    private String chg_id;
    private String chg_dt;

    // 회원가입시, 중복가입을 방지 위해 사용할 변수
    // DB를 조회해서 회원이 존재하면 Y값을 반환함
    // DB테이블에 존재하지 않는 가상의 컬럼(ALIAS)
    private String exists_yn;

}
