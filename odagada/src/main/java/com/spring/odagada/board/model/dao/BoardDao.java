package com.spring.odagada.board.model.dao;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


public interface BoardDao {
	
	int selectBoardCount();
	List<Map<String,String>> selectBoardList(int cPage,int numPerPage);

}
