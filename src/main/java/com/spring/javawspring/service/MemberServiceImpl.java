package com.spring.javawspring.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.javawspring.dao.MemberDAO;
import com.spring.javawspring.vo.MemberVO;

@Service
public class MemberServiceImpl implements MemberService {
	
	@Autowired
	MemberDAO memberDAO;

	@Override
	public MemberVO getMemberIdCheck(String mid) {
		return memberDAO.getMemberIdCheck(mid);
	}

	@Override
	public MemberVO getMemberNickNameCheck(String nickName) {
		return memberDAO.getMemberNickNameCheck(nickName);
	}

	@Override
	public int setMemberJoinOk(MemberVO vo) {
		return memberDAO.setMemberJoinOk(vo);
	}

	@Override
	public void setMemberVisitProcess(MemberVO vo) {
		//오늘날짜 편집
		Date now = new Date(); //Date생성(오늘) util에있는걸로 import걸기!
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //날짜형식으로 생성
		String strNow = sdf.format(now); //오늘을 날짜형식으로해서 strNow변수에담음 (strNow = 오늘날짜)
		
		//오늘 처음 방문시는 오늘 방문카운트(todayCnt)를 0으로 셋팅한다
		if(!vo.getLastDate().substring(0,10).equals(strNow)) { //vo에있는 최종(마지막)방문날짜를 0번째부터 10번째앞 자리까지 꺼낸거하고 strNow(오늘날짜)와 같지 않을경우 오늘처음방문했다는뜻
			//memberDAO.setTodayCntUpdate(vo.getMid()); //memberDAO에 mid를 가지고가서 오늘 방문카운트 0 으로셋팅처리한걸 저장
			vo.setTodayCnt(0); //첫방문이였을경우 오늘방문횟수를 0으로 세팅
		}
		int todayCnt = vo.getTodayCnt() + 1; 
		
		//재방문시
		int nowTodayPoint = 0;
		if(vo.getTodayCnt() >= 5) { //vo에있는 오늘 방문한횟수가 5보다 크거나 같으면
			nowTodayPoint = vo.getPoint(); //기존걸 그냥 업데이트(더이상 포인트늘어나지않게함)
		}
		else { //5보다 작을경우
			nowTodayPoint = vo.getPoint() + 10; //기존거에다가 +10하고 넘겨주자
		}
		//오늘 재방문이라면 '총방문수','오늘방문수','포인트' 누적처리
		memberDAO.setMemTotalUpdate(vo.getMid(), nowTodayPoint, todayCnt); //dao에 누적처리한걸 저장(아이디와, 위에서 받은포인트, 오늘 방문카운트)가지고감
			
	}

	@Override
	public int totRecCnt() {
		return memberDAO.totRecCnt();
	}

	@Override
	public ArrayList<MemberVO> getMemberList(int startIndexNo, int pageSize) {
		return memberDAO.getMemberList(startIndexNo,pageSize);
	}

	@Override
	public int totTermRecCnt(String mid) {
		return memberDAO.totTermRecCnt(mid);
	}

	@Override
	public ArrayList<MemberVO> getTermMemberList(int startIndexNo, int pageSize, String mid) {
		return memberDAO.getTermMemberList(startIndexNo,pageSize,mid);
	}
}
