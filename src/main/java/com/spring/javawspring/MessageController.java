package com.spring.javawspring;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MessageController {
	
	@RequestMapping(value="/msg/{msgFlag}", method=RequestMethod.GET) //매핑 :url주소를 경로랑 같나 비교? {msgFlag} :컨트롤러에서 메세지를 담아서 넘기는거 {}를 쓰면 변수로 인식 @PathVariable이걸 꼭 써줘야함
	//컨트롤러에서 두번째인자를 메세지변수로 받겠다(중괄호가 없으면 값으로안보고 경로로 봄,중괄호 안에 적어줌으로써 경로로 안보고 값으로봄) 값을 잡아오기위해 @PathVariable을 걸어서 String타입의 변수로적어줌
	public String msgGet(@PathVariable String msgFlag, Model model,
			@RequestParam(value="mid", defaultValue = "", required = false) String mid,//문자면 기본값 공백으로 쓰니까 defaultValue = ""
			@RequestParam(value="flag", defaultValue = "", required = false) String flag) { //문자면 기본값 공백으로 쓰니까 defaultValue = ""
		
		if(msgFlag.equals("memberLoginOk")) {
			model.addAttribute("msg", mid + "님 로그인 되었습니다.");
			model.addAttribute("url", "member/memberMain"); //어디로 보낼건지
		}
		else if(msgFlag.equals("memberLogout")) {
			model.addAttribute("msg", mid + "님 로그아웃 되었습니다.");
			model.addAttribute("url", "member/memberLogin");
		}
		else if(msgFlag.equals("memberLoginNo")) {
			model.addAttribute("msg", "로그인 실패!!!");
			model.addAttribute("url", "member/memberLogin");
		}
		else if(msgFlag.equals("guestInputOk")) {
			model.addAttribute("msg", "방명록에 글이 등록되었습니다.");
			model.addAttribute("url", "guest/guestList");
		}
		else if(msgFlag.equals("guestDeleteOk")) {
			model.addAttribute("msg", "방명록 글이 삭제되었습니다.");
			model.addAttribute("url", "guest/guestList");
		}
		else if(msgFlag.equals("memberJoinOk")) {
			model.addAttribute("msg", "회원가입이 완료되었습니다.");
			model.addAttribute("url", "member/memberLogin");
		}
		else if(msgFlag.equals("memberJoinNo")) {
			model.addAttribute("msg", "회원가입 실패!!");
			model.addAttribute("url", "member/memberJoin");
		}
		else if(msgFlag.equals("memberIdCheckNo")) {
			model.addAttribute("msg", "중복된 아이디입니다.");
			model.addAttribute("url", "member/memberJoin");
		}
		else if(msgFlag.equals("memberNickNameCheckNo")) {
			model.addAttribute("msg", "중복된 닉네임입니다.");
			model.addAttribute("url", "member/memberJoin");
		}
		else if(msgFlag.equals("adminNo")) {
			model.addAttribute("msg", "관리자가 아니시군요!");
			model.addAttribute("url", "member/memberLogin");
		}
		else if(msgFlag.equals("memberNo")) {
			model.addAttribute("msg", "로그인후 사용하세요.");
			model.addAttribute("url", "member/memberLogin");
		}
		else if(msgFlag.equals("levelCheckNo")) {
			model.addAttribute("msg", "회원등급을 확인하세요.");
			model.addAttribute("url", "member/memberMain");
		}
		else if(msgFlag.equals("mailSendOk")) {
			model.addAttribute("msg", "메일을 정상적으로 전송했습니다.");
			model.addAttribute("url", "study/mail/mailForm");
		}
		else if(msgFlag.equals("memberImsiPwdOk")) {
			model.addAttribute("msg", "임시비밀번호를 발송하였습니다.\\n메일을 확인하세요.");
			model.addAttribute("url", "member/memberLogin");
		}
		else if(msgFlag.equals("memberImsiPwdNo")) {
			model.addAttribute("msg", "아이디와 이메일주소를 확인해 주세요...");
			model.addAttribute("url", "member/memberPwdSearch");
		}
		else if(msgFlag.equals("memberPwdUpdateOk")) {
			model.addAttribute("msg", "비밀번호가 변경되었습니다.");
			model.addAttribute("url", "member/memberMain");
		}
		else if(msgFlag.equals("fileUploadOk")) {
			model.addAttribute("msg", "파일 업로드 되었습니다.");
			model.addAttribute("url", "study/fileUpload/fileUploadForm");
		}
		else if(msgFlag.equals("fileUploadNo")) {
			model.addAttribute("msg", "파일 업로드 실패");
			model.addAttribute("url", "study/fileUpload/fileUploadForm");
		}
		else if(msgFlag.equals("boardInputOk")) {
			model.addAttribute("msg", "게시글이 등록되었습니다.");
			model.addAttribute("url", "board/boardList");
		}
		else if(msgFlag.equals("boardInputNo")) {
			model.addAttribute("msg", "게시글 등록실패!");
			model.addAttribute("url", "board/boardInput");
		}
		else if(msgFlag.equals("memberDeleteOK")) {
			model.addAttribute("msg", mid + "님 회원탈퇴가 완료되었습니다.\\n같은 아이디로 1달이내 재가입 하실 수 없습니다.");
			model.addAttribute("url", "member/memberLogin");
		}
		else if(msgFlag.equals("adminMemberDelOk")) {
			model.addAttribute("msg","탈퇴신청회원의 정보를 삭제처리했습니다.");
			model.addAttribute("url", "admin/member/memberList");
		}
		else if(msgFlag.equals("memberPwdCheckNo")) {
			model.addAttribute("msg","등록되어있는 비밀번호와 일치하지 않습니다.");
			model.addAttribute("url", "member/memberPwdUpdate");
		}
		else if(msgFlag.equals("memberUpdateOk")) {
			model.addAttribute("msg","회원정보가 수정되었습니다.");
			model.addAttribute("url", "member/memberMain");
		}
		else if(msgFlag.equals("memberUpdateNo")) {
			model.addAttribute("msg", "회원 정보 수정실패~~");
			model.addAttribute("url", "member/memberPwdUpdate?flag=pwdCheck");
		}                                                                                                                                                                                                                                                    
		else if(msgFlag.equals("memberNickNameCheckNo2")) {
			model.addAttribute("msg", "닉네임을 확인해주세요");
			model.addAttribute("url", "member/memberPwdUpdate?flag=pwdCheck");
		}                                                                                                                                                                                                                                                    
		else if(msgFlag.equals("boardDeleteOk")) {
			model.addAttribute("msg", "게시글이 삭제되었습니다.");
			model.addAttribute("url", "board/boardList"+flag);
		}                                                                                                                                                                                                                                                    
		else if(msgFlag.equals("boardUpdateOk")) {
			model.addAttribute("msg", "게시글이 수정되었습니다.");
			model.addAttribute("url", "board/boardList"+flag);
		}                                                                                                                                                                                                                                                    
		else if(msgFlag.equals("pdsInputOk")) {
			model.addAttribute("msg", "자료실에 파일이 업로드 되었습니다.");
			model.addAttribute("url", "pds/pdsList");
		}                                                                                                                                                                                                                                                    


		
		return "include/message";
	}
}
