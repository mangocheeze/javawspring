<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
	<title>adminLeft.jsp</title>
	<jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
</head>
<body>
<p><br/></p>
<div class="container">
	<h5>관리자 홈 화면</h5>
	<hr/>
	<p>
		신규 가입자(한달기준) : <b><font color='blue'>${monthJoin}</font></b>명
	</p>
	<hr/>
	<p>
		새로운 글(24시간기준) : <b><font color='red'>${newContent}</font></b>개
	</p>
	
</div>
<p><br/></p>
</body>
</html>