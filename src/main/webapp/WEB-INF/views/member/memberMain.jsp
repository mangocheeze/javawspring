<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="ctp" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>MemberMain.jsp</title>
	<jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
</head>
<body>
<jsp:include page="/WEB-INF/views/include/nav.jsp"></jsp:include>
<jsp:include page="/WEB-INF/views/include/slide2.jsp"></jsp:include>
<p><br/></p>
<div class="container">
	<h2>회 원 전 용 방</h2>
	<hr/>
  <div id="memberInfo1" class="mr-5">
		<p><font color="blue"><b>${sNickName}</b></font>님 로그인중이십니다</p>
		<p>현재<font color="blue"><b>${sStrLevel}</b></font>등급이십니다</p>
	  <c:if test="${!empty sImsiPwd}">
		  <hr/>
		  현재 임시비밀번호를 발급하여 메일로 전송처리 하였습니다.<br/>
		  개인정보를 확인하시고 필수입력사항을 기재해 주세요.<br/>
		  <a href="${ctp}/member/memberPwdUpdate" class="btn btn-secondary">비밀번호변경으로이동하기</a>
		  <hr/>
	  </c:if>
		<p>누적 포인트 : ${vo.point}</p>
		<p>최종 접속일 : <span class="viewCheck">${fn:substring(vo.lastDate,0,fn:length(vo.lastDate)-2)}</span></p>
		<p>총 방문횟수 : ${vo.visitCnt}</p>
		<p>오늘 방문횟수 : ${vo.todayCnt} </p>
	</div>
	<hr/>
  <div id="memberInfo2">
    <h3>회원사진</h3>
	  <p><img src="${ctp}/member/${vo.photo}" width="200px" /></p>
  </div>
  <hr id="memberInfo3" />
	<h4>활동내역</h4>
	<p>방명록에 올린글수 : ${cnt}개</p>	<!-- 방명록에 들어가있는 name을, 내 name이랑비교하거나 nickName이 일치할때 가져옴 -->
	<p>게시판에 올린글수 : </p>	
	<p>자료실에 올린글수 : </p>	
</div>
<p><br/></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp"></jsp:include>
</body>
</html>