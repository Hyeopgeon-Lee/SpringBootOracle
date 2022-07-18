<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ page import="kopo.poly.util.CmmUtil" %>
<%

    // 채팅방 입장전 입력한 별명
    String nickname = CmmUtil.nvl(request.getParameter("nickname"));

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>SeoGram - SEO Agency Template</title>
    <script src="/js/jquery-3.6.0.min.js"></script>
    <script type="text/javascript">

        let data = {};//전송 데이터(JSON)
        let ws;
        let ss_user_name = "<%=nickname%>";

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

            // ws = new WebSocket("ws://13.208.147.39:11000/chat");  //aws 소켓
            ws = new WebSocket("ws://" + location.host + "/ws"); //local 소켓

            ws.onopen = function (event) {
                if (event.data === undefined)
                    return;

                console.log(event.data)
            };

            //웹소캣으로부터 메세지를 받음
            ws.onmessage = function (msg) {

                // 웹소켓으로부터 받은 데이터를 JSON 구조로 변환하기
                let data = JSON.parse(msg.data);


                if (data.name == ss_user_name) {
                    $("#chat").append("[내가보낸 메시지] : " + data.msg + " | 보낸사람 : " + data.name + " | 메시지 보낸시간 : " + data.date + "<br/>");

                } else {
                    $("#chat").append("[다른 사람이 보낸 메시지] : " + data.msg + " | 보낸사람 : " + data.name + " | 메시지 보낸시간 : " + data.date + "<br/>");
                }
            }
        });

        function send() {

            // alert(msgObj);

            let msgObj = $("#msg"); // Object

            // alert(msgObj);
            if (msgObj.value != "") {
                data.name = "<%=nickname%>"; // 별명
                data.msg = msgObj.val();  // 입력한 메시지
                data.date = new Date().toLocaleString(); //발송시간(내 PC시간)

                // 데이터 구조를 JSON 형태로 변경하기
                let temp = JSON.stringify(data);

                // 채팅 메시지 전송하기
                ws.send(temp);
            }

            // 채팅 메시지 전송 후, 입력한 채팅내용 지우기
            msgObj.val("");
        };

    </script>
</head>
<body>

<h2><%=nickname%> 님의 채팅화면</h2>
<h2>채팅내용</h2>
<hr/>
<div id="chat"></div>

<input type="text" id="msg">
<input type="button" id="btnSend" value="Send"/>


</body>
</html>