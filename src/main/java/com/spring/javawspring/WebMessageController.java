package com.spring.javawspring;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.javawspring.pagination.PageProcess;
import com.spring.javawspring.pagination.PageVO;
import com.spring.javawspring.service.WebMessageService;
import com.spring.javawspring.vo.WebMessageVO;

@Controller
@RequestMapping("/webMessage")
public class WebMessageController {

	@Autowired
//	WebMessageServiceInterpace webMessageServiceInterpace; 원래 이렇게쓰는건데 길어서 인터페이스는삭제
	WebMessageService webMessageService;
	
	@Autowired
	PageProcess pageProcess;
	
	//input,list,content의 내용이 다들어감
	@RequestMapping(value= "/webMessage" , method = RequestMethod.GET)
	public String webMessageGet(Model model, HttpSession session,
			@RequestParam(name="mSw" , defaultValue = "1" ,required = false) int mSw, //기본값:받은메세지
			@RequestParam(name="mFlag" , defaultValue = "11" ,required = false) int mFlag, //기본값:받은메세지
			@RequestParam(name="pag" , defaultValue = "1" ,required = false) int pag, //기본값:받은메세지
			@RequestParam(name="pageSize" , defaultValue = "5" ,required = false) int pageSize, //기본값:받은메세지
			@RequestParam(name="idx" , defaultValue = "0" ,required = false) int idx ) {
		String mid = (String) session.getAttribute("sMid");
		
		if(mSw == 0) { //메세지 작성하기
			
		}
		else if(mSw == 6) { //메세지내용 상세보기
			WebMessageVO vo = webMessageService.getWmMessageOne(idx, mid, mFlag); //현재 여기서 사용은idx만함
			//1건의 메세지만가져와야함 , mFlag가지고가는이유는 받은메세지랑 보낸메세지는 누르면 mFlag가 바뀌는데 휴지통은바뀔게없으니까
			model.addAttribute("vo", vo);
		}
		else { //받은메세지, 보낸메세지, 새메세지, 수신확인, 휴지통
			PageVO pageVo = pageProcess.totRecCnt(pag, pageSize, "webMessage", mid, mSw+""); //mSw를 문자로 형변환해줌
			List<WebMessageVO> vos = webMessageService.getWmMessageList(mid, mSw , pageVo.getStartIndexNo(), pageSize);
			
			model.addAttribute("vos", vos);
			model.addAttribute("pageVo", pageVo);
		}
		model.addAttribute("mSw", mSw); //스위치는 항상 가지고다녀야하는데 안에써주면 하나하나추가해줘야돼서 귀찮으니까 한번에적용하게 여기써줌
		
		return "webMessage/wmMessage";
	}
}
