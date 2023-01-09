package com.spring.javawspring;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.javawspring.pagination.PageProcess;
import com.spring.javawspring.pagination.PageVO;
import com.spring.javawspring.service.MemberService;
import com.spring.javawspring.vo.MemberVO;

@Controller
@RequestMapping("/member") //중개경로
public class MemberController {

	@Autowired
	MemberService memberService; //인터페이스
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;		
	
	@Autowired
	PageProcess pageProcess; //위엔 다 인터페이스들을 건건데 이건 클래스를 건거임-얘도 서비스객체가되는거임 (페이지네이션)
			
	//로그인
	@RequestMapping(value = "/memberLogin", method = RequestMethod.GET)
	public String memberLoginGet(HttpServletRequest request) {
		//로그인폼 호출시 기존에 저장된 쿠키가 있다면 불러와서 mid에 담아서 넘겨준다(쿠키에 아이디저장)
		Cookie[] cookies = request.getCookies();
		for(int i=0; i<cookies.length; i++) {
			if(cookies[i].getName().equals("cMid")) {
				request.setAttribute("mid", cookies[i].getValue());
				break;
			}
		}
		return "member/memberLogin";
	}
	
	//로그인 인증처리
	@RequestMapping(value = "/memberLogin", method = RequestMethod.POST)
	public String memberLoginPost(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam(name="mid", defaultValue = "", required = false) String mid,
			@RequestParam(name="pwd", defaultValue = "", required = false)String pwd,
			@RequestParam(name="idCheck", defaultValue = "", required = false)String idCheck) { //여기에 올려줘야 아래서 사용가능. 세션 생성하기위해HttpServletRequest작성, 앞에서 아이디 비번, 아이디저장 체크되어있는지 등 넘어온거작성과 null값체크를 같이해줌
		
		MemberVO vo = memberService.getMemberIdCheck(mid);  //mid만 넘겨주면 리턴타입이 vo라 vo로 받아주면됨
		
		//정상적인 회원일경우(vo에 정보가 null이 아닐때 , 비밀번호가 맞을때, 탈퇴신청한 회원이 아닐때
		if(vo != null && passwordEncoder.matches(pwd, vo.getPwd()) && vo.getUserDel().equals("NO")) { //matches(앞에서읽어온 pwd, DB에있는 pwd ) 리턴값이 boolean이라 참 or거짓
			// 회원인증처리된경우 수행할 내용 ? strLevel처리, session에 필요한 자료를 저장, 쿠키값 처리, 그날 방문자수 1증가(방문포인트 증가)..
			// strLevel처리
			String strLevel = "";
			if(vo.getLevel() == 0) strLevel = "관리자";
			else if(vo.getLevel() == 1) strLevel = "운영자";
			else if(vo.getLevel() == 2) strLevel = "우수회원";
			else if(vo.getLevel() == 3) strLevel = "정회원";
			else if(vo.getLevel() == 4) strLevel = "준회원";
			
			//session에 필요한 자료를 저장
			session.setAttribute("sStrLevel", strLevel);  //???
			session.setAttribute("sLevel", vo.getLevel());
			session.setAttribute("sMid", mid); //vo.getMid()라고해도됨(DB에있는거 가져올거라)
			session.setAttribute("sNickName", vo.getNickName());
			
			
			if(idCheck.equals("on")) { //아이디 저장에 체크가 되어있으면
				Cookie cookie = new Cookie("cMid", mid);
				cookie.setMaxAge(60*60*24*7); //60초 * 60분 *24시간 * 7일 => 유효기간 7일
				response.addCookie(cookie);
			}
			else {
				Cookie[] cookies = request.getCookies(); //쿠키가 한개가아니라 여러개가 있을수있어서(저장할땐 한개지만 저장되어있는것들은 여러개일수있음)
				for(int i=0; i<cookies.length; i++) {
					if(cookies[i].getName().equals("cMid")) {//변수를 비교
						cookies[i].setMaxAge(0);
						response.addCookie(cookies[i]);
						break;
					}
				}
			}
			
			//로그인한 사용자의 오늘 방문횟수(포인트) 누적..
			memberService.setMemberVisitProcess(vo); //너무 컨트롤러에서다해서 서비스객체에게 일시킴
			
			return "redirect:/msg/memberLoginOk?mid="+mid; //model저장소에 담아도됨
			}
			else {
				return "redirect:/msg/memberLoginNo";
			}
		}
	
	
		@RequestMapping(value="/memberLogout", method=RequestMethod.GET)
		public String memberLogoutGet(HttpSession session) {
			String mid = (String) session.getAttribute("sMid");
		
			session.invalidate();
		
			return "redirect:/msg/memberLogout?mid="+mid;
	}
		
	@RequestMapping(value = "/memberMain", method = RequestMethod.GET)
	public String memberMainGet(HttpSession session , Model model) {
		String mid = (String) session.getAttribute("sMid");
		
		MemberVO vo = memberService.getMemberIdCheck(mid);
		
		model.addAttribute("vo", vo);
		
		return "member/memberMain";
	}
	
	@RequestMapping(value = "/adminLogout", method = RequestMethod.GET)
	public String adminLogoutGet(HttpSession session) {
		String mid = (String) session.getAttribute("sAMid");
		
		session.invalidate();
		
		return "redirect:/msg/memberLogout?mid="+mid;
	}

	//회원가입폼
	@RequestMapping(value= "/memberJoin", method=RequestMethod.GET)
	public String memberJoinGet() {
		return "member/memberJoin";
	}
	
	//회원가입처리
	@RequestMapping(value= "/memberJoin", method=RequestMethod.POST)
	public String memberJoinPost(MemberVO vo) {
		//아이디 중복 체크 (여긴 백엔드체크 - 두번체크한거임)
		if(memberService.getMemberIdCheck(vo.getMid()) != null ) { //아이디가 이미 있으니 진행되면 X
			return "redirect:/msg/memberIdCheckNo";
		}
		//닉네임 중복 체크
		if(memberService.getMemberNickNameCheck(vo.getNickName()) != null ) { 
			return "redirect:/msg/memberNickNameCheckNo";
		}
		
		//비밀번호 암호화 (BCryptPasswordEncoder)
		vo.setPwd(passwordEncoder.encode(vo.getPwd())); //우린 비밀번호 최대 20자까지 허용했는데 암호화하면 커지니까 Sql에서 크기ㅣ 100까지는 해주기	
		
		//체크가 완료되면 vo에 담긴 자료를 DB에 저장시켜준다(회원 가입)
		int res = memberService.setMemberJoinOk(vo);
		
		if(res == 1) return "redirect:/msg/memberJoinOk";
		else return "redirect:/msg/memberJoinNo";
	}
	
	@ResponseBody  //ajax 사용하면 이거 넣어주기
	@RequestMapping(value= "/memberIdCheck", method=RequestMethod.POST)
	public String memberIdCheckGet(String mid) {
		String res = "0";
		MemberVO vo = memberService.getMemberIdCheck(mid);
		
		if(vo != null) res = "1"; //1이면 아이디사용할수없음
		
		return res;
	}
	
	@ResponseBody  //ajax 사용하면 이거 넣어주기
	@RequestMapping(value= "/memberNickNameCheck", method=RequestMethod.POST)
	public String memberNickNameCheckGet(String nickName) {
		String res = "0";
		MemberVO vo = memberService.getMemberNickNameCheck(nickName);
		
		if(vo != null) res = "1"; //1이면 아이디사용할수없음
		
		return res;
	}
	/*
	@RequestMapping(value = "/memberList", method= RequestMethod.GET)
	public String memberListGet(Model model,
			@RequestParam(name="pag", defaultValue = "1", required = false) int pag,
			@RequestParam(name="pageSize", defaultValue = "3", required = false) int pageSize){
		
		int totRecCnt = memberService.totRecCnt(); //3.총 레코드 건수를 구한다
		int totPage = (totRecCnt % pageSize) == 0 ? totRecCnt  / pageSize : (totRecCnt/ pageSize) +1; 	//4.총 페이지 건수를 구한다 
		int startIndexNo = (pag - 1) * pageSize;//5.현재페이지의 시작 인덱스 번호를 구한다
		int curScrStartNo = totRecCnt - startIndexNo; //6.현재 화면에 보여주는 시작 번호를 구한다
		
		int blockSize = 3; //1.블록의 크기를 결정한다(여기선 3으로지정)
		int curBlock = (pag-1) / blockSize; //2.현재페이지가 위치하고있는 블록 번호를 구한다(예.1~3페이지는 0블록, 4~6페이지는1블록, 7~9페이지는 2블록)
		int lastBlock = (totPage - 1) / blockSize; // 3. 마지막블록을 구한다
		
		ArrayList<MemberVO> vos = memberService.getMemberList(startIndexNo, pageSize); //시작인덱스번호와 한페이지 크기를 같이 넘기는거임
		
		
		model.addAttribute("vos", vos); //이렇게하면 방명록리스트에 5건만 나오는거임 
		model.addAttribute("pag", pag); //현재페이지 이전페이지 하려고 넘겨야함
		model.addAttribute("totPage", totPage); //현재페이지 이전페이지 하려고 넘겨야함
		model.addAttribute("curScrStartNo", curScrStartNo);
		model.addAttribute("blockSize", blockSize);
		model.addAttribute("curBlock", curBlock);
		model.addAttribute("lastBlock", lastBlock);
		
		
		return "member/memberList";
	}
	*/
	/* 전체리스트와 검색리스트를 하나의 메소드로 처리  --> 동적쿼리
	@RequestMapping(value = "/memberList", method= RequestMethod.GET)
	public String memberListGet(Model model,
			@RequestParam(name="mid", defaultValue = "", required = false) String mid,
			@RequestParam(name="pag", defaultValue = "1", required = false) int pag,
			@RequestParam(name="pageSize", defaultValue = "3", required = false) int pageSize){
		
		int totRecCnt = memberService.totTermRecCnt(mid); //3.총 레코드 건수를 구한다 //여기가 달라져야함
		int totPage = (totRecCnt % pageSize) == 0 ? totRecCnt  / pageSize : (totRecCnt/ pageSize) +1; 	//4.총 페이지 건수를 구한다 
		int startIndexNo = (pag - 1) * pageSize;//5.현재페이지의 시작 인덱스 번호를 구한다
		int curScrStartNo = totRecCnt - startIndexNo; //6.현재 화면에 보여주는 시작 번호를 구한다
		
		int blockSize = 3; //1.블록의 크기를 결정한다(여기선 3으로지정)
		int curBlock = (pag-1) / blockSize; //2.현재페이지가 위치하고있는 블록 번호를 구한다(예.1~3페이지는 0블록, 4~6페이지는1블록, 7~9페이지는 2블록)
		int lastBlock = (totPage - 1) / blockSize; // 3. 마지막블록을 구한다
		
		ArrayList<MemberVO> vos = memberService.getTermMemberList(startIndexNo, pageSize,mid); //시작인덱스번호와 한페이지 크기와, 아이디를 같이 넘기는거임(아이디는 넘어올수도있고 안넘어올수도있고)
		
		
		model.addAttribute("vos", vos); //이렇게하면 방명록리스트에 5건만 나오는거임 
		model.addAttribute("pag", pag); //현재페이지 이전페이지 하려고 넘겨야함
		model.addAttribute("totPage", totPage); 
		model.addAttribute("totRecCnt", totRecCnt); 
		model.addAttribute("curScrStartNo", curScrStartNo);
		model.addAttribute("blockSize", blockSize);
		model.addAttribute("curBlock", curBlock);
		model.addAttribute("lastBlock", lastBlock);
		
		model.addAttribute("mid", mid);
		
		return "member/memberList";
	}
	*/
	
	// 동적쿼리 + pagination 이용하기...
	@RequestMapping(value = "/memberList", method = RequestMethod.GET)
	public String memberListGet(Model model,
			@RequestParam(name="mid", defaultValue = "", required = false) String mid,
			@RequestParam(name="pag", defaultValue = "1", required = false) int pag,
			@RequestParam(name="pageSize", defaultValue = "3", required = false) int pageSize) {
		
		PageVO pageVo = pageProcess.totRecCnt(pag, pageSize, "member", "", "");//3.총 레코드 건수를 구한다 //여기가 달라져야함, "member"는 나는 멤버야라는뜻? 
		
		List<MemberVO> vos = memberService.getMemberList(pageVo.getStartIndexNo(), pageSize);
		
		model.addAttribute("vos", vos);
		model.addAttribute("pageVo", pageVo);
		
		return "member/memberList";
	}
}
