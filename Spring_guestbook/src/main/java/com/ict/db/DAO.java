package com.ict.db;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

public class DAO {
	// 실제 사용할 클래스 : SqlSessionTemplate
	// 클래스를 자료형으로 사용
	// 사용하려면 클래스를 객체(bean)로 만들어야 함. 어디서? root-context에서!
	// root-context 가보면 만들었음!

	private SqlSessionTemplate session;

	public SqlSessionTemplate getSession() {
		return session;
	}

	public void setSession(SqlSessionTemplate session) {
		this.session = session;
	}
}
