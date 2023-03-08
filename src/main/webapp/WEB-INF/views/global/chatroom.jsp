<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ page import="kopo.poly.util.CmmUtil" %>
<%
    // 채팅방 명
    String roomName = CmmUtil.nvl(request.getParameter("roomName"));

    // 채팅방 입장전 입력한 별명
    String userName = CmmUtil.nvl(request.getParameter("userName"));

    // 채팅 대화를 보여줄 언어(한국어를 설정하면, 무조건 한국어로 번역됨, 영어를 선택하면 무조건 영어로 영작됨)
    String langCode = CmmUtil.nvl(request.getParameter("langCode"));

%>
<html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title><%=roomName%> 채팅방 입장 </title>
    <link rel="stylesheet" href="/css/table.css"/>
    <script type="text/javascript" src="/js/jquery-3.6.0.min.js"></script>
    <script type="text/javascript">

        let data = {};//전송 데이터(JSON)
        let ws; // 웹소켓 객체
        const roomName = "<%=roomName%>"; // 채팅룸 이름
        const userName = "<%=userName%>"; // 채팅유저 이름
        const langCode = "<%=langCode%>"; // 보여줄 언어

        $(document).ready(function () {

            //웹소켓 객체를 생성하는중
            if (ws !== undefined && ws.readyState !== WebSocket.CLOSED) {
                console.log("WebSocket is already opened.");
                return;

            }

            // 접속 URL 예 : ws://localhost:10000/ws/테스트방/별명/ko
            ws = new WebSocket("ws://" + location.host + "/ws/" + roomName + "/" + userName + "/" + langCode);

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

                let chatMsg;

                if (langCode === "ko") { // 한국어 채팅방인 경우, 한국어 메시지만 출력하기
                    chatMsg = data.koMsg;

                } else {
                    chatMsg = data.enMsg; // 영어 채팅방인 경우, 영어 메시지만 출력하기
                }

                if (data.name === userName) { // 내가 발송한 채팅 메시지는 파란색 글씩
                    $("#chat").append("<div>");
                    $("#chat").append("<span style='color: blue'><b>[보낸 사람] : </b></span>");
                    $("#chat").append("<span style='color: blue'> 나 </span>");
                    $("#chat").append("<span style='color: blue'><b>[발송 메시지] : </b></span>");
                    $("#chat").append("<span style='color: blue'>" + chatMsg + " </span>");
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
                    $("#chat").append("<span>" + chatMsg + " </span>");
                    $("#chat").append("<span><b>[발송시간] : </b></span>");
                    $("#chat").append("<span>" + data.date + " </span>");
                    $("#chat").append("</div>");

                }
            }

            $("#btnSend").on("click", function () {
                data.name = userName; // 별명
                data.msg = $("#msg").val();  // 입력한 메시지

                let chatMsg = JSON.stringify(data); // 데이터 구조를 JSON 형태로 변경하기

                ws.send(chatMsg); // 채팅 메시지 전송하기

                $("#msg").val("") // 채팅 메시지 전송 후, 입력한 채팅내용 지우기
            })
        });
    </script>
</head>
<body>
<h1><%=userName%>님! <%=roomName%> 입장을 환영합니다.</h1>
<hr/>
<br/><br/>
<div class="divTable minimalistBlack">
    <div class="divTableHeading">
        <div class="divTableRow">
            <div class="divTableHead">대화 내용[<%=(langCode.equals("ko")) ? "한국어" : "영어"%> 표시]</div>
        </div>
    </div>
    <div class="divTableBody">
        <div class="divTableRow">
            <div class="divTableCell" id="chat"></div>
        </div>
    </div>
</div>
<br/><br/>
<div class="divTable minimalistBlack">
    <div class="divTableBody">
        <div class="divTableRow">
            <div class="divTableCell">전달할 메시지</div>
            <div class="divTableCell">
                <input type="text" id="msg">
                <button id="btnSend">메시지 전송</button>
            </div>
        </div>
    </div>
</div>
</body>
</html>
