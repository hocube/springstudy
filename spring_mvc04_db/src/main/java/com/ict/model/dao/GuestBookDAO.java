package com.ict.model.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ict.model.vo.GuestBookVO;
import com.ict.model.vo.MembersVO;

@Repository
public class GuestBookDAO {

	// 실제 mapper를 호출하는 클래스
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	
	// getter, setter
	public SqlSessionTemplate getSqlSessionTemplate() {
		return sqlSessionTemplate;
	}
	
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}
	
	
	// GuestBookService에서 DB처리하는 메서드를 모두 받아줘야 한다.
	
	// 전체 리스트
	public List<GuestBookVO> getGuestBookList() {
		// List<GuestBookVO> list = sessionTemplate.selectList("guestbook.list");
		// return list;
		// 위에꺼를 줄여서 한 줄로도 쓸 수 있음!
		return sqlSessionTemplate.selectList("guestbook.list");
	}
	
	// 삽입
	public int getGuestBookInsert(GuestBookVO gvo) {
		return sqlSessionTemplate.insert("guestbook.insert", gvo);
	}
}
