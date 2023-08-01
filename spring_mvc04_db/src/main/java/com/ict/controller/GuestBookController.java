package com.ict.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ict.model.service.GuestBookService;
import com.ict.model.vo.GuestBookVO;

@Controller
public class GuestBookController {
	// DB(일처리)에 갔다오려면 무조건 Service를 해야 하고, 어노테이션 해야 함!
	@Autowired
	private GuestBookService guestbookService;

	//getter, setter
	public GuestBookService getGuestbookService() {
		return guestbookService;
	}

	public void setGuestbookService(GuestBookService guestbookService) {
		this.guestbookService = guestbookService;
	}
	
	// 전체 리스트 보기
	@GetMapping("/guestbook_list.do")
	public ModelAndView getGuestBookList() {
		ModelAndView mv = new ModelAndView("guestbook/list");
		List<GuestBookVO> glist = guestbookService.getGuestBookList();
		mv.addObject("glist", glist);
		return mv;
	}
	
	@GetMapping("/guestbookAddForm.do")
	public ModelAndView getGuestBookAddForm() {
		return new ModelAndView("guestbook/write");
	}
	
	// 삽입
	@PostMapping("/guestbook_writeOK.do")
	public ModelAndView getGuestBookInsert(GuestBookVO gvo) {
		ModelAndView mv = new ModelAndView("redirect:/guestbook_list.do");
		int result = guestbookService.getGuestBookInsert(gvo);
		return mv;
	}
}
