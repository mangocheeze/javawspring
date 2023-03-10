package com.spring.javawspring.vo;

import lombok.Data;

@Data
public class BoardVO {
	private int idx;
	private String nickName;
	private String title;
	private String email;
	private String homePage;
	private String content;
	private String wDate;
	private String hostIp;
	private int readNum;
	private int good;
	private String mid;
	
	private int day_diff; //날짜 차이 계산 필드(1일차이 계산필드) /DB에 설계되어있진않지만 프로그램에 필요한 필드
	private int hour_diff; //시간 차이 계산필드 (24시간차이 계산필드)
	
	//이전글 / 다음글을 위한 변수 설정
	private int preIdx;
	private int nextIdx;
	private String preTitle;
	private String nextTitle;
	
	//댓글의 갯수를 저장하기 위한 변수
	private int  replyCount;

}