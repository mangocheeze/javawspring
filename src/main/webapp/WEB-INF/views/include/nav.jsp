<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}"/>

<!-- Navbar -->
<div class="w3-top">
  <div class="w3-bar w3-black w3-card">
    <a class="w3-bar-item w3-button w3-padding-large w3-hide-medium w3-hide-large w3-right" href="javascript:void(0)" onclick="myFunction()" title="Toggle Navigation Menu"><i class="fa fa-bars"></i></a>
    <a href="http://192.168.50.210:9090/javawspring" class="w3-bar-item w3-button w3-padding-large">HOME</a>
    <a href="${ctp}/guest/guestList" class="w3-bar-item w3-button w3-padding-large w3-hide-small">Guest</a>
    <c:if test="${sLevel <= 4}"> <!-- 보는건 준회원도 가능하니까(아예보이지도 않게하려고 - 인터셉터에서 못들어가게해놨지만) -->
	    <a href="${ctp}/board/boardList" class="w3-bar-item w3-button w3-padding-large w3-hide-small">Board</a>
	    <a href="${ctp}/pds/pdsList" class="w3-bar-item w3-button w3-padding-large w3-hide-small">Pds</a>
	    <div class="w3-dropdown-hover w3-hide-small">
	      <button class="w3-padding-large w3-button" title="More">Study1 <i class="fa fa-caret-down"></i></button>   <!-- member라고안하고 닉네임보이게함(세션에 담을거니까 세션닉네임) -->  
	      <div class="w3-dropdown-content w3-bar-block w3-card-4">
	        <a href="${ctp}/study/password/sha256" class="w3-bar-item w3-button">암호화(SHA256)</a>
	        <a href="${ctp}/study/password/aria" class="w3-bar-item w3-button">암호화(ARIA)</a>
	        <a href="${ctp}/study/password/bCryptPassword" class="w3-bar-item w3-button">암호화(bCryptPassword)</a>
	        <a href="${ctp}/study/ajax/ajaxMenu" class="w3-bar-item w3-button">AJax연습</a>
	        <a href="${ctp}/study/mail/mailForm" class="w3-bar-item w3-button">메일연습</a> <!-- 메일인증 -->
	        <a href="${ctp}/study/uuid/uuidForm" class="w3-bar-item w3-button">UUID</a> 
	        <a href="${ctp}/study/fileUpload/fileUploadForm" class="w3-bar-item w3-button">파일업로드연습</a> 
	        <a href="${ctp}/study/calendar" class="w3-bar-item w3-button">인터넷달력</a> 
	      </div>
	    </div>
	    <div class="w3-dropdown-hover w3-hide-small">
	      <button class="w3-padding-large w3-button" title="More">Study2 <i class="fa fa-caret-down"></i></button>   <!-- member라고안하고 닉네임보이게함(세션에 담을거니까 세션닉네임) -->  
	      <div class="w3-dropdown-content w3-bar-block w3-card-4">
	        <a href="${ctp}/study/qrCode" class="w3-bar-item w3-button">쿠폰(QR코드)</a>
	        <a href="${ctp}/study/kakaomap/kakaomap" class="w3-bar-item w3-button">카카오맵</a>
	        <a href="#" class="w3-bar-item w3-button">구글차트</a>
	        <a href="#" class="w3-bar-item w3-button">트랜잭션</a> <!-- 최종결제하는순간 롤백시킴? -->
	        <a href="#" class="w3-bar-item w3-button">장바구니</a>
	      </div>
	    </div>
	    <div class="w3-dropdown-hover w3-hide-small">
	      <button class="w3-padding-large w3-button" title="More">${sNickName}<i class="fa fa-caret-down"></i></button>   <!-- member라고안하고 닉네임보이게함(세션에 담을거니까 세션닉네임) -->  
	      <div class="w3-dropdown-content w3-bar-block w3-card-4">
	        <a href="${ctp}/member/memberMain" class="w3-bar-item w3-button">회원메인화면</a>
	        <a href="${ctp}/schedule/schedule" class="w3-bar-item w3-button">일정관리</a>
	        <a href="${ctp}/webMessage/webMessage" class="w3-bar-item w3-button">웹메세지</a>
	        <a href="${ctp}/member/memberList" class="w3-bar-item w3-button">회원리스트</a>
	        <a href="${ctp}/member/memberPwdUpdate?flag=pwdCheck" class="w3-bar-item w3-button">회원정보수정</a>
	        <a href="${ctp}/member/memberPwdUpdate" class="w3-bar-item w3-button">비밀번호수정</a>
	        <a href="javascript:memberDelete()" class="w3-bar-item w3-button">회원탈퇴</a>
	        <a href="${ctp}/admin/adminMain" class="w3-bar-item w3-button">관리자메뉴</a>
	      </div>
	    </div>
    </c:if>
    <c:if test="${empty sLevel}">
    	<a href="${ctp}/member/memberLogin" class="w3-padding-large w3-button">Login</a>
    	<a href="${ctp}/member/memberJoin" class="w3-padding-large w3-button">Join</a>
    </c:if> <!-- 세션에 있는 레벨이 비어있으면 로그인보임 -->
    <c:if test="${!empty sLevel}"><a href="${ctp}/member/memberLogout" class="w3-padding-large w3-button">Logout</a></c:if> <!-- 세션에 있는 레벨이 비어있지 않으면 로그아웃 -->
    <a href="javascript:void(0)" class="w3-padding-large w3-hover-red w3-hide-small w3-right"><i class="fa fa-search"></i></a> <!-- 돋보기 -->
  </div>
</div>

<!-- 햄버거 버튼 -->
<!-- Navbar on small screens (remove the onclick attribute if you want the navbar to always show on top of the content when clicking on the links) --> 
<div id="navDemo" class="w3-bar-block w3-black w3-hide w3-hide-large w3-hide-medium w3-top" style="margin-top:46px">
  <a href="#band" class="w3-bar-item w3-button w3-padding-large" onclick="myFunction()">BAND</a>
  <a href="#tour" class="w3-bar-item w3-button w3-padding-large" onclick="myFunction()">TOUR</a>
  <a href="#contact" class="w3-bar-item w3-button w3-padding-large" onclick="myFunction()">CONTACT</a>
  <a href="#" class="w3-bar-item w3-button w3-padding-large" onclick="myFunction()">MERCH</a>
</div>
<script>
	function memberDelete() {
		let ans = confirm("탈퇴하시겠습니까?");
		if(ans) {
			ans = confirm("탈퇴하시면 1개월간 같은 아이디로 재가입하실수 없습니다.\n그래도 탈퇴하시겠습니까?");
			if(ans) location.href = "${ctp}/member/memberDelete";
		}
	}
</script>