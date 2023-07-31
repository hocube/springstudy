package com.ict.model.service;

import java.util.List;

import com.ict.model.vo.GuestbookVO;

public interface GuestbookService {
	// 전체 보기
	List<GuestbookVO> guestbookList();
	
	// 삽입
}
