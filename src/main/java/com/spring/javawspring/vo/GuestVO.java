package com.spring.javawspring.vo;

import lombok.Data;

@Data //롬복
public class GuestVO {
	private int idx;
	private String name;
	private String email;
	private String homePage;
	private String visitDate;
	private String hostIp;
	private String content;
}
