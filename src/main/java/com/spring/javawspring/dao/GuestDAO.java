package com.spring.javawspring.dao;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Param;

import com.spring.javawspring.vo.GuestVO;
//DAO는 Mapper한테 일을 시킴
public interface GuestDAO {

	public ArrayList<GuestVO> getGuestList(@Param("startIndexNo") int startIndexNo, @Param("pageSize") int pageSize); //public적어주기, startIndexNo는 startIndexNo로 변수줄거임?? 

	public void setGuestInput(@Param("vo") GuestVO vo); //mapper에 일을시켜야하는 mapper에 변수를 만들어서 보내줘야 하니까 @Param으로 변수를 만들어서 Mapper로 보내줘야함(#{}에 변수를 쓰기위해?)

	public int totRecCnt();

	public void setGuestDelete(@Param("idx") int idx);

}
