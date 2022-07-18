<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="kopo.poly.util.CmmUtil" %>
<%@ page import="java.util.List" %>
<%@ page import="kopo.poly.dto.FoodDTO" %>
<%
    //Controller로부터 전달받은 데이터
    List<FoodDTO> rList = (List<FoodDTO>) request.getAttribute("rList");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>서울강서캠퍼스 식단</title>
<body>
이번주 학교 점심식사<br/>
----------------------------------<br/>
<%
    for (FoodDTO rDTO : rList) {
        out.println(CmmUtil.nvl(rDTO.getDay()));
        out.println("메뉴 : " + CmmUtil.nvl(rDTO.getFood_nm()));
        out.println("<hr/>");
    }
%>
</body>
</html>

