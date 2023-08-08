package com.ict.bbs.model.service;

import java.util.List;

import com.ict.bbs.model.vo.BBS_VO;
import com.ict.bbs.model.vo.Comment_VO;

public interface BBS_Service {
	
	// 전체 게시물의 수
	public int getTotalCount();
	
	// 페이징처리를 위한 리스트
	public List<BBS_VO> getList(int offset, int limit);

	// 전체보기
	public List<BBS_VO> getList();
	
	// 글쓰기(삽입)
	public int getInsert(BBS_VO bvo);	
	
	// 상세보기
	public BBS_VO getOneList(String b_idx);
	// 조회수(hit) 업데이트
	public int getHitUpdate(String b_idx);
	// 코멘트 가져오기
	public List<Comment_VO> getCommList(String b_idx);
	// 코멘트 삽입하기
	public int getCommInsert(Comment_VO cvo);
}