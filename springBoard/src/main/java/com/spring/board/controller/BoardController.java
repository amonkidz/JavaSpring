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
	
	// Main�� List�� �����ִ� ����
	@RequestMapping(value = "/board/boardList.do", method = RequestMethod.GET)
	// board/boardList.do�� url�� ����Ǿ� ������ GET ������� �ش� url�� �������� ����
	public String boardList(Locale locale, Model model, PageVo pageVo, ComVo comVo) throws Exception {
		
		List<BoardVo> boardList = new ArrayList<BoardVo>();
		// BoardVo�� ����� ���� �����ֱ� ����
		
		List<ComVo> ComList = new ArrayList<ComVo>();
		// Com_CodeVo�� ����� ���� �����ֱ� ����
		
		int page = 1;
		// Page�� count�ϱ� ���� ���� ���� (�ʱ�ȭ 1)
		int totalCnt = 0;
		// �� ������ count�ϱ� ���� ���� ���� (�ʱ�ȭ 0)
		
		if(pageVo.getPageNo() == 0){
			pageVo.setPageNo(page);
		}
		// Page�� count�Ͽ� ȭ�鿡 Page�� ǥ��� �� �ֵ��� ��
		
		boardList = boardService.SelectBoardList(pageVo);
		// PageVo�� �̿��Ͽ� List �ȿ� ����ִ� BoardVo ���� ������ �� �ֵ��� Service�� ���� ����
		totalCnt = boardService.selectBoardCnt();
		// Cnt���� Service�� ���� �������� ���ư��鼭 cnt�� ������ �� �ֵ���
		ComList = boardService.SelectTypeList(comVo);
		// Com_CodeVo�� ����� ���� Service�� ����
		
		model.addAttribute("boardList", boardList);
		model.addAttribute("totalCnt", totalCnt);
		model.addAttribute("pageNo", page);
		model.addAttribute("comList", ComList);
		// Model�� �̿��Ͽ� ��Ÿ���� ���� ���� View�� ���� key, value�� ���·� key�� �̿��Ͽ� View���� �ش� ���� ȭ������ ��Ÿ�� �� ����
		
		return "board/boardList";
		// board/boardList�� ȭ���� �Ѿ �� �ֵ��� ��
	}
	
	// List���� ���� Ŭ���� ��� �������� ȭ��
	@RequestMapping(value = "/board/{boardType}/{boardNum}/boardView.do", method = RequestMethod.GET)
	// board/{boardType}/{boardNum}/boardView.do�� ����Ǹ� GET �������� ������ : GET �������� �����;� PathVariable�� �̿��Ͽ� ���� �̾Ƴ� �� ����
	public String boardView(Locale locale, Model model, @PathVariable("boardType")String boardType, @PathVariable("boardNum")int boardNum) throws Exception {

		BoardVo boardVo = boardService.selectBoard(boardType, boardNum);
		// boardType�� boardNum�� �̿��Ͽ� ������ ���� Ư����
		
		model.addAttribute("boardNum", boardNum);
		model.addAttribute("board", boardVo);
		model.addAttribute("com", boardVo.getComList());
		// Model�� �̿��Ͽ� ��Ÿ���� ���� ���� View�� ���� key, value�� ���·� key�� �̿��Ͽ� View���� �ش� ���� ȭ������ ��Ÿ�� �� ����
		// boardVo.getCom_codeList�� BoardVo�ȿ� colletion ���·� ������ Com_CodeVo���� ���� �������� ����
		
		return "board/boardView";
		// board/boardView�� ȭ���� �Ѿ �� �ֵ��� ��
	}
	
	// �Խñ� �ۼ��� �������� ȭ��
	@RequestMapping(value = "/board/boardWrite.do", method = RequestMethod.GET)
	// board/boardWrite.do�� ����
	public String boardWrite(Locale locale, Model model, ComVo comVo) throws Exception {
		
		List<ComVo> ComList = new ArrayList<ComVo>();
		// Com_CodeVo�� ����� ���� �����ֱ� ����
		ComList = boardService.SelectTypeList(comVo);
		// �ۼ��� ������ �� SelectBox�� CodeId ���� �޾� Service�� �Ѱ��� 
		model.addAttribute("comList", ComList);
		// Model�� �̿��Ͽ� ��Ÿ���� ���� ���� View�� ���� key, value�� ���·� key�� �̿��Ͽ� View���� �ش� ���� ȭ������ ��Ÿ�� �� ����
		// DB�� �����Ͽ� SelectBox�� List�� �����ֱ� ����
		
		return "board/boardWrite";
		// board/boardWrite�� ȭ���� �Ѿ �� �ֵ��� ��
	}
	
	// �Խñ��� �ۼ��Ǵ� ����
	@RequestMapping(value = "/board/boardWriteAction.do", method = RequestMethod.POST)
	// board/boardWriteAction.do�� �����ϰ� POST������ �̿��ؾ� url�� Data�� ������� �ʴ´�
	@ResponseBody
	// Json type���� �Ѿ�� data���� �̿��ϱ� ���ؼ� ���
	public String boardWriteAction(Locale locale, BoardVo boardVo, ComVo comVo) throws Exception {
		
		HashMap<String, String> result = new HashMap<String, String>();
		// result�� Ȯ���ϱ� ���� HashMap�̿�
		CommonUtil commonUtil = new CommonUtil();
		
		HashMap<String, Object> hashmap = new HashMap<String, Object>();
		// BoardVo ������ data : boardTitle, boardComment ��
		// Com_CodeVo ������ data : codeId, codeName ��
		// ������ �Բ� ��� �����ϱ� ���� ( = Vo�� �ΰ� �����ϱ� ����) HashMap�� ���
		
		hashmap.put("boardVo", boardVo);
		hashmap.put("comVo", comVo);
		// HashMap�� �ΰ��� ������ ����
		
		int resultCnt = boardService.boardInsert(hashmap);
		// Service�� ���� ����� ���� �Ǵ��� Ȯ���ϴ� Cnt ���� ����
		
		result.put("success", (resultCnt > 0) ? "Y":"N");
		// jsp���� �ۼ��� script������ success�� ���� 0�� �̻��̶�� Y�� �ƴ϶�� N�� ����ϵ��� �� (���׿����� ���)
		String callbackMsg = commonUtil.getJsonCallBackString(" ",result);
		// callbackMsg�� ����
		
		System.out.println("callbackMsg::" + callbackMsg);
		// callbackMsg�� ����� ��µǴ��� Ȯ��
		
		return callbackMsg;
		// callbackMsg�� ȭ�鿡�� ��µ� �� �ֵ��� ����
	}
	
	// �Խñ� ����
	@RequestMapping(value = "/board/boardDeleteAction.do", method = RequestMethod.GET)
	// board/boardDeleteAction.do�� ����
	public String boardDeleteAction(Locale locale, @RequestParam("boardNum") int boardNum) throws Exception {
		// boardNum�� �̿��Ͽ� ������ ���� �����ǵ��� ��
		

		int result = boardService.boardDelete(boardNum);
		// result ������ ����Ͽ� Service�� ����� ���� ���޵Ǵ��� Ȯ��
		if(result == 0) {
			return "board/boardDelete";
		}
		// result ���� 0�̶�� �̹� ������ ������ <�̹� ������ �Խñ� �Դϴ�>��� �ȳ� �������� �̵��� �� �ֵ��� ��
		
		return "redirect:boardList.do";
		// result ���� 1�̶�� ���� ������ ���� ����Ǹ� ������� redirect�� �̿��Ͽ� boardList.do�� ������
	}
	
	// �Խñ� ����
	@RequestMapping(value = "/board/boardModify.do", method = RequestMethod.GET)
	// board/boardModify.do�� ���� GET������ �̿��Ͽ� url���� ���� Ȯ���� �� �ֵ��� ��
	public String boardModify(Locale locale, Model model, @RequestParam("codeId")String boardType, @RequestParam("boardNum")int boardNum, ComVo comVo) throws Exception {
		
		List<ComVo> ComList = new ArrayList<ComVo>();
		// Com_CodeVo�� ����� ���� �����ֱ� ����
		ComList = boardService.SelectTypeList(comVo);
		// CodeId�� ���� ������ �� �ֵ��� SelectBox List�� �����ֱ� ���� Service�� ���� ������
	
		BoardVo boardVo = boardService.selectBoard(boardType, boardNum);
		// boardType�� boardNum�� �̿��Ͽ� ������ �ۼ��� ���� �ҷ��� �� �ֵ��� ��
		
		model.addAttribute("boardNum", boardNum);
		model.addAttribute("board", boardVo);
		model.addAttribute("comList", ComList);
		// Model�� �̿��Ͽ� View�� key, value�� ����
		
		return "board/boardModify";
	}
	
	// �Խñ� ���� method
	@RequestMapping(value = "/board/boardModifyAction.do", method = RequestMethod.POST)
	@ResponseBody
	public String boardModifyAction(Locale locale, Model model, BoardVo boardVo, ComVo comVo) throws Exception {
		
		HashMap<String, String> result = new HashMap<String, String>();
		// result�� Ȯ���ϱ� ���� HashMap�̿�
		CommonUtil commonUtil = new CommonUtil();
		
		HashMap<String, Object> hashmap = new HashMap<String, Object>();
		// BoardVo ������ data : boardTitle, boardComment ��
		// Com_CodeVo ������ data : codeId, codeName ��
		// ������ �Բ� ��� �����ϱ� ���� ( = Vo�� �ΰ� �����ϱ� ����) HashMap�� ��� -> Write�� ����
		
		hashmap.put("boardVo", boardVo);
		hashmap.put("comVo", comVo);
		// HashMap�� �ΰ��� ������ ����
		
		int resultCnt = boardService.boardModify(hashmap);
		// �Է��� ������ ���������� ������ �� �ֵ��� Service�� ���� ����, resultCnt�� �̿��Ͽ� callbackMsg �ۼ� -> Write�� ����
		
		result.put("success", (resultCnt > 0) ? "Y":"N");
		String callbackMsg = commonUtil.getJsonCallBackString(" ",result);
		
		return callbackMsg;
	}
	
	@RequestMapping(value = "/board/boardSelectAction.do", method = RequestMethod.POST)
	@ResponseBody
	public String boardSelectAction(@RequestParam(value = "checkArr[]") List<String> checkArr) throws Exception {
		
		logger.info("�Ѿ�� �� Ȯ�� : " + checkArr);
		
		for(int i = 0; i<checkArr.size(); i++) {
			System.out.println(checkArr.get(i));
			System.out.println("*************");
		}

		
		
		return "redirect:boardList.do";
		
	}

}
