package com.spring.javawspring.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.javawspring.dao.GuestDAO;
import com.spring.javawspring.vo.GuestVO;

//구현객체 (얘도 실질적으로 일하진않고 DAO에 일을시킴)
@Service
public class GuestServiceImpl implements GuestService{

	@Autowired  //감시하다
	GuestDAO guestDAO; //DAO를 감시하다

	@Override
	public ArrayList<GuestVO> getGuestList(int startIndexNo, int pageSize) {
		return guestDAO.getGuestList(startIndexNo,pageSize);
	}

	@Override
	public void setGuestInput(GuestVO vo) {
		guestDAO.setGuestInput(vo);
	}

	@Override
	public int totRecCnt() {
		return guestDAO.totRecCnt();
	}

	//setGuestDelete(int idx)그대로 복사후 타입(int) 지우기
	@Override
	public void setGuestDelete(int idx) { 
		guestDAO.setGuestDelete(idx);  
	}
}
