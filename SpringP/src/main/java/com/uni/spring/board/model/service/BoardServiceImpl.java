package com.uni.spring.board.model.service;

import java.util.ArrayList;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uni.spring.board.model.dao.BoardDao;
import com.uni.spring.board.model.dto.Board;
import com.uni.spring.board.model.dto.PageInfo;
import com.uni.spring.board.model.dto.Reply;
import com.uni.spring.common.CommException;

@Service
public class BoardServiceImpl implements BoardService {

	@Autowired
	private SqlSessionTemplate sqlSession;
	
	@Autowired
	private BoardDao boardDao;
	
	@Override
	public int selectListCount() {
		
		return boardDao.selectListCount(sqlSession);
	}

	@Override
	public ArrayList<Board> selectList(PageInfo pi) {
		// TODO Auto-generated method stub
		return boardDao.selectList(sqlSession, pi);
	}

	@Override
	public void insertBoard(Board b) {
		
		int result = boardDao.insertBoard(sqlSession, b);
		
		if(result < 0) {
			throw new CommException("게시글추가실패 ");
		}
		
	}

	@Override
	public Board selectBoard(int bno) {
		Board b = null;
		
		int result = boardDao.inceaseCount(sqlSession, bno);
		if(result < 0) {
			throw new CommException("inceaseCount 실패");
		}else {
			b = boardDao.selectBoard(sqlSession, bno);
		}
		return b;
	}

	@Override
	public void updateBoard(Board b) {
		int result = boardDao.updateBoard(sqlSession, b);
		
		if(result < 0) {
			throw new CommException("게시글수정실패 ");
		}
		
		
	}

	@Override
	public void deleteBoard(int bno) {
	int result = boardDao.deleteBoard(sqlSession, bno);
		
		if(result < 0) {
			throw new CommException("게시글삭제실패 ");
		}
	}

	@Override
	public ArrayList<Reply> selectReplyList(int bno) {
		
		return boardDao.selectReplyList(sqlSession , bno);
	}

	@Override
	public int insertReply(Reply r) {
	int result = boardDao.insertReply(sqlSession, r);
		
		if(result < 0) {
			throw new CommException("댓글추가실패 ");
		}
		return result;
	}

	@Override
	public ArrayList<Board> selectTopList() {
		// TODO Auto-generated method stub
		return boardDao.selectTopList(sqlSession);
	}
	

}
