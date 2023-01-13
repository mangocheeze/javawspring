<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>boardUpdate.jsp</title>
	<script src="${ctp}/ckeditor/ckeditor.js"></script>
  <jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
  <style>
  	th {
  		text-align : center;
  		background-color: #eee;
  	}
  </style>
  <script>
  	'use strict';
		/* 숙제 유효성검사 꼭 하기 !!! */
  	function fCheck(){
  		let title = myform.title.value;
  		let content = myform.content.value;
  		
  		if(title.trim() == "") {
  			alert("게시글 제목을 입력하세요");
  			myform.title.focus();
  		}
  		else {
  			myform.submit();
  		}
  	}
  </script>
</head>
<body>
<jsp:include page="/WEB-INF/views/include/nav.jsp" />
<jsp:include page="/WEB-INF/views/include/slide2.jsp" />
<p><br/></p>
<div class="container">
  <form name="myform" method="post">
    <h2 class="text-center">게시판 글쓰기</h2>
    <br/>
    <table class="table table-bodered">
      <tr>
        <th>글쓴이</th>
        <td>${sNickName}</td>
      </tr>
      <tr>
        <th>글제목</th>
        <td><input type="text" name="title" value="${vo.title}" required class="form-control"/></td>
      </tr>
      <tr>
        <th>이메일</th>
        <td><input type="text" name="email" value="${vo.email}" class="form-control"/></td> <!-- 여기의 value는 vo에 있는게아닌 변수에 담긴걸 뿌리겠단 소리 -->
      </tr>
      <tr>
        <th>홈페이지</th>
        <td><input type="text" name="homePage"  value="${vo.homePage}" class="form-control"/></td>
      </tr>
      <tr>
        <th>글내용</th>
        <td><textarea rows="6" name="content" id="CKEDITOR" class="form-control" required>${vo.content}</textarea> </td>
        <script>
        	CKEDITOR.replace("content",{
        		height:500,
        		filebrowserUploadUrl:"${ctp}/imageUpload",
        		uploadUrl : "${ctp}/imageUpload"
        	}); 
        //여기서 루트는 홈컨트롤러
        //폭은 건들지말고 높이만건들기
        //filebrowserUploadUrl : 이미지에서 이미지업로드에서 파일선택하고 서버에 올려야 올려주는 예약어
        //uploadUrl : 게시판 글쓰기 칸자체에 복붙해서 올려도 올려지는 예약어
        </script>
      </tr>
      <tr>
        <td colspan="2" class="text-center">
        	<input type="button" value="글수정하기" onclick="fCheck()" class="btn btn-info"/> &nbsp;
        	<input type="reset" value="다시입력" class="btn btn-warning"/> &nbsp;
        	<input type="button" value="돌아가기" onclick="location.href='${ctp}/board/boardList?pag=${pag}&pageSize=${pageSize}';" class="btn btn-secondary"/> &nbsp;
        </td>
      </tr>
    </table>
    <input type="hidden" name="hostIp" value="${pageContext.request.remoteAddr}"/>
    <input type="hidden" name="mid" value="${sMid}"/> <!-- 닉네임은 변경가능하게해놔서 진짜 이글의 주인인지 아이디를 통해 알수있기때문 -->
    <input type="hidden" name="nickName" value="${sNickName}"/> <!-- 입력받지않아서 hidden으로 넘겨야함 -->
    <input type="hidden" name="pag" value="${pag}"/> 
    <input type="hidden" name="pageSize" value="${pageSize}"/> 
  </form>
</div>
<p><br/></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp" />
</body>
</html>