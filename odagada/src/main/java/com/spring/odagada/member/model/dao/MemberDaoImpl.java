package com.spring.odagada.member.model.dao;

import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MemberDaoImpl implements MemberDao {
	
	@Autowired
	SqlSessionTemplate session;

	@Override
	public Map<String, String> login(Map<String, String> login) {
		// TODO Auto-generated method stub
		return session.selectOne("member.login", login);
	}

	
}
