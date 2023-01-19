<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>kakaoEx5.jsp</title>
  <jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
</head>
<body>
<jsp:include page="/WEB-INF/views/include/nav.jsp" />
<jsp:include page="/WEB-INF/views/include/slide2.jsp" />
<p><br/></p>
<div class="container">
  <h2>거리 비교하기</h2>
  <hr/>
  <div id="map" style="width:100%;height:350px;"></div>
  
  <hr/>
  
  <jsp:include page="kakaoMenu.jsp"/>

	<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=5b97ef649f5d9df28ecf21280488505c"></script>
	<script>
	var mapContainer = document.getElementById('map'), // 지도를 표시할 div 
	    mapOption = { 
	        center: new kakao.maps.LatLng(36.62935542331672, 127.45760875128917), // 지도의 중심좌표
	        level: 3 // 지도의 확대 레벨
	    };
	
	var map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다
	
	<c:forEach var="vo" items="${vos}"> /* 충북대기점으로 15km안에것들이 vos에담겨있음 */
		// 마커가 표시될 위치입니다 
		var markerPosition  = new kakao.maps.LatLng(${vo.latitude}, ${vo.longitude}); 
		
		// 마커를 생성합니다
		var marker = new kakao.maps.Marker({
		    position: markerPosition
		});
		
		// 마커가 지도 위에 표시되도록 설정합니다
		marker.setMap(map);
	</c:forEach>
	
	// 아래 코드는 지도 위의 마커를 제거하는 코드입니다
	// marker.setMap(null);    
	</script>
</div>
<p><br/></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp" />
</body>
</html>