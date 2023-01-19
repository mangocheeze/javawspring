<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="ctp" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>wmList.jsp</title>
	<jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
</head>
<body>
<p><br/></p>
<div class="container">
	<table class="table table-hover">
		<tr class="table-dark text-dark">
			<th>번호</th>
			<th>
				<c:if test="${mSw == 1 || mSw == 2 || mSw == 5}">보낸사람(발신자)</c:if> <!-- 보낸사람이 표시되어야할것들 -->
				<c:if test="${mSw == 3 || mSw == 4}">받은사람(수신자)</c:if> 
			</th>
			<th>제목</th>
			<th>
				<c:if test="${mSw == 1 || mSw == 2 || mSw == 5}">보낸/확인날짜(발신/확인날짜)</c:if> 
				<c:if test="${mSw == 3 || mSw == 4}">받은날짜(수신날짜)</c:if> 
			</th>
		</tr>
<!-- 
	mSw   => 1:받은메세지,  2:새메세지, 3:보낸메세지, 4:수신확인, 5:휴지통, 6:메세지상세보기, 0:메세지작성
	mFlag => 11:받은메세지, 12:보낸메세지 , 13:휴지통
 -->
 	<c:set var="curScrStartNo" value="${pageVo.curScrStartNo}"/>
 	<c:forEach var ="vo" items="${vos}">
 		<tr>
 			<td>${curScrStartNo}</td>
 			<td>
 				<c:if test="${mSw == 1 || mSw == 2 || mSw == 5}">${vo.sendId}</c:if>
				<c:if test="${mSw == 3 || mSw == 4}">${vo.receiveId}</c:if> 
 			</td>
 			<td>
 				${vo.title}
 				<c:if test="${vo.receiveSw =='n'}"><img src="${ctp}/images/new.gif"/></c:if>
 			</td>
 			<td>
 				<span style="font-size:0.8em">${fn:substring(vo.receiveDate,0,19)}</span>
 			</td>
 		</tr>
		<c:set var="curScrStartNo" value="${curScrStartNo - 1}"/>
 	</c:forEach>
 	<tr><td colspan="4" class="m-0 p-0"></td></tr> <!-- 마감처리 -->
	</table> 
</div>
<p></p>
</body>
</html>