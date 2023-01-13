package com.spring.javawspring.pagination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.javawspring.dao.BoardDAO;
import com.spring.javawspring.dao.GuestDAO;
import com.spring.javawspring.dao.MemberDAO;

@Service //서비스객체라는뜻으로 걸어줌
public class PageProcess {
	//앞으로 페이지네이션 필요한것들은 여기서 Autowired걸어줌?
	
	@Autowired
	GuestDAO guestDAO;
	
	@Autowired
	MemberDAO memberDAO;

	@Autowired
	BoardDAO boardDAO;
	
	public PageVO totRecCnt(int pag, int pageSize, String section, String part, String searchString) { //section은 board,member등등 ,매개변수 받는건 마음대로줘도됨, 아이디개별검색을하게되면 searchString자리에 아이디가 들어옴,part는 나중에써먹으려고써놓음
		PageVO pageVO = new PageVO();
		
		int totRecCnt = 0;
		
		if(section.equals("member")) { //section이 member일때 
			totRecCnt = memberDAO.totRecCnt(searchString); //전체자료건수를 가져옴
		}
		else if(section.equals("guest")) { //이렇게써주면 guest에도 페이지네이션 가져다 쓰는거임
			totRecCnt = guestDAO.totRecCnt();
		}
		else if(section.equals("board")) { //위에 @Autowired 걸기
			totRecCnt = boardDAO.totRecCnt(part, searchString);
		}
		
		
		//여기서 구할거임
		int totPage = (totRecCnt % pageSize) == 0 ? totRecCnt  / pageSize : (totRecCnt/ pageSize) +1; 	//4.총 페이지 건수를 구한다 
		int startIndexNo = (pag - 1) * pageSize;//5.현재페이지의 시작 인덱스 번호를 구한다
		int curScrStartNo = totRecCnt - startIndexNo; //6.현재 화면에 보여주는 시작 번호를 구한다
		
		int blockSize = 3; //1.블록의 크기를 결정한다(여기선 3으로지정)
		int curBlock = (pag-1) / blockSize; //2.현재페이지가 위치하고있는 블록 번호를 구한다(예.1~3페이지는 0블록, 4~6페이지는1블록, 7~9페이지는 2블록)
		int lastBlock = (totPage - 1) / blockSize; // 3. 마지막블록을 구한다

		pageVO.setPag(pag);
		pageVO.setPageSize(pageSize);
		pageVO.setTotRecCnt(totRecCnt);
		pageVO.setTotPage(totPage);
		pageVO.setStartIndexNo(startIndexNo);
		pageVO.setCurScrStartNo(curScrStartNo);
		pageVO.setBlockSize(blockSize);
		pageVO.setCurBlock(curBlock);
		pageVO.setLastBlock(lastBlock);
		
		return pageVO;
	}
	
}
