package com.spring.javawspring.service;

import java.util.List;

import com.spring.javawspring.vo.WebMessageVO;

public interface WebMessageService {

	public WebMessageVO getWmMessageOne(int idx, String mid, int mFlag);

	public List<WebMessageVO> getWmMessageList(String mid, int mSw, int startIndexNo, int pageSize);

}
