package com.spring.javawspring.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.spring.javawspring.dao.BoardDAO;
import com.spring.javawspring.vo.BoardReplyVO;
import com.spring.javawspring.vo.BoardVO;
import com.spring.javawspring.vo.GoodVO;

@Service
public class BoardServiceImpl implements BoardService {

	@Autowired
	BoardDAO boardDAO;


	@Override
	public List<BoardVO> getBoardList(int startIndexNo, int pageSize, String search, String searchString) {
		return boardDAO.getBoardList(startIndexNo, pageSize, search, searchString);
	}


	@Override
	public int setBoardInput(BoardVO vo) {
		return boardDAO.setBoardInput(vo);
	}


	@Override
	public BoardVO getBoardContent(int idx) {
		return  boardDAO.getBoardContent(idx) ;
	}


	@Override
	public void setBoardReadNum(int idx) {
		boardDAO.setBoardReadNum(idx);
	}
	
	@Override
	public void setBoardGoodPlus(int idx) {
		boardDAO.setBoardGoodPlus(idx);
	}

	@Override
	public void setGoodPlusMinus(int idx, int goodCnt) {
		boardDAO.setGoodPlusMinus(idx, goodCnt);
	}

	@Override
	public void boardGoodFlagCheck(int idx, int gFlag) {
		boardDAO.boardGoodFlagCheck(idx, gFlag);
	}

	@Override
	public GoodVO getBoardGoodCheck(int partIdx, String part, String mid) {
		return boardDAO.getBoardGoodCheck(partIdx, part, mid);
	}


	@Override
	public ArrayList<BoardVO> getPrevNext(int idx) {
		return boardDAO.getPrevNext(idx);
	}


	@Override
	public void imgCheck(String content) {
		//     0				 1				 2         3         4         5         6
		//     01234567890123456789012345678901234567890123456789012345678901234567890123456789
		//<img src="/javawspring/data/ckeditor/230111121331_cat5.jpg" style="height:4076px; width:2717px" />
		//content안에 그림파일이 존재할때만  작업을 수행할수있도록한다. (src = "/____~~ 가있을때가 그림파일존재함)
		if(content.indexOf("src=\"/") == -1) return; //사진파일이 있니없니,그림파일들의 공통적인특징 src="/ 부터시작할건데 src=" 이 없다면 그냥 나가버림
		
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		String uploadPath = request.getSession().getServletContext().getRealPath("/resources/data/ckeditor/");
	
		int position = 32;  	//실제 그림은 32번 인덱스 부터시작하니까 (다른곳에서도 써먹으려고 변수로받음)
		String nextImg = content.substring(content.indexOf("src=\"/") + position); //src위치 + position위치부터 나머지위치(32번째부터 그뒤전부다)를 다 가져올거임 => 그래야 그림만 뽑아서 가져오는거니까
		boolean sw = true;
		
		while(sw) {
			String imgFile = nextImg.substring(0, nextImg.indexOf("\"")); //변수 nextImg의 0번째부터 큰따옴표를 찾을때까지 가져와라  ex.230111121331_cat5.jpg
			
			String origFilePath = uploadPath + imgFile; //오리지널파일 경로의 위치설정
			String copyFilePath = uploadPath + "board/" + imgFile; //복사될경로의 위치설정
			
			fileCopyCheck(origFilePath, copyFilePath); //board폴더에 파일을 복사하고자 메소드만듦.
	
			if(nextImg.indexOf("src=\"/") == -1) { //nextImg에서 src="/가 없으면
				sw = false; //없으면 돌지말아야되니까
			}
			else { //nextImg에서 src="/가 또 있으면 다시 반복해서 그림을 받아와야함
				nextImg = nextImg.substring(nextImg.indexOf("src=\"/") + position);
			}
		
		}
	}


	//파일복사메소드
	private void fileCopyCheck(String origFilePath, String copyFilePath) {
		File origFile = new File(origFilePath); //원본파일
		File copyFile = new File(copyFilePath); //복사파일
		
		try {
			FileInputStream fis = new FileInputStream(origFile);
			FileOutputStream fos = new FileOutputStream(copyFile);
			
			byte[] buffer = new byte[2048]; //2k씩 넣을거임
			int cnt = 0;
			while((cnt = fis.read(buffer)) != -1) {
				fos.write(buffer, 0, cnt); //buffer에 있는0번째부터 있는대로 다가져가라
			}
			fos.flush(); //빠진거있으면 남은거까지 다보내줘
			fos.close(); //사용한거 닫아주기
			fis.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void setBoardDeleteOk(int idx) {
		boardDAO.setBoardDeleteOk(idx);
	}


	@Override
	public void imgDelete(String content) {
		//     0				 1				 2         3         4         5         6
		//     01234567890123456789012345678901234567890123456789012345678901234567890123456789
		//<img src="/javawspring/data/ckeditor/board/230111121331_cat5.jpg" style="height:4076px; width:2717px" />
		//content안에 그림파일이 존재할때만  작업을 수행할수있도록한다. (src = "/____~~ 가있을때가 그림파일존재함)
		if(content.indexOf("src=\"/") == -1) return; //사진파일이 있니없니,그림파일들의 공통적인특징 src="/ 부터시작할건데 src=" 이 없다면 그냥 나가버림
		
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		String uploadPath = request.getSession().getServletContext().getRealPath("/resources/data/ckeditor/board/");
	
		int position = 38;  	//실제 그림은 38번 인덱스 부터시작하니까 (다른곳에서도 써먹으려고 변수로받음)
		String nextImg = content.substring(content.indexOf("src=\"/") + position); //src위치 + position위치부터 나머지위치(32번째부터 그뒤전부다)를 다 가져올거임 => 그래야 그림만 뽑아서 가져오는거니까
		boolean sw = true;
		
		while(sw) {
			String imgFile = nextImg.substring(0, nextImg.indexOf("\"")); //그림파일명 꺼내오기 
			//변수 nextImg의 0번째부터 큰따옴표를 찾을때까지 가져와라  ex.230111121331_cat5.jpg
			
			String origFilePath = uploadPath + imgFile; //오리지널파일 경로의 위치설정
			
			fileDelete(origFilePath); //board폴더에 파일을 삭제하고자 메소드만듦.
	
			if(nextImg.indexOf("src=\"/") == -1) { //nextImg에서 src="/가 없으면
				sw = false; //없으면 돌지말아야되니까
			}
			else { //nextImg에서 src="/가 또 있으면 다시 반복해서 그림을 받아와야함
				nextImg = nextImg.substring(nextImg.indexOf("src=\"/") + position);
			}
		}
	}

	//그림파일삭제
	private void fileDelete(String origFilePath) {
		File delFile = new File(origFilePath);
		if(delFile.exists()) delFile.delete();
	}


	@Override
	public void imgCheckUpdate(String content) {
		//     0				 1				 2         3         4         5         6
		//     01234567890123456789012345678901234567890123456789012345678901234567890123456789
		//<img src="/javawspring/data/ckeditor/board/230111121331_cat5.jpg" style="height:4076px; width:2717px" /> 원본파일
		//<img src="/javawspring/data/ckeditor/230111121331_cat5.jpg" style="height:4076px; width:2717px" /> 복사한파일
		//content안에 그림파일이 존재할때만  작업을 수행할수있도록한다. (src = "/____~~ 가있을때가 그림파일존재함)
		if(content.indexOf("src=\"/") == -1) return; //사진파일이 있니없니,그림파일들의 공통적인특징 src="/ 부터시작할건데 src=" 이 없다면 그냥 나가버림
		
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		String uploadPath = request.getSession().getServletContext().getRealPath("/resources/data/ckeditor/board/");
	
		int position = 38;  	//원본파일 시작위치, 실제 그림은 32번 인덱스 부터시작하니까 (다른곳에서도 써먹으려고 변수로받음)
		String nextImg = content.substring(content.indexOf("src=\"/") + position); //src위치 + position위치부터 나머지위치(32번째부터 그뒤전부다)를 다 가져올거임 => 그래야 그림만 뽑아서 가져오는거니까
		boolean sw = true;
		
		while(sw) {
			String imgFile = nextImg.substring(0, nextImg.indexOf("\"")); //변수 nextImg의 0번째부터 큰따옴표를 찾을때까지 가져와라  ex.230111121331_cat5.jpg
			
			String origFilePath = uploadPath + imgFile; //오리지널파일 경로의 위치설정
			String copyFilePath = request.getSession().getServletContext().getRealPath("/resources/data/ckeditor/" +  imgFile) ;  
			
			fileCopyCheck(origFilePath, copyFilePath); //board폴더에 파일을 복사하고자 메소드만듦.
	
			if(nextImg.indexOf("src=\"/") == -1) { //nextImg에서 src="/가 없으면
				sw = false; //없으면 돌지말아야되니까
			}
			else { //nextImg에서 src="/가 또 있으면 다시 반복해서 그림을 받아와야함
				nextImg = nextImg.substring(nextImg.indexOf("src=\"/") + position);
			}
		
		}		
	}


	@Override
	public void setboardUpdateOk(BoardVO vo) {
		boardDAO.setboardUpdateOk(vo);
	}
	
	@Override
	public void setGoodDBInput(GoodVO goodVo) {
		boardDAO.setGoodDBInput(goodVo);
	}

	@Override
	public void setGoodDBDelete(int idx) {
		boardDAO.setGoodDBDelete(idx);
	}

	@Override
	public void setGoodUpdate(int idx, int item) {
		boardDAO.setGoodUpdate(idx, item);
	}


	@Override
	public void setBoardReplyInput(BoardReplyVO replyVo) {
		boardDAO.setBoardReplyInput(replyVo);
	}


	@Override
	public List<BoardReplyVO> getBoardReply(int idx) {
		return boardDAO.getBoardReply(idx);
	}


	@Override
	public void setBoardReplyDeleteOk(int idx) {
		boardDAO.setBoardReplyDeleteOk(idx);
	}


	@Override
	public String getMaxLevelOrder(int boardIdx) {
		return boardDAO.getMaxLevelOrder(boardIdx);
	}


	@Override
	public void setLevelOrderPlusUpdate(BoardReplyVO replyVo) {
		boardDAO.setLevelOrderPlusUpdate(replyVo);
	}


	@Override
	public void setBoardReplyInput2(BoardReplyVO replyVo) {
		boardDAO.setBoardReplyInput2(replyVo);
	}


	@Override
	public int getNewContent() {
		return boardDAO.getNewContent();
	}
	
	
}
