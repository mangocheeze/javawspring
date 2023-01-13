package com.spring.javawspring;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.javawspring.pagination.PageProcess;
import com.spring.javawspring.pagination.PageVO;
import com.spring.javawspring.service.AdminService;
import com.spring.javawspring.service.BoardService;
import com.spring.javawspring.service.MemberService;
import com.spring.javawspring.vo.MemberVO;

@Controller
@RequestMapping("/admin") //중개경로
public class AdminController {

	@Autowired
	AdminService adminService; //인터페이스
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;		
	
	@Autowired
	PageProcess pageProcess; //위엔 다 인터페이스들을 건건데 이건 클래스를 건거임-얘도 서비스객체가되는거임 (페이지네이션)
			

	@Autowired
	JavaMailSender mailSender;
	
	@Autowired
	MemberService memberService;
	
	@Autowired
	BoardService boardService;
	
	
	@RequestMapping(value = "/adminMain", method = RequestMethod.GET)
	public String adminMainGet() {
		return "admin/adminMain";
	}
	
	@RequestMapping(value = "/adminLeft", method = RequestMethod.GET)
	public String adminLeftGet() {
		return "admin/adminLeft";
	}
	
	@RequestMapping(value = "/adminContent", method = RequestMethod.GET)
	public String adminContentGet(Model model) {
		/*한달 신규 가입자 수, 새로운글수*/
		int monthJoin = memberService.getMonthNewUser();
		int newContent = boardService.getNewContent();
		
		model.addAttribute("monthJoin", monthJoin);
		model.addAttribute("newContent", newContent);
		
		return "admin/adminContent";
	}
	
	//관리자메뉴 -> 회원리스트 (페이지네이션처리)
	@RequestMapping(value = "/member/adminMemberList", method = RequestMethod.GET)
	public String adminMemberListGet(Model model,
			@RequestParam(name="mid", defaultValue = "", required = false) String mid,
			@RequestParam(name="pag", defaultValue = "1", required = false) int pag,
			@RequestParam(name="pageSize", defaultValue = "3", required = false) int pageSize) {
		//@RequestParam 은 null값 처리해주는거임, 제일처음엔아이디가 필요없이 전체자료가 나오는 화면이라 맨처음은 mid가 공백이 들어옴
		
		PageVO pageVo = pageProcess.totRecCnt(pag, pageSize, "member", "", mid); //페이지네이션처리를 하기위해서 총 자료건수 구하기 , 아이디개별검색할때 아이디필요하니까 같이 넘기기
		
		List<MemberVO> vos = memberService.getMemberList(pageVo.getStartIndexNo(), pageSize ,mid); //memberService꺼 @Autowired 걸어서 사용, 페이지네이션처리하기위해서 전체자료구하기
		
		model.addAttribute("vos", vos);
		model.addAttribute("pageVo", pageVo);
		
		model.addAttribute("mid", mid);
		
		return "admin/member/adminMemberList";
	}
	
	//회원등급변경하기
	@ResponseBody
	@RequestMapping(value = "/member/adminMemberLevel", method = RequestMethod.POST)
	public String adminMemberLevelPost(int idx, int level) {
		int res = adminService.setMemberLevelCheck(idx, level);
		
		return res+ ""; //res가 int 타입일때 그냥쓰면 빨간줄뜸 ""를통해 형변환해줌
	}
	
	//회원탈퇴하기
	@ResponseBody //ajax사용
	@RequestMapping(value = "/member/adminMemberDel", method = RequestMethod.POST)
	public String adminMemberDelPost(int idx) {
		
		int res = adminService.setMemberDel(idx);
		
		return res+ "";
	}
	
	//ckeditor폴더의 파일리스트 보여주기 (관리자메뉴 -임시화일)
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/file/fileList", method = RequestMethod.GET)
	public String fileListGet(HttpServletRequest request,Model model) {
		String realPath = request.getRealPath("/resources/data/ckeditor/");
		
		String[] files = new File(realPath).list(); //앞에서 넘겼던 해당 폴더에 들어가있는 그파일의 리스트정보를가지고 문자배열로 넘겨줌
		
		model.addAttribute("files",files);
		
		return "admin/file/fileList";
	}

	
	// 선택된 파일 삭제처리하기
	@SuppressWarnings("deprecation")
	@ResponseBody
	@RequestMapping(value = "/fileSelectDelete", method = RequestMethod.POST)
	public String fileSelectDeleteGet(HttpServletRequest request, String delItems) {
		// System.out.println("delItems : " + delItems);
		String realPath = request.getRealPath("/resources/data/ckeditor/");
		delItems = delItems.substring(0, delItems.length()-1);
		
		String[] fileNames = delItems.split("/");
		
		for(String fileName : fileNames) {
			String realPathFile = realPath + fileName;
			new File(realPathFile).delete();
		}
		
		return "1";
	}
	
	
	
}
