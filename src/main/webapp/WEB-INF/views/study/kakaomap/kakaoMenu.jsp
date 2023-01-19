<%-- kakaoMenu.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}"/>
<div>
	<p>
		<a href="${ctp}/study/kakaomap/kakaoEx1" class="btn btn-success">마커표시/DB저장</a>&nbsp;
		<a href="${ctp}/study/kakaomap/kakaoEx2" class="btn btn-primary">DB에저장된지명검색/삭제</a>&nbsp;
		<a href="${ctp}/study/kakaomap/kakaoEx3" class="btn btn-secondary">카카오제공정보DB저장</a>&nbsp;<!-- 카카오제공DB사용 -->
		<a href="${ctp}/study/kakaomap/kakaoEx4" class="btn btn-danger">검색후주변시설찾기</a>&nbsp;
		<a href="${ctp}/study/kakaomap/kakaoEx5" class="btn btn-warning">거리별검색</a>&nbsp;
	</p>
</div>