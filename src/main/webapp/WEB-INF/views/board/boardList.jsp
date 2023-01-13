<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="ctp" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>boardList.jsp</title>
  <jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
  <script>
    'use strict';
    function pageCheck() {
    	let pageSize = document.getElementById("pageSize").value;
    	location.href = "${ctp}/board/boardList?pageSize="+pageSize+"&pag=${pageVo.pag}";
    }
    
    function searchCheck() {
    	let searchString = $("#searchString").val();
    	
    	if(searchString.trim() == "") {
    		alert("찾고자 하는 검색어를 입력하세요!");
    		searchForm.searchString.focus();
    	}
    	else {
    		searchForm.submit();
    	}
    }
    
	  // 전체선택
	  $(function(){
	  	$("#checkAll").click(function(){
	  		if($("#checkAll").prop("checked")) { //.prop() : 속성의 실제의미하는 값을 return해줌 /전체선택의 속성이 checked일때 
		    		$(".chk").prop("checked", true); //체크박스를 체크처리해줌
	  		}
	  		else { //전체선택에 체크되어있지않으면 
		    		$(".chk").prop("checked", false); //체크박스 체크하지않음
	  		}
	  	});
	  });
	  
	  // 선택항목 반전
	  $(function(){
	  	$("#reversekAll").click(function(){ //선택반전 누르면
	  		$(".chk").prop("checked", function(){ // 체크박스를 체크되어있을때 기능을 넣을건데
	  			return !$(this).prop("checked"); // this :현재상태 , 현재상태 체크되어있는걸 해제하겠다
	  		});
	  	});
	  });
	  
	  // 선택항목 삭제하기(ajax처리하기)
	  function selectDelCheck() {
	  	let ans = confirm("선택된 모든 게시물을 삭제 하시겠습니까?");
	  	if(!ans) return false;
	  	let delItems = ""; //누적할거라 이렇게 변수를 선언함
	  	for(let i=0; i<myform.chk.length; i++) { //체크박스의 길이만큼 반복할건데
	  		if(myform.chk[i].checked == true) delItems += myform.chk[i].value + "/"; //체크박스 i번째가 체크되어있을때 0번째부터i까지 해당 체크박스의 idx를 가져와서 /와함께 누적함
	  		//찍어보면 idx가 12/15/16/ 이렇게 들어가있음
	  	}
	  	if(delItems == "") {
	  		alert("한개 이상을 선택후 처리하세요.");
	  		return false;
	  	}
			
	  	$.ajax({
	  		type : "post",
	  		url  : "${ctp}/board/boardAdminDelete",
	  		data : {delItems : delItems},
	  		success:function(res) {
	  			if(res == "1") {
	  				alert("선택된 파일을 삭제처리 하였습니다.");
	  			  location.reload();
	  			}
	  		},
	  		error  :function() {
	  			alert("전송오류!!");
	  		}
	  	});
	  }
  </script>
</head>
<body>
<jsp:include page="/WEB-INF/views/include/nav.jsp" />
<jsp:include page="/WEB-INF/views/include/slide2.jsp" />
<p><br/></p>
<div class="container">
  <div class="text-center">
  <c:if test="${empty searchString}"><h2>게 시 판 리 스 트</h2></c:if>
  <c:if test="${!empty searchString}"><h2>게 시 판 리 스 트 &nbsp;<font color='blue'><b>"${searchString}"</b>&nbsp;</font>(총<font color='red'>${pageVo.totRecCnt}</font>건)</h2></c:if>
  </div>
  <br/>
  <form name="myform">
	  <table class="table table-borderless">
	    <tr>
	      <td class="text-left p-0">
	        <c:if test="${sLevel != 4}"><a href="${ctp}/board/boardInput?pag=${pageVo.pag}&pageSize=${pageVo.pageSize}" class="btn btn-secondary btn-sm">글쓰기</a></c:if>
	      </td>
	      <c:if test="${sLevel == 0}">
		    	<td colspan="4">
		        <input type="checkbox" id="checkAll" onclick="checkAllCheck()"/>전체선택/해제 &nbsp;&nbsp;
		        <input type="checkbox" id="reversekAll" onclick="reverseAllCheck()"/>선택반전 &nbsp;&nbsp;
		        <input type="button" value="선택항목삭제" onclick="selectDelCheck()" class="btn btn-danger btn-sm"/>
		      </td>
	      </c:if>
	      <td class="text-right p-0">
	        <select name="pageSize" id="pageSize" onchange="pageCheck()">
	          <option value="5"  ${pageVo.pageSize==5  ? 'selected' : ''}>5건</option>
	          <option value="10" ${pageVo.pageSize==10 ? 'selected' : ''}>10건</option>
	          <option value="15" ${pageVo.pageSize==15 ? 'selected' : ''}>15건</option>
	          <option value="20" ${pageVo.pageSize==20 ? 'selected' : ''}>20건</option>
	        </select>
	      </td>
	    </tr>
	  </table>
	  <table class="table table-hover text-center">
	    <tr class="table-dark text-dark">
	      <c:if test="${sLevel == 0}"><th>선택</th></c:if>
	      <th>글번호</th>
	      <th>글제목</th>
	      <th>글쓴이</th>
	      <th>글쓴날짜</th>
	      <th>조회수</th>
	      <th>좋아요</th>
	    </tr>
	  	<c:set var="curScrStartNo" value="${pageVo.curScrStartNo}"/>
	    <c:forEach var="vo" items="${vos}">
	    	<tr>
	        <c:if test="${sLevel == 0}">
			      <td>
		        	<input type="checkbox" name="chk" class="chk" value="${vo.idx}"/> 
		        	<!-- 체크박스 , 체크박스들의 값은 idx임 => 체크박스 하나당 해당글의 정보들이 vo에 담겨있는데 vo를 굳이 다 가지고올필욘없어서 vo의 idx만 가져옴(idx로 삭제처리를할거니까)-->
		      	</td>
	        </c:if>
	    	  <td>${curScrStartNo}</td>
	    	  <td class="text-left">
	    	    <a href="${ctp}/board/boardContent?idx=${vo.idx}&pageSize=${pageVo.pageSize}&pag=${pageVo.pag}">${vo.title}</a>
	    	    <c:if test="${vo.replyCount != 0}">(${vo.replyCount})</c:if>
	    	    <c:if test="${vo.hour_diff <= 24}"><img src="${ctp}/images/new.gif"/></c:if>
	    	  </td>
	    	  <td>${vo.nickName}</td>
	    	  <td>
	    	    <!-- 1일(24시간)이 지난것은 날짜만표시, 1일(24시간)이내것은 시간을 표시하되, 24시간 이내중 현재시간보다 이후시간은 날짜와 시간을 함께 표시 -->
	    	    <c:if test="${vo.hour_diff > 24}">${fn:substring(vo.WDate,0,10)}</c:if>
	    	    <c:if test="${vo.hour_diff < 24}">
	    	      ${vo.day_diff > 0 ? fn:substring(vo.WDate,0,16) : fn:substring(vo.WDate,11,19)}
	    	    </c:if>
	    	  </td>
	    	  <td>${vo.readNum}</td>
	    	  <td>${vo.good}</td>
	    	</tr>
	    	<c:set var="curScrStartNo" value="${curScrStartNo-1}"/>
	    </c:forEach>
	    <tr><td colspan="6" class="m-0 p-0"></td></tr>
	  </table>
  </form>
</div>

<!-- 블록 페이지 시작 -->
<div class="text-center">
  <ul class="pagination justify-content-center">
    <c:if test="${pageVo.pag > 1}">
      <li class="page-item"><a class="page-link text-secondary" href="${ctp}/board/boardList?pageSize=${pageVo.pageSize}&pag=1&search=${search}&searchString=${searchString}">첫페이지</a></li> <!-- get방식으로 보낸거라 페이징처리할때 search랑 searchString가지고 다녀야함 안그럼 잃어버림 -->
    </c:if>
    <c:if test="${pageVo.curBlock > 0}">
      <li class="page-item"><a class="page-link text-secondary" href="${ctp}/board/boardList?pageSize=${pageVo.pageSize}&pag=${(pageVo.curBlock-1)*pageVo.blockSize + 1}&search=${search}&searchString=${searchString}">이전블록</a></li>
    </c:if>
    <c:forEach var="i" begin="${(pageVo.curBlock)*pageVo.blockSize + 1}" end="${(pageVo.curBlock)*pageVo.blockSize + pageVo.blockSize}" varStatus="st">
      <c:if test="${i <= pageVo.totPage && i == pageVo.pag}">
    		<li class="page-item active"><a class="page-link bg-secondary border-secondary" href="${ctp}/board/boardList?pageSize=${pageVo.pageSize}&pag=${i}&search=${search}&searchString=${searchString}">${i}</a></li>
    	</c:if>
      <c:if test="${i <= pageVo.totPage && i != pageVo.pag}">
    		<li class="page-item"><a class="page-link text-secondary" href="${ctp}/board/boardList?pageSize=${pageVo.pageSize}&pag=${i}&search=${search}&searchString=${searchString}">${i}</a></li>
    	</c:if>
    </c:forEach>
    <c:if test="${pageVo.curBlock < pageVo.lastBlock}">
      <li class="page-item"><a class="page-link text-secondary" href="${ctp}/board/boardList?pageSize=${pageVo.pageSize}&pag=${(pageVo.curBlock+1)*pageVo.blockSize + 1}&search=${search}&searchString=${searchString}">다음블록</a></li>
    </c:if>
    <c:if test="${pageVo.pag < pageVo.totPage}">
      <li class="page-item"><a class="page-link text-secondary" href="${ctp}/board/boardList?pageSize=${pageVo.pageSize}&pag=${pageVo.totPage}&search=${search}&searchString=${searchString}">마지막페이지</a></li>
    </c:if>
  </ul>
</div>
<!-- 블록 페이지 끝 -->
<br/>
<!-- 검색기 처리 시작  -->
<div class="container text-center">
  <form name="searchForm"><!-- get방식으로 보내줄거라 안씀, 검색기는 post말고 get방식을씀 리스트에서 바로보여줘야되니까 -->
    <b>검색 : </b>
    <select name="search">
      <option value="title" ${search = 'title' ? "selected" : ""}>글제목</option>
      <option value="nickName" ${search = 'nickName' ? "selected" : ""}>글쓴이</option>
      <option value="content" ${search = 'content' ? "selected" : ""}>글내용</option>
    </select>
    <input type="text" name="searchString" id="searchString"/>
    <input type="button" value="검색" onclick="searchCheck()" class="btn btn-secondary btn-sm"/>
    <input type="hidden" name="pag" value="${pageVo.pag}"/>
    <input type="hidden" name="pageSize" value="${pageVo.pageSize}"/>
  </form>
</div>
<!-- 검색기 처리 끝  -->
<p><br/></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp" />
</body>
</html>