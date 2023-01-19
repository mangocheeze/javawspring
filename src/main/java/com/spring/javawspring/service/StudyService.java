package com.spring.javawspring.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.spring.javawspring.vo.GuestVO;
import com.spring.javawspring.vo.KakaoAddressVO;
import com.spring.javawspring.vo.QrCodeVO;

public interface StudyService {

	public String[] getCityStringArr(String dodo);

	public ArrayList<String> getCityArrayListArr(String dodo);

	public GuestVO getGuestMid(String mid);

	public ArrayList<GuestVO> getGuestNames(String mid);

	public int fileUpload(MultipartFile fName);

	public void getCalendar();

	public String qrCreate(String mid, String moveFlag, String realPath);

	public void setQrInput(QrCodeVO vo);

	public QrCodeVO getQrSearch(String qrIdx);

	public KakaoAddressVO getKakaoAddressName(String address);

	public void setKakaoAddressName(KakaoAddressVO vo);

	public List<KakaoAddressVO> getAddressNameList();

	public void setKakaoAddressDelete(String address);

	public ArrayList<KakaoAddressVO> getDistanceList();

}
