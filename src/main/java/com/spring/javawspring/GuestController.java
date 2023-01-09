package com.spring.javawspring;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.javawspring.pagination.PageProcess;
import com.spring.javawspring.pagination.PageVO;
import com.spring.javawspring.service.GuestService;
import com.spring.javawspring.vo.GuestVO;

@Controller
@RequestMapping("/guest") //중개경로
public class GuestController {

	@Autowired
	GuestService guestService; //인터페이스
	
	@Autowired
	PageProcess pageProcess; //페이지네이션
	
	/*
	@RequestMapping(value = "/guestList", method = RequestMethod.GET)
	public String guestListGet(Model model,
			@RequestParam(name="pag", defaultValue = "1", required = false) int pag) { 
		//이제 널값처리 RequestParam으로함 , defaultValue는 무조건 1 required는 필수는아님 => 숫자로 바꿈,name은 jsp에서 null값 삼항연산자쓸때	<int pag = request.getParameter("pag")==null? 0 : Integer.parseInt(request.getParameter("pag")); >  에서 pag랑같은의미
		//defaultValues는 ?뒤에쓰는 기본값 required는 필수냐 아니냐(false는 필수 x) 외부에서오는건 무조건 문자로와서 1이라고쓰면안되고 문자로간주해서 "1"로 써줘야함
		
		//페이징처리
		//1.페이지(pag)를 결정한다 (위에)
		int pageSize = 3;   //2.한페이지의 분량을 결정한다(한 page건수)
		int totRecCnt = guestService.totRecCnt(); //3.총 레코드 건수를 구한다
		int totPage = (totRecCnt % pageSize) == 0 ? totRecCnt  / pageSize : (totRecCnt/ pageSize) +1; 	//4.총 페이지 건수를 구한다 
		int startIndexNo = (pag - 1) * pageSize;//5.현재페이지의 시작 인덱스 번호를 구한다
		int curScrStartNo = totRecCnt - startIndexNo; //6.현재 화면에 보여주는 시작 번호를 구한다
		
		int blockSize = 3; //1.블록의 크기를 결정한다(여기선 3으로지정)
		int curBlock = (pag-1) / blockSize; //2.현재페이지가 위치하고있는 블록 번호를 구한다(예.1~3페이지는 0블록, 4~6페이지는1블록, 7~9페이지는 2블록)
		int lastBlock = (totPage - 1) / blockSize; // 3. 마지막블록을 구한다
		
		ArrayList<GuestVO> vos = guestService.getGuestList(startIndexNo, pageSize); //시작인덱스번호와 한페이지 크기를 같이 넘기는거임
		
		
		model.addAttribute("vos", vos); //이렇게하면 방명록리스트에 5건만 나오는거임 
		model.addAttribute("pag", pag); //현재페이지 이전페이지 하려고 넘겨야함
		model.addAttribute("totPage", totPage); //현재페이지 이전페이지 하려고 넘겨야함
		model.addAttribute("curScrStartNo", curScrStartNo);
		model.addAttribute("blockSize", blockSize);
		model.addAttribute("curBlock", curBlock);
		model.addAttribute("lastBlock", lastBlock);
		model.addAttribute("pageSize", pageSize);
		
		return "guest/guestList";
	}
	*/
	
	//pagination 이용하기...
	@RequestMapping(value = "/guestList", method = RequestMethod.GET)
	public String guestListGet(Model model,
			@RequestParam(name="pag", defaultValue = "1", required = false) int pag, 
			@RequestParam(name="pageSize", defaultValue = "3", required = false) int pageSize) {
		
		PageVO pageVo = pageProcess.totRecCnt(pag, pageSize, "guest", "", "");
		
		List<GuestVO> vos = guestService.getGuestList(pageVo.getStartIndexNo(), pageSize);
		
		model.addAttribute("vos", vos);  
		model.addAttribute("pageVo", pageVo); 
		
		return "guest/guestList";
	}
	
	//폼뜨게함
	@RequestMapping(value = "/guestInput", method = RequestMethod.GET)
	public String guestListGet() {
		
		return "guest/guestInput"; //servlet-context.xml에 앞뒤 적어줘서 생략함
	}
	
	//왜 post로 하나???폼태그에서 post로 전송
	@RequestMapping(value = "/guestInput", method = RequestMethod.POST)
	public String guestListPost(GuestVO vo) { //vo는 왜 넣나???특정값만 가지고오고싶으면 idx 여러개면 vo
		guestService.setGuestInput(vo); //jsp에서 dao가 할일을 스프링에선 서비스객체가함 (서비스(인터페이스)가 implement(실제 구현객체)에게 얘가 dao에 dao가 매퍼에 일을 시킴 매퍼가 sql을씀)-dao 여기선 인터페이스로 하는데 클래스로해도되긴함(클래스는 일을 하나밖에못해서 인터페이스로함)
		
		return "redirect:/msg/guestInputOk"; //redirect: 는 컨트롤러 -> 컨트롤러로 갈때 방향을 틀어라라는뜻
	}
	
	
	//방명록 삭제
	@RequestMapping(value = "/guestDelete", method = RequestMethod.GET)
	public String guestDeleteGet(int idx) {
		guestService.setGuestDelete(idx);
		
		return "redirect:/msg/guestDeleteOk";
	}
	
	
}
