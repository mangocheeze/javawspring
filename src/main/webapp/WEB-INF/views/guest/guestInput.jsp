<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
	<title>guInput.jsp</title> <!-- 입력창 -->
	<jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
</head>
<body>
<jsp:include page="/WEB-INF/views/include/nav.jsp"></jsp:include>
<jsp:include page="/WEB-INF/views/include/slide2.jsp"></jsp:include>
<p><br/></p>
<div class="container">
	<form name="myform" method="post" class="was-validated"> <!-- 전송 누르면 컨트롤러로보냄  -->
		<h2>방 명 록 글 올 리 기</h2>
		<br/>
		<div class="form-group" >
      <label for="name">성명</label>
      <input type="text" class="form-control" name="name" id="name" placeholder="이름을 입력하세요." required />
      <div class="valid-feedback">통과!!</div>
      <div class="invalid-feedback">이름 입력은 필수입니다</div>
    </div>
		<div class="form-group">
      <label for="email">E-mail</label>
      <input type="text" class="form-control" name="email" id="email" placeholder="이메일을 형식에 맞게 입력하세요" />	
    </div>
		<div class="form-group">
      <label for="homePage">HomePage</label>
      <input type="text" class="form-control" name="homePage" id="homePage" value="http://" placeholder="홈페이지주소를 입력하세요" />
    </div>
		<div class="form-group">
      <label for="content">방문소감</label>
      <textarea rows="5" class="form-control" name="content" id="content" required></textarea> <!-- textarea:여러줄쓸수있어서 -->
      <div class="valid-feedback">통과!!</div>
      <div class="invalid-feedback">방문소감 입력은 필수입니다</div>
    </div>
    
    <div class="form-group">
	    <button type="submit" class="btn btn-primary">방명록 등록</button>
	    <!-- 우리는 프로젝트에서  타입 button으로해서 프론트체크해야함 -->
	    <button type="reset" class="btn btn-primary">방명록 다시입력</button>
	    <button type="button" onclick="location.href='${ctp}/guest/guestList';" class="btn btn-primary">돌아가기</button>
	  </div>
	  <input type="hidden" name="hostIp" value="<%=request.getRemoteAddr()%>"/> <!-- hostIp는 not null이라 필수로 들어가야에러안나는데 몰래넘겨야해서 hidden사용 -->
	</form>
</div>
<p><br/></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp"></jsp:include>
</body>
</html>