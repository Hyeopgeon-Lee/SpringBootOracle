<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>회원가입 화면</title>
    <link rel="stylesheet" href="/css/table.css"/>
    <script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
    <script type="text/javascript">
        //회원가입 정보의 유효성 체크하기
        function doRegUserCheck(f) {

            if (f.user_id.value === "") {
                alert("아이디를 입력하세요.");
                f.user_id.focus();
                return false;
            }

            if (f.user_name.value === "") {
                alert("이름을 입력하세요.");
                f.user_name.focus();
                return false;
            }

            if (f.password.value === "") {
                alert("비밀번호를 입력하세요.");
                f.password.focus();
                return false;
            }

            if (f.password2.value === "") {
                alert("비밀번호확인을 입력하세요.");
                f.password2.focus();
                return false;
            }

            if (f.password.value !== f.password2.value) {
                alert("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
                f.password.focus();
                return false;
            }

            if (f.email.value === "") {
                alert("이메일을 입력하세요.");
                f.email.focus();
                return false;
            }

            if (f.addr1.value === "") {
                alert("주소를 입력하세요.");
                f.addr1.focus();
                return false;
            }

            if (f.addr2.value === "") {
                alert("상세주소를 입력하세요.");
                f.addr2.focus();
                return false;
            }
        }

        function kakaoPost(f) {
            new daum.Postcode({
                oncomplete: function (data) {

                    // Kakao에서 제공하는 data는 JSON구조로 주소 조회 결과값을 전달함
                    // 주요 결과값
                    // 주소 : data.address
                    // 우편번호 : data.zonecode
                    let address = data.address; // 주소
                    let zonecode = data.zonecode; // 우편번호
                    f.addr1.value = "(" + zonecode + ")" + address
                }
            }).open();
        }
    </script>


</head>
<body>
<h2>회원 가입하기</h2>
<hr/>
<br/>
<form name="f" method="post" action="/user/insertUserInfo" onsubmit="return doRegUserCheck(this);">

    <div class="divTable minimalistBlack">
        <div class="divTableBody">
            <div class="divTableRow">
                <div class="divTableCell">* 아이디
                </div>
                <div class="divTableCell">
                    <input type="text" name="user_id" style="width:95%"/>
                </div>
            </div>
            <div class="divTableRow">
                <div class="divTableCell">* 이름
                </div>
                <div class="divTableCell">
                    <input type="text" name="user_name" style="width:95%"/>
                </div>
            </div>
            <div class="divTableRow">
                <div class="divTableCell">* 비밀번호
                </div>
                <div class="divTableCell">
                    <input type="password" name="password" style="width:95%"/>
                </div>
            </div>
            <div class="divTableRow">
                <div class="divTableCell">* 비밀번호확인
                </div>
                <div class="divTableCell">
                    <input type="password" name="password2" style="width:95%"/>
                </div>
            </div>
            <div class="divTableRow">
                <div class="divTableCell">* 이메일
                </div>
                <div class="divTableCell">
                    <input type="email" name="email" style="width:95%"/>
                </div>
            </div>
            <div class="divTableRow">
                <div class="divTableCell">* 주소
                </div>
                <div class="divTableCell">
                    <input type="text" name="addr1" style="width:85%"/>
                    <input type="button" value="우편번호" onclick="kakaoPost(this.form)"/>
                </div>
            </div>
            <div class="divTableRow">
                <div class="divTableCell">* 상세 주소
                </div>
                <div class="divTableCell">
                    <input type="text" name="addr2" style="width:95%"/>
                </div>
            </div>
        </div>
    </div>
    <div><input type="submit" value="회원가입"/></div>
</form>
</body>
</html>