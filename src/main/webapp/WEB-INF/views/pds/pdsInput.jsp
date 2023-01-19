<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>pdsInput.jsp</title>
  <jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
  <script>
    'use strict';
	  let cnt = 1;
	  
	  function fCheck() {
		  let maxSize = 1024 * 1024 * 20;
		  let title = $("#title").val();
		  let pwd = $("#pwd").val();
		  let file = $("#file").val();
		  
		  if(file == "" || file == null) {
			  alert("업로드할 파일명을 선택해 주세요.");
			  return false;
		  }
		  else if(title.trim() == "") {
			  alert("업로드할 파일명을 입력하세요.");
			  document.getElementById("title").focus();
			  return false;
		  }
		  else if(pwd.trim() == "") {
			  alert("비밀번호를 입력하세요.");
			  document.getElementById("pwd").focus();
			  return false;
		  }
		  
		  // 파일 사이즈 처리...
		  let fileSize = 0;
		  let files = document.getElementById("file").files;
		  for(let i=0; i<files.length; i++) {
			  file = files[i];
			  let fName;
			  
			  if(file.name != "") {
				  //fileSize += document.getElementById("file").files[0].size;
				  fName = file.name;
				  let ext = fName.substr(fName.lastIndexOf(".")+1);
				  let uExt = ext.toUpperCase();
				  if(uExt != "JPG" && uExt != "GIF" && uExt != "PNG" && uExt != "ZIP" && uExt != "HWP" && uExt != "PPT" && uExt != "PPTX" && uExt != "PDF" && uExt != "TXT") {
					  alert("업로드 가능한 파일은 'JPG/GIF/PNG/ZIP/HWP/PPT/PDF/TXT' 입니다.");
					  return false;
			    }
				  fileSize += file.size;
			  }
		  }
		  if(fileSize > maxSize) {
			  alert("업로드할 파일의 최대용량은 20MByte 입니다.");
			  return false;
		  }
		  else {
			  myform.fileSize.value = fileSize;
			  //alert("fileSize : " + fileSize)
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
  <form name="myform" method="post" enctype="multipart/form-data"> <!-- enctype="multipart/form-data" : 파일을 업로드한다고하면 무조건 써주야함,받는건 라이브러리쓸거면 multipartRequest 안쓰면 parameter -->
	  <h2 class="text-center">자 료 올 리 기</h2>
	  <br/>
	  <div>
	    <input type="file" name="file" id="file" multiple="multiple" class="form-control-file border mb-2" accept=".jpg,.gif,.png,.zip,.ppt,.pptx,.hwp,.pdf,.txt"/> <!-- accept: 확장자, multiple써서 여러개 선택가능 -->
	  </div>
	  <div class="mb-2">올린이 : ${sNickName}</div>
	  <div class="mb-2">
	    제목 :
	    <input type="text" name="title" id="title" placeholder="자료 제목을 입력하세요" class="form-control" required />
	  </div>
	  <div class="mb-2">
	    내용 :
	    <textarea rows="4" name="content" id="content" class="form-control"></textarea>
	  </div>
	  <div class="mb-2">
	    분류 :
	    <select name="part" id="part" class="form-control">
	  	  <option value="학습" ${part == '학습' ? 'selected' : ''}>학습</option>
	  	  <option value="여행" ${part == '여행' ? 'selected' : ''}>여행</option>
	  	  <option value="음식" ${part == '음식' ? 'selected' : ''}>음식</option>
	  	  <option value="기타" ${part == '기타' ? 'selected' : ''}>기타</option>
	  	</select>
	  </div>
	  <div class="mb-2">
	    공개여부 :
	    <input type="radio" name="openSw" value="공개" checked />공개 &nbsp;&nbsp;
	    <input type="radio" name="openSw" value="비공개" />비공개
	  </div>
	  <div class="mb-2">
	    비밀번호 :
	    <input type="password" name="pwd" id="pwd" placeholder="비밀번호를 입력하세요" required />
	  </div>
	  <div class="mb-2">
	    <input type="button" value="자료올리기" onclick="fCheck()" class="btn btn-primary"/> &nbsp;
	    <input type="reset" value="다시쓰기" class="btn btn-warning"/> &nbsp;
	    <input type="button" value="돌아가기" onclick="location.href='${ctp}/pds/pdsList?part=${pageVo.part}&pag=${pageVo.pag}&pageSize=${pageVo.pageSize}';" class="btn btn-secondary"/> &nbsp;    
	  </div>
	  <input type="hidden" name="listPart" value="${part}"/>
	  <input type="hidden" name="fileSize"/>
	  <input type="hidden" name="hostIp" value="${pageContext.request.remoteAddr}"/>
	  <input type="hidden" name="mid" value="${sMid}"/>
	  <input type="hidden" name="nickName" value="${sNickName}"/>
  </form>
</div>
<p><br/></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp" />
</body>
</html>