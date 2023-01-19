package com.spring.javawspring.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.javawspring.dao.WebMessageDAO;
import com.spring.javawspring.vo.WebMessageVO;

@Service
public class WebMessageServiceImpl implements WebMessageService {

	@Autowired
	WebMessageDAO webMessageDAO;

	@Override
	public WebMessageVO getWmMessageOne(int idx, String mid, int mFlag) {
		return webMessageDAO.getWmMessageOne(idx, mid, mFlag);
	}

	@Override
	public List<WebMessageVO> getWmMessageList(String mid, int mSw, int startIndexNo, int pageSize) {
		return webMessageDAO.getWmMessageList(mid, mSw, startIndexNo, pageSize);
	}
}
