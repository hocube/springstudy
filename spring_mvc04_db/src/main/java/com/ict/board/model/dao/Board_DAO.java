package com.ict.board.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ict.board.model.vo.Board_VO;

@Repository
public class Board_DAO {
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	
	
	public int getTotalCount() {
		return sqlSessionTemplate.selectOne("board.count");
	}
	
	// 전체 리스트
	public List<Board_VO> getList(int offset, int limit){
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("limit", limit);
		map.put("offset", offset);
		return sqlSessionTemplate.selectList("board.list", map);
	}
	
	// 삽입
	public int getInsert(Board_VO bv) {
		return sqlSessionTemplate.insert("board.insert", bv);
	}
	
	// 조회수
	public int getHitUpdate(String idx) {
		return sqlSessionTemplate.update("board.hitup", idx);
	}
	// 상세보기
	public Board_VO getOneList(String idx) {
		return sqlSessionTemplate.selectOne("board.onelist", idx);
	}
}