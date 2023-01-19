<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
<c:set var="ctp" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
	<title>pdsList.jsp</title>
	<jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
	<script>
		'use strict';
		
		//파트별 리스트 출력하기
		function partCheck() { /* 자료실리스트() 괄호안에가 옵션에 선택한대로 바뀜 */
    	let part = partForm.part.value;
    	location.href = "${ctp}/pds/pdsList?part="+part;
    }
		
		//모달창을 통한 상세내역 보여주기
		function modalView(title,nickName,mid,part,fName,fSName,fSize,downNum,fDate) {
    	let imgs = fSName.split("/");  // fName은 안잘라도되지만 fSName은 잘라줘야 /빼고 안에내용들이 들어가서 파일을 보여줌, 슬래시를 기준으로 잘라줌
    	fDate = fDate.substring(0,19);
    	fSize = parseInt(fSize / 1024) + "KByte";
    	
    	$("#myModal").on("show.bs.modal",function(e){ //show.bs.modal : 명령어, 모달이 열릴때 바로 실행되는 이벤트(모달창을부름)
				$(".modal-header #title").html(title);
				$(".modal-header #part").html(part); //#part : header에위치한 part에다가 part를 넣는다
				$(".modal-body #nickName").html(nickName);
				$(".modal-body #mid").html(mid);
				$(".modal-body #fName").html(fName);
				$(".modal-body #fDate").html(fDate);
				$(".modal-body #fSize").html(fSize);
				$(".modal-body #downNum").html(downNum);
				$(".modal-body #fSName").html(fSName);
				//$(".modal-body #imgSrc").attr("src",'${ctp}/data/pds/'+imgs[0]); //속성이라 attr
				
				// 저장된 파일에 그림이 1장이라도 있다면 1장만 화면에 출력시켜준다. 
	    		$(".modal-body #imgSrc").attr("src",'');	// 기존에 존재하는 그림파일이 있으면 제거시킨다.(src값을 없앴기에 보이지 않게처리했다.)
	    		for(let i=0; i<imgs.length; i++) {
	    			let imgExt = imgs[i].substring(imgs[i].lastIndexOf(".")+1).toUpperCase();
	    			if(imgExt=="JPG" || imgExt=="GIF" || imgExt=="PNG") {
	    				$(".modal-body #imgSrc").attr("src",'${ctp}/data/pds/'+imgs[i]);
	    				break;
	    			}
	    		}
			});
		}
		
		
		//modal창을 통해서 비밀번호 확인후 파일을 삭제처리(Ajax처리)
		function padDelCheckModalOk() {
			let idx = pwdModalForm.idx.value;
			let pwd = pwdModalForm.pwd.value;
	
			//query는 마음대로 줘도되는 변수 data는 명령어
			let query = {
					idx : idx,
					pwd : pwd
			}
			
			$.ajax({
				type  : "post",
				url   : "${ctp}/pds/pdsDelete",
				data  : query, 
				success : function(res) {
					if(res == "1") {
						alert("삭제되었습니다.");
						location.reload();
					}
					else {
						alert("삭제 실패 ~~");
					}
				},
				error : function() {
					alert("전송 오류 ~~");
				}
			});
		}
		
		//다운로드 횟수 증가
		function downNumCheck(idx) {
			$.ajax({
				type : "post",
				url  : "${ctp}/pds/pdsDownNum",
				data : {idx : idx},
				success : function(){
					location.reload();
				},
				error : function(){
					alert("전송오류!!");
				}
			});
		}
		
		//modal창을 통해서 비밀번호 확인후 파일을 삭제처리
		function pdsDelCheckModal(idx) {
			$("#myPwdModal").on("show.bs.modal",function(e){ //모달창을 부름
				$(".modal-body #idx").val(idx); //idx와 fSName은 숨겨서 hidden으로 넘겨야함 .val(idx)라고 써주면됨
			});
		}
		
		
		
	</script>
</head>
<body>
<jsp:include page="/WEB-INF/views/include/nav.jsp"></jsp:include>
<jsp:include page="/WEB-INF/views/include/slide2.jsp"></jsp:include>
<p><br/></p>
<div class="container">
	<h2 class="text-center">자 료 실 리 스 트 (${pageVo.part})</h2>
	<br/>
	<table class="table table-borderless">
		<tr>
			<td style="width:20%" class="text-left"> <!-- 반응형이라 %가 좋음(px는고정이되니까) -->
				<form name="partForm"> <!-- 분류할거임 -->
					<select name="part" onchange="partCheck()" class="form-control">
						<option value="전체" ${pageVo.part == '전체' ? 'selected' : ''}>전체</option>
						<option value="학습" ${pageVo.part == '학습' ? 'selected' : ''}>학습</option>
						<option value="여행" ${pageVo.part == '여행' ? 'selected' : ''}>여행</option>
						<option value="음식" ${pageVo.part == '음식' ? 'selected' : ''}>음식</option>
						<option value="기타" ${pageVo.part == '기타' ? 'selected' : ''}>기타</option>
					</select>
				</form>
			</td>
			<td class="text-right">
				<a href="${ctp}/pds/pdsInput?part=${pageVo.part}" class="btn btn-success">자료올리기</a> <!-- part를 pageVo에 넣어놨으니까 / 자료의 주소보려고 button대신 a 씀 -->
			</td>
		</tr>
	</table>
	<table class="table table-hover text-center">
    <tr class="table-dark text-dark">
			<th>번호</th>
			<th>자료제목</th>
			<th>올린사람</th>
			<th>올린날짜</th>
			<th>분류</th>
			<th>파일명(크기)</th>
			<th>다운수</th>
			<th>비고</th> <!-- 삭제하는거 -->
		</tr>
		<c:set var="curScrStartNo" value="${pageVo.curScrStartNo}"/>
		<c:forEach var="vo" items="${vos}">
			<tr>
				<td>${curScrStartNo}</td>
        <td> <!-- 제목누르면 내용보여주기 -->
          <a href="${ctp}/pds/pdsContent?idx=${vo.idx}&pag=${pageVo.pag}&part=${pageVo.part}">${vo.title}</a> <!-- 현재글에대해서 봐야되니까 idx넘거야함 -->
    	    <c:if test="${vo.hour_diff <= 24}"><img src="${ctp}/images/new.gif"/></c:if>
        </td>
				<td>${vo.nickName}</td>
        <td>
          <%-- ${vo.fDate} --%>
          <c:if test="${vo.hour_diff > 24}">${fn:substring(vo.FDate,0,10)}</c:if>
          <c:if test="${vo.hour_diff < 24}">		<!-- 24시간이 지나지 않았지만 현재시간~자정까찌는 '날짜:시간', 나머지는 '시간'만 출력 -->
    	      ${vo.day_diff > 0 ? fn:substring(vo.FDate,0,16) : fn:substring(vo.FDate,11,19)}
    	    </c:if>
        </td>
				<td>${vo.part}</td>
				<td>
					<c:set var="fNames" value="${fn:split(vo.FName,'/')}"/>
					<c:set var="fSNames" value="${fn:split(vo.FSName,'/')}"/>
					<c:forEach var="fName" items="${fNames}" varStatus="st">
						<a href="${ctp}/data/pds/${fSNames[st.index]}" download="${fName}" onclick="downNumCheck(${vo.idx})">${fName}</a><br/> <!-- vo강 아니라 forEach문에서 fName이라고해서 첫글자소문자로함 -->
					</c:forEach>
					(<fmt:formatNumber value="${vo.FSize/1024}" pattern="#,###" />KByte)  <!-- 1000단위마다 , 찍힘 -->
				</td>
				<td>${vo.downNum}</td>
				<td>
					<h5><a href="#" onclick="modalView('${vo.title}','${vo.nickName}','${vo.mid}','${vo.part}','${vo.FName}','${vo.FSName}','${vo.FSize}','${vo.downNum}','${vo.FDate}')" class="badge badge-primary" data-toggle="modal" data-target="#myModal">모달창</a><br/></h5> <!-- #은 현재창에서 처리하겠다, 여기다가 onclick값 넣어보면 혹시값안넘어갔을때 볼수있음 --> 
					<h5><a href="${ctp}/pds/pdsTotalDown?idx=${vo.idx}" class="badge badge-success">전체다운</a><br/></h5>
					<h5><a href="#" onclick="pdsDelCheckModal(${vo.idx})" data-toggle="modal" data-target="#myPwdModal" class="badge badge-danger">삭제</a></h5> <!-- 모달창 이용해서 받을거임 -->
				</td> <!-- 모달은 메세지창이떠도 뒤에 내용들이 다 보임 -->
			</tr>
			<c:set var="curScrStartNo" value="${curScrStartNo-1}"/>
		</c:forEach>
		<tr><td colspan="8" class="m-0 p-0"></td></tr>
	</table>
</div>

<!-- 블록 페이지 시작 -->
<div class="text-center">
  <ul class="pagination justify-content-center">
    <c:if test="${pageVo.pag > 1}">
      <li class="page-item"><a class="page-link text-secondary" href="pdsList?part=${pageVo.part}&pag=1">첫페이지</a></li>
    </c:if>
    <c:if test="${pageVo.curBlock > 0}">
      <li class="page-item"><a class="page-link text-secondary" href="pdsList?part=${pageVo.part}&pag=${(pageVo.curBlock-1)*pageVo.blockSize + 1}">이전블록</a></li>
    </c:if>
    <c:forEach var="i" begin="${(pageVo.curBlock)*pageVo.blockSize + 1}" end="${(pageVo.curBlock)*pageVo.blockSize + pageVo.blockSize}" varStatus="st">
      <c:if test="${i <= pageVo.totPage && i == pageVo.pag}">
    		<li class="page-item active"><a class="page-link bg-secondary border-secondary" href="pdsList?part=${pageVo.part}&pag=${i}">${i}</a></li>
    	</c:if>
      <c:if test="${i <= pageVo.totPage && i != pageVo.pag}">
    		<li class="page-item"><a class="page-link text-secondary" href="pdsList?part=${pageVo.part}&pag=${i}">${i}</a></li>
    	</c:if>
    </c:forEach>
    <c:if test="${pageVo.curBlock < pageVo.lastBlock}">
      <li class="page-item"><a class="page-link text-secondary" href="pdsList?part=${pageVo.part}&pag=${(pageVo.curBlock+1)*pageVo.blockSize + 1}">다음블록</a></li>
    </c:if>
    <c:if test="${pageVo.pag < pageVo.totPage}">
      <li class="page-item"><a class="page-link text-secondary" href="pdsList?part=${pageVo.part}&pag=${pageVo.totPage}">마지막페이지</a></li>
    </c:if>
  </ul>
</div>
<!-- 블록 페이지 끝 -->


 <!-- The Modal(모달창 클릭시 자료실의 내용을 모달창에 출력한다)  -->
<div class="modal fade" id="myModal">
  <div class="modal-dialog">
    <div class="modal-content">
    
      <!-- Modal Header -->
      <div class="modal-header">
        <h4 class="modal-title"><span id="title"></span>(분류:<span id="part"></span>)</h4>
        <button type="button" class="close" data-dismiss="modal">&times;</button> <!--&times; => x버튼(누르면닫힘)  -->
      </div>
      
      <!-- Modal body -->
      <div class="modal-body"> <!-- 여긴 마음대로 꾸미기 -->
        - 올린이 : <span id ="nickName"></span>
        <hr/>
        - 아이디 : <span id ="mid"></span><br/>
        - 파일명 : <span id ="fName"></span><br/>
        - 파일올린날짜 : <span id="fDate"></span><br/>
        - 파일크기 : <span id ="fSize"></span><br/>
        - 파일다운횟수 : <span id ="downNum"></span><br/>
        <hr/>
        - 저장파일명 : <span id ="fSName"></span><br/>
				<img id="imgSrc" width="350px"><br/>        
      </div>
      
      <!-- Modal footer -->
      <div class="modal-footer">
        <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
      </div>
    </div>
  </div>
</div>

 <!-- The Modal(폼태그로 비밀번호 처리하기 위한 모달창)  -->
<div class="modal fade" id="myPwdModal">
  <div class="modal-dialog">
    <div class="modal-content">
    
      <!-- Modal Header -->
      <div class="modal-header">
        <h4 class="modal-title">비밀번호 조회</h4>
        <button type="button" class="close" data-dismiss="modal">&times;</button> <!--&times; => x버튼(누르면닫힘)  -->
      </div>
      
      <!-- Modal body -->
      <div class="modal-body"> <!-- 여긴 마음대로 꾸미기 -->
        <form name="pwdModalForm" class="was-validated">
        	비밀번호 :
        	<input type="password" name="pwd" id="pwd" placeholder="비밀번호를 입력하세요" class="form-control mb-2" required />
        	<input type="button" value="비밀번호확인후전송" onclick="padDelCheckModalOk()" class="btn btn-success form-control"	/>
        	<input type="hidden" name="idx" id="idx" />
        </form>   
      </div>
      
      <!-- Modal footer -->
      <div class="modal-footer">
        <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
      </div>
    </div>
  </div>
</div>
  
<p><br/></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp"></jsp:include>
</body>
</html>