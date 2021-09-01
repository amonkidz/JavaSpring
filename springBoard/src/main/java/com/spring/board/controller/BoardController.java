package com.spring.board.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

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

import com.spring.board.HomeController;
import com.spring.board.service.boardService;
import com.spring.board.vo.BoardVo;
import com.spring.board.vo.ComVo;
import com.spring.board.vo.PageVo;
import com.spring.common.CommonUtil;

@Controller
public class BoardController {
	
	@Autowired 
	boardService boardService;
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	// Main에 List를 보여주는 역할
	@RequestMapping(value = "/board/boardList.do", method = RequestMethod.GET)
	// board/boardList.do의 url과 연결되어 있으며 GET 방식으로 해당 url을 가져오는 역할
	public String boardList(Locale locale, Model model, PageVo pageVo, ComVo comVo) throws Exception {
		
		List<BoardVo> boardList = new ArrayList<BoardVo>();
		// BoardVo에 저장된 값을 보여주기 위함
		
		List<ComVo> ComList = new ArrayList<ComVo>();
		// Com_CodeVo에 저장된 값을 보여주기 위함
		
		int page = 1;
		// Page를 count하기 위한 변수 설정 (초기화 1)
		int totalCnt = 0;
		// 글 갯수를 count하기 위한 변수 설정 (초기화 0)
		
		if(pageVo.getPageNo() == 0){
			pageVo.setPageNo(page);
		}
		// Page를 count하여 화면에 Page가 표기될 수 있도록 함
		
		boardList = boardService.SelectBoardList(pageVo);
		// PageVo를 이용하여 List 안에 들어있는 BoardVo 값을 보여줄 수 있도록 Service로 값을 전달
		totalCnt = boardService.selectBoardCnt();
		// Cnt값을 Service로 전달 쿼리문이 돌아가면서 cnt가 증가될 수 있도록
		ComList = boardService.SelectTypeList(comVo);
		// Com_CodeVo에 저장된 값을 Service로 전달
		
		model.addAttribute("boardList", boardList);
		model.addAttribute("totalCnt", totalCnt);
		model.addAttribute("pageNo", page);
		model.addAttribute("comList", ComList);
		// Model을 이용하여 나타내고 싶은 값을 View로 전달 key, value의 형태로 key를 이용하여 View에서 해당 값을 화면으로 나타낼 수 있음
		
		return "board/boardList";
		// board/boardList로 화면이 넘어갈 수 있도록 함
	}
	
	// List에서 글을 클릭할 경우 보여지는 화면
	@RequestMapping(value = "/board/{boardType}/{boardNum}/boardView.do", method = RequestMethod.GET)
	// board/{boardType}/{boardNum}/boardView.do와 연결되며 GET 형식으로 가져옴 : GET 형식으로 가져와야 PathVariable을 이용하여 값을 뽑아낼 수 있음
	public String boardView(Locale locale, Model model, @PathVariable("boardType")String boardType, @PathVariable("boardNum")int boardNum) throws Exception {

		BoardVo boardVo = boardService.selectBoard(boardType, boardNum);
		// boardType과 boardNum을 이용하여 선택한 글을 특정함
		
		model.addAttribute("boardNum", boardNum);
		model.addAttribute("board", boardVo);
		model.addAttribute("com", boardVo.getComList());
		// Model을 이용하여 나타내고 싶은 값을 View로 전달 key, value의 형태로 key를 이용하여 View에서 해당 값을 화면으로 나타낼 수 있음
		// boardVo.getCom_codeList는 BoardVo안에 colletion 형태로 지정된 Com_CodeVo안의 값을 가져오기 위함
		
		return "board/boardView";
		// board/boardView로 화면이 넘어갈 수 있도록 함
	}
	
	// 게시글 작성시 보여지는 화면
	@RequestMapping(value = "/board/boardWrite.do", method = RequestMethod.GET)
	// board/boardWrite.do와 연결
	public String boardWrite(Locale locale, Model model, ComVo comVo) throws Exception {
		
		List<ComVo> ComList = new ArrayList<ComVo>();
		// Com_CodeVo에 저장된 값을 보여주기 위함
		ComList = boardService.SelectTypeList(comVo);
		// 작성한 내역들 중 SelectBox의 CodeId 값을 받아 Service로 넘겨줌 
		model.addAttribute("comList", ComList);
		// Model을 이용하여 나타내고 싶은 값을 View로 전달 key, value의 형태로 key를 이용하여 View에서 해당 값을 화면으로 나타낼 수 있음
		// DB와 연동하여 SelectBox에 List를 보여주기 위함
		
		return "board/boardWrite";
		// board/boardWrite로 화면이 넘어갈 수 있도록 함
	}
	
	// 게시글이 작성되는 과정
	@RequestMapping(value = "/board/boardWriteAction.do", method = RequestMethod.POST)
	// board/boardWriteAction.do와 연결하고 POST형식을 이용해야 url에 Data가 노출되지 않는다
	@ResponseBody
	// Json type으로 넘어온 data값을 이용하기 위해서 사용
	public String boardWriteAction(Locale locale, BoardVo boardVo, ComVo comVo) throws Exception {
		
		HashMap<String, String> result = new HashMap<String, String>();
		// result를 확인하기 위한 HashMap이용
		CommonUtil commonUtil = new CommonUtil();
		
		HashMap<String, Object> hashmap = new HashMap<String, Object>();
		// BoardVo 내부의 data : boardTitle, boardComment 등
		// Com_CodeVo 내부의 data : codeId, codeName 등
		// 정보를 함께 담아 전달하기 위해 ( = Vo를 두개 전달하기 위해) HashMap을 사용
		
		hashmap.put("boardVo", boardVo);
		hashmap.put("comVo", comVo);
		// HashMap에 두가지 정보를 담음
		
		int resultCnt = boardService.boardInsert(hashmap);
		// Service로 값이 제대로 전달 되는지 확인하는 Cnt 변수 설정
		
		result.put("success", (resultCnt > 0) ? "Y":"N");
		// jsp에서 작성한 script문에서 success된 값이 0개 이상이라면 Y를 아니라면 N을 출력하도록 함 (삼항연산자 사용)
		String callbackMsg = commonUtil.getJsonCallBackString(" ",result);
		// callbackMsg를 지정
		
		System.out.println("callbackMsg::" + callbackMsg);
		// callbackMsg이 제대로 출력되는지 확인
		
		return callbackMsg;
		// callbackMsg가 화면에서 출력될 수 있도록 전달
	}
	
	// 게시글 삭제
	@RequestMapping(value = "/board/boardDeleteAction.do", method = RequestMethod.GET)
	// board/boardDeleteAction.do와 연결
	public String boardDeleteAction(Locale locale, @RequestParam("boardNum") int boardNum) throws Exception {
		// boardNum을 이용하여 선택한 글이 삭제되도록 함
		

		int result = boardService.boardDelete(boardNum);
		// result 변수를 사용하여 Service로 제대로 값이 전달되는지 확인
		if(result == 0) {
			return "board/boardDelete";
		}
		// result 값이 0이라면 이미 삭제된 것으로 <이미 삭제된 게시글 입니다>라는 안내 페이지로 이동할 수 있도록 함
		
		return "redirect:boardList.do";
		// result 값이 1이라면 삭제 과정이 정상 진행되며 결과값을 redirect를 이용하여 boardList.do로 전달함
	}
	
	// 게시글 수정
	@RequestMapping(value = "/board/boardModify.do", method = RequestMethod.GET)
	// board/boardModify.do와 연결 GET형식을 이용하여 url에서 값을 확인할 수 있도록 함
	public String boardModify(Locale locale, Model model, @RequestParam("codeId")String boardType, @RequestParam("boardNum")int boardNum, ComVo comVo) throws Exception {
		
		List<ComVo> ComList = new ArrayList<ComVo>();
		// Com_CodeVo에 저장된 값을 보여주기 위함
		ComList = boardService.SelectTypeList(comVo);
		// CodeId값 또한 수정할 수 있도록 SelectBox List를 보여주기 위해 Service로 값을 전달함
	
		BoardVo boardVo = boardService.selectBoard(boardType, boardNum);
		// boardType과 boardNum을 이용하여 기존에 작성된 글을 불러올 수 있도록 함
		
		model.addAttribute("boardNum", boardNum);
		model.addAttribute("board", boardVo);
		model.addAttribute("comList", ComList);
		// Model을 이용하여 View로 key, value를 전달
		
		return "board/boardModify";
	}
	
	// 게시글 수정 method
	@RequestMapping(value = "/board/boardModifyAction.do", method = RequestMethod.POST)
	@ResponseBody
	public String boardModifyAction(Locale locale, Model model, BoardVo boardVo, ComVo comVo) throws Exception {
		
		HashMap<String, String> result = new HashMap<String, String>();
		// result를 확인하기 위한 HashMap이용
		CommonUtil commonUtil = new CommonUtil();
		
		HashMap<String, Object> hashmap = new HashMap<String, Object>();
		// BoardVo 내부의 data : boardTitle, boardComment 등
		// Com_CodeVo 내부의 data : codeId, codeName 등
		// 정보를 함께 담아 전달하기 위해 ( = Vo를 두개 전달하기 위해) HashMap을 사용 -> Write와 동일
		
		hashmap.put("boardVo", boardVo);
		hashmap.put("comVo", comVo);
		// HashMap에 두가지 정보를 담음
		
		int resultCnt = boardService.boardModify(hashmap);
		// 입력한 내용을 쿼리문으로 전달할 수 있도록 Service로 값을 전달, resultCnt를 이용하여 callbackMsg 작성 -> Write와 동일
		
		result.put("success", (resultCnt > 0) ? "Y":"N");
		String callbackMsg = commonUtil.getJsonCallBackString(" ",result);
		
		return callbackMsg;
	}
	
	@RequestMapping(value = "/board/boardSelectAction.do", method = RequestMethod.POST)
	@ResponseBody
	public String boardSelectAction(@RequestParam(value = "checkArr[]") List<String> checkArr) throws Exception {
		
		logger.info("넘어온 값 확인 : " + checkArr);
		
		for(int i = 0; i<checkArr.size(); i++) {
			System.out.println(checkArr.get(i));
			System.out.println("*************");
		}

		
		
		return "redirect:boardList.do";
		
	}

}
