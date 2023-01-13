<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="ctp" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>adminMemberList.jsp</title>
  <jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
  <script>
  	'use strict';
  	
  	function midSearch()	{
  		let mid = myform.mid.value;
  		if(mid.trim() == "") {
  			alert("아이디를 입력하세요!");
  			myform.mid.focus();
  		}
  		else {
  			myform.submit();
  		}
  	}
  	
  	//탈퇴처리
    function delCheck(idx) {
    	let ans = confirm("탈퇴처리 시키겠습니까?");
    	// if(ans) location.href='${ctp}/admin/member/adminMemberDel?pag=${pageVo.pag}&idx='+idx;
    	if(!ans) return false;
    	
    	$.ajax({
    		type : "post",
    		url : "${ctp}/admin/member/adminMemberDel",
    		data : {idx : idx},
    		success : function(res) {
    			if(res == "1") {
    				alert("탈퇴처리 되었습니다.");
    				location.reload();
    			}
    			else {
    				alert("탈퇴처리 실패");
    			}
    		},
    		error : function() {
    			alert("전송 오류");
    		}
    	});
    	
    	
    }
    
    //등급변경
    function searchCheck(e) {
    	let ans = confirm("등급을 수정하시겠습니까?");
    	if(!ans) return false;
    	
    	let items = e.value.split("/");
    	
    	let query = {
    			idx : items[1],
    			level : items[0]
    	}
    	
    	$.ajax({
    		type  : "post",
    		url   : "${ctp}/admin/member/adminMemberLevel",
    		data  : query,
    		success:function(res) {
    			if(res == "1") alert("등급 수정완료!");
    			else alert("등급 수정실패~~");
    			location.reload();
    		},
    		error : function() {
    			alert("전송 오류~~");
    		}
    	});
    }
  </script>
</head>
<body>
<p><br/></p>
<div class="container">
  <h2 class="text-center">전체 회원 리스트</h2>
  <br/>
  <form name="myform">
  	<div class="row">
  	  <div class="col form-inline">
  	    <input type="text" name="mid" class="form-control" autofocus />&nbsp;
  	    <input type="button" value="아이디개별검색" onclick="midSearch();" class="btn btn-secondary" />
  	  </div>
  	  <div class="col text-right"><button type="button" onclick="location.href='${ctp}/admin/member/adminMemberList';" class="btn btn-success">전체검색</button></div> <!-- 어떤페이지가던 다시 돌아옴 전체검색으로 돌아감 -->
  	</div>
  </form>
  <table class="table table-hover text-center">
    <tr class="table-dark text-dark">
      <th>번호</th> <!-- 나중에 바꾸기??? -->
      <th>아이디</th>
      <th>별명</th>
      <th>성명</th>
      <th>최초가입일</th> <!-- 관리자는 이것도 한눈에 보게하는게좋음 -->
      <th>마지막접속일</th>
      <th>등급</th>  <!-- 현재등급보여줌 -->
      <th>탈퇴유무</th>  <!-- DB에서 바로 날리지않고 우선 신청만받음, 한달후에 관리자가 지우게하던,관리작 바로지우게하면됨 -->
    </tr>
    <c:set var="curScrStartNo" value="${pageVo.curScrStartNo}"/>
    <c:forEach var="vo" items="${vos}" varStatus="st">
      <tr>
        <td>${curScrStartNo}</td>
        <td><a href="${ctp}/adMemInfor.ad?mid=${vo.mid}&pag=${pageVo.pag}">${vo.mid}</a></td> <!-- 아이디에 링크걸기,전체회원중 아이디를 클릭한회원만 회원정보 상세보기 보이게함 /memInfor에 id랑 페이지를 같이넘김(상세보기하고 돌아오기하면 갔던페이지그대로가게함)-->
        <td>${vo.nickName}</td>
				<td>${vo.name}<c:if test="${sLevel == 0 && vo.userInfor == '비공개'}"><font color='red'>(비공개)</font></c:if></td> <!-- 관리자나 비공개면 (비공개)라고 써있음,회원리스트에서 관리자는 회원들이 비공개면 비공개로보이게하기 -->
        <td>${fn:substring(vo.startDate,0,19)}</td>
        <td>${fn:substring(vo.lastDate,0,19)}</td>
        <td>
        	<form name="levelForm">
						<select name="level" onchange="searchCheck(this)"><!--  onchange: 콤보상자,내용이 바뀌니까 ,콤보상자바꾸면 메세지가 뜨게함,javascript: 자바스크립트를 사용할거야 -->
         			<option value="0/${vo.idx}" <c:if test="${vo.level==0}">selected</c:if>>관리자</option> <!-- 등급을바꿀건데 고유번호를(5번)을 0번레벨로 바꾸겠다? -->
              <option value="1/${vo.idx}" <c:if test="${vo.level==1}">selected</c:if>>운영자</option>
              <option value="2/${vo.idx}" <c:if test="${vo.level==2}">selected</c:if>>우수회원</option>
              <option value="3/${vo.idx}" <c:if test="${vo.level==3}">selected</c:if>>정회원</option>
              <option value="4/${vo.idx}" <c:if test="${vo.level==4}">selected</c:if>>준회원</option>
            </select>
        	</form>
        </td>
        <td> 
        	<c:if test="${vo.userDel=='OK'}"><a href="javascript:delCheck(${vo.idx})"><font color="red">탈퇴신청</font></a></c:if> <!-- Ok면 탈퇴신청함 -->
        	<c:if test="${vo.userDel!='OK'}">활동중</c:if>
        </td>
      </tr>
      <c:set var="curScrStartNo" value="${curScrStartNo-1}"/>
    </c:forEach>
    <tr><td colspan="8" class="m-0 p-0"></td></tr> 
  </table>
</div>
<br/>
<!-- 블록 페이지 시작 -->
<div class="text-center">
  <ul class="pagination justify-content-center">
    <c:if test="${pageVo.pag > 1}">
      <li class="page-item"><a class="page-link text-secondary" href="${ctp}/admin/member/adminMemberList?pag=1">첫페이지</a></li>
    </c:if>
    <c:if test="${pageVo.curBlock > 0}">
      <li class="page-item"><a class="page-link text-secondary" href="${ctp}/admin/member/adminMemberList?pag=${(pageVo.curBlock-1)*pageVo.blockSize + 1}">이전블록</a></li>
    </c:if>
    <c:forEach var="i" begin="${(pageVo.curBlock)*pageVo.blockSize + 1}" end="${(pageVo.curBlock)*pageVo.blockSize + pageVo.blockSize}" varStatus="st">
      <c:if test="${i <= pageVo.totPage && i == pageVo.pag}">
    		<li class="page-item active"><a class="page-link bg-secondary border-secondary" href="${ctp}/admin/member/adminMemberList?pag=${i}">${i}</a></li>
    	</c:if>
      <c:if test="${i <= pageVo.totPage && i != pageVo.pag}">
    		<li class="page-item"><a class="page-link text-secondary" href="${ctp}/admin/member/adminMemberList?pag=${i}">${i}</a></li>
    	</c:if>
    </c:forEach>
    <c:if test="${pageVo.curBlock < pageVo.lastBlock}">
      <li class="page-item"><a class="page-link text-secondary" href="${ctp}/admin/member/adminMemberList?pag=${(pageVo.curBlock+1)*pageVo.blockSize + 1}">다음블록</a></li>
    </c:if>
    <c:if test="${pageVo.pag < pageVo.totPage}">
      <li class="page-item"><a class="page-link text-secondary" href="${ctp}/admin/member/adminMemberList?pag=${pageVo.totPage}">마지막페이지</a></li>
    </c:if>
  </ul>
</div>
<!-- 블록 페이지 끝 -->
<p><br/></p>
</body>
</html>