<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>이미지 파일로부터 텍스트 인식을 위한 파일 업로드 페이지</title>
<body>
<h2>인식할 이미지 파일 업로드</h2>
<hr/>
<form name="form1" method="post" enctype="multipart/form-data" action="/ocr/readImage">
    <br/>
    이미지 파일 업로드 : <input type="file" name="fileUpload"/>
    <br/>
    <br/>
    <input type="submit" value="전송"/>
</form>
</body>
</html>

