package kopo.poly.controller;

import kopo.poly.dto.NoticeDTO;
import kopo.poly.service.INoticeService;
import kopo.poly.util.CmmUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;


/*
 * Controller 선언해야만 Spring 프레임워크에서 Controller인지 인식 가능
 * 자바 서블릿 역할 수행
 * 
 * slf4j는 스프링 프레임워크에서 로그 처리하는 인터페이스 기술이며,
 * 로그처리 기술인 log4j와 logback과 인터페이스 역할 수행함
 * 스프링 프레임워크는 기본으로 logback을 채택해서 로그 처리함
 * */
@Slf4j
@Controller
public class NoticeController {

    /*

     * 비즈니스 로직(중요 로직을 수행하기 위해 사용되는 서비스를 메모리에 적재(싱글톤패턴 적용됨)
     */
    @Resource(name = "NoticeService")
    private INoticeService noticeService;

    /**
     * GetMapping은 GET방식을 통해 접속되는 URL 호출에 대해 실행되는 함수로 설정함을 의미함
     * PostMapping은 POST방식을 통해 접속되는 URL 호출에 대해 실행되는 함수로 설정함을 의미함
     * GetMapping(value = "index") =>  GET방식을 통해 접속되는 URL이 index인 경우 아래 함수를 실행함
     */
    @GetMapping(value = "index")
    public String Index() {
        return "/index";

    }

    /**
     * 게시판 리스트 보여주기
     *
     * GetMapping(value = "notice/NoticeList") =>  GET방식을 통해 접속되는 URL이 notice/NoticeList인 경우 아래 함수를 실행함
     */
    @GetMapping(value = "notice/NoticeList")
    public String NoticeList(ModelMap model)
            throws Exception {

        // 로그 찍기(추후 찍은 로그를 통해 이 함수에 접근했는지 파악하기 용이하다.)
        log.info(this.getClass().getName() + ".NoticeList start!");

        // 공지사항 리스트 가져오기
        List<NoticeDTO> rList = noticeService.getNoticeList();

        if (rList == null) {
            rList = new ArrayList<>();

        }

        // 조회된 리스트 결과값 넣어주기
        model.addAttribute("rList", rList);

        // 로그 찍기(추후 찍은 로그를 통해 이 함수 호출이 끝났는지 파악하기 용이하다.)
        log.info(this.getClass().getName() + ".NoticeList end!");

        // 함수 처리가 끝나고 보여줄 JSP 파일명(/WEB-INF/view/notice/NoticeList.jsp)
        return "/notice/NoticeList";

    }

    /**
     * 게시판 작성 페이지 이동
     *
     * 이 함수는 게시판 작성 페이지로 접근하기 위해 만듬. WEB-INF 밑에 존재하는 JSP는 직접 호출할 수 없음 따라서 WEB-INF 밑에
     * 존재하는 JSP를 호출하기 위해서는 반드시 Controller 등록해야함
     *
     * GetMapping(value = "notice/NoticeReg") =>  GET방식을 통해 접속되는 URL이 notice/NoticeReg인 경우 아래 함수를 실행함
     */
    @GetMapping(value = "notice/NoticeReg")
    public String NoticeReg() {

        log.info(this.getClass().getName() + ".NoticeReg start!");

        log.info(this.getClass().getName() + ".NoticeReg end!");

        return "/notice/NoticeReg";
    }

    /**
     * 게시판 글 등록
     */
    @PostMapping(value = "notice/NoticeInsert")
    public String NoticeInsert(HttpSession session, HttpServletRequest request, ModelMap model) {

        log.info(this.getClass().getName() + ".NoticeInsert start!");

        String msg = "";

        try {
            /*
             * 게시판 글 등록되기 위해 사용되는 form객체의 하위 input 객체 등을 받아오기 위해 사용함
             */
            String user_id = CmmUtil.nvl((String) session.getAttribute("SESSION_USER_ID"));
            String title = CmmUtil.nvl(request.getParameter("title")); // 제목
            String noticeYn = CmmUtil.nvl(request.getParameter("noticeYn")); // 공지글 여부
            String contents = CmmUtil.nvl(request.getParameter("contents")); // 내용

            /*
             * ####################################################################################
             * 반드시, 값을 받았으면, 꼭 로그를 찍어서 값이 제대로 들어오는지 파악해야함 반드시 작성할 것
             * ####################################################################################
             */
            log.info("user_id : " + user_id);
            log.info("title : " + title);
            log.info("noticeYn : " + noticeYn);
            log.info("contents : " + contents);

            NoticeDTO pDTO = new NoticeDTO();

            pDTO.setUser_id(user_id);
            pDTO.setTitle(title);
            pDTO.setNotice_yn(noticeYn);
            pDTO.setContents(contents);

            /*
             * 게시글 등록하기위한 비즈니스 로직을 호출
             */
            noticeService.InsertNoticeInfo(pDTO);

            // 저장이 완료되면 사용자에게 보여줄 메시지
            msg = "등록되었습니다.";


        } catch (Exception e) {

            // 저장이 실패되면 사용자에게 보여줄 메시지
            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();

        } finally {
            log.info(this.getClass().getName() + ".NoticeInsert end!");

            // 결과 메시지 전달하기
            model.addAttribute("msg", msg);

        }

        return "/notice/MsgToList";
    }

    /**
     * 게시판 상세보기
     */
    @GetMapping(value = "notice/NoticeInfo")
    public String NoticeInfo(HttpServletRequest request, ModelMap model) {

        log.info(this.getClass().getName() + ".NoticeInfo start!");

        String msg = "";

        try {
            /*
             * 게시판 글 등록되기 위해 사용되는 form객체의 하위 input 객체 등을 받아오기 위해 사용함
             */
            String nSeq = CmmUtil.nvl(request.getParameter("nSeq")); // 공지글번호(PK)

            /*
             * ####################################################################################
             * 반드시, 값을 받았으면, 꼭 로그를 찍어서 값이 제대로 들어오는지 파악해야함 반드시 작성할 것
             * ####################################################################################
             */
            log.info("nSeq : " + nSeq);

            /*
             * 값 전달은 반드시 DTO 객체를 이용해서 처리함 전달 받은 값을 DTO 객체에 넣는다.
             */
            NoticeDTO pDTO = new NoticeDTO();
            pDTO.setNotice_seq(nSeq);

            // 공지사항 상세정보 가져오기
            NoticeDTO rDTO = noticeService.getNoticeInfo(pDTO);

            if (rDTO == null) {
                rDTO = new NoticeDTO();

            }

            log.info("getNoticeInfo success!!!");

            // 조회된 리스트 결과값 넣어주기
            model.addAttribute("rDTO", rDTO);


        } catch (Exception e) {

            // 저장이 실패되면 사용자에게 보여줄 메시지
            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();

        } finally {
            log.info(this.getClass().getName() + ".NoticeInsert end!");

            // 결과 메시지 전달하기
            model.addAttribute("msg", msg);

        }

        log.info(this.getClass().getName() + ".NoticeInfo end!");

        return "/notice/NoticeInfo";
    }

    /**
     * 게시판 수정 보기
     */
    @GetMapping(value = "notice/NoticeEditInfo")
    public String NoticeEditInfo(HttpServletRequest request, ModelMap model) {

        log.info(this.getClass().getName() + ".NoticeEditInfo start!");

        String msg = "";

        try {

            String nSeq = CmmUtil.nvl(request.getParameter("nSeq")); // 공지글번호(PK)

            log.info("nSeq : " + nSeq);

            NoticeDTO pDTO = new NoticeDTO();

            pDTO.setNotice_seq(nSeq);

            /*
             * ####################################################### 공지사항 수정정보 가져오기(상세보기
             * 쿼리와 동일하여, 같은 서비스 쿼리 사용함)
             * #######################################################
             */
            NoticeDTO rDTO = noticeService.getNoticeInfo(pDTO);

            if (rDTO == null) {
                rDTO = new NoticeDTO();

            }

            // 조회된 리스트 결과값 넣어주기
            model.addAttribute("rDTO", rDTO);

        } catch (Exception e) {
            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();

        } finally {
            log.info(this.getClass().getName() + ".NoticeUpdate end!");

            // 결과 메시지 전달하기
            model.addAttribute("msg", msg);

        }

        log.info(this.getClass().getName() + ".NoticeEditInfo end!");

        return "/notice/NoticeEditInfo";
    }

    /**
     * 게시판 글 수정
     */
    @PostMapping(value = "notice/NoticeUpdate")
    public String NoticeUpdate(HttpSession session, HttpServletRequest request, ModelMap model) {

        log.info(this.getClass().getName() + ".NoticeUpdate start!");

        String msg = "";

        try {

            String user_id = CmmUtil.nvl((String) session.getAttribute("SESSION_USER_ID")); // 아이디
            String nSeq = CmmUtil.nvl(request.getParameter("nSeq")); // 글번호(PK)
            String title = CmmUtil.nvl(request.getParameter("title")); // 제목
            String noticeYn = CmmUtil.nvl(request.getParameter("noticeYn")); // 공지글 여부
            String contents = CmmUtil.nvl(request.getParameter("contents")); // 내용

            log.info("user_id : " + user_id);
            log.info("nSeq : " + nSeq);
            log.info("title : " + title);
            log.info("noticeYn : " + noticeYn);
            log.info("contents : " + contents);

            NoticeDTO pDTO = new NoticeDTO();

            pDTO.setUser_id(user_id);
            pDTO.setNotice_seq(nSeq);
            pDTO.setTitle(title);
            pDTO.setNotice_yn(noticeYn);
            pDTO.setContents(contents);

            // 게시글 수정하기 DB
            noticeService.updateNoticeInfo(pDTO);

            msg = "수정되었습니다.";

        } catch (Exception e) {
            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();

        } finally {
            log.info(this.getClass().getName() + ".NoticeUpdate end!");

            // 결과 메시지 전달하기
            model.addAttribute("msg", msg);

        }

        return "/notice/MsgToList";
    }

    /**
     * 게시판 글 삭제
     */
    @GetMapping(value = "notice/NoticeDelete")
    public String NoticeDelete(HttpServletRequest request, ModelMap model) {

        log.info(this.getClass().getName() + ".NoticeDelete start!");

        String msg = "";

        try {

            String nSeq = CmmUtil.nvl(request.getParameter("nSeq")); // 글번호(PK)

            log.info("nSeq : " + nSeq);

            NoticeDTO pDTO = new NoticeDTO();

            pDTO.setNotice_seq(nSeq);

            // 게시글 삭제하기 DB
            noticeService.deleteNoticeInfo(pDTO);

            msg = "삭제되었습니다.";

        } catch (Exception e) {
            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();

        } finally {
            log.info(this.getClass().getName() + ".NoticeDelete end!");

            // 결과 메시지 전달하기
            model.addAttribute("msg", msg);

        }

        return "/notice/MsgToList";
    }

}
