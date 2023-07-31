package com.ict.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ict.model.service.GuestbookService;
import com.ict.model.vo.GuestbookVO;

@Controller
public class GuestbookController {
	@Autowired
	private GuestbookService guestbookService;

	public GuestbookService getGuestbookService() {
		return guestbookService;
	}

	public void setGuestbookService(GuestbookService guestbookService) {
		this.guestbookService = guestbookService;
	}
	
	@GetMapping("/guestbook_list.do")
	public ModelAndView guestbookList() {
		ModelAndView mv = new ModelAndView("guestbook/list");
		List<GuestbookVO> list = guestbookService.guestbookList();
		mv.addObject("list", list);
		return mv;
	}
}
