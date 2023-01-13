package com.spring.javawspring.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.spring.javawspring.vo.MemberVO;

public interface MemberDAO {

	public MemberVO getMemberIdCheck(@Param("mid") String mid);
	
	public MemberVO getMemberNickNameCheck(@Param("nickName") String nickName);

	public int setMemberJoinOk(@Param("vo") MemberVO vo);

	public void setMemTotalUpdate(@Param("mid") String mid,@Param("nowTodayPoint") int nowTodayPoint,@Param("todayCnt") int todayCnt);

	public int totRecCnt(@Param("mid") String mid);

	public List<MemberVO> getMemberList(@Param("startIndexNo") int startIndexNo, @Param("pageSize") int pageSize ,@Param("mid") String mid);

	public int totTermRecCnt(@Param("mid") String mid);

	public ArrayList<MemberVO> getTermMemberList(@Param("startIndexNo") int startIndexNo,@Param("pageSize") int pageSize,@Param("mid") String mid);
	//실무에선 ArrayList대신 List로 대부분씀.

	public void setMemberPwdUpdate(@Param("mid")String mid,@Param("pwd") String pwd);

	public String getMemberIdSearch(@Param("name") String name,@Param("tel") String tel);

	public void setMemberDeleteOk(@Param("mid") String mid);

	public void setMemberUpdateOk(@Param("vo") MemberVO vo);

	public int getMonthNewUser();

	

}
