package com.uni.spring.board.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.GsonBuilder;
import com.uni.spring.board.model.dto.Board;
import com.uni.spring.board.model.dto.PageInfo;
import com.uni.spring.board.model.dto.Reply;
import com.uni.spring.board.model.service.BoardService;
import com.uni.spring.common.CommException;
import com.uni.spring.common.Pagination;

@Controller
public class BoardController {
	
	@Autowired
	public BoardService boardService;
	
	
	@RequestMapping("listBoard.do")
	public String selectList(@RequestParam(value="currentPage", required = false, defaultValue = "1") int currentPage, Model model ) {
		
		//@RequestParam(value="currentPage") int currentPage  --> 값이 넘어오지않아서 에러발생
		
		//@RequestParam(value="currentPage", required = false) int currentPage
		//required : 해당파라미터가 필수인지 여부 (기본값 true : 필수)
		//required = false 값을 꼭 받아줄필요가 없다고 선언. -> null 이 들어올수있다
	
		//--> null 은 기본형 int 에 들어갈수 없기때문에 에러 발생 
		
		
		//@RequestParam(value="currentPage", required = false, defaultValue = "1" int currentPage
		// defaultValue : 넘어오는 값이 null 인경우 해당파라미터 기본값을 지정 
		
		int listCount = boardService.selectListCount();
		System.out.println( listCount);
		
		PageInfo pi = Pagination.getPageInfo(listCount, currentPage, 10, 5);
		
		ArrayList<Board> list = boardService.selectList(pi);
		
		model.addAttribute("list", list);
		model.addAttribute("pi", pi);
		return "board/boardListView";
	}
	
	@RequestMapping("enrollFormBoard.do")
	public String enrollForm() {
		return "board/boardEnrollForm";
	}
	
	@RequestMapping("insertBoard.do")
	public String insertBoard(Board b , HttpServletRequest request , @RequestParam(name = "uploadFile", required =false) MultipartFile file) {
		System.out.println(b);
		System.out.println(file.getOriginalFilename());
		
		
		if(!file.getOriginalFilename().equals("")) {
			String changeName = saveFile(file, request);
			
			
			if(changeName != null) {
				b.setOriginName(file.getOriginalFilename());
				b.setChangeName(changeName);
			}
			
		}
		
		boardService.insertBoard(b);
		
		
		return "redirect:listBoard.do";
	}

	//전달받은 파일을 업로드시키고 수정된 파일명을 리턴
	private String saveFile(MultipartFile file, HttpServletRequest request) {
		
		String resources = request.getSession().getServletContext().getRealPath("resources");
		String savePath = resources +"\\upload_files\\";
		
		
		String originName = file.getOriginalFilename();
		String currentTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		
		String ext = originName.substring(originName.lastIndexOf("."));
		
		String changeName = currentTime + ext;
		System.out.println("changeName : " + changeName);
		
		
		try {
			file.transferTo(new File(savePath+changeName));
		} catch (IllegalStateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new CommException("file Upload Error");
		}
		return changeName;
	}
	@RequestMapping("detailBoard.do")
	public ModelAndView selectBoard(int bno, ModelAndView mv) {
		
		
		Board b = boardService.selectBoard(bno);
		mv.addObject("b", b).setViewName("board/boardDetailView");
		return mv;
	}
	@RequestMapping("updateFormBoard.do")
	public ModelAndView updateForm(int bno, ModelAndView mv) {
		
		
		mv.addObject("b", boardService.selectBoard(bno))
		.setViewName("board/boardUpdateForm");
		
		return mv;
	}
	
	@RequestMapping("updateBoard.do")
	public ModelAndView updateBoard(Board b , ModelAndView mv, HttpServletRequest request,
									@RequestParam(name = "reUploadFile", required=false) MultipartFile file) {
		
		/*
		 * 1. 기존의 첨부파일 X, 새로 첨부된 파일 X 	
		 * 	  --> originName : null, changeName : null
		 * 
		 * 2. 기존의 첨부파일 X, 새로 첨부된 파일 O		
		 * 	  --> 서버에 업로드 후 
		 * 	  --> originName : 새로첨부된파일원본명, changeName : 새로첨부된파일수정명
		 * 
		 * 3. 기존의 첨부파일 O, 새로 첨부된 파일 X		
		 * 	  --> originName : 기존첨부파일원본명, changeName : 기존첨부파일수정명
		 * 
		 * 4. 기존의 첨부파일 O, 새로 첨부된 파일 O  
		 * 	  --> 서버에 업로드 후	
		 * 	  --> originName : 새로첨부된파일원본명, changeName : 새로첨부된파일수정명
		 */
		
		String orgChangeName = b.getChangeName();
		
		if(!file.getOriginalFilename().equals("")) {
			
			String changeName = saveFile(file, request);
			
			b.setOriginName(file.getOriginalFilename());
			b.setChangeName(changeName);
		}
		
		boardService.updateBoard(b);
		
		if(orgChangeName != null) {
			deleteFile(orgChangeName, request);
		}
		
		mv.addObject("bno", b.getBoardNo()).setViewName("redirect:detailBoard.do");
		return mv;
	}

	private void deleteFile(String orgChangeName, HttpServletRequest request) {
		
		String resources = request.getSession().getServletContext().getRealPath("resources");
		String savePath = resources +"\\upload_files\\";
		
		File deleteFile = new File(savePath + orgChangeName);
		
		deleteFile.delete();
	}
	
	@RequestMapping("deleteBoard.do")
	private String deleteBoard(int bno, String fileName , HttpServletRequest request) {
		
		boardService.deleteBoard(bno);
		
		if(!fileName.equals("")) {
			deleteFile(fileName, request);
			
		}
		
		return "redirect:listBoard.do";
	}
	
	@ResponseBody
	@RequestMapping(value = "rlistBoard.do", produces= "application/json; charset=utf-8")
	public String selectReplyList(int bno) {
		
		ArrayList<Reply> list = boardService.selectReplyList(bno);
		
		return new GsonBuilder().setDateFormat("yyyy 년 MM 월 dd 일 HH:mm:ss").create().toJson(list);
	}
	
	@ResponseBody
	@RequestMapping(value = "rinsertBoard.do")
	public String insertReply(Reply r) {
		int result = boardService.insertReply(r);
		
		return String.valueOf(result);
	}
	
	
	
	@ResponseBody
	@RequestMapping(value = "topListBoard.do", produces= "application/json; charset=utf-8")
	public String selectTopList() {
		
		ArrayList<Board> list = boardService.selectTopList();
		
		return new GsonBuilder().setDateFormat("yyyy 년 MM 월 dd 일 HH:mm:ss").create().toJson(list);
	}
}
