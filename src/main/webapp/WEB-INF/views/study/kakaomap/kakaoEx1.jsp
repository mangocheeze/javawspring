<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>kakaoEx1.jsp</title>
	<jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
	<script>
		function addressCheck(latitude, longitude) {
			//alert(latitude + "/" + longitude);
			var address = myform.address.value; //let으로 해도되는데 아래랑ㅇ통일하기위해 var로함
			if(address == "") {
				alert("선택한 지점의 장소명을 입력하세요.");
				myform.address.focus();
				return false;
			}
			var query = {
					address : address,
					latitude : latitude,
					longitude : longitude
			}
			$.ajax({
				type : "post",
				url  : "${ctp}/study/kakaomap/kakaoEx1",
				data : query,
				success : function(res) {
					if(res == "1") alert("선택한 지점이 등록되었습니다.");
					else alert ("이미 같은지점이 있습니다. 이름을 변경해서 다시 등록할수있습니다.");
				},
				error : function(){
					alert("전송오류!");
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
	<h2>클릭한 위치에 마커 표시하기</h2>
	<hr/>
	<div id="map" style="width:100%;height:500px;"></div> <!-- 지도크기 -->
	<p><b>마커를 표시할 지도의 위치를 클릭해 주세요</b></p>
	
	<form name="myform">
		<div id="clickPoint"></div>
	</form>
	
	<!-- 본문실행하고 스크립트 실행하게하려고 아래로 내림 -->
	<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=e396cf203137da8fbf84ef169241b20d"></script>  <!-- kakao developers페이지에서 appkey에서 javascript키 복붙 -->
	<script>
		var container = document.getElementById('map'); //지도를 표시한 div태그 아이디
		var options = {
			center: new kakao.maps.LatLng(36.60193673842298 , 127.50169961494618), //지도의 중심좌표
			level: 2  //지도의 확대 레벨 (숫자가 클수록 지도안에가 작게보임-더 많은내용보임)
		};

		// 지도를 표시할 div의 아이디와 지도 옵션으로 지도를 생성한다. 
		var map = new kakao.maps.Map(container, options);
		
		// 마커를 생성합니다
		var marker = new kakao.maps.Marker({
				// 지도 중심좌표에 마커를 생성한다.
		    position: map.getCenter()
		});

		// 마커가 지도 위에 표시되도록 설정합니다
		marker.setMap(map);
		
		// 지도에 클릭 이벤트를 등록합니다
		// 지도를 클릭하면 마지막 파라미터로 넘어온 함수를 호출합니다
		kakao.maps.event.addListener(map, 'click', function(mouseEvent) {        
		    
		    // 클릭한 위도, 경도 정보를 가져옵니다 
		    var latlng = mouseEvent.latLng; 
		    
		    // 마커 위치를 클릭한 위치로 옮깁니다
		    marker.setPosition(latlng);
		    
		    var message = '클릭한 위치의 위도는 <font color="red">' + latlng.getLat() + '</font> 이고, ';
		    message += '경도는 <font color="red">' + latlng.getLng() + '</font> 입니다. &nbsp;';
		    message += '<input type="button" value="처음위치로복귀" onclick="location.reload();"/>';
		    message += '<p>선택한 지점의 장소명 : <input type="text" name="address"/> &nbsp;';
		    message += '<input type="button" value="장소저장" onclick="addressCheck('+latlng.getLat()+','+latlng.getLng()+')"/></p>'; //바깥쪽이 ''니까 변수도따당도 '' , .getLat():위도를 가져옴
	    
	    //var resultDiv = document.getElementById('clickLatlng'); 
	   	//resultDiv.innerHTML = message;
	   	document.getElementById("clickPoint").innerHTML = message;
		});
	</script>
	<hr/>
	<jsp:include page="kakaoMenu.jsp"/>
	<hr/>
</div>
<p><br/></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp"></jsp:include>
</body>
</html>