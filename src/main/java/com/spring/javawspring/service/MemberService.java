package com.spring.javawspring.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.spring.javawspring.vo.MemberVO;

public interface MemberService {

	public MemberVO getMemberIdCheck(String mid);

	public MemberVO getMemberNickNameCheck(String nickName);

	public int setMemberJoinOk(MultipartFile fName, MemberVO vo);

	public void setMemberVisitProcess(MemberVO vo);

//	public int totRecCnt();

	public List<MemberVO> getMemberList(int startIndexNo, int pageSize, String mid);

	public int totTermRecCnt(String mid);

	public ArrayList<MemberVO> getTermMemberList(int startIndexNo, int pageSize, String mid);

	public void setMemberPwdUpdate(String mid, String pwd);

	public String getMemberIdSearch(String name, String tel);

	public void setMemberDeleteOk(String mid);

	public int setMemberUpdateOk(MultipartFile fName, MemberVO vo);

	public int getMonthNewUser();

	public MemberVO getMemberNickNameEmailCheck(String nickName, String email);

	public void setKakaoMemberInputOk(String mid, String pwd, String nickName, String email);

	public void setMemberUserDelCheck(String mid);



}
