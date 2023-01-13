package com.spring.javawspring;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@RequestMapping(value = {"/","/h"}, method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "home";
	}
	
	@RequestMapping("/imageUpload")
	public void imageUploadGet(MultipartFile upload,
			HttpServletRequest request,
			HttpServletResponse response) throws IOException { //upload란 변수로 그림의속성받아옴, 이렇게 3개를 써줘야함
		/*파일 업로드*/
		response.setCharacterEncoding("utf-8"); //해도되고 안해도됨
		response.setContentType("text/html; charset=utf-8");
		
		String oFileName = upload.getOriginalFilename();
		
		//같은이름의 파일도 올라가니 uuid로이름바꿔주거나, 날짜로 바꿔주기(오늘은 날짜로)
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss"); //년월일 시분초까지도 다 올려줘야함 (편집)
		oFileName = sdf.format(date) + "_" + oFileName; //오늘날짜가 형식이 위 형태 'yyMMddHHmmss_업로드시파일명 '로 바뀐걸 변수에 담음
	
		byte[] bytes = upload.getBytes();  //getBytes():byte단위를 byte배열로 바꿔주는 메소드
		
		// ckeditor에서 올린(전송한)파일을 , 서버파일시스템에 실제로 저장할 경로를 결정한다.
		String realPath = request.getSession().getServletContext().getRealPath("/resources/data/ckeditor/");//서버에 올라가는 실제경로 , 여기서 루트는 webapp ,마지막/써주기
		//넘어온 파일을 넣어준다
		OutputStream os = new FileOutputStream(new File(realPath + oFileName)); //두줄로쓰는걸 한줄로 바꿔씀 , 서버에 저장할때는 OutputStream 서버에있는걸 읽을때는 InputStream
		os.write(bytes);
		
		/* 서버 파일시스템에 저장되어 있는 파일을 브라우저 편집 화면에 보여주기 위한 작업(글쓸때 화면)*/
		PrintWriter out = response.getWriter();
		String fileUrl = request.getContextPath() + "/data/ckeditor/" + oFileName; //resources폴더 -> data폴더 -> ckeditor폴더 ->파일이름 , 얘는 무조건 resources의 ckeditor에있는 업로드시 파일명으로 읽어올수있음
		out.println("{\"originalFilename\":\""+oFileName+"\" , \"uploaded\":1 , \"url\":\""+fileUrl+"\"}"); //uploaded ,url은 예약어
		
		out.flush(); //빠진거있으면 남은거까지 다보내줘
		os.close(); //사용한 객체 닫아줌
		
	}
}
