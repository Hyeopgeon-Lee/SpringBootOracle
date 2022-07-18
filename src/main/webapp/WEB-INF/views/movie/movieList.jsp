<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="kopo.poly.dto.MovieDTO" %>
<%@ page import="java.util.List" %>
<%@ page import="kopo.poly.util.CmmUtil" %>
<%
    //Controller로부터 전달받은 데이터
    List<MovieDTO> rList = (List<MovieDTO>) request.getAttribute("rList");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>영화 순위 결과</title>
<body>
영화 순위 정보<br/>
----------------------------------<br/>
<%
    for (MovieDTO rDTO : rList) {
        out.println("영화 순위 : " + CmmUtil.nvl(rDTO.getMovie_rank()));
        out.println("영화 제목 : " + CmmUtil.nvl(rDTO.getMovie_nm()));
        out.println("영화 평점 : " + CmmUtil.nvl(rDTO.getScore()));
        out.println("개봉일 : " + CmmUtil.nvl(rDTO.getOpen_day()));
        out.println("<hr/>");
    }
%>
</body>
</html>

