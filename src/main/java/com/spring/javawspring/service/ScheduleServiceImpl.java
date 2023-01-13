package com.spring.javawspring.service;

import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.spring.javawspring.dao.ScheduleDAO;
import com.spring.javawspring.vo.ScheduleVO;

@Service
public class ScheduleServiceImpl implements ScheduleService {

	@Autowired
	ScheduleDAO scheduleDAO;

	@Override
	public void getSchedule() {
		//DB에 넣는게 아니라 읽어올거임
	  // model객체를 사용하게되면 불필요한 메소드가 많이 따라오기에 여기서는 request객체를 사용했다.
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest(); //request객체로 불러오려면 써줘야함
		
		//로그인한 사람(나)의 일정을 가져와야되니까 mid가져와야되는데 변수로가져온게없으니 세션에 있는 mid
		HttpSession session = request.getSession();
		String mid = (String) session.getAttribute("sMid");
		
		
		// 오늘 날짜 저장시켜둔다.(calToday변수, 년(toYear), 월(toMonth), 일(toDay))
		Calendar calToday = Calendar.getInstance();
		int toYear = calToday.get(Calendar.YEAR);
		int toMonth = calToday.get(Calendar.MONTH);
		int toDay = calToday.get(Calendar.DATE);
				
		// 화면에 보여줄 해당 '년(yy)/월(mm)'을 셋팅하는 부분(처음에는 오늘 년도와 월을 가져오지만, '이전/다음'버튼 클릭하면 해당 년과 월을 가져오도록 한다.
		Calendar calView = Calendar.getInstance();
		int yy = request.getParameter("yy")==null ? calView.get(Calendar.YEAR) : Integer.parseInt(request.getParameter("yy")); //request직접써서 이렇게 null값처리가능함
	  int mm = request.getParameter("mm")==null ? calView.get(Calendar.MONTH) : Integer.parseInt(request.getParameter("mm"));
	  
	  if(mm < 0) { // 1월에서 전월 버튼을 클릭시에 실행
	  	yy--;
	  	mm = 11;
	  }
	  if(mm > 11) { // 12월에서 다음월 버튼을 클릭시에 실행
	  	yy++;
	  	mm = 0;
	  }
	  calView.set(yy, mm, 1);		// 현재 '년/월'의 1일을 달력의 날짜로 셋팅한다.
	  
	  int startWeek = calView.get(Calendar.DAY_OF_WEEK);  						// 해당 '년/월'의 1일에 해당하는 요일값을 숫자로 가져온다.
	  int lastDay = calView.getActualMaximum(Calendar.DAY_OF_MONTH);  // 해당월의 마지막일자(getActualMaxximum메소드사용-이 메소드 덕분에 월마다 마지막날짜를 일일히구하지않아도됨)를 구한다.
	  
	  // 화면에 보여줄 년월기준 전년도/다음년도를 구하기 위한 부분
	  int prevYear = yy;  			// 전년도
	  int prevMonth = (mm) - 1; // 이전월
	  int nextYear = yy;  			// 다음년도
	  int nextMonth = (mm) + 1; // 다음월
	  
	  if(prevMonth == -1) {  // 1월에서 전월 버튼을 클릭시에 실행..
	  	prevYear--;
	  	prevMonth = 11;
	  }
	  
	  if(nextMonth == 12) {  // 12월에서 다음월 버튼을 클릭시에 실행..
	  	nextYear++;
	  	nextMonth = 0;
	  }
	  
	  // 현재달력에서 앞쪽의 빈공간은 '이전달력'을 보여주고, 뒷쪽의 남은공간은 '다음달력'을 보여주기위한 처리부분(아래 6줄)
	  Calendar calPre = Calendar.getInstance(); // 이전달력
	  calPre.set(prevYear, prevMonth, 1);  			// 이전 달력 셋팅
	  int preLastDay = calPre.getActualMaximum(Calendar.DAY_OF_MONTH);  // 해당월의 마지막일자를 구한다.
	  
	  Calendar calNext = Calendar.getInstance();// 다음달력
	  calNext.set(nextYear, nextMonth, 1);  		// 다음 달력 셋팅
	  int nextStartWeek = calNext.get(Calendar.DAY_OF_WEEK);  // 다음달의 1일에 해당하는 요일값을 가져온다.
	  
	  //sql에 등록된 dataformat의 비교형식을 맞춰주기위한 날짜형식변환작업(sql에는 2023-01-17 00:00:00이런식으로 들어옴)
	  //해당 년월의 형식변환? 2023-1 => 2023-01
	  String ym ="";
	  int intMM = mm + 1; //1월이면 0으로들어와서 +1을해줌
	  if(intMM >= 1 && intMM <=9) ym = yy + "-0" + (mm + 1); //1월보다 크거나같고 9월보다 작거나같을경우(1월~9월) 0을붙여줌
	  else ym = yy + "-" + (mm + 1); //10~12월일경우
	  
	  //스케줄에 등록되어있는 일정들을 가져오기
	  List<ScheduleVO> vos = scheduleDAO.getScheduleList(mid, ym); //ym에는 연월을줘야하는데 편집을해줘서넘겨야함
	  request.setAttribute("vos", vos);
	  
	  /* ---------  아래는  앞에서 처리된 값들을 모두 request객체에 담는다.  -----------------  */
	  
	  // 오늘기준 달력...
	  request.setAttribute("toYear", toYear);
	  request.setAttribute("toMonth", toMonth);
	  request.setAttribute("toDay", toDay);
	  
	  // 화면에 보여줄 해당 달력...
	  request.setAttribute("yy", yy);
	  request.setAttribute("mm", mm);
	  request.setAttribute("startWeek", startWeek);
	  request.setAttribute("lastDay", lastDay);
	  
	  // 화면에 보여줄 해당 달력 기준의 전년도, 전월, 다음년도, 다음월 ...
	  request.setAttribute("prevYear", prevYear);
		request.setAttribute("prevMonth", prevMonth);
		request.setAttribute("nextYear", nextYear);
		request.setAttribute("nextMonth", nextMonth);
		
		// 현재 달력의 '앞/뒤' 빈공간을 채울, 이전달의 뒷부분과 다음달의 앞부분을 보여주기위해 넘겨주는 변수
		request.setAttribute("preLastDay", preLastDay);				// 이전달의 마지막일자를 기억하고 있는 변수
		request.setAttribute("nextStartWeek", nextStartWeek);	// 다음달의 1일에 해당하는 요일을 기억하고있는 변수
	}

	@Override
	public List<ScheduleVO> getScheduleMenu(String mid, String ymd) {
		//2023-1-1/2023-1-15/2023-10-5 => 2023-01-01 식으로변환
		//10글자가 안되면 뭐가 잘못된거임
		String mm =  "", dd= "";
		String[] ymdArr = ymd.split("-"); // -기준으로 잘라줌
		
		if(ymd.length() != 10) { //ymd가 10이아니면 뭐가 잘못된거임
			if(ymdArr[1].length() == 1) mm = "0" + ymdArr[1];
			else mm = ymdArr[1];
			if(ymdArr[2].length() == 1) dd = "0" + ymdArr[2];
			else dd = ymdArr[2];
			ymd = ymdArr[0] + "-" + mm +"-" + dd;//0번지는 그대로가져가도됨(년도)
		}
		
		return scheduleDAO.getScheduleMenu(mid, ymd);
	}

	@Override
	public void getScheduleInputOk(ScheduleVO vo) {
		scheduleDAO.getScheduleInputOk(vo);
	}

	@Override
	public void getScheduleUpdateOk(ScheduleVO vo) {
		scheduleDAO.getScheduleUpdateOk(vo);
	}

	@Override
	public void getScheduleDeleteOk(int idx) {
		scheduleDAO.getScheduleDeleteOk(idx);
	}
	
	
}
