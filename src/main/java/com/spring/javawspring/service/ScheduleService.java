package com.spring.javawspring.service;

import java.util.List;

import com.spring.javawspring.vo.ScheduleVO;

public interface ScheduleService {

	public void getSchedule();

	public List<ScheduleVO> getScheduleMenu(String mid, String ymd);

	public void getScheduleInputOk(ScheduleVO vo);

	public void getScheduleUpdateOk(ScheduleVO vo);

	public void getScheduleDeleteOk(int idx);

}
