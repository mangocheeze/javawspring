<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>memberIdSearch.jsp</title>
	<jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
	<script>
		'use strict';
		
		function idSearch() {
			let name = myform.name.value;
			let tel = myform.tel.value;
			
			$.ajax({
				type : "post",
				url  : "${ctp}/member/memberIdSearch",
				data : {name: name,
								tel : tel},
				success : function(res) {
					if(res == "") {
						alert("일치하는 정보가 없습니다.");
					}
					else {
						$("#demo").html("아이디는 <font color='red'>"+res+"</font>입니다.<br/><br/>");
					}
				},
				error : function(){
					alert("전송실패");
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
	<h2 class="text-center">아이디 찾기</h2>
	<form name="myform" method="post" class="was-validated">
		<table class="table table-bordered">
			<tr>
				<th>성함</th>
				<td><input type="text" name="name" id="name" class="form-control" placeholder="가입시 등록했던 성함을 입력하세요."required/></td>
			</tr>
			<tr>
				<th>전화번호</th>
				<td><input type="text" name="tel" id="tel" class="form-control" placeholder="전화번호를 형식에맞게 입력하세요.(010-1234-5678)" required/></td>
			</tr>
			<tr>
				<td colspan="2" class="text-center">
					<input type="button" value="아이디찾기" class="btn btn-info" onclick="idSearch()"/>
					<input type="reset" value="다시입력" class="btn btn-warning"/>
					<input type="button" value="돌아가기" onclick="location.href='${ctp}/member/memberLogin';" class="btn btn-success"/>
					<input type="button" value="회원가입" onclick="location.href='${ctp}/member/memberJoin';" class="btn btn-primary"/>
				</td>
			</tr>
		</table>
		<div id="demo"></div>
	</form>
</div>
<p><br/></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp"></jsp:include>
</body>
</html>