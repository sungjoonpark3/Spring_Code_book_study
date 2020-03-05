package org.zerock.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.domain.BoardVO;
import org.zerock.service.BoardService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;


@Log4j
@AllArgsConstructor
@Controller
@RequestMapping("/board/*")
public class BoardController {
	
	private BoardService service;
	
	@GetMapping("/list")
	public void list(Model model) {
		
		log.info("list");	
		model.addAttribute("list",service.getList());		
	}
	
	@GetMapping("/register")
	public void register() {
		
	}
	
	@PostMapping("/register")
	public String register(BoardVO board, RedirectAttributes rttr) throws IOException {
		
		log.info("register.:"+board);
		System.out.println("register 테스트"+board);
		service.register(board);
		
		rttr.addFlashAttribute("result",board.getBno());
		
		return "redirect:/board/list";
	}
	
	//@GetMapping("/get")
	@GetMapping({"/get","/modify"})
	public void get(@RequestParam("bno") Long bno,Model model) {
		log.info("/get or modify");
		model.addAttribute("board", service.get(bno));
	}
	
	@PostMapping("/modify")
	public String modify(BoardVO board, RedirectAttributes rttr) {
		log.info("modify:"+board);
		
		if(service.modify(board)) {
			rttr.addFlashAttribute("result", "sucess");
		}
		return "redirect:/board/list";
		}
	@PostMapping({"/get","/remove"})
	public String remove(@RequestParam("bno") Long bno, RedirectAttributes rttr) {
		log.info("remove"+bno);
		
		if(service.remove(bno)) {
			rttr.addFlashAttribute("result","sucess");
			
		}
		return "redirect:/board/list";
	}
}


	