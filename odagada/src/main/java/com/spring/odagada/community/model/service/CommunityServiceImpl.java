package com.spring.odagada.community.model.service;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.spring.odagada.community.model.dao.CommunityDao;
import com.spring.odagada.community.model.vo.ChatRoomVo;
import com.spring.odagada.community.model.vo.MessageVo;

@Service
public class CommunityServiceImpl implements CommunityService {
	@Autowired
	CommunityDao dao;
	
	private Logger logger = LoggerFactory.getLogger(CommunityServiceImpl.class);

	@Override
	public int insertRoomId(Map roomIdData) {
		// TODO Auto-generated method stub
		return dao.insertRoomId(roomIdData);
	}

	@Override
	public List<Map<String, String>> bringUserInfo(Map<String,String> roomIdData) {
		// TODO Auto-generated method stub
		return dao.bringUserInfo(roomIdData);
	}

	@Override
	public String roomIdCheck(Map<String,String> roomIdData) {
		// TODO Auto-generated method stub
		return dao.roomIdCheck(roomIdData);
	}

	@Override
	public int jsutCheckMsg(String myId) {
		// TODO Auto-generated method stub
		return dao.jsutCheckMsg(myId);
	}

	@Override
	public int saveMessage(MessageVo msg) {
		// TODO Auto-generated method stub
		return dao.saveMessage(msg);
	}
	
	@Override
	public int checkedMessage(Map isreadData) {
		// TODO Auto-generated method stub
		return dao.checkedMessage(isreadData);
	}

	//채팅 내용들 가져오는거
	@Override
	public List<Map<String, String>> bringMsg(String roomId) {
		// TODO Auto-generated method stub
		return dao.bringMsg(roomId);
	}
	
	//채팅방 리스트 가져오는거
	@Override
	public List<ChatRoomVo> bringChatRooms(String loginId) {
		// TODO Auto-generated method stub
		return dao.bringChatRooms(loginId);
	}
	
	//신고 글쓰기
	@Override
	public int insertNotify(Map<String, String> map) {
		return dao.insertNotify(map);
	}

	@Override
	public int insertReview(Map<String, Object> map) {
		return dao.insertReview(map);
	}

	@Override
	public Map<String, String> selectMyReview(int writerNum) {
		return dao.selectMyReview(writerNum);
	}
	
	
	
	
	
	
}
