<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>이미지 파일로부터 텍스트 인식을 위한 파일 업로드 페이지</title>
    <link rel="stylesheet" href="/css/table.css"/>
</head>
<body>
<h2>인식할 이미지 파일 업로드</h2>
<hr/>
<form name="f" method="post" enctype="multipart/form-data" action="/ocr/readImage">
    <div class="divTable minimalistBlack">
        <div class="divTableHeading">
            <div class="divTableRow">
                <div class="divTableHead">이미지 파일 업로드</div>
            </div>
        </div>
        <div class="divTableBody">
            <div class="divTableRow">
                <div class="divTableCell"><input type="file" name="fileUpload"/>
                </div>
            </div>
        </div>
    </div>
    <div>
        <button type="submit">파일 업로드</button>
    </div>
</form>
</body>
</html>

