<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>채팅방 입장을 위한 별명 설정</title>
    <script src="/js/jquery-3.6.0.min.js"></script>
    <script>

        $(document).ready(function () {

            // id를 통해 button html태그 객체 가져오기
            let btnSend = document.getElementById("btnSend");

            btnSend.onclick = function () {

                // id를 통해 form html태그 객체 가져오기
                const f = document.getElementById("f");
                f.submit(); // form 태그 정보 전송하기
            }
        });

        setInterval(function () {
            $.ajax({
                url: "/chat/roomList",
                type: "get",
                dataType: "JSON",
                success: function (json) {

                    let roomHtml = "";

                    for (const room of json) {
                        roomHtml += ("<span>" + room + "</span> ");
                    }

                    $("#rooms").html(roomHtml);
                }
            });

        }, 5000)
    </script>
</head>
<body>
<div><b>현재 오픈된 채팅방</b></div>
<hr/>
<div id="rooms"></div>
<br/>
<br/>
<form method="post" id="f" action="/chat/room">
    <div><b>채팅 입장</b></div>
    <hr/>
    <div><span>채팅방 이름 : <input type="text" name="roomname"></span></div>
    <div><span>채팅 별명 : <input type="text" name="nickname"></span></div>
    <button id="btnSend">채팅방 입장</button>
</form>
</body>
</html>