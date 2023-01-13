<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="ctp" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>memberInfro.jsp</title>
	<jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
</head>
<body>
<jsp:include page="/WEB-INF/views/include/nav.jsp"></jsp:include>
<jsp:include page="/WEB-INF/views/include/slide2.jsp"></jsp:include>
<p><br/></p>
<div class="container">
	<h2>회원 정보 상세 보기</h2>
	<br/>
	<table class="table">
    <tr><td>아이디: ${vo.mid}</td></tr>
    <tr><td>별명: ${vo.nickName}</td></tr>
    <tr><td>성명: ${vo.name}</td></tr>
    <tr><td>성별: ${vo.gender}</td></tr>
    <tr><td>생일: ${fn:substring(vo.birthday,0,10)}</td></tr>
    <tr><td>전화번호: ${vo.tel}</td></tr>
    <tr><td>주소: ${vo.address}</td></tr>
    <tr><td>이메일: ${vo.email}</td></tr>
    <tr><td>홈페이지: ${vo.homePage}</td></tr>
    <tr><td>직업 : ${vo.job}</td></tr>
    <tr><td>취미 : ${vo.hobby}</td></tr>
    <tr><td>자기소개 : ${vo.content}</td></tr>
    <tr><td>등급 : ${strLevel}</td></tr>
    <tr><td>총방문횟수 : ${vo.visitCnt}</td></tr>
    <tr><td>최초 가입일 : ${fn:substring(vo.startDate,0,16)}</td></tr>
    <tr><td>마지막 방문일 : ${fn:substring(vo.lastDate,0,16)}</td></tr>
    <tr><td>오늘 방문횟수: ${vo.todayCnt}</td></tr>
    <c:if test="${sLevel == 0}">
        <tr><td>총 포인트: ${vo.point}</td></tr>
        <tr><td>공개유무: ${vo.userInfor}</td></tr>
    </c:if>	
    <tr><td>사진 : <img src="${ctp}/member/${vo.photo}" width="150px" /></td></tr>
    <tr><td><button type="button" class="btn btn-secondary" onclick="location.href='${ctp}/member/memberList?pag=${pag}';">돌아가기</button></td></tr>
	</table>
</div>
<p><br/></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp"></jsp:include>
</body>
</html>