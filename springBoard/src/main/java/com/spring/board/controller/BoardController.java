package com.spring.board.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.spring.board.HomeController;
import com.spring.board.service.boardService;
import com.spring.board.vo.BoardVo;
import com.spring.board.vo.Com_CodeVo;
import com.spring.board.vo.PageVo;
import com.spring.common.CommonUtil;

@Controller
public class BoardController {
	
	@Autowired 
	boardService boardService;
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	// 화면에 List를 보여주는 method
	@RequestMapping(value = "/board/boardList.do", method = RequestMethod.GET)
	public String boardList(Locale locale, Model model, PageVo pageVo, Com_CodeVo com_codeVo) throws Exception{
		
		List<BoardVo> boardList = new ArrayList<BoardVo>();
		// BoardVo에 저장된 값을 보여주기 위함
		
		List<Com_CodeVo> Com_CodeList = new ArrayList<Com_CodeVo>();
		// Com_CodeVo에 저장된 값을 보여주기 위함
		Com_CodeList = boardService.SelectTypeList(com_codeVo);
		
		int page = 1;
		int totalCnt = 0;
		
		if(pageVo.getPageNo() == 0){
			pageVo.setPageNo(page);
		}
		
		boardList = boardService.SelectBoardList(pageVo);
		totalCnt = boardService.selectBoardCnt();
		
		model.addAttribute("boardList", boardList);
		model.addAttribute("totalCnt", totalCnt);
		model.addAttribute("pageNo", page);
		
		return "board/boardList";
	}
	
	// 게시글 클릭 시 보여지는 화면
	@RequestMapping(value = "/board/{boardType}/{boardNum}/boardView.do", method = RequestMethod.GET)
	public String boardView(Locale locale, Model model
			,@PathVariable("boardType")String boardType
			,@PathVariable("boardNum")int boardNum) throws Exception{

		BoardVo boardVo = boardService.selectBoard(boardType,boardNum);
		
		model.addAttribute("boardNum", boardNum);
		model.addAttribute("board", boardVo);
		model.addAttribute("com", boardVo.getCom_codeList());
		
		return "board/boardView";
	}
	
	// 게시글 작성시 보여지는 화면
	@RequestMapping(value = "/board/boardWrite.do", method = RequestMethod.GET)
	public String boardWrite(Locale locale, Model model, Com_CodeVo com_codeVo) throws Exception{
		
		List<Com_CodeVo> Com_CodeList = new ArrayList<Com_CodeVo>();
		// Com_CodeVo에 저장된 값을 보여주기 위함
		Com_CodeList = boardService.SelectTypeList(com_codeVo);
		model.addAttribute("com_codeList", Com_CodeList);
		
		return "board/boardWrite";
	}
	
	// 게시글이 작성되는 과정
	@RequestMapping(value = "/board/boardWriteAction.do", method = RequestMethod.POST)
	@ResponseBody
	public String boardWriteAction(Locale locale, BoardVo boardVo, Com_CodeVo com_codeVo) throws Exception{
		
		HashMap<String, String> result = new HashMap<String, String>();
		CommonUtil commonUtil = new CommonUtil();
		
		HashMap<String, Object> hashmap = new HashMap<String, Object>();
		
		hashmap.put("boardVo", boardVo);
		hashmap.put("com_codeVo", com_codeVo);
		
		int resultCnt = boardService.boardInsert(hashmap);
		
		result.put("success", (resultCnt > 0)?"Y":"N");
		String callbackMsg = commonUtil.getJsonCallBackString(" ",result);
		
		System.out.println("callbackMsg::"+callbackMsg);
		
		return callbackMsg;
	}
	
	// 게시글 삭제
	@RequestMapping(value = "/board/boardDeleteAction.do")
	public String boardDeleteAction(Locale locale, int boardNum) throws Exception{
		
		int result = boardService.boardDelete(boardNum);
		if(result == 0) {
			return "board/boardDelete";
		}
		
		// 기존에 이미 삭제된 게시물일 경우 안내 페이지로 이동할 수 있도록 함
		return "redirect:boardList.do";
	}
	
	// 게시글 수정
	@RequestMapping(value = "/board/boardModify.do", method = RequestMethod.GET)
	public String boardModify(Locale locale, Model model, @RequestParam("boardType")String boardType
			, @RequestParam("boardNum")int boardNum, Com_CodeVo com_codeVo) throws Exception {
		
		List<Com_CodeVo> Com_CodeList = new ArrayList<Com_CodeVo>();
		// Com_CodeVo에 저장된 값을 보여주기 위함
		Com_CodeList = boardService.SelectTypeList(com_codeVo);
	
		BoardVo boardVo = boardService.selectBoard(boardType,boardNum);
		
		model.addAttribute("boardNum", boardNum);
		model.addAttribute("board", boardVo);
		model.addAttribute("com_codeList", Com_CodeList);
		
		return "board/boardModify";
	}
	
	// 게시글 수정 method
	@RequestMapping(value = "/board/boardModifyAction.do", method = RequestMethod.POST)
	@ResponseBody
	public String boardModifyAction(Locale locale, Model model, BoardVo boardVo, Com_CodeVo com_codeVo) throws Exception{
		
		HashMap<String, String> result = new HashMap<String, String>();
		CommonUtil commonUtil = new CommonUtil();
		
		HashMap<String, Object> hashmap = new HashMap<String, Object>();
		
		hashmap.put("boardVo", boardVo);
		hashmap.put("com_codeVo", com_codeVo);
		
		int resultCnt = boardService.boardModify(hashmap);
		
		result.put("success", (resultCnt > 0)?"Y":"N");
		String callbackMsg = commonUtil.getJsonCallBackString(" ",result);
		
		System.out.println("callbackMsg::"+callbackMsg);
		
		return callbackMsg;
	}
	
}
