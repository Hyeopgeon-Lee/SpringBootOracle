<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>글로벌 채팅방 입장 및 채팅 리스트</title>
    <link rel="stylesheet" href="/css/table.css"/>
    <script type="text/javascript" src="/js/jquery-3.6.0.min.js"></script>
    <script type="text/javascript">

        // HTML로딩이 완료되고, 실행됨
        $(document).ready(function () {

            //화면 로딩이 완료되면 첫번째로 실행함
            getRoomList(); //전체 채팅방 리스트 가져오기

            //2번쨰부터 채팅방 전체리스트를 5초마다 로딩함
            setInterval("getRoomList()", 5000);

        })

        //전체 채팅방 리스트 가져오기
        function getRoomList() {

            //Ajax 호출
            $.ajax({
                url: "/global/roomList", // 채팅방 정보 가져올 URL
                type: "post", // 전송방식
                dataType: "JSON", // 전달받을 데이터 타입
                success: function (json) {

                    // 기존 데이터 삭제하기
                    $("#room_list").empty();

                    for (let i = 0; i < json.length; i++) {
                        $("#room_list").append(json[i] + "<br/>"); // 채팅방마다 한줄씩 추가

                    }
                }
            })

        }
    </script>
</head>
<body>
<h1>글로벌 채팅방 전체 리스트</h1>
<div class="divTable minimalistBlack">
    <div class="divTableHeading">
        <div class="divTableRow">
            <div class="divTableHead">대화가능한 채팅방들</div>
        </div>
    </div>
    <div class="divTableBody">
        <div class="divTableRow">
            <div class="divTableCell" id="room_list"></div>
        </div>
    </div>
</div>
<br/><br/>
<h1>글로벌 채팅방 입장 정보</h1>
<hr/>
<br/><br/>
<form name="f" id="f" method="post" action="/global/chatroom">
    <div class="divTable minimalistBlack">
        <div class="divTableBody">
            <div class="divTableRow">
                <div class="divTableCell">채팅방 이름</div>
                <div class="divTableCell">
                    <input type="text" name="roomName">
                </div>
                <div class="divTableCell">대화명(별명)</div>
                <div class="divTableCell">
                    <input type="text" name="userName">
                </div>
                <div class="divTableCell">채팅창에 표시할 언어</div>
                <div class="divTableCell">
                    <select name="langCode">
                        <option value="ko">한국어</option>
                        <option value="en">영어</option>
                    </select>
                </div>
            </div>
        </div>
    </div>
    <div>
        <button>입장하기</button>
    </div>
</form>
</body>
</html>