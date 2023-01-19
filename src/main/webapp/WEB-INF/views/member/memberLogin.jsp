<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
	<title>memberLogin.jsp</title>
	<jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
	<script src="https://developers.kakao.com/sdk/js/kakao.js"></script> <!-- 추가해주기 -->
	<script>
		'use strict';
		window.Kakao.init("e396cf203137da8fbf84ef169241b20d"); //javascript키 복사한거 붙여넣기
		
		//카카오 로그인
	 	function kakaoLogin() {
  		window.Kakao.Auth.login({ //로그인 인증하겠다, 기능을적을거라 중괄호
  			scope: 'profile_nickname, account_email',//동의항목에서 닉네임,이메일 변수
  			success:function(autoObj) {
  				console.log(Kakao.Auth.getAccessToken(),"로그인 OK"); //인증과정을거쳐서 토큰값을 보내줌
  				console.log(autoObj);
  				window.Kakao.API.request({
  					url : '/v2/user/me',
  					success:function(res) {
  						const kakao_account = res.kakao_account;
  						console.log(kakao_account);
  						//alert(kakao_account.email + "/" + kakao_account.profile.nickname);  // 이메일/닉네임 이렇게나옴
  						
  						//KakaoLoginOk만드는건(아래처럼하지말고 ok로 바로보내기) 숙제
  						location.href = "${ctp}/member/memberKakaoLogin?nickName="+kakao_account.profile.nickname+"&email="+kakao_account.email;
  					}
  				}); //api쓴다고 요청함 ,$.ajax랑 똑같음
  			}
			}); 
		}
		
		//카카오 로그아웃
		function kakaoLogout(kakaoKey) { //kakaoKey나중에 해보라고 우선써놓음(지금은 의미없음)
	   	//다음에 로그인시에 동의항목 체크하고 로그인할수있도록 로그아웃시키기
			/*
	   	Kakao.API.request({ //연결끊기
        url: '/v1/user/unlink',
      })
      */
      
			Kakao.Auth.logout(function() {
				console.log(Kakao.Auth.getAccessToken(), "토큰 정보가 없습니다.(로그아웃되셨습니다)"); //토큰이 null값이면 세션이 끊어진거임
			});
		}
		
	</script>
</head>
<body>
<jsp:include page="/WEB-INF/views/include/nav.jsp"></jsp:include>
<jsp:include page="/WEB-INF/views/include/slide2.jsp"></jsp:include>
<p><br/></p>
<div class="container">
	<div class="modal-dialog"> <!-- 입력창 길어지는거 막는태그 -->
		<div class="modal-content"> 
			<div class="container" style="padding:30px">
				<form name="myform" method="post" class="was-validated"> <!-- 실무에선 액션없는경우 많아서 안씀 -->
					<h2>회원 로그인</h2>
					<p>회원 아이디와 비밀번호를 입력해주세요</p>
					<div class="form-group" >
			      <label for="mid">회원 아이디:</label>
			      <input type="text" class="form-control" name="mid" id="mid" value="${mid}" placeholder="아이디를 입력하세요." required />
			      <div class="valid-feedback">입력성공!!</div>
			      <div class="invalid-feedback">회원 아이디는 필수 입력사항입니다</div>
			    </div>
					<div class="form-group">
			      <label for="pwd">비밀번호 :</label>
			      <input type="password" class="form-control" name="pwd" id="pwd" placeholder="비밀번호를 입력하세요." required/>	
			    	<div class="valid-feedback">입력성공!!</div>
			    	<div class="invalid-feedback">회원 비밀번호는 필수 입력사항입니다</div>
			    </div>
			    <div class="form-group">
				    <button type="submit" class="btn btn-primary">로그인</button>
				    <button type="reset" class="btn btn-danger">다시입력</button>
				    <button type="button" onclick="location.href='${ctp}/';" class="btn btn-success">돌아가기</button> <!-- root로 보냄 -->
				    <button type="button" onclick="location.href='${ctp}/member/memberJoin';" class="btn btn-primary">회원가입</button> <!-- memJoin.mem으로 보냄 -->
				  </div>
				  <div class="mb-2">
				  	<a href="javascript:kakaoLogin();"><img src="${ctp}/images/kakao_login_medium_narrow.png" /></a> <!-- 카카오 로그인 -->
				  	<!-- <a href="javascript:kakaoLogout();" class="btn btn-danger">로그아웃</a>  -->
				  </div>
				  <div class="row" style="font-size:12px">
				  	<span class="col"><input type="checkbox" name="idCheck" checked/>아이디 저장</span>
				  	<span class="col">
				  		[<a href="${ctp}/member/memberIdSearch">아이디찾기</a>] /
				  		[<a href="${ctp}/member/memberPwdSearch">비밀번호찾기</a>]
				  	</span>
				  </div>
				</form>
			</div>
		</div>
	</div>
</div>
<p><br/></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp"></jsp:include>
</body>
</html>