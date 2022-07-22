<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>채팅방 입장을 위한 별명 설정</title>
    <script src="/js/jquery-3.6.0.min.js"></script>
</head>
<body>
<form method="post" action="/chat/room">
    <div>채팅 입장</div>
    <div><span>채팅 별명 입력 : </span></div>
    <input type="text" name="nickname">
    <input type="submit" value="Send"/>
</form>
</body>
</html>