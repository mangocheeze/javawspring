<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>kakaoEx3.jsp</title>
  <jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
  <script>
      var address = '${address}';
  
      function addressSearch() {
            address = myform.searchAddress.value;
            if(address == ""){
                alert("검색할 지점을 선택하세요");
                return false;
            }
            location.href = "${ctp}/study/kakaomap/kakaoEx3?address="+address;
        }
      
      function addAddress() {
            address = myform.address.value;
            latitude = myform.latitude.value;
            longitude = myform.longitude.value;
            if(latitude == ""){
                alert("저장할 장소를 선택하세요");
                return false;
            }
            var query = {
                address : address,
                latitude : latitude,
                longitude : longitude
            };
            $.ajax({
                type : "post",
                url : "${ctp}/study/kakaomap/kakaoEx1",
                data : query,
                success : function(res) {
                    if(res=="1") alert("선택한 지점이 등록되었습니다.");
                    else alert("이미 저장된 지점입니다.");
                },
                error : function() {
                    alert("전송오류!!");
                }
            });
        }
      
  </script>
</head>
<body>
<jsp:include page="/WEB-INF/views/include/nav.jsp"/>
<jsp:include page="/WEB-INF/views/include/slide2.jsp"/>
<p><br/></p>
<div class="container">
  <h2>지역 검색 후 DB에 저장하기</h2>
  <hr/>
  <div>
      <form name="myform" method="post">
          <div>
              <font size="4"><b>지역으로 검색하기</b></font>
                <input type="text" name="searchAddress" id="searchAddress" />
              <input type="button" value="지역검색" onclick="addressSearch()"/>
              <input type="button" value="주소추가(DB)" onclick="addAddress()"/>&nbsp;
              선택된 지점 : <span id="selectAddress"></span>
              <input type="hidden" name="address" id="address"/>
              <input type="hidden" name="latitude" id="latitude"/>
              <input type="hidden" name="longitude" id="longitude"/>
          </div>
      </form>
  </div>
  <hr/>
  <div id="map" style="width:100%;height:500px;"></div>
  <hr/>
  <jsp:include page="kakaoMenu.jsp"/>
    <script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=e396cf203137da8fbf84ef169241b20d&libraries=services"></script>
  <script>
        //마커를 클릭하면 장소명을 표출할 인포윈도우 입니다
      var infowindow = new kakao.maps.InfoWindow({zIndex:1});
    
      var mapContainer = document.getElementById('map'), // 지도를 표시할 div 
          mapOption = {
              center: new kakao.maps.LatLng(37.566826, 126.9786567), // 지도의 중심좌표
              level: 3 // 지도의 확대 레벨
          };  
    
      // 지도를 생성합니다    
      var map = new kakao.maps.Map(mapContainer, mapOption); 
    
      // 장소 검색 객체를 생성합니다
      var ps = new kakao.maps.services.Places(); 
    
      // 키워드로 장소를 검색합니다
      ps.keywordSearch(address, placesSearchCB); 
    
      // 키워드 검색 완료 시 호출되는 콜백함수 입니다
      function placesSearchCB (data, status, pagination) {
          if (status === kakao.maps.services.Status.OK) {
    
              // 검색된 장소 위치를 기준으로 지도 범위를 재설정하기위해
              // LatLngBounds 객체에 좌표를 추가합니다
              var bounds = new kakao.maps.LatLngBounds();
    
              for (var i=0; i<data.length; i++) {
                  displayMarker(data[i]);    
                  bounds.extend(new kakao.maps.LatLng(data[i].y, data[i].x));
              }       
    
              // 검색된 장소 위치를 기준으로 지도 범위를 재설정합니다
              map.setBounds(bounds);
          } 
      }
    
      // 지도에 마커를 표시하는 함수입니다
      function displayMarker(place) {
          
          // 마커를 생성하고 지도에 표시합니다
          var marker = new kakao.maps.Marker({
              map: map,
              position: new kakao.maps.LatLng(place.y, place.x) 
          });
    
          // 마커에 클릭이벤트를 등록합니다
          kakao.maps.event.addListener(marker, 'click', function() {
              // 마커를 클릭하면 장소명이 인포윈도우에 표출됩니다
              infowindow.setContent('<div style="padding:5px;font-size:12px;">' + place.place_name + '</div>');
              infowindow.open(map, marker);
              $("#selectAddress").html("<font color='red'><b>"+place.place_name+"</b></font>");
              $("#address").val(place.place_name);
              $("#latitude").val(place.y);
              $("#longitude").val(place.x);
          });
      }
  </script>
</div>
<p><br/></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp"/>
</body>
</html>