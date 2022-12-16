package com.uni.spring.member.model.service;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uni.spring.common.CommException;
import com.uni.spring.member.model.dao.MemberDao;
import com.uni.spring.member.model.dto.Member;


//@EnableAspectJAutoProxy
//@Transactional(rollbackFor = {Exception.class})   
@Service
public class MemberServiceImpl implements MemberService {

	@Autowired
	private SqlSessionTemplate sqlSession;
	
	@Autowired
	private MemberDao memberDao;
	
	@Override
	public Member loginMember(Member m) throws Exception {
		
		Member loginUser = memberDao.loginMember(sqlSession, m);
		
		System.out.println("loginUser  : " + loginUser);
		
		
		if(loginUser == null) {
			throw new Exception("loginUser확인");
		}
		return loginUser;
	}

	@Override
	public void insertMember(Member m) {
		
		int result = memberDao.insertMember(sqlSession, m);
		
		if(result < 0 ) {
			throw new CommException("회원가입에 실패 하였습니다.");
		}
	}

	@Override
	public Member loginMember(BCryptPasswordEncoder bCryptPasswordEncoder, Member m) {
		
		Member loginUser = memberDao.loginMember(sqlSession, m);
		if(loginUser == null) {
			throw new CommException("loginUser확인");
		}
		
		
		//matches(평문, 암호화) --> true, false
		if(!bCryptPasswordEncoder.matches(m.getUserPwd(), loginUser.getUserPwd())) {
			throw new CommException("암호 불일치");
		}
		return loginUser;
	}

	@Override
	public Member updateMember(Member m) throws Exception {
		
		int result = memberDao.updateMember(sqlSession, m);
		//memberDao.insertMember(sqlSession, m);
		if(result < 0) {
			Member loginUser = memberDao.loginMember(sqlSession, m);
			return loginUser;
		}else {
			throw new Exception("회원수정실패");
		}
		

	}

	@Override
	public int idCheck(String userId) {
		int result = memberDao.idCheck(sqlSession, userId);
		
		if(result < 0 ) {
			throw new CommException("아이디체크에 실패하였습니다.");
		}
		return result;
	}

}
