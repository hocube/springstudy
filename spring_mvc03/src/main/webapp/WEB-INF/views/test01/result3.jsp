<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h2>결과보기</h2>
	<h3>
		<li>올린사람: ${name}</li>
		<li>파일이름(올릴 때 이름): ${f_name}</li>
		<li>파일이름(저장할 때 이름): ${f_name2}</li>
		<li>파일타입: ${file_type}</li>
		<li>파일크기: ${size} KB</li>
		<li>수정날짜: ${lastday}</li>
		<img src="resources/images/${f_name2}" style="width:200px;">
	</h3>
	
	
</body>
</html>