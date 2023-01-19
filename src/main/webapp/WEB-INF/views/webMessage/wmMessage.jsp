<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>wmMessage.jsp</title>
	<jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
	<style>
		#leftWindow {
			float: left; /* 왼쪽으로배치 */
			width: 25%;
			height: 520px;
			text-align: center;
			background-color: #ddd;
		}
		#rightWindow {
			float: left;
			width: 75%;
			height: 520px;
			text-align: center; 
			background-color: #eee;
		}
		#footerMargin {
			clear: both; /* 오른쪽/왼쪽을 취소 */
			margin: 10px;
		}
		h3 {
			text-align: center;
		}
		
	</style>
</head>
<body>
<jsp:include page="/WEB-INF/views/include/nav.jsp"></jsp:include>
<jsp:include page="/WEB-INF/views/include/slide2.jsp"></jsp:include>
<p><br/></p>
<!-- 
	mSw   => 1:받은메세지,  2:새메세지, 3:보낸메세지, 4:수신확인, 5:휴지통, 6:메세지상세보기, 0:메세지작성
	mFlag => 11:받은메세지, 12:보낸메세지 , 13:휴지통
 -->
<div class="container">
	<h3>☆ 메세지 관리 ☆</h3>
	<div>(현재접속자:<font color='red'>${sMid}</font>)</div>
	<div id="leftWindow"> <!-- 왼쪽화면 -->
		<p><br/></p>
		<p><a href="${ctp}/webMessage/webMessage?mSw=0">메세지작성</a></p>
		<p><a href="${ctp}/webMessage/webMessage?mSw=1&mFlag=11">받은메세지</a></p><!-- 메세지작성말고 나머지는 폼이 같음,스위치로 분리 -->
		<p><a href="${ctp}/webMessage/webMessage?mSw=2">새메세지</a></p>
		<p><a href="${ctp}/webMessage/webMessage?mSw=3&mFlag=12">보낸메세지</a></p>
		<p><a href="${ctp}/webMessage/webMessage?mSw=4">수신확인</a></p>
		<p><a href="${ctp}/webMessage/webMessage?mSw=5&mFlag=13">휴지통</a></p>
	</div>
	<div id="rightWindow"> <!-- 오른쪽화면 -->
		<p>
			<c:if test="${mSw ==0}">
				<h3>메세지작성</h3>
				<div class="text-right"><a href="#" class="btn btn-success btn-sm m-0 mr-3">주소록</a></div><!-- 숙제 -->
				<jsp:include page="wmInput.jsp"></jsp:include> <!-- 같은폴더라(같은위치라) ${ctp}안써도됨. 그리고 include는 애초에 못씀 -->
			</c:if>
			<c:if test="${mSw ==1}">
				<h3>받은메세지</h3>
				<jsp:include page="wmList.jsp"></jsp:include> 
			</c:if>
			<c:if test="${mSw ==2}">
				<h3>새메세지</h3>
				<jsp:include page="wmList.jsp"></jsp:include> 
			</c:if>
			<c:if test="${mSw ==3}">
				<h3>보낸메세지</h3>
				<jsp:include page="wmList.jsp"></jsp:include> 
			</c:if>
			<c:if test="${mSw ==4}">
				<h3>수신확인</h3>
				<jsp:include page="wmList.jsp"></jsp:include> 
			</c:if>
			<c:if test="${mSw ==5}">
				<h3>휴지통</h3>
				<jsp:include page="wmList.jsp"></jsp:include> 
			</c:if>
			<c:if test="${mSw ==6}">
				<h3>메세지 내용보기</h3>
				<jsp:include page="wmContent.jsp"></jsp:include> 
			</c:if>
		</p>
	</div>
</div>
<!-- float를 다 left로해서 그냥쓰면 푸터도 같이 올라가서 이름을 하나줘서 style줌 -->
<div id="footerMargin"></div>
<jsp:include page="/WEB-INF/views/include/footer.jsp"></jsp:include>
</body>
</html>