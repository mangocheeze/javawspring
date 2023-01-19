package com.spring.javawspring;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
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

import com.spring.javawspring.common.ARIAUtil;
import com.spring.javawspring.common.SecurityUtil;
import com.spring.javawspring.service.MemberService;
import com.spring.javawspring.service.StudyService;
import com.spring.javawspring.vo.GuestVO;
import com.spring.javawspring.vo.KakaoAddressVO;
import com.spring.javawspring.vo.MailVO;
import com.spring.javawspring.vo.MemberVO;
import com.spring.javawspring.vo.QrCodeVO;

@Controller
@RequestMapping("/study")
public class StudyController {

	
	@Autowired
	StudyService studyService;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;  //의존성 주입함

	@Autowired
	JavaMailSender mailSender;
	
	@Autowired
	MemberService memberService;
	
	
	@RequestMapping(value="/ajax/ajaxMenu" , method = RequestMethod.GET)
	public String ajaxMenuGet() {
		return "study/ajax/ajaxMenu";
	}
	
	// 일반 String값의 전달 1(숫자/영문자)
	@ResponseBody  //String형식의 자료를 주고받도록해줌! 중요 (문자로 자료를 주고받을때사용)
	@RequestMapping(value="/ajax/ajaxTest1_1", method = RequestMethod.POST)
	public String ajaxTest1_1Post(int idx) {
		idx = (int)(Math.random()*idx) +1 ; //1에서부터 넘겨진숫자까지의 난수발생
		String res = idx + " : Happy a Good Time!!";
		return res;//res자리가 jsp자리인데 주소가아니라서 404에러가남
	}
	
	// 일반 String값의 전달 2(숫자/영문자/한글)
	@ResponseBody  //String형식의 자료를 주고받도록해줌! 중요 (문자로 자료를 주고받을때사용)
	@RequestMapping(value="/ajax/ajaxTest1_2", method = RequestMethod.POST, produces = "application/text; charset=utf8") //한글깨질때(문자낱개로 보낼때) 작성-객체로넘길땐 안써도됨 (produces)
	public String ajaxTest1_2Post(int idx) {
		idx = (int)(Math.random()*idx) +1 ; //1에서부터 넘겨진숫자까지의 난수발생
		String res = idx + " : 안녕하세요....Happy a Good Time!!";
		return res;//res자리가 jsp자리인데 주소가아니라서 404에러가남
	}
	
	// 일반 배열값의 전달 폼
	@RequestMapping(value = "/ajax/ajaxTest2_1" , method = RequestMethod.GET)
		public String ajaxTest2_1Get() {
			return "study/ajax/ajaxTest2_1";
	}
	
	// 일반 배열값의 전달
	@ResponseBody
	@RequestMapping(value = "/ajax/ajaxTest2_1" , method = RequestMethod.POST)
	public String[] ajaxTest2_1Post(String dodo) {
//		String[] strArr = new String[100];
//		strArr = studyService.getCityStringArr(dodo);
//		return strArr;
		return studyService.getCityStringArr(dodo); //위에 3줄을 한번에 작성, 실무에선 이렇게씀
	}
	
  // 객체배열(ArrayList)값의 전달 폼
	@RequestMapping(value = "/ajax/ajaxTest2_2", method = RequestMethod.GET)
	public String ajaxTest2_2Get() {
		return "study/ajax/ajaxTest2_2";
	}
	
	// 객체배열(ArrayList)값의 전달
	@ResponseBody
	@RequestMapping(value = "/ajax/ajaxTest2_2", method = RequestMethod.POST)
	public ArrayList<String> ajaxTest2_2Post(String dodo) {
		return studyService.getCityArrayListArr(dodo);
	}
	
	// Map(HashMap<k,v>)값의 전달 폼
	@RequestMapping(value = "/ajax/ajaxTest2_3", method = RequestMethod.GET)
	public String ajaxTest2_3Get() {
		return "study/ajax/ajaxTest2_3";
	}
	
	// Map(HashMap<k,v>)값의 전달
	@ResponseBody
	@RequestMapping(value = "/ajax/ajaxTest2_3", method = RequestMethod.POST)
	public HashMap<Object, Object> ajaxTest2_3Post(String dodo) {
		ArrayList<String> vos = new ArrayList<String>();
		vos = studyService.getCityArrayListArr(dodo);
		
		HashMap<Object, Object> map = new HashMap<Object, Object>();
		map.put("city", vos);
		
		return map;
	}
	
	// DB를 활용한 값의 전달 폼
	@RequestMapping(value = "/ajax/ajaxTest3", method=RequestMethod.GET)
	public String ajaxTest3Get() {
		return "study/ajax/ajaxTest3";
	}
	
	// DB를 활용한 값의 전달1(vo)
	@ResponseBody
	@RequestMapping(value = "/ajax/ajaxTest3_1", method=RequestMethod.POST)
	public GuestVO ajaxTest3_1Post(String mid) {
//		GuestVO vo = studyService.getGuestMid(mid);
//		return vo;
		return studyService.getGuestMid(mid);
	}
	
	// DB를 활용한 값의 전달2(vos)
	@ResponseBody
	@RequestMapping(value = "/ajax/ajaxTest3_2", method=RequestMethod.POST)
	public ArrayList<GuestVO> ajaxTest3_2Post(String mid) {
//		ArrayList<GuestVO> vos = studyService.getGuestNames(mid);
//		return vos;
		return studyService.getGuestNames(mid);
	}
	
	//암호화 연습(sha256) 폼
	@RequestMapping(value = "/password/sha256" , method= RequestMethod.GET)
	public String sha256Get() {
		return "study/password/sha256";
	}
	
	
	//암호화 연습(sha256) 결과 처리
	@ResponseBody
	@RequestMapping(value = "/password/sha256" , method= RequestMethod.POST, produces = "application/text; charset=utf8")
	public String sha256Post(String pwd) {
		String encPwd = SecurityUtil.encryptSHA256(pwd); //이렇게하면 암호화가 끝남
		pwd = "원본 비밀번호 : " + pwd + " / 암호화된 비밀번호 : " + encPwd;
		return pwd;
	}
	
	
	//암호화 연습(aria)
	@RequestMapping(value = "/password/aria" , method= RequestMethod.GET)
	public String ariaGet() {
		return "study/password/aria";
	}
	
	
	//암호화 연습(aria) 결과 처리
	@ResponseBody
	@RequestMapping(value = "/password/aria" , method= RequestMethod.POST, produces = "application/text; charset=utf8")
	public String ariaPost(String pwd) {
		String encPwd="";
		String decPwd="";
		try {
			encPwd = ARIAUtil.ariaEncrypt(pwd);//암호화 .외부라이브러리나 자신들이 만든 클래스 사용할땐 무조건 예외처리하게끔함
			decPwd = ARIAUtil.ariaDecrypt(encPwd); //복호화
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}  
		
		pwd = "원본 비밀번호 : " + pwd + " / 암호화된 비밀번호 : " + encPwd + "/ 복호화된 비밀번호 : " + decPwd;
		return pwd;
	}
	
	
	//암호화 연습(bCryptPassword)  폼
	@RequestMapping(value = "/password/bCryptPassword" , method= RequestMethod.GET)
	public String bCryptPasswordGet() {
		return "study/password/security";
	}
	
	
	//암호화 연습(bCryptPassword) 결과 처리
	@ResponseBody
	@RequestMapping(value = "/password/bCryptPassword" , method= RequestMethod.POST, produces = "application/text; charset=utf8")
	public String bCryptPasswordPost(String pwd) {
		String encPwd="";
		encPwd = passwordEncoder.encode(pwd);  //암호화
		pwd = "원본 비밀번호 : " + pwd + " / 암호화된 비밀번호 : " + encPwd ;
		
		return pwd;
	}
	
	
	//SMTP 메일보내기 (study -> 메일작성폼)
	@RequestMapping(value = "/mail/mailForm", method=RequestMethod.GET)
	public String mailFormGet(Model model, String email) {
		
		List<MemberVO> vos = memberService.getMemberList(0, 1000 ,"");
		model.addAttribute("vos", vos);
		model.addAttribute("cnt", vos.size());
		model.addAttribute("email",email);
		
		
		return "study/mail/mailForm";
	}
	
	// 주소록 호출하기
	
	
	
	//메일 전송하기
	@RequestMapping(value = "/mail/mailForm", method=RequestMethod.POST)
	public String mailFormPost(MailVO vo, HttpServletRequest request) {
		try {
			String toMail = vo.getToMail();
			String title = vo.getTitle();
			String content = vo.getContent();
			
			//앞에서 넘긴걸 받아서 메일을 보냄
			//메일을 전송하기 위한 객체 : MimeMessage , MimeMessageHelper()
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8"); //보관함
			
			// 메일 보관함에 회원이 보내온 메세지들을 모두 저장시킨다.
			messageHelper.setTo(toMail);
			messageHelper.setSubject(title);
			messageHelper.setText(content);
			
			//메세지 보관함의 내용(content)에 필요한 정보를 추가로 담아서 전송시킬수 있도록한다.
			content = content.replace("\n", "<br/>");
			content += "<br><hr><h3>CJ Green에서 보냅니다..총총</h3></hr></br>";  //<br/>에서버전땜에 /빼고함 
			content += "<p><img src=\"cid:main.png\" width='500px'></p>";  //그림의 껍데기만 지정됨
			content += "<p>방문하기 : <a href='http://49.142.157.251:9090/green2209J_06/main.ani'>동물보호관리시스템</a></p>";
			content += "<hr>";
			
			messageHelper.setText(content, true); //true는 덮어써진거임
			
			//본문에 기재된 그림파일의 경로를 따로 표시시켜준다.그리고 , 보관함에 다시 저장시켜준다.
			FileSystemResource file = new FileSystemResource("D:\\JavaWorkspace\\springframework\\works\\javawspring\\src\\main\\webapp\\resources\\images\\main.png");
			messageHelper.addInline("main.png", file); //일반그림넣기
			
			//첨부파일 보내기(서버 파일시스템에 있는 파일만 가능-서버에 올려놔야가능)
			file = new FileSystemResource("D:\\JavaWorkspace\\springframework\\works\\javawspring\\src\\main\\webapp\\resources\\images\\chicago.jpg");
			messageHelper.addAttachment("chicago.jpg", file); //첨부파일로 넣기

			//zip파일 보내기
			file = new FileSystemResource("D:\\JavaWorkspace\\springframework\\works\\javawspring\\src\\main\\webapp\\resources\\images\\images.zip");
			messageHelper.addAttachment("images.zip", file); 
			
			//절대경로(realPath)로 보내기
			//file = new FileSystemResource(request.getRealPath("/resources/images/paris.jpg")); //절대경로(realPath)
			file = new FileSystemResource(request.getSession().getServletContext().getRealPath("/resources/images/paris.jpg")); //위에 검정줄 거슬리면 이렇게써줘도됨
			messageHelper.addAttachment("paris.jpg", file); 
			
			//메일 전송하기
			mailSender.send(message);
			
			
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
		return "redirect:/msg/mailSendOk";
	}
	
	
	//UUID 입력폼
	@RequestMapping(value= "/uuid/uuidForm", method=RequestMethod.GET)
	public String uuidFormGet() {
		return "study/uuid/uuidForm";
	}
	
	//UUID 처리하기
	@ResponseBody
	@RequestMapping(value= "/uuid/uuidProcess", method=RequestMethod.POST)
	
	public String uuidFormPost() {
		UUID uid = UUID.randomUUID();
		return uid.toString(); //문자열로 바꾸려고 toString()
	}
	
	//파일업로드 폼
	@RequestMapping(value="fileUpload/fileUploadForm", method= RequestMethod.GET)
	public String fileUploadFormGet() {
		
		return "study/fileUpload/fileUploadForm";
	}
	
	//파일업로드 처리하기
	@RequestMapping(value="fileUpload/fileUploadForm", method= RequestMethod.POST)
	public String fileUploadFormPost(MultipartFile fName) {
		int res = studyService.fileUpload(fName); //서비스단에 업무를 전과시킴
		if(res == 1) return "redirect:/msg/fileUploadOk";
		else return "redirect:/msg/fileUploadNo";
	}
	
	
	//달력내역 가져오기
	@RequestMapping(value="/calendar", method = RequestMethod.GET)
	public String calendarGet()	{
		studyService.getCalendar();
		return "study/calendar/calendar";
	}
	
	//QR Code 작성 폼
	@RequestMapping(value="/qrCode", method = RequestMethod.GET)
	public String qrCodeGet(HttpSession session, Model model)	{
		String mid = (String) session.getAttribute("sMid");
		MemberVO vo = memberService.getMemberIdCheck(mid);
		
		model.addAttribute("vo", vo);
		
		return "study/qrCode/qrCode";
	}
	
	//QR Code 생성하기
	/*
	@ResponseBody
	@RequestMapping(value = "/qrCode", method = RequestMethod.POST, produces = "application/text; charset=utf8")//파일저장할거면 request무조건있어야함
	public String qrCodePost(HttpServletRequest request,
			@RequestParam(name="mid", defaultValue = "", required = false) String mid,
			@RequestParam(name="moveFlag", defaultValue = "", required = false) String moveFlag) {
		String realPath = request.getSession().getServletContext().getRealPath("/resources/data/qrCode/");
		
		String qrCodeName = studyService.qrCreate(mid, moveFlag, realPath);
		
		return qrCodeName;
	}
	*/
	
	//QR코드 영화예매- 생성,DB에 등록
	@ResponseBody
	@RequestMapping(value = "/qrCode", method = RequestMethod.POST, produces = "application/text; charset=utf8")//파일저장할거면 request무조건있어야함
	public String qrCodePost(HttpServletRequest request, QrCodeVO vo,
			@RequestParam(name="mid", defaultValue = "", required = false) String mid,
			@RequestParam(name="moveFlag", defaultValue = "", required = false) String moveFlag,
			@RequestParam(name="movie", defaultValue = "", required = false) String movie,
			@RequestParam(name="cinema", defaultValue = "", required = false) String cinema,
			@RequestParam(name="time", defaultValue = "", required = false) String time,
			@RequestParam(name="adult", defaultValue = "", required = false) String adult,
			@RequestParam(name="child", defaultValue = "", required = false) String child ) {
		String realPath = request.getSession().getServletContext().getRealPath("/resources/data/qrCode/");
		
		String bigo = movie + "/" + cinema + "/" + time + "/" + adult + "/" + child;
		String qrCodeName = studyService.qrCreate(mid, moveFlag, realPath);
		String idx = qrCodeName.substring(qrCodeName.lastIndexOf("_")+1);
		
		vo.setIdx(idx);
		vo.setQrCode(qrCodeName);
		vo.setBigo(bigo);
		
		studyService.setQrInput(vo); // vo가지고 가서 DB에 등록
		
		return qrCodeName;
	}
	
	// QR영화예매 조회
	@ResponseBody
	@RequestMapping(value="/qrSearch", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	public String qrSearchPost(String qrIdx, QrCodeVO vo) {
		vo = studyService.getQrSearch(qrIdx);
		if(vo == null) {
			return "";
		}
		else {
			String bigo = vo.getBigo();
			return bigo;
		}
	}
	
	
	//카카오맵 기본 지도보기
	@RequestMapping(value="/kakaomap/kakaomap" , method = RequestMethod.GET)
	public String kakaomapGet() {
		return "study/kakaomap/kakaomap";
	}
	
	//EX1
	//카카오맵 '마커표시/DB저장' (kakaoMenu.jsp에서부름)
	@RequestMapping(value="/kakaomap/kakaoEx1" , method = RequestMethod.GET)
	public String kakaoEx1Get() {
		return "study/kakaomap/kakaoEx1";
	}
	
	//EX1
	//카카오맵 '마커표시/DB저장'
	@ResponseBody
	@RequestMapping(value="/kakaomap/kakaoEx1" , method = RequestMethod.POST)
	public String kakaoEx1Post(KakaoAddressVO vo) { //vo를 받아옴
		KakaoAddressVO searchVo = studyService.getKakaoAddressName(vo.getAddress()); //기존에 DB에 주소가 저장되어있는지 읽어와서 searchVo변수에담음
		if(searchVo != null) return "0"; //주소가 있으면 0을 리턴하고
		
		studyService.setKakaoAddressName(vo); //그게아니면 주소를 저장한다
		
		return "1"; //그리고 1을 리턴함
	}
	
	//Ex2
	//카카오맵 'DB저장된 지역의 검색'
	@RequestMapping(value="/kakaomap/kakaoEx2" , method = RequestMethod.GET)
	public String kakaoEx2Get(Model model,
			@RequestParam(name="address", defaultValue = "그린컴퓨터학원", required = false) String address) { //vo를 뿌릴거라 model받아옴
		KakaoAddressVO vo = studyService.getKakaoAddressName(address);
		List<KakaoAddressVO> vos = studyService.getAddressNameList(); //다가져오는거라 매개변수넣을필요 x
		
		model.addAttribute("vo", vo);
		model.addAttribute("vos", vos);
		model.addAttribute("address", address);
		
		return "study/kakaomap/kakaoEx2";
	}
	
	//Ex2
	//선택된 지역을 카카오 DB에서 삭제하기
	@ResponseBody
	@RequestMapping(value="/kakaomap/kakaoEx2Delete" , method = RequestMethod.POST)
	public String kakaoEx2DeletePost(String address) {
		studyService.setKakaoAddressDelete(address);
		
		return "";
	}
	
	//EX3
  // 카카오제공해주는 정보DB를 가지고 검색후 kakaoAddress 테이블에 주소저장
  @RequestMapping(value="/kakaomap/kakaoEx3",method=RequestMethod.GET)
  public String kakaoEx3Get(Model model,
          @RequestParam(name="address",defaultValue = "단재초등학교",required = false) String address) {
      
      model.addAttribute("address",address);
      
      return "study/kakaomap/kakaoEx3";
  }
  
  //EX4
  //kakaoAddress에 있는 주소를가지고 주변시설 찾기 (편의점,주유소..)
  @RequestMapping(value="/kakaomap/kakaoEx4",method=RequestMethod.GET)
  public String kakaoEx4Get(Model model,
          @RequestParam(name="address",defaultValue = "단재초등학교",required = false) String address) {
      
      KakaoAddressVO vo = studyService.getKakaoAddressName(address);
      model.addAttribute("vo",vo);
      
      return "study/kakaomap/kakaoEx4";
  }
	
	
	
	
	//Ex5
  //지정해놓은곳의 거리랑 15km이내에 있는 kakaoAddress테이블에있는 정보검색
	@RequestMapping(value="/kakaomap/kakaoEx5" , method = RequestMethod.GET)
	public String kakaoEx3Post(Model model) {
		
		ArrayList<KakaoAddressVO> vos = studyService.getDistanceList();
		
		model.addAttribute("vos",vos);
		
		return "study/kakaomap/kakaoEx5";
	}
}

