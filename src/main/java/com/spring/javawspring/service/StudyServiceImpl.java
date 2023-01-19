package com.spring.javawspring.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.spring.javawspring.common.DistanceCal;
import com.spring.javawspring.dao.StudyDAO;
import com.spring.javawspring.vo.GuestVO;
import com.spring.javawspring.vo.KakaoAddressVO;
import com.spring.javawspring.vo.QrCodeVO;

@Service
public class StudyServiceImpl implements StudyService {
	
	@Autowired
	StudyDAO studyDAO;

	@Override
	public String[] getCityStringArr(String dodo) {
		String[] strArr = new String[100];
		
		if(dodo.equals("서울")) {
			strArr[0] = "강남구";
			strArr[1] = "서초구";
			strArr[2] = "동대문구";
			strArr[3] = "성북구";
			strArr[4] = "마포구";
			strArr[5] = "강동구";
			strArr[6] = "관악구";
			strArr[7] = "광진구";
			strArr[8] = "중구";
			strArr[9] = "서구";
		}
		else if(dodo.equals("경기")) {
			strArr[0] = "수원시";
			strArr[1] = "이천시";
			strArr[2] = "일산시";
			strArr[3] = "용인시";
			strArr[4] = "시흥시";
			strArr[5] = "광명시";
			strArr[6] = "광주시";
			strArr[7] = "의정부시";
			strArr[8] = "평택시";
			strArr[9] = "안성시";
		}
		else if(dodo.equals("충북")) {
			strArr[0] = "청주시";
			strArr[1] = "충주시";
			strArr[2] = "괴산군";
			strArr[3] = "진천군";
			strArr[4] = "제천시";
			strArr[5] = "음성군";
			strArr[6] = "옥천군";
			strArr[7] = "영동군";
			strArr[8] = "단양군";
			strArr[9] = "증평군";
		}
		else if(dodo.equals("충남")) {
			strArr[0] = "천안시";
			strArr[1] = "병천시";
			strArr[2] = "옥산군";
			strArr[3] = "아산시";
			strArr[4] = "공주시";
			strArr[5] = "당진군";
			strArr[6] = "보령시";
			strArr[7] = "계룡시";
			strArr[8] = "논산시";
			strArr[9] = "예산군";
		}
		return strArr;
	}

	@Override
	public ArrayList<String> getCityArrayListArr(String dodo) {
		ArrayList<String> vos = new ArrayList<String>();
		
		if(dodo.equals("서울")) {
			vos.add("강남구");		// vos.add("강남구");
			vos.add("서초구");
			vos.add("동대문구");
			vos.add("성북구");
			vos.add("마포구");
			vos.add("강동구");
			vos.add("관악구");
			vos.add("광진구");
			vos.add("중구");
			vos.add("서구");
		}
		else if(dodo.equals("경기")) {
			vos.add("수원시");
			vos.add("이천시");
			vos.add("일산시");
			vos.add("용인시");
			vos.add("시흥시");
			vos.add("광명시");
			vos.add("광주시");
			vos.add("의정부시");
			vos.add("평택시");
			vos.add("안성시");
		}
		else if(dodo.equals("충북")) {
			vos.add("청주시");
			vos.add("충주시");
			vos.add("괴산군");
			vos.add("진천군");
			vos.add("제천시");
			vos.add("음성군");
			vos.add("옥천군");
			vos.add("영동군");
			vos.add("단양군");
			vos.add("증평군");
		}
		else if(dodo.equals("충남")) {
			vos.add("천안시");
			vos.add("병천시");
			vos.add("옥산군");
			vos.add("아산시");
			vos.add("공주시");
			vos.add("당진군");
			vos.add("보령시");
			vos.add("계룡시");
			vos.add("논산시");
			vos.add("예산군");
		}
		
		return vos;
	}

	@Override
	public GuestVO getGuestMid(String mid) {
		return studyDAO.getGuestMid(mid);
	}

	@Override
	public ArrayList<GuestVO> getGuestNames(String mid) {
		return studyDAO.getGuestNames(mid);
	}

	@Override
	public int fileUpload(MultipartFile fName) {
		int res = 0;
		try {  //예외처리왜해주지???????
			UUID uid = UUID.randomUUID();
			String oFileName =  fName.getOriginalFilename(); //원래 파일이름(올렸을때 파일이름)
			String saveFileName = uid + "_" + oFileName; //저장되는 파일이름 (_구분자로 구분함)
			
			//다른곳에서도 쓴다고가정
			writeFile(fName, saveFileName); //아래서 만듦
			res = 1; //정상적이면 res 1
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return res;
	}

	
	public void writeFile(MultipartFile fName, String saveFileName) throws IOException { //떠넘겨버림
		byte[] data = fName.getBytes(); //위에서 예외처리 해줬으니 위에로 떠넘겨버림
		
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest(); //Httpservlet에서는 request가 요청하는거라
		
		String realPath = request.getSession().getServletContext().getRealPath("/resources/pds/temp/"); 
		
		FileOutputStream fos = new FileOutputStream(realPath + saveFileName); //절대경로저장위치 + 실제저장되는 파일이름
		fos.write(data);
		fos.close();
	}

	//달력내역
	@Override
	public void getCalendar() {
	  // model객체를 사용하게되면 불필요한 메소드가 많이 따라오기에 여기서는 request객체를 사용했다.
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest(); //request객체로 불러오려면 써줘야함
		
		// 오늘 날짜 저장시켜둔다.(calToday변수, 년(toYear), 월(toMonth), 일(toDay))
		Calendar calToday = Calendar.getInstance();
		int toYear = calToday.get(Calendar.YEAR);
		int toMonth = calToday.get(Calendar.MONTH);
		int toDay = calToday.get(Calendar.DATE);
				
		// 화면에 보여줄 해당 '년(yy)/월(mm)'을 셋팅하는 부분(처음에는 오늘 년도와 월을 가져오지만, '이전/다음'버튼 클릭하면 해당 년과 월을 가져오도록 한다.
		Calendar calView = Calendar.getInstance();
		int yy = request.getParameter("yy")==null ? calView.get(Calendar.YEAR) : Integer.parseInt(request.getParameter("yy")); //request직접써서 이렇게 null값처리가능함
	  int mm = request.getParameter("mm")==null ? calView.get(Calendar.MONTH) : Integer.parseInt(request.getParameter("mm"));
	  
	  if(mm < 0) { // 1월에서 전월 버튼을 클릭시에 실행
	  	yy--;
	  	mm = 11;
	  }
	  if(mm > 11) { // 12월에서 다음월 버튼을 클릭시에 실행
	  	yy++;
	  	mm = 0;
	  }
	  calView.set(yy, mm, 1);		// 현재 '년/월'의 1일을 달력의 날짜로 셋팅한다.
	  
	  int startWeek = calView.get(Calendar.DAY_OF_WEEK);  						// 해당 '년/월'의 1일에 해당하는 요일값을 숫자로 가져온다.
	  int lastDay = calView.getActualMaximum(Calendar.DAY_OF_MONTH);  // 해당월의 마지막일자(getActualMaxximum메소드사용-이 메소드 덕분에 월마다 마지막날짜를 일일히구하지않아도됨)를 구한다.
	  
	  // 화면에 보여줄 년월기준 전년도/다음년도를 구하기 위한 부분
	  int prevYear = yy;  			// 전년도
	  int prevMonth = (mm) - 1; // 이전월
	  int nextYear = yy;  			// 다음년도
	  int nextMonth = (mm) + 1; // 다음월
	  
	  if(prevMonth == -1) {  // 1월에서 전월 버튼을 클릭시에 실행..
	  	prevYear--;
	  	prevMonth = 11;
	  }
	  
	  if(nextMonth == 12) {  // 12월에서 다음월 버튼을 클릭시에 실행..
	  	nextYear++;
	  	nextMonth = 0;
	  }
	  
	  // 현재달력에서 앞쪽의 빈공간은 '이전달력'을 보여주고, 뒷쪽의 남은공간은 '다음달력'을 보여주기위한 처리부분(아래 6줄)
	  Calendar calPre = Calendar.getInstance(); // 이전달력
	  calPre.set(prevYear, prevMonth, 1);  			// 이전 달력 셋팅
	  int preLastDay = calPre.getActualMaximum(Calendar.DAY_OF_MONTH);  // 해당월의 마지막일자를 구한다.
	  
	  Calendar calNext = Calendar.getInstance();// 다음달력
	  calNext.set(nextYear, nextMonth, 1);  		// 다음 달력 셋팅
	  int nextStartWeek = calNext.get(Calendar.DAY_OF_WEEK);  // 다음달의 1일에 해당하는 요일값을 가져온다.
	  
	  /* ---------  아래는  앞에서 처리된 값들을 모두 request객체에 담는다.  -----------------  */
	  
	  // 오늘기준 달력...
	  request.setAttribute("toYear", toYear);
	  request.setAttribute("toMonth", toMonth);
	  request.setAttribute("toDay", toDay);
	  
	  // 화면에 보여줄 해당 달력...
	  request.setAttribute("yy", yy);
	  request.setAttribute("mm", mm);
	  request.setAttribute("startWeek", startWeek);
	  request.setAttribute("lastDay", lastDay);
	  
	  // 화면에 보여줄 해당 달력 기준의 전년도, 전월, 다음년도, 다음월 ...
	  request.setAttribute("preYear", prevYear);
		request.setAttribute("preMonth", prevMonth);
		request.setAttribute("nextYear", nextYear);
		request.setAttribute("nextMonth", nextMonth);
		
		// 현재 달력의 '앞/뒤' 빈공간을 채울, 이전달의 뒷부분과 다음달의 앞부분을 보여주기위해 넘겨주는 변수
		request.setAttribute("preLastDay", preLastDay);				// 이전달의 마지막일자를 기억하고 있는 변수
		request.setAttribute("nextStartWeek", nextStartWeek);	// 다음달의 1일에 해당하는 요일을 기억하고있는 변수
	}

	/*
	//QR코드 만들기
	@Override
	public String qrCreate(String mid, String moveFlag, String realPath) {
		String qrCodeName = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss"); //인터넷에선 1초도긴데 그 사이에 같은게 들어올수도있으니까 uuid로 랜덤이름만들기
		UUID uid = UUID.randomUUID(); //32글자가 만들어지는데 그중 앞에 2개만꺼내쓸거임
		String strUid = uid.toString().substring(0,2);
		
		qrCodeName = sdf.format(new Date()) + "_" + mid + "_" + moveFlag + "_" + strUid; //여기서 바로 날짜 생성 (yyyyMMddhhmmss형식으로만들어짐)
		
		//생성하기위한 예외처리
		try {
			File file = new File(realPath); //껍데기 먼저만들기 , realPath는 폴더임
			if(!file.exists()) file.mkdirs();//exists() : 있냐없냐(존재하냐) ,true면 존재한다 ,.mkdirs() :명령어
			
			String codeFlag = new String(moveFlag.getBytes("UTF-8"), "ISO-8859-1"); 
			
			//qr코드 만들기
			int qrCodecolor = 0xFF000000;//QR코드 전경색(글자색-검정색) 16진수에 문자코드색깔을 넣겠다, 글자색깔
			int qrCodeBackcolor = 0xFFFFFFFF; //배경색깔 (흰색)
			
			QRCodeWriter qrCodeWriter = new QRCodeWriter(); //QR코드 객체생성
			//BitMatrix bitMatrix = qrCodeWriter.encode(codeFlag, BarcodeFormat.QR_CODE, qrCodecolor, qrCodeBackcolor);
			BitMatrix bitMatrix = qrCodeWriter.encode(codeFlag, BarcodeFormat.QR_CODE, 200, 200);
			
			MatrixToImageConfig matrixToImageConfig = new MatrixToImageConfig(qrCodecolor, qrCodeBackcolor);
			BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix, matrixToImageConfig);
			
			ImageIO.write(bufferedImage, "png", new File(realPath + qrCodeName + ".png"));
			
		} catch (IOException e) {
			e.printStackTrace();
			
		} catch (WriterException e) {
			e.printStackTrace();
		}
		return qrCodeName;
	}
	*/
	
	//QR코드 만들기
	@Override
	public String qrCreate(String mid, String moveFlag, String realPath) {
		String qrCodeName = "";
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss"); //인터넷에선 1초도긴데 그 사이에 같은게 들어올수도있으니까 uuid로 랜덤이름만들기
		UUID uid = UUID.randomUUID(); //32글자가 만들어지는데 그중 앞에 2개만꺼내쓸거임
		//String strUid = uid.toString().substring(0,2);
		String strUid = uid.toString().substring(0,8);
		
		qrCodeName = sdf.format(new Date()) + "_" + mid + "_" + moveFlag + "_" + strUid; //QR코드파일이름. 여기서 바로 날짜 생성 (yyyyMMddhhmmss형식으로만들어짐)
		
		//생성하기위한 예외처리
		try {
			File file = new File(realPath); //껍데기 먼저만들기 , realPath는 폴더임
			if(!file.exists()) file.mkdirs(); //exists() : 있냐없냐(존재하냐) ,true면 존재한다 ,.mkdirs() :명령어
			
			String codeFlag = new String(moveFlag.getBytes("UTF-8"), "ISO-8859-1")+ "_" + strUid; //핸드폰으로 QR코드찍었을때 뜨는이름
			
			//qr코드 만들기
			int qrCodecolor = 0xFF000000;//QR코드 전경색(글자색-검정색) 16진수에 문자코드색깔을 넣겠다, 글자색깔
			int qrCodeBackcolor = 0xFFFFFFFF; //배경색깔 (흰색)
			
			QRCodeWriter qrCodeWriter = new QRCodeWriter(); //QR코드 객체생성
			//BitMatrix bitMatrix = qrCodeWriter.encode(codeFlag, BarcodeFormat.QR_CODE, qrCodecolor, qrCodeBackcolor);
			BitMatrix bitMatrix = qrCodeWriter.encode(codeFlag, BarcodeFormat.QR_CODE, 200, 200); //codeFlag로 QR코드모양을 만듦
			
			//전경색(글자색) , 배경색 설정
			MatrixToImageConfig matrixToImageConfig = new MatrixToImageConfig(qrCodecolor, qrCodeBackcolor); //생성하면서 전경색(글자색)과 배경색을 같이넣겠다
			BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix, matrixToImageConfig); //설계된것 과 색상을 집어넣음
			
			ImageIO.write(bufferedImage, "png", new File(realPath + qrCodeName + ".png")); //변한된 이미지 객체는 write메소드에 의해서 지정된 형식의 이미지파일로 출력된다
			
		} catch (IOException e) {
			e.printStackTrace();
			
		} catch (WriterException e) {
			e.printStackTrace();
		}
		
		return qrCodeName;
	}

	//등록
	@Override
	public void setQrInput(QrCodeVO vo) {
		studyDAO.setQrInput(vo);
	}

	//조회
	@Override
	public QrCodeVO getQrSearch(String qrIdx) {
		return studyDAO.getQrSearch(qrIdx);
	}

	//카카오맵
	@Override
	public KakaoAddressVO getKakaoAddressName(String address) {
		return studyDAO.getKakaoAddressName(address);
	}

	@Override
	public void setKakaoAddressName(KakaoAddressVO vo) {
		studyDAO.setKakaoAddressName(vo);
	}

	@Override
	public List<KakaoAddressVO> getAddressNameList() {
		return studyDAO.getAddressNameList();
	}

	@Override
	public void setKakaoAddressDelete(String address) {
		studyDAO.setKakaoAddressDelete(address);
	}

	@Override
	public ArrayList<KakaoAddressVO> getDistanceList() {
		
		double centerLat=36.62935542331672;
		double centerLongi=127.45760875128917;
		
		ArrayList<KakaoAddressVO> dbVOS=studyDAO.getKakaoList(); //기존 DB에 있던자료
		
		ArrayList<KakaoAddressVO> vos=new ArrayList<KakaoAddressVO>(); //거리비교할자료
		
		for(int i=0; i<dbVOS.size(); i++) {
			double distance=DistanceCal.distance(centerLat, centerLongi, dbVOS.get(i).getLatitude(), dbVOS.get(i).getLongitude(), "kilometer");
			if(distance<15) { //15km안쪽
				vos.add(dbVOS.get(i));
			}
		}
		
		return vos;
	}
}
