package com.boxfox.dms.board.dao;

import java.util.Calendar;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.boxfox.dms.board.dto.FacilityReportContext;
import com.boxfox.dms.board.mapper.FacilityMapper;

@Repository
public class FacilityDAOImpl implements FacilityDAO{
	
	@Autowired
	private SqlSession sqlSession;

	@Override
	public FacilityReportContext getPost(int number) {
		FacilityMapper facilityMapper = sqlSession.getMapper(FacilityMapper.class);
		FacilityReportContext post = null;
		List<FacilityReportContext> posts = facilityMapper.getPost(number*10);
		if(posts.size()>0)
			post = posts.get(0);
		return post;
	}

	@Override
	public void writePost(String title, String content, int room, String writer) {
		FacilityMapper facilityMapper = sqlSession.getMapper(FacilityMapper.class);
		Calendar c = Calendar.getInstance();
		String date = c.get(Calendar.YEAR)+"-"+c.get(Calendar.MONTH)+"-"+c.get(Calendar.DAY_OF_MONTH);
		FacilityReportContext post = new FacilityReportContext();
		post.setDate(date);
		post.setRoom(room);
		post.setTitle(title);
		post.setContent(content);
		post.setWriter(writer);
		facilityMapper.writePost(post);
	}

	@Override
	public void setResult(int num, String result) {
		FacilityMapper facilityMapper = sqlSession.getMapper(FacilityMapper.class);
		Calendar c = Calendar.getInstance();
		String date = c.get(Calendar.YEAR)+"-"+c.get(Calendar.MONTH)+"-"+c.get(Calendar.DAY_OF_MONTH);
		FacilityReportContext post = new FacilityReportContext();
		post.setResultDate(date);
		post.setResult(result);
		facilityMapper.setResult(post);
	}

	@Override
	public void editPost(int no, String title, String content, int room) {
		FacilityMapper facilityMapper = sqlSession.getMapper(FacilityMapper.class);
		FacilityReportContext post = new FacilityReportContext();
		post.setNo(no);
		post.setTitle(title);
		post.setContent(content);
		post.setRoom(room);
		facilityMapper.editPost(post);
	}

	@Override
	public int deletePost(int number) {
		FacilityMapper facilityMapper = sqlSession.getMapper(FacilityMapper.class);
		return facilityMapper.deletePost(number);
	}


}
