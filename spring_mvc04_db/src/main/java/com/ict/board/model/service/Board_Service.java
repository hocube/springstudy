package com.ict.board.model.service;

import java.util.List;

import com.ict.board.model.vo.Board_VO;

public interface Board_Service {
	
	// 전체 게시물의 수
	int getTotalCount();
	
	// 전체 보기
	List<Board_VO> getList(int offset, int limit);
	
	// 삽입
	int getInsert(Board_VO bv);
	
	// 조회수
	int getHitUpdate(String idx); 
	
	// 상세보기
	Board_VO getOneList(String idx);
}
