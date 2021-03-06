package com.spring.odagada.member.model.dao;

import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.odagada.member.model.vo.Member;

@Repository
public class MemberDaoImpl implements MemberDao {
	
	@Autowired
	SqlSessionTemplate session;

	@Override
	public Map<String, String> login(Map<String, String> login) {
		// TODO Auto-generated method stub
		return session.selectOne("member.login", login);
	}

	@Override
	public Member selectMember(String memberId) {
		// TODO Auto-generated method stub
		return session.selectOne("member.selectMember", memberId);
	}

	@Override
	public int checkId(String memberId) {
		// TODO Auto-generated method stub
		return session.selectOne("member.checkId", memberId);
	}
	
	@Override
	public int updateMember(Member m) {
		// TODO Auto-generated method stub
		return session.selectOne("member.updateMember", m);
	}
	
	//이메일 중복확인
	@Override
	public int checkEmail(String email) {
		// TODO Auto-generated method stub
		return session.selectOne("member.checkEmail", email);
	}	
	
	//1. 회원가입
	@Override
	public void insertMember(Member m) throws Exception {
		// TODO Auto-generated method stub
		session.insert("member.insertMember", m);
	}

    //2. 해당 email에 인증 키 생성 후 업데이트
    @Override
    public void createAuthKey(String email, String mailCode) throws Exception {
        Member m = new Member();
        m.setMailCode(mailCode);
        m.setEmail(email);
        session.update("member.updateCode", m);
    }
    //3. 이메일 인증 코드 확인
    /* @Override
    public Member chkAuth(Member m) throws Exception {
        return session.selectOne("member.chkCode", m);
    }*/

    
 /*   //4. 인증 후 계정 활성화
    @Override
    public void updateEmailStatus(Member m) throws Exception {
        session.update("member.authStatus", m.getIsEmailAuth());
        System.out.println(m.getIsEmailAuth());
    }*/


    //E-mail status 변환
	@Override
	public int updateStatus(Map<String, String>map) {
		// TODO Auto-generated method stub
		return session.update("member.mailStatus", map);
	}
	
}
