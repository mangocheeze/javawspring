package com.spring.javawspring;

import java.util.List;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

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
			

	@Autowired
	JavaMailSender mailSender;
	
	
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
	
	//카카오 로그인 완료후 수행할 내용들을 기술한다.
	@RequestMapping(value = "/memberKakaoLogin", method=RequestMethod.GET) //무조건 get임
	public String memberKakaoLoginGet(HttpSession session, HttpServletRequest request, HttpServletResponse response,
			String nickName,
			String email) {
		
		//카카오 로그인한 회원이 현재 우리 회원인지를 조회한다
		//이미 가입된 회원이라면 서비스를 사용하게 하고, 그렇지 않으면 강제로 회원가입시킨다.
		MemberVO vo = memberService.getMemberNickNameEmailCheck(nickName,email);
		
		//현재 우리회원이 아니면 자동회원가입처리(가입필수사항 : 아이디,닉네임, 이메일) - 아이디는 이메일주소의 '@' 앞쪽 이름을 사용하기로한다. (우리가 정한거임, 이 방법의 문제점 - 아이디를 못고침)
		if(vo == null) { //비회원이라면
			//아이디 결정하기
			String mid = email.substring(0, email.indexOf("@")); //0번째부터 @이를 찾을때까지.
		
			//임시비밀번호 발급하기 (난수로 해도되나, 여기선 편의상 '0000'으로 발급하기로함)
			//String pwd = passwordEncoder.encode("0000");
			
			// 임시 비밀번호 발급하기(UUID 8자리로 발급하기로 한다. -> 발급후 암호화시켜 DB에 저장)
			UUID uid = UUID.randomUUID();
			String pwd = uid.toString().substring(0,8);
			session.setAttribute("sImsiPwd", pwd);	// 임시비밀번호를 발급하여 로그인후 변경처리하도록 한다.
			pwd = passwordEncoder.encode(pwd);
			
			// 새로 발급된 임시비밀번호를 메일로 전송처리한다.
			//  메일 처리부분... 생략함.
			
			//자동 회원 가입 처리한다.
			memberService.setKakaoMemberInputOk(mid, pwd, nickName, email);
			
			//가입 처리된 회원의 정보를 다시 읽어와서 vo에 담아준다
			vo = memberService.getMemberIdCheck(mid);
			
		}
		
		//만약에 탈퇴신청한 회원이 카카오로그인처리하였더라면 'userDel' 필드를 'No'로 업데이트한다.
		if(!vo.getUserDel().equals("NO")) {
			memberService.setMemberUserDelCheck(vo.getMid());
		}
		
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
		session.setAttribute("sMid", vo.getMid()); 
		session.setAttribute("sNickName", vo.getNickName());
		
		//로그인한 사용자의 오늘 방문횟수(포인트) 누적..
		memberService.setMemberVisitProcess(vo); //너무 컨트롤러에서다해서 서비스객체에게 일시킴
		
		return "redirect:/msg/memberLoginOk?mid="+vo.getMid(); 
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
	
	//로그아웃
	@RequestMapping(value="/memberLogout", method=RequestMethod.GET)
	public String memberLogoutGet(HttpSession session) {
		String mid = (String) session.getAttribute("sMid");
	
		session.invalidate();
	
		return "redirect:/msg/memberLogout?mid="+mid;
	}
	
	//회원메인
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
	public String memberJoinPost(MultipartFile fName, MemberVO vo) {
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
		
		//체크가 완료되면 사진파일업로드후, vo에 담긴 자료를 DB에 저장시켜준다(회원 가입) - 서비스객체에서 수행처리했다
		int res = memberService.setMemberJoinOk(fName, vo);
		
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
		//@RequestParam 은 null값 처리해주는거임, 제일처음엔아이디가 필요없이 전체자료가 나오는 화면이라 맨처음은 mid가 공백이 들어옴
		
		PageVO pageVo = pageProcess.totRecCnt(pag, pageSize, "member", "", mid);//페이지네이션처리를 하기위해서 총 자료건수 구하기 , 아이디개별검색할때 아이디필요하니까 같이 넘기기
		
		List<MemberVO> vos = memberService.getMemberList(pageVo.getStartIndexNo(), pageSize ,mid); //페이지네이션처리하기위해서 전체자료구하기
		
		model.addAttribute("vos", vos);
		model.addAttribute("pageVo", pageVo);
		
		model.addAttribute("mid", mid);
		
		return "member/memberList";
	}
	
	//비밀번호 찾기를 위한 임시비밀번호 발급 폼
	@RequestMapping (value= "/memberPwdSearch", method= RequestMethod.GET)
	public String memberPwdSearchGet() {
		return "/member/memberPwdSearch";
	}
	
	//비밀번호 찾기를 위한 임시비밀번호 발급처리(임시비밀번호 메일로보내기)
	@RequestMapping (value= "/memberPwdSearch", method= RequestMethod.POST)
	public String memberPwdSearchPost(String  mid, String toMail) { //앞에서 넘긴 받아오기
		MemberVO vo = memberService.getMemberIdCheck(mid);
		if(vo.getEmail().equals(toMail)) {
			//회원정보가 맞으면 임시비밀번호를 발급받는다
			UUID uid = UUID.randomUUID();
			String pwd = uid.toString().substring(0,8); //uid중 8개만 가져옴
			
			//발급받은 임시비밀번호를 암호화처리시켜서 DB에 저장한다.
			memberService.setMemberPwdUpdate(mid, passwordEncoder.encode(pwd)); //아이디와 변경한 패스워드가지고감
			String content = pwd;
			
			//임시비밀번호를 메일로 전송 
			String res = mailSend(toMail, content); //아래쪽에 만듦
			if(res.equals("1")) {
				return "redirect:/msg/memberImsiPwdOk";
			}
			else {
				return "redirect:/msg/memberImsiPwdNo";
			}
		}
		else {
			return "redirect:/msg/memberImsiPwdNo";
		}
		
	}

	//임시비밀번호 메일로보내기
	public String mailSend(String toMail, String content) {
		try {
			String title = "임시비밀번호가 발급되었습니다."; //메일의 타이틀로 뜸
			
			//앞에서 넘긴걸 받아서 메일을 보냄
			//메일을 전송하기 위한 객체 : MimeMessage , MimeMessageHelper()
			MimeMessage message = mailSender.createMimeMessage(); //메세지보내기위해 필요
			MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8"); //메세지 저장소(보관함)
			
			// 메일 보관함에 회원이 보내온 메세지들을 모두 저장시킨다.
			messageHelper.setTo(toMail);
			messageHelper.setSubject(title);
			messageHelper.setText(content);
			
			//메세지 보관함의 내용(content)에 필요한 정보를 추가로 담아서 전송시킬수 있도록한다.
			content = "<br><hr><h3>신규비밀번호는 <font color='red'>"+content+"</font></h3></hr></br>";  //<br/>에서버전땜에 /빼고함 
			content += "<br><hr>아래 주소로 로그인하셔서 비밀번호를 변경하시길 바랍니다.</hr></br>"; 
			content += "<p>방문하기 : <a href='http://49.142.157.251:9090/green2209J_06/main.ani'>동물보호관리시스템</a></p>";
			content += "<p><img src=\"cid:main.png\" width='500px'></p>";  //그림의 껍데기만 지정됨
			content += "<hr>";
			messageHelper.setText(content, true); //true는 덮어써진거임
			
			//본문에 기재된 그림파일의 경로를 따로 표시시켜준다.그리고 , 보관함에 다시 저장시켜준다.
			FileSystemResource file = new FileSystemResource("D:\\JavaWorkspace\\springframework\\works\\javawspring\\src\\main\\webapp\\resources\\images\\main.png");
			messageHelper.addInline("main.png", file); //일반그림넣기
			
			//메일 전송하기
			mailSender.send(message);
			return "1";
			
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
		return "0";
	}
	
	
	/*
	//회원상세보기 (memberList2.jsp용)
	@RequestMapping(value="/memberInfor" , method=RequestMethod.GET)
		public String memberInforGet(String mid, int pag, Model model) { // 저장소 model
			MemberVO vo = memberService.getMemberIdCheck(mid); //mid를 가지고 최종 mapper까지가서 가져온 정보를 변수 vo에 담음
			
			model.addAttribute("pag",pag);
			model.addAttribute("vo",vo);
			
			return "/member/memberInfor"; //memberInfor.jsp로 넘김
		}
	*/
	
	
	
	//아이디찾기폼
	@RequestMapping(value="/memberIdSearch", method = RequestMethod.GET)
	public String memberIdSearchGet() {
		return "member/memberIdSearch";
	}
	
	//아이디찾기처리
	@ResponseBody
	@RequestMapping(value="/memberIdSearch", method = RequestMethod.POST)
	public String memberIdSearchPost(String name,String tel) {
		String res ="";
		if(memberService.getMemberIdSearch(name,tel)!= null) {
			res = memberService.getMemberIdSearch(name,tel);
		}
		return res;
	}
	
	
	//회원탈퇴신청 (userDel = 'No' 로변경)
	@RequestMapping(value="/memberDelete", method = RequestMethod.GET)
	public String memberDelete(HttpSession session, Model model) {
		String mid = (String) session.getAttribute("sMid");
		
		memberService.setMemberDeleteOk(mid);
		
		model.addAttribute("mid", mid);
		
		return "redirect:/msg/memberDeleteOK";
	}
	
	//비밀번호수정 폼
	@RequestMapping(value="/memberPwdUpdate" , method=RequestMethod.GET)
	public String memberPwdUpdateGet(Model model, String flag) {
		model.addAttribute("flag", flag);
		return "member/memberPwdUpdate";
	}
	
	//비밀번호수정처리
	@RequestMapping(value = "/memberPwdUpdate", method = RequestMethod.POST)
	public String memberPwdUpdatePost(String mid, String pwd, HttpSession session) {
		memberService.setMemberPwdUpdate(mid, passwordEncoder.encode(pwd));
		
		//카카오 로그인으로 비밀번호 변경한 사용자는 임시비밀번호 세션을 삭제한다
		if(session.getAttribute("sImsiPwd") != null) session.removeAttribute("sImsiPwd");
		
		return "redirect:/msg/memberPwdUpdateOk";
	}
	
	//회원정보수정처리를 위한 비밀번호 확인처리
	@RequestMapping(value="/memberPwdCheck", method = RequestMethod.POST) //memberPwdUpdate.jsp에서 flag가 pwdCheck일경우
	public String memberPwdCheckPost(Model model, String mid, String pwd) {
		MemberVO vo = memberService.getMemberIdCheck(mid);
		if(vo != null && passwordEncoder.matches(pwd, vo.getPwd())) { //vo가 null이 아니고, 비밀번호 입력된거와 DB에 있는 비밀번호 암호화 되어있는게 같을때
			model.addAttribute("vo",vo);
			return "member/memberUpdate";
		}
		else {
			return "redirect:/msg/memberPwdCheckNo";
		}
	}
	
	//회원 정보 수정처리..
	@RequestMapping(value = "/memberUpdateOk", method = RequestMethod.POST)
	public String memberUpdateOkPost(MultipartFile fName, MemberVO vo, HttpSession session) {
		// 닉네임 백엔드 체크 (jsp에서 프론트체크후 한번더 체크)
		String nickName = (String) session.getAttribute("sNickName");
		if(memberService.getMemberNickNameCheck(vo.getNickName()) != null && !nickName.equals(vo.getNickName())) { //vo에 바꾼닉네임이 null이아니고(이미 닉네임이존재),세션에있는닉네임과 DB에있는 닉네임이 다를때
			return "redirect:/msg/memberNickNameCheckNo2";   //닉네임을 바꿀수없다고 메세지띄움
		}
		
		// 비밀번호 암호화 처리(이곳에서는 비밀번호를 수정하지 않았다.)
		// vo.setPwd(passwordEncoder.encode(vo.getPwd()));
		
		// 체크 완료된 자료를 다시 vo에 담았다면 파일체크처리후, DB에 저장시켜준다.(회원 수정처리)
		int res = memberService.setMemberUpdateOk(fName, vo);
		
		if(res == 1) {
			session.setAttribute("sNickName", vo.getNickName());
			return "redirect:/msg/memberUpdateOk";
		}
		else return "redirect:/msg/memberUpdateNo";
	}
	
	

	
	
}
