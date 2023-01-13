package com.spring.javawspring.common;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

public class JavawspringProvide {

	public int fileUpload(MultipartFile fName) {
		int res = 0;
		try { 
			UUID uid = UUID.randomUUID();
			String oFileName =  fName.getOriginalFilename(); //원래 파일이름(올렸을때 파일이름)
			String saveFileName = uid + "_" + oFileName; //저장되는 파일이름 (_구분자로 구분함)
			
			//다른곳에서도 쓴다고가정
			writeFile(fName, saveFileName,""); //아래서 만듦
			res = 1; //정상적이면 res 1
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return res;
	}

	
	public void writeFile(MultipartFile fName, String saveFileName, String flag) throws IOException { //떠넘겨버림
		byte[] data = fName.getBytes(); //위에서 예외처리 해줬으니 위에로 떠넘겨버림
		
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest(); //Httpservlet에서는 request가 요청하는거라
		//request.getRealPath("/resources/pds/temp/");
		String realPath = request.getSession().getServletContext().getRealPath("/resources/"+flag+"/"); //둘중하나로 하면됨
		
		FileOutputStream fos = new FileOutputStream(realPath + saveFileName); //절대경로저장위치 + 실제저장되는 파일이름
		fos.write(data);
		fos.close();
	}
}
