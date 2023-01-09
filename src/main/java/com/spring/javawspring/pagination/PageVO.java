package com.spring.javawspring.pagination;

import lombok.Data;

@Data
public class PageVO {
	//페이지 네이션에만 필요한 변수들 , 추가로필요한건 아래다가 더 넣어주면됨
	private int pag;
	private int pageSize;
	private int totRecCnt;
	private int totPage;
	private int startIndexNo;
	private int curScrStartNo;
	private int blockSize;
	private int curBlock;
	private int lastBlock;
	
	private String part;
	
}
