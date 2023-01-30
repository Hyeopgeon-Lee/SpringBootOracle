<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ page import="kopo.poly.util.CmmUtil" %>
<%@ page import="kopo.poly.dto.NoticeDTO" %>
<%
    // NoticeController 함수에서 model 객체에 저장된 값 불러오기
    NoticeDTO rDTO = (NoticeDTO) request.getAttribute("rDTO");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>공지사항 수정하기</title>
    <link rel="stylesheet" href="/css/table.css"/>
    <script type="text/javascript" src="/js/jquery-3.6.0.min.js"></script>
    <script type="text/javascript">

        // HTML로딩이 완료되고, 실행됨
        $(document).ready(function () {
            // 버튼 클릭했을때, 발생되는 이벤트 생성함(onclick 이벤트와 동일함)
            $("#btnSend").on("click", function () {
                doSubmit(); // 공지사항 수정하기 실행
            })
        })

        //글자 길이 바이트 단위로 체크하기(바이트값 전달)
        function calBytes(str) {
            let tcount = 0;
            let tmpStr = String(str);
            let strCnt = tmpStr.length;
            let onechar;
            for (let i = 0; i < strCnt; i++) {
                onechar = tmpStr.charAt(i);
                if (escape(onechar).length > 4) {
                    tcount += 2;
                } else {
                    tcount += 1;
                }
            }
            return tcount;
        }

        // 공지사항 저장하기
        function doSubmit() {

            let f = document.getElementById("f"); // form 태그

            if (f.title.value === "") {
                alert("제목을 입력하시기 바랍니다.");
                f.title.focus();
                return;
            }
            if (calBytes(f.title.value) > 200) {
                alert("최대 200Bytes까지 입력 가능합니다.");
                f.title.focus();
                return;
            }
            let noticeCheck = false; //체크 여부 확인 변수
            for (let i = 0; i < f.noticeYn.length; i++) {
                if (f.noticeYn[i].checked) {
                    noticeCheck = true;
                    break;
                }
            }
            if (noticeCheck === false) {
                alert("공지글 여부를 선택하시기 바랍니다.");
                f.noticeYn[0].focus();
                return;
            }
            if (f.contents.value === "") {
                alert("내용을 입력하시기 바랍니다.");
                f.contents.focus();
                return;
            }
            if (calBytes(f.contents.value) > 4000) {
                alert("최대 4000Bytes까지 입력 가능합니다.");
                f.contents.focus();
                return;
            }

            // Ajax 호출해서 글 등록하기
            $.ajax({
                    url: "/notice/noticeUpdate",
                    type: "post", // 전송방식은 Post
                    dataType: "JSON", // 전송 결과는 JSON으로 받기
                    data: $("#f").serialize(), // form 태그 내 input 등 객체를 자동으로 전송할 형태로 변경하기
                    success: function (json) { // /notice/noticeUpdate 호출이 성공했다면..
                        alert(json.msg); // 메시지 띄우기
                        location.href = "/notice/noticeList"; // 공지사항 리스트 이동
                    }
                }
            )
        }
    </script>
</head>
<body>
<h2>공지사항 수정하기</h2>
<hr/>
<br/>
<form name="f" id="f">
    <input type="hidden" name="nSeq" value="<%=CmmUtil.nvl(request.getParameter("nSeq")) %>"/>
    <div class="divTable minimalistBlack">
        <div class="divTableBody">
            <div class="divTableRow">
                <div class="divTableCell">제목
                </div>
                <div class="divTableCell">
                    <input type="text" name="title" maxlength="100"
                           style="width: 95%"/><%=CmmUtil.nvl(rDTO.getTitle()) %>
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
                <div class="divTableCell">조회수
                </div>
                <div class="divTableCell"><%=CmmUtil.nvl(rDTO.getReadCnt()) %>
                </div>
            </div>
            <div class="divTableRow">
                <div class="divTableCell">내용
                </div>
                <div class="divTableCell">
                    <textarea name="contents"
                              style="width: 95%; height: 400px"><%=CmmUtil.nvl(rDTO.getContents()) %></textarea>
                </div>
            </div>
        </div>
    </div>
    <div>
        <button id="btnSend" type="button">수정</button>
        <button type="reset">다시 작성</button>
    </div>
</form>
</body>
</html>