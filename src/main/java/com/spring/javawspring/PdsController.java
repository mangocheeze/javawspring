package com.spring.javawspring;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.spring.javawspring.common.SecurityUtil;
import com.spring.javawspring.pagination.PageProcess;
import com.spring.javawspring.pagination.PageVO;
import com.spring.javawspring.service.PdsService;
import com.spring.javawspring.vo.PdsVO;

@Controller
@RequestMapping("/pds") //중개경로
public class PdsController {

	@Autowired
	PdsService pdsService; //인터페이스

	@Autowired
	PageProcess pageProcess;
	
	
	@RequestMapping(value = "/pdsList", method = RequestMethod.GET)
	public String pdsListGet(Model model,
			@RequestParam(name="pag", defaultValue = "1", required = false) int pag,
			@RequestParam(name="pageSize", defaultValue = "5", required = false) int pageSize,
			@RequestParam(name="part", defaultValue = "전체", required = false) String part ) {
		
		PageVO pageVo = pageProcess.totRecCnt(pag, pageSize, "pds", part, "");
		List<PdsVO> vos = pdsService.getPdsList(pageVo.getStartIndexNo(), pageVo.getPageSize(), part); //리스트로 받아서 뿌려줄거임
		pageVo.setPart(part);
		
		model.addAttribute("vos", vos);
		model.addAttribute("pageVo", pageVo);
		
		return "pds/pdsList";
	}
	
	
	@RequestMapping(value="/pdsInput", method = RequestMethod.GET)
	public String pdsInputGet()	{
		
		return "pds/pdsInput";
	}
	
	@SuppressWarnings("static-access")
	@RequestMapping(value="/pdsInput", method = RequestMethod.POST)
	public String pdsInputPost(PdsVO vo,
			MultipartHttpServletRequest file)	{
		String pwd = vo.getPwd();
		SecurityUtil security = new SecurityUtil();
		pwd = security.encryptSHA256(pwd);
		vo.setPwd(pwd);
		
		//멀티파일을 서버에 저장시키고,파일의 정보를 vo에담아서 DB에 저장시킨다
		pdsService.setPdsInput(file, vo);
		
		return "redirect:/msg/pdsInputOk";
	}
	
	//다운로드(파일이름 누르면 다운)시 다운횟수 증가
	@ResponseBody
	@RequestMapping(value="/pdsDownNum", method = RequestMethod.POST)
	public String pdsDownNumPost(int idx)	{
		pdsService.setPdsDownNum(idx);
		return "pds/pdsInput";
	}
	
	//PDS 내용 삭제처리하기 (삭제처리하기전에 비밀번호를 먼저 체크하여 맞으면 삭제처리한다)
	@ResponseBody
	@RequestMapping(value="/pdsDelete", method = RequestMethod.POST)
	public String pdsDeletePost(int idx,String pwd)	{
		//SecurityUtil security = new SecurityUtil();  //SecurityUtil를 static로 선언하였기에 메소드 영역에 올라가있다 .이때는 클래스명ㅇ으로 호출해서 사용한다.
		pwd = SecurityUtil.encryptSHA256(pwd); //암호화시킨다음 변수 pwd에담음
		
		PdsVO vo = pdsService.getPdsContent(idx);
		if(!pwd.equals(vo.getPwd())) return "0";
		
		//비밀번호가 맞으면 파일 삭제후 DB의 내역을 삭제처리한다,.
		pdsService.setPdsDelete(vo); //앞에서 자료를 vo에 담아서 와서 vo를 넘겨야함
		
		return "1";
	}
	
	
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/pdsTotalDown", method = RequestMethod.GET)
	public String pdsTotalDownGet(HttpServletRequest request, int idx) throws IOException {
		//서비스객체에서 해도되는걸 컨트롤러에서 해보기
		//파일 압축다운로드전에 다운로드수를 증가시킨다.
		pdsService.setPdsDownNum(idx);
		
		// 여러개의 파일일 경우 모든 파일을 하나의 파일로 압축(=통합)하여 다운한다. 이때 압축파일명은 '제목'으로 처리한다.
		String realPath = request.getSession().getServletContext().getRealPath("/resources/data/pds/");
		
		PdsVO vo = pdsService.getPdsContent(idx);
		
		String[] fNames = vo.getFName().split("/");
		String[] fSNames = vo.getFSName().split("/");
		
		String zipPath = realPath + "temp/"; //파일명
		String zipName = vo.getTitle() + ".zip"; //확장자
		
		FileInputStream fis = null;
		FileOutputStream fos = null;
		
		ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(zipPath + zipName)); //fos는 빈껍데기파일로 생성해서 넣어야함 ,zip파일 박스(껍데기)
		
		byte[] buffer = new byte[2048]; //새로생성해서 버퍼에다가 조금씩 담아서 넘겨야함
		
		//위에서 생성안하고 선언만한이유
		for(int i=0; i<fSNames.length; i++) {
			//껍데기 만들기
			fis = new FileInputStream(realPath + fSNames[i]);
			fos = new FileOutputStream(zipPath + fNames[i]); //temp파일에는 올릴때 이름으로저장해야함
			File moveAndRename= new File(zipPath + fNames[i]); //zip안에 저장할 파일이름. 파일을 옮겨서 이름바꿔서 저장
			
			//fos에 파일 쓰기작업
			int data = 0;
			while((data = fis.read(buffer, 0 , buffer.length)) != -1) { //fis를 읽어서 data변수에담음( buffer에있는걸 0번째부터 길이만큼) 읽은게 -1이 아닐때까지 반복(있을때까지) 
				fos.write(buffer, 0 , data); //0에서부터 data까지 가져와라
			}
			fos.flush();
			fos.close();
			fis.close();
			
			//zip파일에 fos를 넣는 작업
			fis = new FileInputStream(moveAndRename); //fis를 다시생성해줌 ( zip안에 저장할 파일이름)
			zout.putNextEntry(new ZipEntry(fNames[i])); //zip파일안에서 파일을 묶어주기위해서 껍데기를 만들어줌
			
			//temp에 있는 파일을 바로 위에 만든 껍데기에 넣어줌
			while((data = fis.read(buffer, 0 , buffer.length)) != -1) { //fis를 읽어서 data변수에담음( buffer에있는걸 0번째부터 길이만큼) 읽은게 -1이 아닐때까지 반복(있을때까지) 
				zout.write(buffer, 0 , data); //0에서부터 data까지 가져와라
			}
			zout.flush();
			zout.closeEntry();
			//zout.close(); //아직 완전히 닫으면안됨
			fis.close();
		}
		zout.close();
		
		return "redirect:/pds/pdsDownAction?file="+ java.net.URLEncoder.encode(zipName); //zipName 경로타고가니까 무조건 한글깨짐, 인코딩해줘야함
	}
	
	//경로지정해서 불러들이는 메소드 생성
	@RequestMapping(value = "/pdsDownAction", method = RequestMethod.GET)
	public void pdsDownActhodGet(HttpServletRequest request, HttpServletResponse response) throws IOException { //response는 응답객체(클라이언트 요구를했으면 응답하는게필요하니까 여기에 필요)
		String file = request.getParameter("file"); //위에 return에서 넘긴 file을 받음
		
		String downPathFile = request.getSession().getServletContext().getRealPath("/resources/data/pds/temp/") + file; //변수이름 realPath로 주면헷갈리니까 downPathFile로함 ,downPath에 파일명까지 붙임
		
		File downFile = new File(downPathFile); 
		
		String downFileName = new String(file.getBytes("UTF-8"), "8859_1"); 
		
		response.setHeader("Content-Disposition", "attachment;filename=" + downFileName); //다 예약어임
		
		FileInputStream fis = new FileInputStream(downFile); //서버에있는 zip파일
		
		ServletOutputStream sos = response.getOutputStream(); //servlet은 무조건 응답쪽임
		
		//알맹이 채움
		byte[] buffer = new byte[2048];
		
		int data = 0; //일반변수는 초기값주는게 남
		while((data = fis.read(buffer, 0 , buffer.length)) != -1) { //fis를 읽어서 data변수에담음( buffer에있는걸 0번째부터 길이만큼) 읽은게 -1이 아닐때까지 반복(있을때까지) 
			sos.write(buffer, 0 , data); //0에서부터 data까지 가져와라
		}
		sos.flush();
		sos.close();
		fis.close();
		
		//다운로드완료후 temp폴더의 파일들을 모두 삭제한다 (숙제)
		//new File(downPathFile).delete(); //zip파일 생성후 삭제
		downFile.delete(); //zip파일 삭제
		
	}
}
