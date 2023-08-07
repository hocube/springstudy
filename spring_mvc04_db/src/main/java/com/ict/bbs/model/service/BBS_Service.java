package com.ict.bbs.model.service;

import java.util.List;

import com.ict.bbs.model.vo.BBS_VO;

public interface BBS_Service {

	// 전체보기
	public List<BBS_VO> getList();
	
	// 상세보기
	public BBS_VO getOneList(String b_idx);
	
	// hit(조회수) 업데이트
	public int getHitUpdate(String b_idx);
	
	// 글쓰기(삽입)
	public int getInsert(BBS_VO bvo);
	
}
