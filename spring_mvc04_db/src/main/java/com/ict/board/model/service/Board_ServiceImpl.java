package com.ict.board.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ict.board.model.dao.Board_DAO;
import com.ict.board.model.vo.Board_VO;

@Service
public class Board_ServiceImpl implements Board_Service{
	@Autowired
	private Board_DAO board_DAO;

	// 카운트
	@Override
	public int getTotalCount() {
		return board_DAO.getTotalCount();
	}

	// 전체 리스트
	@Override
	public List<Board_VO> getList(int offset, int limit) {
		return board_DAO.getList(offset, limit);
	}
	
	// 삽입
	@Override
	public int getInsert(Board_VO bv) {
		return board_DAO.getInsert(bv);
	}
	
	// 조회수
	@Override
	public int getHitUpdate(String idx) {
		return  board_DAO.getHitUpdate(idx);
	}
	// 상세보기
	@Override
	public Board_VO getOneList(String idx) {
		return board_DAO.getOneList(idx);
	}
}