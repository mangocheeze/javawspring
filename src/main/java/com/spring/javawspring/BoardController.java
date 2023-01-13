package com.spring.javawspring;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.javawspring.pagination.PageProcess;
import com.spring.javawspring.pagination.PageVO;
import com.spring.javawspring.service.BoardService;
import com.spring.javawspring.service.MemberService;
import com.spring.javawspring.vo.BoardReplyVO;
import com.spring.javawspring.vo.BoardVO;
import com.spring.javawspring.vo.GoodVO;
import com.spring.javawspring.vo.MemberVO;

@Controller
@RequestMapping("/board") //중개경로
public class BoardController {

	@Autowired
	BoardService boardService; //인터페이스
	
	@Autowired
	PageProcess pageProcess;

	@Autowired
	MemberService memberService;
	
	//게시판 리스트 
	@RequestMapping(value = "/boardList", method = RequestMethod.GET)
	public String boardListGet(Model model,
			@RequestParam(name="pag", defaultValue = "1", required = false) int pag,
			@RequestParam(name="pageSize", defaultValue = "5", required = false) int pageSize, //우선 페이지사이즈 5개만해놓음
			@RequestParam(name="search", defaultValue = "", required = false) String search, //검색조건 (part역할)
			@RequestParam(name="searchString", defaultValue = "", required = false) String searchString) { //검색내용
		
		PageVO pageVo = pageProcess.totRecCnt(pag, pageSize, "board", search, searchString);
		List<BoardVO> vos = boardService.getBoardList(pageVo.getStartIndexNo(), pageSize, search, searchString);
		
		model.addAttribute("vos", vos);
		model.addAttribute("pageVo", pageVo);
		model.addAttribute("search", search);
		model.addAttribute("searchString", searchString);
		
		return "board/boardList";
	}
	
	//게시판 글쓰기 입력폼
	@RequestMapping(value = "/boardInput", method = RequestMethod.GET)
	public String boardInputGet(Model model, HttpSession session, int pag, int pageSize) {
		String mid = (String) session.getAttribute("sMid");
		MemberVO vo =  memberService.getMemberIdCheck(mid);
		
		model.addAttribute("pag" , pag);
		model.addAttribute("pageSize" , pageSize);
		model.addAttribute("email" , vo.getEmail());
		model.addAttribute("homePage" , vo.getHomePage());
		
		return "board/boardInput";
	}

	//게시판 글쓰기 처리
	@RequestMapping(value = "/boardInput", method = RequestMethod.POST)
	public String boardInputPost(BoardVO vo) {
		//content에 이미지가 저장되어 있다면, 저장된 이미지만 골라서 /resources/data/board/폴더에 저장시켜준다
		boardService.imgCheck(vo.getContent());
		
		//이미지 복사작업이 끝나면, board폴더에 실제로 저장된 파일명을 DB에 저장시켜준다. (/resources/data/ckeditor/ ==> /resources/data/board/ ) 
		vo.setContent(vo.getContent().replace("/data/ckeditor/", "/data/ckeditor/board/")); //replace메소드로 쉼표앞에거를 쉼표뒤에걸로 바꿔줌
		
		int res =  boardService.setBoardInput(vo);
		
		if(res == 1) return "redirect:/msg/boardInputOk";
		else  return "redirect:/msg/boardInputNo";
	}
	
	// 게시글 조회하기
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/boardContent", method = RequestMethod.GET)
	public String boardContentGet(Model model, int idx, int pag, int pageSize, HttpSession session) {
		// 글 조회수 1회 증가시키기.(조회수 중복방지처리 - 세션 사용 : 'board+고유번호'를 객체배열에 추가시킨다.)
		ArrayList<String> contentIdx = (ArrayList) session.getAttribute("sContentIdx");
		if(contentIdx == null) {
			contentIdx = new ArrayList<String>();
		}
		String imsiContentIdx = "board" + idx;
		if(!contentIdx.contains(imsiContentIdx)) {
			boardService.setBoardReadNum(idx);
			contentIdx.add(imsiContentIdx);
		}
		session.setAttribute("sContentIdx", contentIdx);
		
		// 게시글 읽어와서 vo에 담아주기
		BoardVO vo = boardService.getBoardContent(idx);
		
		model.addAttribute("vo", vo);
		model.addAttribute("pag", pag);
		model.addAttribute("pageSize", pageSize);
		
		
		// 해당글에 '좋아요' 버튼을 클릭하였었다면 '좋아요세션'에 아이디를 저장시켜두었기에 찾아서 있다면 sSw값을 1로 보내어 하트색을 빨강색으로 변경유지하게한다.(세션이 끈기면 다시 초기화 된다.)
		ArrayList<String> goodIdx = (ArrayList) session.getAttribute("sGoodIdx");
		if(goodIdx == null) {
			goodIdx = new ArrayList<String>();
		}
		String imsiGoodIdx = "boardGood" + idx;
		if(goodIdx.contains(imsiGoodIdx)) {
			session.setAttribute("sSw", "1");		// 로그인 사용자가 이미 좋아요를 클릭한 게시글이라면 빨강색으로 표시가히위해 sSW에 1을 전송하고 있다.
		}
		else {
			session.setAttribute("sSw", "0");
		}
		
		
		// DB에서 현재 게시글에 '좋아요'가 체크되어있는지를 알아오자.
		String mid = (String) session.getAttribute("sMid");
		GoodVO goodVo = boardService.getBoardGoodCheck(idx, "board", mid);
		model.addAttribute("goodVo", goodVo);
		
		
		// 이전글/다음글 가져오기 (한번에처리할거임)
		ArrayList<BoardVO> pnVos = boardService.getPrevNext(idx); //idx는 현재게시글의 글번호를 가져오겠다
		//System.out.println("pnVos: " +pnVos); //이전글 idx와 다음글 idx가뜸
		model.addAttribute("pnVos", pnVos);
		
		// 댓글 가져오기(replyVos)
		List<BoardReplyVO> replyVos = boardService.getBoardReply(idx);
		
		model.addAttribute("replyVos",replyVos);
		
		return "board/boardContent";
	}
	
	// 좋아요수 증가처리하기
	// 좋아요 버튼을 클릭했을때 처리(처음으로 '좋아요'누르면 1을, 이미 '좋아요'가 한번이라도 눌렸었다면 '0'을 돌려준다.
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ResponseBody
	@RequestMapping(value = "/boardGood", method = RequestMethod.POST)
	public String boardGoodGet(HttpSession session, int idx) {
		String res = "0"; // 처음 0으로 셋팅하고, 처음 좋아요 버튼을 누르면 '1'을 돌려준다., 이미 '좋아요'를 한번 눌렀으면 '0'으로 res값을 보내준다.
		ArrayList<String> goodIdx = (ArrayList) session.getAttribute("sGoodIdx");
		if(goodIdx == null) {
			goodIdx = new ArrayList<String>();
		}
		String imsiGoodIdx = "boardGood" + idx;
		if(!goodIdx.contains(imsiGoodIdx)) {
			boardService.setBoardGoodPlus(idx);
			goodIdx.add(imsiGoodIdx);
			res = "1";	// 처음으로 좋아요 버튼을 클릭하였기에 '1'을 반환
		}
		session.setAttribute("sGoodIdx", goodIdx);
		
		return res;	// '좋아요'를 이미 눌렸을때 이곳으로 들어오면 처음설정값인 res는 '0'을 반환, 처음 눌렀으면 '1'을 반환~~
	}
	
	// 좋아요수 Plus버튼누르면 1증가처리..Minus버튼누르면 1감소 
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ResponseBody
	@RequestMapping(value = "/boardGoodPlusMinus", method = RequestMethod.POST)
	public String boardGoodGet(HttpSession session, int idx, int goodCnt) {
		String res = "0";
		// 좋아요수 단 한번씩만 1회 증가/감소시키기.(중복방지처리 - 세션 사용 : 'good+(고유번호*goodCnt)'를 객체배열에 추가시킨다.)
		ArrayList<String> goodIdx = (ArrayList) session.getAttribute("sGoodIdx");
		if(goodIdx == null) {
			goodIdx = new ArrayList<String>();
		}
		String imsiGoodIdx = "good" + (idx*goodCnt);
		if(!goodIdx.contains(imsiGoodIdx)) {
			boardService.setGoodPlusMinus(idx, goodCnt);	// 좋아요수 증가/감소
			goodIdx.add(imsiGoodIdx);
		}
		else {
			res = "1";
		}
		session.setAttribute("sGoodIdx", goodIdx);
		return res;
	}
	
	// 좋아요수 증가처리하기
	// 좋아요 버튼을 클릭했을때 처리(처음으로 '좋아요'누르면 1을, 이미 '좋아요'가 한번이라도 눌렸었다면 '0'을 돌려준다.
	// 다른글에서도 같은 세션이 적용되기에 문제 분석이 틀렸다...xxx 따라서 DB를 이용해서 좋아요 정보를 저장처리한다.
	@ResponseBody
	@RequestMapping(value = "/boardGoodFlagCheck", method = RequestMethod.POST)
	public void boardGoodFlagCheckPost(HttpSession session, int idx, int gFlag) {
		gFlag = gFlag * (-1);
		boardService.boardGoodFlagCheck(idx, gFlag);
		session.setAttribute("sGFlag", gFlag);
	}
	
	// 좋아요~ 토글 처리(DB를 활용한 예제)
	@ResponseBody
	@RequestMapping(value = "/boardGoodDBCheck", method=RequestMethod.POST)
	public void boardGoodDBCheckPost(GoodVO goodVo) {
		// 처음 '좋아요'클릭시는 무조건 레코드를 생성, 그렇지 않으면, 즉 기존에 '좋아요'레코드가 있었다면 '해당레코드를 삭제' 처리한다.
		if(goodVo.getIdx() == 0) {
			boardService.setGoodDBInput(goodVo);
			boardService.setGoodUpdate(goodVo.getPartIdx(), 1);
		}
		else {
			boardService.setGoodDBDelete(goodVo.getIdx());
			boardService.setGoodUpdate(goodVo.getPartIdx(), -1);
		}
	}
	
	
	//게시글 삭제하기
	@RequestMapping(value = "/boardDeleteOk", method = RequestMethod.GET)
	public String boardDeleteOkGet(int idx, int pag, int pageSize, Model model ) {
		//게시글에 사진이 존재한다면 서버에 있는 사진 파일을 먼저 삭제한다.
		BoardVO vo = boardService.getBoardContent(idx);
		if(vo.getContent().indexOf("src=\"/") != -1) boardService.imgDelete(vo.getContent()); //사진파일이 있냐 없냐 확인, 여기선 사진이 있으면
		
		//DB에서 실제로 존재하는 게시글을 삭제처리한다.
		boardService.setBoardDeleteOk(idx);
		
		model.addAttribute("flag","?pag="+pag+"&pageSize="+pageSize); //페이지,페이지사이즈 넘기려면 모델에 담으면됨
		
		return "redirect:/msg/boardDeleteOk";
	}
	
	//게시글 수정하기 폼
	@RequestMapping(value="boardUpdate", method = RequestMethod.GET) 
	public String boardUpdateGet(Model model, int idx, int pag, int pageSize) { //()안에는 넘어오는걸 적어줌
		//수정창으로 이동시에는 먼저 원본파일의 그림화일들이 있다면 , 현재폴더(board)의 그림파일을 ckeditor폴더로 복사시켜둔다.
		BoardVO vo = boardService.getBoardContent(idx);
		if(vo.getContent().indexOf("src=\"/") != -1) boardService.imgCheckUpdate(vo.getContent()); //사진파일이 있냐 없냐 확인
		
		model.addAttribute("vo", vo);
		model.addAttribute("pag", pag);
		model.addAttribute("pageSize", pageSize);
		
		return "board/boardUpdate";
	}
	
	//게시글 수정하기 처리 (변경된내용수정처리 -그림포함)
	@RequestMapping(value="boardUpdate", method = RequestMethod.POST) 
	public String boardUpdatePost(Model model, BoardVO vo, int pag, int pageSize) { //()안에는 넘어오는걸 적어줌
		//위에 vo는 수정폼에서 온거니까 수정을실제로했던안했던 수정한 vo로 봄
		//수정된 자료가 원본자료와 완전히 동일하다면 (하나도 수정안했으면) 수정할필요가없기에, DB에저장된 원본자료를 불러와서 비교해준다.
		BoardVO origVo = boardService.getBoardContent(vo.getIdx());  //DB에서 넘어온 idx를 가져감
		
		//content의 내용이 조금이라도 변경된것이 있다면 아래 내용을 수행처리시킨다.
		if(!origVo.getContent().equals(vo.getContent())) { //origVo에 있는 content(내용)와 vo에있는 content랑 다르면
			//실제로 수정하기 버튼을 클릭하게되면 기존의 board폴더에 저장된 현재 content의 그림파일 모두를 삭제시킨다.(그림있을때만)
			if(origVo.getContent().indexOf("src=\"/") != -1) boardService.imgDelete(origVo.getContent()); //그림이 있을때 사진삭제
			
			// vo.getContent()에 들어있는 파일의 경로는 'ckeditor/board'경로를 'ckeditor'로 변경시켜줘야한다.
			vo.setContent(vo.getContent().replace("/data/ckeditor/board/", "/data/ckeditor/"));
			
			//앞의 모든 준비가 끝나면, 파일을 처음 업로드한것과 같은 작업을처리한다.(이작업은 처음 게시글을 올릴때의 파일복사작업과 동일한 작업이다)
			boardService.imgCheck(vo.getContent());
			
			//파일 업로드가 끝나면 다시 경로를 수정한다. 'ckeditor' 경로를 'ckeditor/board' 변경시켜줘야한다(즉, 변경된 vo.getContent()를 vo.setContent() 처리한다.)
			vo.setContent(vo.getContent().replace("/data/ckeditor/", "/data/ckeditor/board/"));
		}	
		
		// 잘 정비된 vo를 DB에 Update시켜준다.
		boardService.setboardUpdateOk(vo);
		
		model.addAttribute("flag","?pag="+pag+"&pageSize="+pageSize); 
		
		return "redirect:/msg/boardUpdateOk";
	}
	
	//댓글달기 (부모댓글)
	@ResponseBody
	@RequestMapping(value = "/boardReplyInput", method = RequestMethod.POST)
	public String boardReplyInputPost(BoardReplyVO replyVo) {
		int levelOrder = 0;
		String strLevelOrder = boardService.getMaxLevelOrder(replyVo.getBoardIdx()); //리턴타입을 int로 levelOrder하면 댓글처음달때 다른댓글이없는 max를 구하라고해서 xml에러남
		
		if(strLevelOrder != null) levelOrder = Integer.parseInt(strLevelOrder) + 1;
		
		//System.out.println("aaa" + levelOrder);
		
		replyVo.setLevelOrder(levelOrder); //그냥 levelOrder는 하나작게나옴 그래서 +1해줌
		
		boardService.setBoardReplyInput(replyVo);
		
		return "1";
	}
	
	//대댓글(답글)달기
	@ResponseBody
	@RequestMapping(value = "/boardReplyInput2", method = RequestMethod.POST)
	public String boardReplyInput2Post(BoardReplyVO replyVo) {
		boardService.setLevelOrderPlusUpdate(replyVo);  //댓글순서를 업데이트함(기존에있던 애들레벨이 대댓글달 부모글 레벨보다 큰애들은 다 +1됨)
		replyVo.setLevel(replyVo.getLevel() + 1);
		replyVo.setLevelOrder(replyVo.getLevelOrder() + 1);
		boardService.setBoardReplyInput2(replyVo);
		return "";
	}
	
	//댓글삭제하기
	@ResponseBody
	@RequestMapping(value = "/boardReplyDeleteOk", method = RequestMethod.POST)
	public String boardReplyDeleteOkPost(int idx) {
		boardService.setBoardReplyDeleteOk(idx);
		
		return "";
	}
	
	
	//게시글 관리자 선택/전체선택 삭제하기
	@RequestMapping(value = "/boardAdminDelete", method = RequestMethod.POST)
	public String boardAdminDeletePost(String delItems) {
		int res = 0;
		delItems = delItems.substring(0, delItems.length()-1); //마지막껀 /빼려고해줌 (굳이 안해도됨)
		System.out.println("delItems" + delItems); //한번찍어보기
		
		String[] delItemsArr = delItems.split("/");// 12/15/17 이런식으로 들어와있는데 /기준으로 잘라서 배열 delItemsArr변수에 넣어줌
		System.out.println("delItemsArr[0]" +delItemsArr[0]); //0번방에 뭐들어왔는지 찍어봄
		
		for(int i=0; i<delItemsArr.length; i++) { //배열의 길이만큼 반복
			int checkIdx = Integer.parseInt((String)delItemsArr[i]); //배열은 무조건 String타입인데 나중에 idx로 비교해서 삭제해줘야하니 int타입으로 형변환해주기
			res = boardService.setBoardAdminDelete(checkIdx); //idx가 int타입이니까 int타입 res로 받기
		}
		
		return res + ""; //다시 res를 문자타입으로 형변환해줌
	}
	
	
}
