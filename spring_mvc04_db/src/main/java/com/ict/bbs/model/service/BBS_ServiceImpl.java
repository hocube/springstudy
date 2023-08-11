package com.ict.bbs.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ict.bbs.model.dao.BBS_DAO;
import com.ict.bbs.model.vo.BBS_VO;
import com.ict.bbs.model.vo.Comment_VO;
import com.ict.board.model.vo.Board_VO;

@Service
public class BBS_ServiceImpl implements BBS_Service{
	@Autowired
	private BBS_DAO bBS_DAO;
	
	@Override
	public int getTotalCount() {
		return bBS_DAO.getTotalCount();
	}
	
	@Override
	public List<BBS_VO> getList(int offset, int limit) {
		return bBS_DAO.getList(offset, limit);
	}

	// 전체 리스트
	@Override
	public List<BBS_VO> getList() {
		return bBS_DAO.getList();
	}

	// 삽입
	@Override
	public int getInsert(BBS_VO bvo) {
		return bBS_DAO.getInsert(bvo);
	}
	
	// 상세보기
	@Override
	public BBS_VO getOneList(String b_idx) {
		return bBS_DAO.getOneList(b_idx);
	}
	// 조회수 업데이트
	@Override
	public int getHitUpdate(String b_idx) {
		return bBS_DAO.getHitUpdate(b_idx);
	}
	// 댓글 리스트
	@Override
	public List<Comment_VO> getCommList(String b_idx) {
		return bBS_DAO.getCommList(b_idx);
	}
	// 코멘트 삽입
	@Override
	public int getCommInsert(Comment_VO cvo) {
		return bBS_DAO.getCommInsert(cvo);
	}
	// 코멘트 삭제
	@Override
	public int getCommDelete(String c_idx) {
		return bBS_DAO.getCommDelete(c_idx);
	}
	
	// 삭제
	@Override
	public int getDelete(String b_idx) {
		return bBS_DAO.getDelete(b_idx);
	}

	// 수정
	@Override
	public int getUpdate(BBS_VO bvo) {
		return bBS_DAO.getUpdate(bvo);
	}
}