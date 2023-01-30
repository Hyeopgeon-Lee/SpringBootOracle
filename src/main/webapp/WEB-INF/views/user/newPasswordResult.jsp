<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="kopo.poly.util.CmmUtil" %>
<%
    String msg = CmmUtil.nvl((String) request.getAttribute("msg"));
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title><%=msg%>></title>
    <link rel="stylesheet" href="/css/table.css"/>
    <script type="text/javascript" src="/js/jquery-3.6.0.min.js"></script>
    <script type="text/javascript">

        // HTML로딩이 완료되고, 실행됨
        $(document).ready(function () {

            // 로그인 화면 이동
            $("#btnLogin").on("click", function () { // 버튼 클릭했을때, 발생되는 이벤트 생성함(onclick 이벤트와 동일함)
                location.href = "/user/login";
            })
        })
    </script>
</head>
<body>
<h2>비밀번호 재설정 결과</h2>
<hr/>
<br/>
<form id="f">
    <div class="divTable minimalistBlack">
        <div class="divTableBody">
            <div class="divTableRow">
                <div class="divTableCell"><%=msg%>
                </div>
            </div>
        </div>
    </div>
    <div>
        <button id="btnLogin" type="button">로그인</button>
    </div>
</form>
</body>
</html>