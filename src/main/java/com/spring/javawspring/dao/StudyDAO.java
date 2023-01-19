package com.spring.javawspring.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.spring.javawspring.vo.GuestVO;
import com.spring.javawspring.vo.KakaoAddressVO;
import com.spring.javawspring.vo.QrCodeVO;

public interface StudyDAO {

	public GuestVO getGuestMid(@Param("name") String mid);

	public ArrayList<GuestVO> getGuestNames(@Param("name") String mid);

	public void setQrInput(@Param("vo") QrCodeVO vo);

	public QrCodeVO getQrSearch(@Param("qrIdx") String qrIdx);

	public KakaoAddressVO getKakaoAddressName(@Param("address") String address);

	public void setKakaoAddressName(@Param("vo") KakaoAddressVO vo);

	public List<KakaoAddressVO> getAddressNameList();

	public void setKakaoAddressDelete(@Param("address") String address);

	public ArrayList<KakaoAddressVO> getKakaoList();


}
