package com.spring.javawspring;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.javawspring.service.ScheduleService;
import com.spring.javawspring.vo.ScheduleVO;

@Controller
@RequestMapping("/schedule") //중개경로
public class ScheduleController {

	@Autowired
	ScheduleService scheduleService; //인터페이스
	
	
	@RequestMapping(value = "/schedule", method = RequestMethod.GET)
	public String scheduleGet() {
		scheduleService.getSchedule();
		
		return "schedule/schedule";
	}

	//스케줄 자세히보기(달력에서 스케줄누르면 처리)
	@RequestMapping(value = "/scheduleMenu", method = RequestMethod.GET)
	public String scheduleMenuGet(HttpSession session, String ymd, Model model) {
		String mid = (String) session.getAttribute("sMid");
		List<ScheduleVO> vos = scheduleService.getScheduleMenu(mid, ymd);
		
		model.addAttribute("vos", vos);
		model.addAttribute("ymd", ymd);
		model.addAttribute("scheduleCnt", vos.size()); //총 몇건있는지 나오게하기위해서
		
		return "schedule/scheduleMenu";
	}
	
	//스케줄 등록하기
	@ResponseBody
	@RequestMapping(value = "/scheduleInputOk", method = RequestMethod.POST)
	public String scheduleInputOkPost(ScheduleVO vo) {
		
		scheduleService.getScheduleInputOk(vo);
		
		return "";
	}
	
	//스케줄 수정하기
	@ResponseBody
	@RequestMapping(value = "/scheduleUpdateOk", method = RequestMethod.POST)
	public String scheduleUpdateOkPost(ScheduleVO vo) {
		
		scheduleService.getScheduleUpdateOk(vo);
		
		return "";
	}
	
	
	//스케줄 삭제하기
	@ResponseBody
	@RequestMapping(value = "/scheduleDeleteOk", method = RequestMethod.POST)
	public String scheduleDeleteOkPost(int idx) {
		
		scheduleService.getScheduleDeleteOk(idx);
		
		return "";
	}
	
	
	
}
