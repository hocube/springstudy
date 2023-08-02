package com.ict.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ict.model.dao.GuestBookDAO;
import com.ict.model.vo.GuestBookVO;

@Service
public class GuestBookServiceImpl implements GuestBookService{
	// DAO에 가서 DB처리를 받아 오자
	@Autowired
	private GuestBookDAO guestBookDAO;
	
	// 전체 리스트
	@Override
	public List<GuestBookVO> getGuestBookList() {
		return guestBookDAO.getGuestBookList();
	}

	// 삽입
	@Override
	public int getGuestBookInsert(GuestBookVO gvo) {
		return guestBookDAO.getGuestBookInsert(gvo);
	}
	
	// 상세보기
	@Override
	public GuestBookVO getGuestBookOneList(String idx) {
		return guestBookDAO.getGuestBookOneList(idx);
	}
	
	// 수정
	@Override
	public int getGuestBookUpdate(GuestBookVO gvo) {
		return guestBookDAO.getGuestBookUpdate(gvo);
	}

	// 삭제
	@Override
	public int getGuestBookDelete(String idx) {
		return guestBookDAO.getGuestBookDelete(idx);
	}
}
