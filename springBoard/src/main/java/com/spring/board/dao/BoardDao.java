package com.spring.board.dao;

import java.util.HashMap;
import java.util.List;

import com.spring.board.vo.BoardVo;
import com.spring.board.vo.PageVo;
import com.spring.board.vo.ComVo;

public interface BoardDao {

	public String selectTest() throws Exception;

	public List<BoardVo> selectBoardList(PageVo pageVo) throws Exception;

	public BoardVo selectBoard(BoardVo boardVo) throws Exception;

	public int selectBoardCnt() throws Exception;

	public int boardInsert(HashMap<String, Object> HashVo) throws Exception;
	
	public int boardDelete(int boardNum) throws Exception;
	
	public int boardModify(HashMap<String, Object> HashVo) throws Exception;
	
	public List<ComVo> selectTypeList(ComVo comVo) throws Exception;
	
}
