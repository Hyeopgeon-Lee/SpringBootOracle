<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="kopo.poly.dto.NoticeDTO" %>
<%@ page import="kopo.poly.util.CmmUtil" %>
<%
    // NoticeController 함수에서 model 객체에 저장된 값 불러오기
    List<NoticeDTO> rList = (List<NoticeDTO>) request.getAttribute("rList");
%>
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>공지 리스트</title>
    <link rel="stylesheet" href="/css/table.css"/>
    <script type="text/javascript">

        //상세보기 이동
        function doDetail(seq) {
            location.href = "/notice/noticeInfo?nSeq=" + seq;
        }

    </script>
</head>
<body>
<h2>공지사항</h2>
<hr/>
<br/>
<div class="divTable minimalistBlack">
    <div class="divTableHeading">
        <div class="divTableRow">
            <div class="divTableHead">순번</div>
            <div class="divTableHead">제목</div>
            <div class="divTableHead">조회수</div>
            <div class="divTableHead">등록자</div>
            <div class="divTableHead">등록일</div>
        </div>
    </div>
    <div class="divTableBody">
        <%
            for (NoticeDTO dto : rList) {
        %>
        <div class="divTableRow">
            <%
                if (CmmUtil.nvl(dto.getNoticeYn()).equals("Y")) { //공지글이라면, [공지]표시
                    out.print("<div class=\"divTableCell\">공지</div>");

                } else { //공지글이 아니라면, 글번호 보여주기
                    out.print("<div class=\"divTableCell\">" + CmmUtil.nvl(dto.getNoticeSeq()) + "</div>");

                }
            %>
            <div class="divTableCell"
                 onclick="doDetail('<%=CmmUtil.nvl(dto.getNoticeSeq())%>')"><%=CmmUtil.nvl(dto.getTitle())%>
            </div>
            <div class="divTableCell"><%=CmmUtil.nvl(dto.getReadCnt())%>
            </div>
            <div class="divTableCell"><%=CmmUtil.nvl(dto.getUserName())%>
            </div>
            <div class="divTableCell"><%=CmmUtil.nvl(dto.getRegDt())%>
            </div>
        </div>
        <%
            }
        %>
    </div>
</div>
<a href="/notice/noticeReg">글쓰기</a>
</body>
</html>
