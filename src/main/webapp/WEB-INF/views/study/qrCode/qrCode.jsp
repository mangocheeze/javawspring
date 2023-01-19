<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>qrCode.jsp</title>
	<jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
	<script>
		'use strict';
		
		function qrCreate(no) {
			let mid = '';
			let email = '';
			let query = '';
			let moveUrl = '';
			let movie = '';
			let cinema = '';
			let time = '';
			let adult = '';
			let child = '';
			
			if(no == 1) {
				mid = myform.mid.value;
				email = myform.email.value;
				query = {
						mid : mid,
						moveFlag : email
				}
			}
			else if(no == 2) {
				moveUrl = myform.moveUrl.value;
				query = {
						moveFlag : moveUrl
				}
			}
			else if(no == 3) {
				//mid = $("#mid").val();
				mid = myform.mid.value;
				email = myform.email.value;
				movie = myform.movie.value;
				cinema = myform.cinema.value;
				time = myform.time.value;
				adult = myform.adult.value;
				child = myform.child.value;
				
				query = {
						mid : mid,
						moveFlag : email,
						movie : movie,
						cinema : cinema,
						time : time,
						adult : adult,
						child : child
				}
			}
			
			
			$.ajax({
				type : "post",
				url  : "${ctp}/study/qrCode",
				data : query,
				success : function(res) {
					alert("qr코드가 생성되었습니다. 이름은? " + res);
					$("#qrCodeView").show();
					$("#qrView").html(res);
					let qrImage = '<img src="${ctp}/data/qrCode/'+res+'.png"/>';
					$("#qrImage").html(qrImage);
				},
				error : function() {
					alert("전송오류!!");
				}
			});
		}
		
		function qrSearch() {
			let qrIdx = myform.qrIdx.value;
			
			$.ajax({
				type : "post",
				url : "${ctp}/study/qrSearch",
				data : {qrIdx : qrIdx},
				success : function(res) {
					if(res != "") {
						$("#qrDemo").html(res); //text는글자만 가져오고, html은 태그나 html적인걸 다 가져옴,res를 qrDemo에 띄움
					}
					else {
						$("#qrDemo").text("찾으시는 고유번호에 해당하는 정보가 없습니다.");
					}
				},
				error : function() {
					alert("전송오류");
				}
			});
		}
	</script>
</head>
<body>
<jsp:include page="/WEB-INF/views/include/nav.jsp"></jsp:include>
<jsp:include page="/WEB-INF/views/include/slide2.jsp"></jsp:include>
<p><br/></p>
<div class="container">
	<form name="myform">
		<h2>QR Code 생성연습</h2>
		<p>
			<b>개인정보 입력</b> : <br/>
			아이디 : <input type="text" name="mid" value="${vo.mid}" class="mb-2"/><br/> <!-- ${sMid}로해도됨 -->
			이메일 : <input type="text" name="email" value="${vo.email}" class="mb-2"/><br/>
			<input type="button" value="신상정보QR생성" onclick="qrCreate(1)" class="btn btn-success" />
		</p>
		<hr/>
		<h4>소개하고싶은 사이트 주소를 입력하세요.</h4>
		<p>
			이동할 주소 : <input type="text" name="moveUrl" value="cjsk1126.tistory.com" size="40"/> <!-- value에서 파일명엔 /가 들어가면안됨 -->
									<input type="button" value="소개QR생성" onclick="qrCreate(2)" class="btn btn-primary" />
		</p>
		<hr/>
		<h4>영화 예매
			<input type="button" value="영화예매QR생성" onclick="qrCreate(3)" class="btn btn-info ml-2" />
		</h4>
		<p>
			영화 :
			<select name="movie">
				<option value="">===선택===</option>
				<option value="아바타2">아바타2</option>
				<option value="유령">유령</option>
				<option value="영웅">영웅</option>
			</select>
			상영관 :
			<select name="cinema">
				<option value="">===선택===</option>
				<option value="1관">1관</option>
				<option value="2관">2관</option>
				<option value="3관">3관</option>
			</select>
			시간 :
			<select name="time">
				<option value="">===선택===</option>
				<option value="12시">12시</option>
				<option value="16시">16시</option>
				<option value="18시">18시</option>
			</select>
			성인 :
			<select name="adult">
				<option value="">===선택===</option>
				<option value="성인1명">성인1명</option>
				<option value="성인2명">성인2명</option>
				<option value="성인3명">성인3명</option>
			</select>
			청소년 :
			<select name="child">
				<option value="">===선택===</option>
				<option value="청소년1명">청소년1명</option>
				<option value="청소년2명">청소년2명</option>
				<option value="청소년3명">청소년3명</option>
			</select>
		</p>
		<!-- QR코드생성 -->
		<hr/>
		<div id="qrCodeView" style="display:none;"> <!--style="display:none;" 처음엔 감춰놓고 누르면 나옴  -->
			<h3>생성된 QR코드 확인하기</h3>
			<div>
				- 생성된 qr코드명 : <span id="qrView"></span><br/>
				<span id="qrImage"></span>
			</div>
		</div>
		<div>
			- 영화 예매 확인하기 : <input type="text" name="qrIdx" id="qrIdx"/>
			<input type="button" value="예매확인" onclick="qrSearch()" class="btn btn-danger"/><br/>
		</div>
	</form>
	<br/>
	<div id="qrDemo"></div>
</div>
<p><br/></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp"></jsp:include>
</body>
</html>