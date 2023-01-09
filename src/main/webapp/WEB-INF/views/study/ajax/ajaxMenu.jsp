<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>ajaxMenu.jsp</title>
	<jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
	<script>
    'use strict';
    function aJaxTest1_1(idx) {
    	$.ajax({
    		type  : "post",
    		url   : "${ctp}/study/ajax/ajaxTest1_1",
    		data  : {idx : idx},
    		success:function(res) {
    			$("#demo").html(res);
    		},
    		error : function() {
    			alert("전송 오류!!!");
    		}
    	});
    }
    
    function aJaxTest1_2(idx) {
    	$.ajax({
    		type  : "post",
    		url   : "${ctp}/study/ajax/ajaxTest1_2",
    		data  : {idx : idx},
    		success:function(res) {
    			$("#demo").html(res);
    		},
    		error : function() {
    			alert("전송 오류!!!");
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
	<h2>AJax 연습</h2>
	<hr/>
	<p> 기본(String) :
		<a href= "javascript:aJaxTest1_1(10)" class="btn btn-info">값전달1</a> &nbsp; <!-- 1~10까지의 난수(10은 아무거나 넣어준거임) -->
		<a href= "javascript:aJaxTest1_2(10)" class="btn btn-primary">값전달2</a> &nbsp; 
		: <span id="demo"></span>
	</p>
	<p> 응용1(배열) :  <!-- 다른곳에 왔다갔다하면 깜빡임 심해져서 aJax로 처리하는게좋음 -->
		<a href="${ctp}/study/ajax/ajaxTest2_1" class="btn btn-secondary">시(도)/구(시,군,동)(String배열)</a> &nbsp;
    <a href="${ctp}/study/ajax/ajaxTest2_2" class="btn btn-secondary">시(도)/구(시,군,동)(ArrayList)</a> &nbsp;
    <a href="${ctp}/study/ajax/ajaxTest2_3" class="btn btn-secondary">시(도)/구(시,군,동)(Map(HashMap))</a> &nbsp;
		: <span id="demo"></span>
	</p>
  <p>응용2(DB) :
    <a href="${ctp}/study/ajax/ajaxTest3" class="btn btn-secondary">회원아이디검색(DB활용)</a> &nbsp;
  </p>
</div>
<p><br/></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp"></jsp:include>
</body>
</html>