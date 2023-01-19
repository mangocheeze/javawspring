<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>kakaomap.jsp</title>
	<jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
</head>
<body>
<jsp:include page="/WEB-INF/views/include/nav.jsp"></jsp:include>
<jsp:include page="/WEB-INF/views/include/slide2.jsp"></jsp:include>
<p><br/></p>
<div class="container">
	<h2>KakaoMap API(기본지도)</h2>
	<hr/>
	<div id="map" style="width:100%;height:500px;"></div> <!-- 지도크기 -->
	<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=e396cf203137da8fbf84ef169241b20d"></script>  <!-- kakao developers페이지에서 appkey에서 javascript키 복붙 -->
	<script>
		var container = document.getElementById('map'); //지도를 표시한 div태그 아이디
		var options = {
			center: new kakao.maps.LatLng(36.60193673842298 , 127.50169961494618), //지도의 중심좌표 (위도, 경도)
			level: 3  //지도의 확대 레벨 (숫자가 클수록 지도안에가 작게보임-더 많은내용보임)
		};

		var map = new kakao.maps.Map(container, options);
	</script>
	<hr/>
	<jsp:include page="kakaoMenu.jsp"/>
	<hr/>
</div>
<p><br/></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp"></jsp:include>
</body>
</html>