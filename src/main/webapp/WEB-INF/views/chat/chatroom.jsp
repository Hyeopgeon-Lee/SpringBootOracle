<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ page import="kopo.poly.util.CmmUtil" %>
<%
    // 채팅방 명
    String roomname = CmmUtil.nvl(request.getParameter("roomname"));

    // 채팅방 입장전 입력한 별명
    String nickname = CmmUtil.nvl(request.getParameter("nickname"));

%>
<html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title><%=roomname%> 채팅방 입장 </title>
    <script src="/js/jquery-3.6.0.min.js" type="text/javascript"></script>
    <script type="text/javascript">

        let data = {};//전송 데이터(JSON)
        let ws; // 웹소켓 객체
        const roomname = "<%=roomname%>"; // 채팅룸 이름
        const nickname = "<%=nickname%>"; // 채팅유저 이름

        $(document).ready(function () {

            let btnSend = document.getElementById("btnSend");
            btnSend.onclick = function () {
                send(); //Send 버튼 누르면, Send함수 호출하기
            }

            //웹소켓 객체를 생성하는중
            console.log("openSocket");
            if (ws !== undefined && ws.readyState !== WebSocket.CLOSED) {
                console.log("WebSocket is already opened.");
                return;

            }

            // 접속 URL 예 : ws://localhost:10000/ws/테스트방/별명
            ws = new WebSocket("ws://" + location.host + "/ws/" + roomname + "/" + nickname);
            // ws = new WebSocket("ws://" + location.host + "/ws/DS/DFGHG");

            // 웹소켓 열기
            ws.onopen = function (event) {
                if (event.data === undefined)
                    return;

                console.log(event.data)
            };

            //웹소캣으로부터 메세지를 받을 때마다 실행됨
            ws.onmessage = function (msg) {

                // 웹소켓으로부터 받은 데이터를 JSON 구조로 변환하기
                let data = JSON.parse(msg.data);

                if (data.name === nickname) { // 내가 발송한 채팅 메시지는 파란색 글씩
                    $("#chat").append("<div>");
                    $("#chat").append("<span style='color: blue'><b>[보낸 사람] : </b></span>");
                    $("#chat").append("<span style='color: blue'> 나 </span>");
                    $("#chat").append("<span style='color: blue'><b>[발송 메시지] : </b></span>");
                    $("#chat").append("<span style='color: blue'>" + data.msg + " </span>");
                    $("#chat").append("<span style='color: blue'><b>[발송시간] : </b></span>");
                    $("#chat").append("<span style='color: blue'>" + data.date + " </span>");
                    $("#chat").append("</div>");

                } else if (data.name === "관리자") { // 관리자가 발송한 채팅 메시지는 빨간색 글씩
                    $("#chat").append("<div>");
                    $("#chat").append("<span style='color: red'><b>[보낸 사람] : </b></span>");
                    $("#chat").append("<span style='color: red'>" + data.name + "</span>");
                    $("#chat").append("<span style='color: red'><b>[발송 메시지] : </b></span>");
                    $("#chat").append("<span style='color: red'>" + data.msg + " </span>");
                    $("#chat").append("<span style='color: red'><b>[발송시간] : </b></span>");
                    $("#chat").append("<span style='color: red'>" + data.date + " </span>");
                    $("#chat").append("</div>");

                } else {
                    $("#chat").append("<div>"); // 그 외 채팅참여자들이 발송한 채팅 메시지는 검정색
                    $("#chat").append("<span><b>[보낸 사람] : </b></span>");
                    $("#chat").append("<span>" + data.name + " </span>");
                    $("#chat").append("<span><b>[수신 메시지] : </b></span>");
                    $("#chat").append("<span>" + data.msg + " </span>");
                    $("#chat").append("<span><b>[발송시간] : </b></span>");
                    $("#chat").append("<span>" + data.date + " </span>");
                    $("#chat").append("</div>");

                }
            }
        });

        // 채팅 메시지 보내기
        function send() {

            let msgObj = $("#msg"); // Object

            if (msgObj.value !== "") {
                data.name = nickname; // 별명
                data.msg = msgObj.val();  // 입력한 메시지

                // 데이터 구조를 JSON 형태로 변경하기
                let temp = JSON.stringify(data);

                // 채팅 메시지 전송하기
                ws.send(temp);
            }

            // 채팅 메시지 전송 후, 입력한 채팅내용 지우기
            msgObj.val("");
        }

    </script>
</head>
<body>

<h2><%=nickname%> 님! <%=roomname%> 채팅방 입장하셨습니다.</h2><br/><br/>
<div><b>채팅내용</b></div>
<hr/>
<div id="chat"></div>
<div>
    <label for="msg">전달할 메시지 : </label><input type="text" id="msg">
    <button id="btnSend">메시지 전송</button>
</div>

</body>
</html>
