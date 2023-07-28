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
		<h1>
			<li> 올린사람: ${ name}</li>
			<li> 파일이름: ${ f_name}</li>
			<li> 파일타입: ${ f_type}</li>
			<li> 파일사이즈: ${ size}</li>
			<img src="resources/images/${f_name}" style="width: 150px;">
		</h1>
</body>
</html>