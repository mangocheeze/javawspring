<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<% pageContext.setAttribute("newLine", "\n"); %> 
<c:set var="ctp" value="${pageContext.request.contextPath}"/>
<!-- 이렇게 위에뽑아낸게 MVC2패턴 -->
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
	<title>scheduleMenu.jsp</title>
  <jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
	<script>
		'use strict';
		$(document).ready(function(){
			$("#scheduleInputHidden").hide(); //일정등록 누르기전까진 일정등록창닫기 안보임
		});
		
		//일정 등록폼을 가상에 출력하기 (현재창에서 끝내려고 ajax로 이렇게 작성함)
		function scheduleInputView() {
			let scheduleInput = '<hr/><div id="sheduleInputForm">';
			scheduleInput += '<form name="myform" method="post">'; 
			scheduleInput += '<table class="table table-bordered">';
			scheduleInput += '<tr><th>일정분류</th><td>';
			scheduleInput += '<select name="part" class="form-control">';
			scheduleInput += '<option value="모임">모임</option>';
			scheduleInput += '<option value="업무">업무</option>';
			scheduleInput += '<option value="학습">학습</option>';
			scheduleInput += '<option value="여행">여행</option>';
			scheduleInput += '<option value="기타">기타</option>';
			scheduleInput += '</select>';
			scheduleInput += '</td></tr>';
			scheduleInput += '<tr><th>내용</th><td>';
			scheduleInput += '<textarea name="content" rows="4" class="form-control"></textarea>';
			scheduleInput += '</td></tr>';
			scheduleInput += '<tr><td colspan="2" class="text-center">';
			scheduleInput += '<input type="button" value="일정등록" onclick="scheduleInputOk()" class="btn btn-success form-control"/>';
			scheduleInput += '</td></tr>';
			scheduleInput += '</table>';
			scheduleInput += '</form></div>';
			
			//일정 등록창열기
			$("#scheduleInputView").hide();  //일정등록버튼 안보이게하기
			$("#scheduleInputHidden").show(); //등록창닫기 버튼 보이게하기
			$("#demo").html(scheduleInput);		//데모
		}
		
		//일정등록창닫기
		function scheduleInputHidden(){
			$("#scheduleInputView").show(); 
			$("#scheduleInputHidden").hide(); 
			//$("#sheduleInputForm").hide();
			$("#sheduleInputForm").slideUp(500); //0.5초 느리게
		}
		
		//일정등록하기(ajax처리 -그래야 지금화면을 벗어나지않음)
		function scheduleInputOk() {
			let part = myform.part.value;  //위에 만든 폼
			let content= myform.content.value;
			if(content == "") {
				alert("일정을 입력하세요!");
				myform.content.focus();
				return false;
			}
			let query = {
					mid  : '${sMid}', 
					ymd  : '${ymd}',
					part : part,
					content : content
			}//세션에있는 아이디 ,문자라 따옴표줘야함
			$.ajax({
					type : "post",
					url  : "${ctp}/schedule/scheduleInputOk",
					data : query,
					success: function() {
							alert("일정이 등록되었습니다.");
							location.reload(); 
					},
					error : function(){
						alert("전송오류");
					}
			});
		}
		
		//스케줄 수정하기
		function updateCheck(idx, part, content) {
			let scheduleInput = '<div id="scheduleUpdateForm'+idx+'">';
			scheduleInput += '<form name="updateForm'+idx+'">';
			scheduleInput += '<table class="table table-bordered">';
			scheduleInput += '<tr><th>일정분류</th><td>';
			scheduleInput += '<select name="part" id="part'+idx+'" class="form-control">';
			scheduleInput += '<option value="모임">모임</option>';
			scheduleInput += '<option value="업무">업무</option>';
			scheduleInput += '<option value="학습">학습</option>';
			scheduleInput += '<option value="여행">여행</option>';
			scheduleInput += '<option value="기타">기타</option>';
			scheduleInput += '<option value="'+part+'" selected>'+part+'</option>';
			scheduleInput += '</select>';
			scheduleInput += '</td></tr>';
			scheduleInput += '<tr><th>내용</th><td>';
			scheduleInput += '<textarea name="content" id="content'+idx+'" rows="4" class="form-control">'+content.replaceAll("<br/>","\n")+'</textarea>'; /* '+content+'로만 쓰면안됨- \n을 br태그로바꿔놔서(엔터처리때문) => 다시 \n으로 입력처리해야돼서(replaceAll사용) */
			scheduleInput += '</td></tr>';
			scheduleInput += '<tr><td colspan="2" class="text-center">';
			scheduleInput += '<span class="row">';
			scheduleInput += '<span class="col"><input type="button" value="일정수정" onclick="scheduleUpdateOk('+idx+')" class="btn btn-success form-control"/></span>';
			scheduleInput += '<span class="col"><input type="button" value="수정창닫기" onclick="scheduleUpdateClose('+idx+')" class="btn btn-secondary form-control"/></span>'; /* 어떤창을 넘겨야하는지 알아야되니까 따당하기 */
			scheduleInput += '</span>';
			scheduleInput += '</td></tr>';
			scheduleInput += '</table>';
			scheduleInput += '</form></div>';
			
			
			$("#scheduleUpdateOpen"+idx).hide(); //수정버튼 사라지게하기
			$("#updateDemo"+idx).slideDown(500);		//데모
			$("#updateDemo"+idx).html(scheduleInput);		//데모
			
		}
		
		//수정창 닫기
		function scheduleUpdateClose(idx) {
	    $("#scheduleUpdateOpen"+idx).show();
	    $("#scheduleUpdateForm"+idx).slideUp(500);
		}
		
		//일정수정하기
		function scheduleUpdateOk(idx) {
			let part = $("#part"+idx).val();
			let content = $("#content"+idx).val();
			let query = {
					idx  : idx,
					part : part,
					content : content
			}
			
			$.ajax({
				type : "post",
				url  : "${ctp}/schedule/scheduleUpdateOk",
				data : query,
				success : function() {
						alert("수정완료!!");
						location.reload(); 
				},
				error : function()	{
					alert("전송 실패~~");
				}
			});
		}
		
		//일정 삭제처리
		function delCheck(idx) {
			let ans = confirm("선택된 일정을 삭제하시겠습니까?");
			if(!ans) return false;
			
			$.ajax({
				type : "post",
				url  : "${ctp}/schedule/scheduleDeleteOk",
				data : {idx : idx},
				success : function() {
						alert("일정이 삭제되었습니다.");
						location.reload();
				},
				error : function() {
					alert("전송 오류!")
				}
			});
		}
		
		//스케줄 상세내역을 모달로 출력하기
		function modalView(part,content) {
			$("#myModal").on("show.bs.modal",function(e){ //모달창을 부름
				$(".modal-body #part").html(part);  //.val : 폼태그에 값을넣을때 .html or .text :화면에 출력할때
				$(".modal-body #content").html(content); 
				
			});

		}
		
	</script>
</head>
<body>
<jsp:include page="/WEB-INF/views/include/nav.jsp" />
<jsp:include page="/WEB-INF/views/include/slide2.jsp" />
<p><br/></p>
<div class="container">
	<h4>${ymd} 일정입니다</h4>
	<p>오늘의 일정은 총 ${scheduleCnt}건 있습니다.
	<hr/>
	<div>
		<input type="button" value="일정등록" onclick="scheduleInputView()" id="scheduleInputView" class="btn btn-info"/>
		<input type="button" value="등록창닫기" onclick="scheduleInputHidden()" id="scheduleInputHidden" class="btn btn-danger"/>
		<input type="button" value="돌아가기" onclick="location.href='${ctp}/schedule/schedule?yy=${fn:split(ymd,'-')[0]}&mm=${fn:split(ymd,'-')[1]-1}';" class="btn btn-success"/>
		<!-- yy는 ymd를 -기준으로 나눈거의 0번째, mm은 ymd를 -기준으로 나눈거의 1번째 에서 (돌아갈때 +1돼서 나오니까 -1해주기 )를 가지고 schedule컨트롤러로감 -->
	</div>
	<div id="demo"></div>
	<hr/>
	<c:if test="${scheduleCnt != 0 }"> <!-- 0건이 아닐경우(하나라도있을경우) -->
		<table class="table table-hover text-center">
			<tr class="table-dark text-dark">
				<th>번호</th>
				<th>간단 내역</th>
				<th>분류</th>
				<th>비고</th>
			</tr>
			<c:forEach var="vo" items="${vos}" varStatus="st">
				<tr>
					<td>${st.count}</td>
					<td>
						<a href="#" onclick="modalView('${vo.part}','${fn:replace(vo.content,newLine,'<br/>')}')" data-toggle="modal" data-target="#myModal"><!-- DB에서 내용을가져오는게 아니라 현재창에있는내용을 모달에 한번더 띄우는거라 idx필요없음 -->
            	<c:if test="${fn:indexOf(vo.content,newLine) != -1}">${fn:substring(vo.content,0,fn:indexOf(vo.content,newLine))}</c:if><!-- -1은 없다는건데 부정이니까 있다는뜻 -->
            	<c:if test="${fn:indexOf(vo.content,newLine) == -1}">${fn:substring(vo.content,0,20)}</c:if>
						</a>
					</td>
					<td>${vo.part}</td>
					<td>
						<input type="button" value="수정" onclick="updateCheck('${vo.idx}','${vo.part}','${fn:replace(vo.content,newLine,'<br/>')}')" id="scheduleUpdateOpen${vo.idx}" class="btn btn-warning btn-sm"/><!-- \n을 <br/>로 바꿔줘야함 ,문자와 숫자 둘다 섞여있으면 다 문자로 취급해주기(''붙임) -->
						<input type="button" value="삭제" onclick="delCheck(${vo.idx})" class="btn btn-danger btn-sm"/> <!-- 숫자니까 ''없이씀, 문자로써도됨 -->
					</td>
				</tr>
				<tr><td colspan="4" class="m-0 p-0"><div id="updateDemo${vo.idx}"></div></td></tr> <!-- 수정시 해당내역아래에 나와야되니까 여기다 데모적음 /반복되면 다른내역도 id가 똑같아지는데 중복이되면안되니까 ${vo.idx}붙임 -->
			</c:forEach>
			<tr><td colspan="4" class="m-0 p-0"></td></tr>
		</table>
	</c:if>
</div>

 <!-- The Modal(모달창 클릭시 자료실의 내용을 모달창에 출력한다)  -->
<div class="modal fade" id="myModal">
  <div class="modal-dialog">
    <div class="modal-content">
    
      <!-- Modal Header -->
      <div class="modal-header">
        <h4 class="modal-title"><b>${ymd}</b></h4>
        <button type="button" class="close" data-dismiss="modal">&times;</button> <!--&times; => x버튼(누르면닫힘)  -->
      </div>
      
      <!-- Modal body -->
      <div class="modal-body"> <!-- 여긴 마음대로 꾸미기 -->
				<table class="table">
					<tr><th>분류 :</th><td><span id="part"></span></td></tr>
					<tr><th>내용 :</th><td><span id="content"></span></td></tr>
					<tr><th>작성자 :</th><td>${sMid}</td></tr>
				</table> 
      </div>
      
      <!-- Modal footer -->
      <div class="modal-footer">
        <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
      </div>
    </div>
  </div>
</div>


<p><br/></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp" />
</body>
</html>