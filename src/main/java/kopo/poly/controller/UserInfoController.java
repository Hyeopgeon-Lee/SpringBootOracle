package kopo.poly.controller;

import kopo.poly.dto.MsgDTO;
import kopo.poly.dto.UserInfoDTO;
import kopo.poly.service.IUserInfoService;
import kopo.poly.util.CmmUtil;
import kopo.poly.util.EncryptUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Slf4j
@RequestMapping(value = "/user")
@RequiredArgsConstructor
@Controller
public class UserInfoController {

    private final IUserInfoService userInfoService;

    /**
     * 회원가입 화면으로 이동
     */
    @GetMapping(value = "userRegForm")
    public String userRegForm() {
        log.info(this.getClass().getName() + ".user/userRegForm");

        return "/user/userRegForm";
    }

    /**
     * 회원 가입 전 아이디 중복체크하기(Ajax를 통해 입력한 아이디 정보 받음)
     */
    @ResponseBody
    @PostMapping(value = "getUserIdExists")
    public UserInfoDTO getUserExists(HttpServletRequest request) throws Exception {

        log.info(this.getClass().getName() + ".getUserIdExists Start!");

        String userId = CmmUtil.nvl(request.getParameter("userId")); // 회원아이디

        log.info("userId : " + userId);

        UserInfoDTO pDTO = new UserInfoDTO();
        pDTO.setUserId(userId);

        // 회원아이디를 통해 중복된 아이디인지 조회
        UserInfoDTO rDTO = Optional.ofNullable(userInfoService.getUserIdExists(pDTO)).orElseGet(UserInfoDTO::new);

        log.info(this.getClass().getName() + ".getUserIdExists End!");

        return rDTO;
    }

    /**
     * 회원 가입 전 아이디 중복체크하기(Ajax를 통해 입력한 아이디 정보 받음)
     */
    @ResponseBody
    @PostMapping(value = "getEmailExists")
    public UserInfoDTO getEmailExists(HttpServletRequest request) throws Exception {

        log.info(this.getClass().getName() + ".getEmailExists Start!");

        String email = CmmUtil.nvl(request.getParameter("email")); // 회원아이디

        log.info("email : " + email);

        UserInfoDTO pDTO = new UserInfoDTO();
        pDTO.setEmail(email);

        // 회원아이디를 통해 중복된 아이디인지 조회
        UserInfoDTO rDTO = Optional.ofNullable(userInfoService.getEmailExists(pDTO)).orElseGet(UserInfoDTO::new);

        log.info(this.getClass().getName() + ".getEmailExists End!");

        return rDTO;
    }

    /**
     * 회원가입 로직 처리
     */
    @ResponseBody
    @PostMapping(value = "insertUserInfo")
    public MsgDTO insertUserInfo(HttpServletRequest request) throws Exception {

        log.info(this.getClass().getName() + ".insertUserInfo start!");

        int res = 0; // 회원가입 결과
        String msg = ""; //회원가입 결과에 대한 메시지를 전달할 변수
        MsgDTO dto = null; // 결과 메시지 구조

        //웹(회원정보 입력화면)에서 받는 정보를 저장할 변수
        UserInfoDTO pDTO = null;

        try {

            /*
             * #######################################################
             *        웹(회원정보 입력화면)에서 받는 정보를 String 변수에 저장 시작!!
             *
             *    무조건 웹으로 받은 정보는 DTO에 저장하기 위해 임시로 String 변수에 저장함
             * #######################################################
             */
            String userId = CmmUtil.nvl(request.getParameter("userId")); //아이디
            String userName = CmmUtil.nvl(request.getParameter("userName")); //이름
            String password = CmmUtil.nvl(request.getParameter("password")); //비밀번호
            String email = CmmUtil.nvl(request.getParameter("email")); //이메일
            String addr1 = CmmUtil.nvl(request.getParameter("addr1")); //주소
            String addr2 = CmmUtil.nvl(request.getParameter("addr2")); //상세주소
            /*
             * #######################################################
             *        웹(회원정보 입력화면)에서 받는 정보를 String 변수에 저장 끝!!
             *
             *    무조건 웹으로 받은 정보는 DTO에 저장하기 위해 임시로 String 변수에 저장함
             * #######################################################
             */

            /*
             * #######################################################
             * 	 반드시, 값을 받았으면, 꼭 로그를 찍어서 값이 제대로 들어오는지 파악해야함
             * 						반드시 작성할 것
             * #######################################################
             * */
            log.info("userId : " + userId);
            log.info("userName : " + userName);
            log.info("password : " + password);
            log.info("email : " + email);
            log.info("addr1 : " + addr1);
            log.info("addr2 : " + addr2);

            /*
             * #######################################################
             *        웹(회원정보 입력화면)에서 받는 정보를 DTO에 저장하기 시작!!
             *
             *        무조건 웹으로 받은 정보는 DTO에 저장해야 한다고 이해하길 권함
             * #######################################################
             */

            //웹(회원정보 입력화면)에서 받는 정보를 저장할 변수를 메모리에 올리기
            pDTO = new UserInfoDTO();

            pDTO.setUserId(userId);
            pDTO.setUserName(userName);

            //비밀번호는 절대로 복호화되지 않도록 해시 알고리즘으로 암호화함
            pDTO.setPassword(EncryptUtil.encHashSHA256(password));

            //민감 정보인 이메일은 AES128-CBC로 암호화함
            pDTO.setEmail(EncryptUtil.encAES128CBC(email));
            pDTO.setAddr1(addr1);
            pDTO.setAddr2(addr2);

            /*
             * #######################################################
             *        웹(회원정보 입력화면)에서 받는 정보를 DTO에 저장하기 끝!!
             *
             *        무조건 웹으로 받은 정보는 DTO에 저장해야 한다고 이해하길 권함
             * #######################################################
             */

            /*
             * 회원가입
             * */
            res = userInfoService.insertUserInfo(pDTO);

            log.info("회원가입 결과(res) : " + res);

            if (res == 1) {
                msg = "회원가입되었습니다.";

                //추후 회원가입 입력화면에서 ajax를 활용해서 아이디 중복, 이메일 중복을 체크하길 바람
            } else if (res == 2) {
                msg = "이미 가입된 아이디입니다.";

            } else {
                msg = "오류로 인해 회원가입이 실패하였습니다.";

            }

        } catch (Exception e) {
            //저장이 실패되면 사용자에게 보여줄 메시지
            msg = "실패하였습니다. : " + e;
            log.info(e.toString());
            e.printStackTrace();

        } finally {
            // 결과 메시지 전달하기
            dto = new MsgDTO();
            dto.setResult(res);
            dto.setMsg(msg);

            log.info(this.getClass().getName() + ".insertUserInfo End!");
        }

        return dto;
    }

    /**
     * 로그인을 위한 입력 화면으로 이동
     */
    @GetMapping(value = "login")
    public String login() {
        log.info(this.getClass().getName() + ".user/login Start!");

        log.info(this.getClass().getName() + ".user/login End!");

        return "user/login";
    }


    /**
     * 로그인 처리 및 결과 알려주는 화면으로 이동
     */
    @ResponseBody
    @PostMapping(value = "loginProc")
    public MsgDTO loginProc(HttpServletRequest request, HttpSession session) {

        log.info(this.getClass().getName() + ".loginProc Start!");

        int res = 0; //로그인 처리 결과를 저장할 변수 (로그인 성공 : 1, 아이디, 비밀번호 불일치로인한 실패 : 0, 시스템 에러 : 2)
        String msg = ""; //로그인 결과에 대한 메시지를 전달할 변수
        MsgDTO dto = null; // 결과 메시지 구조

        //웹(회원정보 입력화면)에서 받는 정보를 저장할 변수
        UserInfoDTO pDTO = null;

        try {

            /*
             * ########################################################################
             *        웹(회원정보 입력화면)에서 받는 정보를 String 변수에 저장 시작!!
             *
             *    무조건 웹으로 받은 정보는 DTO에 저장하기 위해 임시로 String 변수에 저장함
             * ########################################################################
             */

            String userId = CmmUtil.nvl(request.getParameter("userId")); //아이디
            String password = CmmUtil.nvl(request.getParameter("password")); //비밀번호

            /*
             * ########################################################################
             *        웹(회원정보 입력화면)에서 받는 정보를 String 변수에 저장 끝!!
             *
             *    무조건 웹으로 받은 정보는 DTO에 저장하기 위해 임시로 String 변수에 저장함
             * ########################################################################
             */

            /*
             * ########################################################################
             * 	 반드시, 값을 받았으면, 꼭 로그를 찍어서 값이 제대로 들어오는지 파악해야함
             * 						반드시 작성할 것
             * ########################################################################
             * */
            log.info("userId : " + userId);
            log.info("password : " + password);

            /*
             * ########################################################################
             *        웹(회원정보 입력화면)에서 받는 정보를 DTO에 저장하기 시작!!
             *
             *        무조건 웹으로 받은 정보는 DTO에 저장해야 한다고 이해하길 권함
             * ########################################################################
             */


            //웹(회원정보 입력화면)에서 받는 정보를 저장할 변수를 메모리에 올리기
            pDTO = new UserInfoDTO();

            pDTO.setUserId(userId);

            //비밀번호는 절대로 복호화되지 않도록 해시 알고리즘으로 암호화함
            pDTO.setPassword(EncryptUtil.encHashSHA256(password));

            /*
             * ########################################################################
             *        웹(회원정보 입력화면)에서 받는 정보를 DTO에 저장하기 끝!!
             *
             *        무조건 웹으로 받은 정보는 DTO에 저장해야 한다고 이해하길 권함
             * ########################################################################
             */

            // 로그인을 위해 아이디와 비밀번호가 일치하는지 확인하기 위한 userInfoService 호출하기
            res = userInfoService.getUserLoginCheck(pDTO);

            log.info("res : " + res);
            /*
             * 로그인을 성공했다면, 회원아이디 정보를 session에 저장함
             *
             * 세션은 톰켓(was)의 메모리에 존재하며, 웹사이트에 접속한 사람(연결된 객체)마다 메모리에 값을 올린다.
             * 			 *
             * 예) 톰켓에 100명의 사용자가 로그인했다면, 사용자 각각 회원아이디를 메모리에 저장하며.
             *    메모리에 저장된 객체의 수는 100개이다.
             *    따라서 과도한 세션은 톰켓의 메모리 부하를 발생시켜 서버가 다운되는 현상이 있을 수 있기때문에,
             *    최소한으로 사용하는 것을 권장한다.
             *
             * 스프링에서 세션을 사용하기 위해서는 함수명의 파라미터에 HttpSession session 존재해야 한다.
             * 세션은 톰켓의 메모리에 저장되기 때문에 url마다 전달하는게 필요하지 않고,
             * 그냥 메모리에서 부르면 되기 때문에 jsp, controller에서 쉽게 불러서 쓸수 있다.
             * */
            if (res == 1) { //로그인 성공

                /*
                 * 세션에 회원아이디 저장하기, 추후 로그인여부를 체크하기 위해 세션에 값이 존재하는지 체크한다.
                 * 일반적으로 세션에 저장되는 키는 대문자로 입력하며, 앞에 SS를 붙인다.
                 *
                 * Session 단어에서 SS를 가져온 것이다.
                 */
                msg = "로그인이 성공했습니다.";
                session.setAttribute("SS_userId", userId);

            } else {
                msg = "아이디와 비밀번호가 올바르지 않습니다.";

            }

        } catch (Exception e) {
            //저장이 실패되면 사용자에게 보여줄 메시지
            msg = "시스템 문제로 로그인이 실패했습니다.";
            res = 2;
            log.info(e.toString());
            e.printStackTrace();

        } finally {
            // 결과 메시지 전달하기
            dto = new MsgDTO();
            dto.setResult(res);
            dto.setMsg(msg);

            log.info(this.getClass().getName() + ".loginProc End!");
        }

        return dto;
    }

    /**
     * 로그인 성공 페이지 이동
     */
    @GetMapping(value = "loginResult")
    public String loginSuccess() {
        log.info(this.getClass().getName() + ".user/loginResult Start!");

        log.info(this.getClass().getName() + ".user/loginResult End!");

        return "user/loginResult";
    }

    /**
     * 아아디 찾기 화면
     */
    @GetMapping(value = "searchUserId")
    public String searchUserId() {
        log.info(this.getClass().getName() + ".user/searchUserId Start!");

        log.info(this.getClass().getName() + ".user/searchUserId End!");

        return "user/searchUserId";

    }

    /**
     * 아아디 찾기 로직 수행
     */
    @PostMapping(value = "searchUserIdProc")
    public String searchUserIdProc(HttpServletRequest request, ModelMap model) throws Exception {
        log.info(this.getClass().getName() + ".user/searchUserIdProc Start!");

        /*
         * ########################################################################
         *        웹(회원정보 입력화면)에서 받는 정보를 String 변수에 저장!!
         *
         *    무조건 웹으로 받은 정보는 DTO에 저장하기 위해 임시로 String 변수에 저장함
         * ########################################################################
         */

        String userName = CmmUtil.nvl(request.getParameter("userName")); // 이름
        String email = CmmUtil.nvl(request.getParameter("email")); // 이메일

        /*
         * ########################################################################
         * 	 반드시, 값을 받았으면, 꼭 로그를 찍어서 값이 제대로 들어오는지 파악해야함
         * 						반드시 작성할 것
         * ########################################################################
         * */
        log.info("userName : " + userName);
        log.info("email : " + email);

        /*
         * ########################################################################
         *        웹(회원정보 입력화면)에서 받는 정보를 DTO에 저장하기!!
         *
         *        무조건 웹으로 받은 정보는 DTO에 저장해야 한다고 이해하길 권함
         * ########################################################################
         */

        UserInfoDTO pDTO = new UserInfoDTO();
        pDTO.setUserName(userName);
        pDTO.setEmail(email);

        UserInfoDTO rDTO = Optional.ofNullable(userInfoService.searchUserIdOrPasswordProc(pDTO)).orElseGet(UserInfoDTO::new);

        model.addAttribute("rDTO", rDTO);

        log.info(this.getClass().getName() + ".user/searchUserIdProc End!");

        return "user/searchUserIdResult";

    }


    /**
     * 비밀번호 찾기 화면
     */
    @GetMapping(value = "searchPassword")
    public String searchPassword() {
        log.info(this.getClass().getName() + ".user/searchPassword Start!");

        log.info(this.getClass().getName() + ".user/searchPassword End!");

        return "user/searchPassword";

    }

    /**
     * 비밀번호 찾기 로직 수행
     * <p>
     * 아이디, 이름, 이메일 일치하면, 비밀번호 재발급 화면 이동
     */
    @PostMapping(value = "searchPasswordProc")
    public String searchPasswordProc(HttpServletRequest request, ModelMap model, HttpSession session) throws Exception {
        log.info(this.getClass().getName() + ".user/searchPasswordProc Start!");

        /*
         * ########################################################################
         *        웹(회원정보 입력화면)에서 받는 정보를 String 변수에 저장!!
         *
         *    무조건 웹으로 받은 정보는 DTO에 저장하기 위해 임시로 String 변수에 저장함
         * ########################################################################
         */

        String userId = CmmUtil.nvl(request.getParameter("userId")); // 아이디
        String userName = CmmUtil.nvl(request.getParameter("userName")); // 이름
        String email = CmmUtil.nvl(request.getParameter("email")); // 이메일

        /*
         * ########################################################################
         * 	 반드시, 값을 받았으면, 꼭 로그를 찍어서 값이 제대로 들어오는지 파악해야함
         * 						반드시 작성할 것
         * ########################################################################
         * */
        log.info("userId : " + userId);
        log.info("userName : " + userName);
        log.info("email : " + email);

        /*
         * ########################################################################
         *        웹(회원정보 입력화면)에서 받는 정보를 DTO에 저장하기!!
         *
         *        무조건 웹으로 받은 정보는 DTO에 저장해야 한다고 이해하길 권함
         * ########################################################################
         */

        UserInfoDTO pDTO = new UserInfoDTO();
        pDTO.setUserId(userId);
        pDTO.setUserName(userName);
        pDTO.setEmail(email);

        // 비밀번호 찾기 가능한지 확인하기
        UserInfoDTO rDTO = Optional.ofNullable(userInfoService.searchUserIdOrPasswordProc(pDTO)).orElseGet(UserInfoDTO::new);

        model.addAttribute("rDTO", rDTO);

        // 비밀번호 재생성하는 화면은 보안을 위해 반드시 NEW_PASSWORD 세션이 존재해야 접속 가능하도록 구현
        // userId 값을 넣은 이유는 비밀번호 재설정하는 newPasswordProc 함수에서 사용하기 위함
        session.setAttribute("NEW_PASSWORD", userId);

        log.info(this.getClass().getName() + ".user/searchPasswordProc End!");

        return "user/newPassword";

    }

    /**
     * 비밀번호 찾기 로직 수행
     * <p>
     * 아이디, 이름, 이메일 일치하면, 비밀번호 재발급 화면 이동
     */
    @PostMapping(value = "newPasswordProc")
    public String newPasswordProc(HttpServletRequest request, ModelMap model, HttpSession session) throws Exception {

        log.info(this.getClass().getName() + ".user/newPasswordProc Start!");

        String msg = ""; // 웹에 보여줄 메시지

        // 정상적인 접근인지 체크
        String newPassword = CmmUtil.nvl((String) session.getAttribute("NEW_PASSWORD"));

        if (newPassword.length() > 0) { //정상 접근

            /*
             * ########################################################################
             *        웹(회원정보 입력화면)에서 받는 정보를 String 변수에 저장!!
             *
             *    무조건 웹으로 받은 정보는 DTO에 저장하기 위해 임시로 String 변수에 저장함
             * ########################################################################
             */

            String password = CmmUtil.nvl(request.getParameter("password")); // 신규 비밀번호

            /*
             * ########################################################################
             * 	 반드시, 값을 받았으면, 꼭 로그를 찍어서 값이 제대로 들어오는지 파악해야함
             * 						반드시 작성할 것
             * ########################################################################
             * */
            log.info("password : " + password);

            /*
             * ########################################################################
             *        웹(회원정보 입력화면)에서 받는 정보를 DTO에 저장하기!!
             *
             *        무조건 웹으로 받은 정보는 DTO에 저장해야 한다고 이해하길 권함
             * ########################################################################
             */

            UserInfoDTO pDTO = new UserInfoDTO();
            pDTO.setUserId(newPassword);
            pDTO.setPassword(EncryptUtil.encHashSHA256(password));

            userInfoService.newPasswordProc(pDTO);

            // 비밀번호 재생성하는 화면은 보안을 위해 생성한 NEW_PASSWORD 세션 삭제
            session.setAttribute("NEW_PASSWORD", "");
            session.removeAttribute("NEW_PASSWORD");

            msg = "비밀번호가 재설정되었습니다.";

        } else { // 비정상 접근
            msg = "비정상 접근입니다.";
        }

        model.addAttribute("msg", msg);


        log.info(this.getClass().getName() + ".user/newPasswordProc End!");

        return "user/newPasswordResult";

    }


}
