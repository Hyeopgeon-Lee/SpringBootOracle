<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ page import="kopo.poly.dto.NoticeDTO" %>
<%@ page import="kopo.poly.util.CmmUtil" %>
<%
    // NoticeController 함수에서 model 객체에 저장된 값 불러오기
    NoticeDTO rDTO = (NoticeDTO) request.getAttribute("rDTO");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>게시판 글보기</title>
    <link rel="stylesheet" href="/css/table.css"/>
    <script type="text/javascript" src="/js/jquery-3.6.0.min.js"></script>
    <script>
        // Controller에서 받은 세션에 저장된 값
        const session_user_id = "<%=CmmUtil.nvl((String)session.getAttribute("SESSION_USER_ID"))%>";

        // 공지사항 게시글 작성자 아이디
        const user_id = "<%=CmmUtil.nvl(rDTO.getUserId())%>";

        // 현재 글번호, 자바에서 받을 변수들은 자바스크립트 변수로 저장하면 편함
        const nSeq = "<%=CmmUtil.nvl(rDTO.getNoticeSeq())%>";

        // HTML로딩이 완료되고, 실행됨
        $(document).ready(function () {
            // 버튼 클릭했을때, 발생되는 이벤트 생성함(onclick 이벤트와 동일함)
            $("#btnEdit").on("click", function () {
                doEdit(); // 공지사항 수정하기 실행
            })

            // 버튼 클릭했을때, 발생되는 이벤트 생성함(onclick 이벤트와 동일함)
            $("#btnDelete").on("click", function () {
                doDelete(); // 공지사항 수정하기 실행
            })

            // 버튼 클릭했을때, 발생되는 이벤트 생성함(onclick 이벤트와 동일함)
            $("#btnDelete").on("click", function () {
                location.href = "/notice/noticeList"; // 공지사항 리스트 이동
            })
        })

        //수정하기
        function doEdit() {
            if (session_user_id === user_id) {
                location.href = "/notice/noticeEditInfo?nSeq=" + nSeq;

            } else if (session_user_id === "") {
                alert("로그인 하시길 바랍니다.");

            } else {
                alert("본인이 작성한 글만 수정 가능합니다.");

            }
        }

        //삭제하기
        function doDelete() {
            if (session_user_id === user_id) {
                if (confirm("작성한 글을 삭제하시겠습니까?")) {

                    // Ajax 호출해서 글 삭제하기
                    $.ajax({
                            url: "/notice/noticeDelete",
                            type: "post", // 전송방식은 Post
                            dataType: "JSON", // 전송 결과는 JSON으로 받기
                            data: {"nSeq": nSeq}, // form 태그 내 input 등 객체를 자동으로 전송할 형태로 변경하기
                            success:
                                function (json) { // /notice/noticeDelete 호출이 성공했다면..
                                    alert(json.msg); // 메시지 띄우기
                                    location.href = "/notice/noticeList"; // 공지사항 리스트 이동
                                }
                        }
                    )
                }

            } else if (session_user_id === "") {
                alert("로그인 하시길 바랍니다.");

            } else {
                alert("본인이 작성한 글만 수정 가능합니다.");

            }
        }
    </script>
</head>
<body>
<h2>공지사항 상세보기</h2>
<hr/>
<br/>
<div class="divTable minimalistBlack">
    <div class="divTableBody">
        <div class="divTableRow">
            <div class="divTableCell">제목
            </div>
            <div class="divTableCell"><%=CmmUtil.nvl(rDTO.getTitle())%>
            </div>
        </div>
        <div class="divTableRow">
            <div class="divTableCell">공지글 여부
            </div>
            <div class="divTableCell">
                예<input type="radio" name="noticeYn"
                        value="Y" <%=CmmUtil.checked(CmmUtil.nvl(rDTO.getNoticeYn()), "Y") %> />
                아니오<input type="radio" name="noticeYn"
                          value="N" <%=CmmUtil.checked(CmmUtil.nvl(rDTO.getNoticeYn()), "N") %>/>
            </div>
        </div>
        <div class="divTableRow">
            <div class="divTableCell">작성일
            </div>
            <div class="divTableCell"><%=CmmUtil.nvl(rDTO.getRegDt())%>
            </div>
        </div>
        <div class="divTableRow">
            <div class="divTableCell">조회수
            </div>
            <div class="divTableCell"><%=CmmUtil.nvl(rDTO.getReadCnt())%>
            </div>
        </div>
        <div class="divTableRow">
            <div class="divTableCell">내용
            </div>
            <div class="divTableCell"><%=CmmUtil.nvl(rDTO.getContents())%>
            </div>
        </div>
    </div>
</div>
<div>
    <button id="btnEdit" type="button">수정</button>
    <button id="btnDelete" type="button">삭제</button>
    <button id="btnList" type="button">목록</button>
</div>
</body>
</html>

