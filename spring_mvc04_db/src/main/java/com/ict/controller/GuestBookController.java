package com.ict.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ict.model.service.GuestBookService;
import com.ict.model.vo.GuestBookVO;

@Controller
public class GuestBookController {
	// DB(일처리)에 갔다오려면 무조건 Service를 해야 하고, 어노테이션 해야 함!
	@Autowired
	private GuestBookService guestbookService;
	@Autowired
	BCryptPasswordEncoder bcryptPasswordEncoder; 
	
	// 전체 리스트 보기
	@GetMapping("/guestbook_list.do")
	public ModelAndView getGuestBookList() {
		ModelAndView mv = new ModelAndView("guestbook/list");
		List<GuestBookVO> glist = guestbookService.getGuestBookList();
		mv.addObject("glist", glist);
		return mv;
	}
	
	// 방명록 작성폼으로 이동
	@GetMapping("/guestbookAddForm.do")
	public ModelAndView getGuestBookAddForm() {
		return new ModelAndView("guestbook/write");
	}
	
	// 삽입
	@PostMapping("/guestbook_writeOK.do")
	public ModelAndView getGuestBookInsert(GuestBookVO gvo) {
		// 이걸 사용하면 암호가 $2a$10$fZL/N/Xotw.zH2n8A/JbUugjC4SegtDKzh2t.GTAauv5k8WRljApa 이런식으로 들어감.
		// String pwd = bcryptPasswordEncoder.encode(gvo.getPwd());
		// gvo.setPwd(pwd);
		// 패스워드 검사(암호화 되어있는 비번은 사용 못 함)
		/*
		String rawPassword = "1234"; //입력된 비밀번호
		//암호화되어 DB에 저장된 패스워드
		String encodedPassword = "$2a$10$fZL/N/Xotw.zH2n8A/JbUugjC4SegtDKzh2t.GTAauv5k8WRljApa";
		if(passwordEncoder.matches(rawPassword, encodedPassword)) {
			System.out.println("계정정보 일치");
		}else{
		System.out.println("계정정보 불일치");
		}
		*/
		ModelAndView mv = new ModelAndView("redirect:/guestbook_list.do");
		int result = guestbookService.getGuestBookInsert(gvo);
		return mv;
	}
	
	// 상세보기
	// idx는 onelist.jsp에도 사용하기 때문에 넘겨야 한다.
	@GetMapping("/guestbook_oneList.do")
	public ModelAndView getGuestBookOneList(@ModelAttribute("idx") String idx) {
		ModelAndView mv = new ModelAndView("guestbook/onelist");
		GuestBookVO gvo = guestbookService.getGuestBookOneList(idx);
		mv.addObject("gvo", gvo);
		return mv;
	}
	
	
	// 삭제 확인
	@PostMapping("/guestbook_delete_Form.do")
	public ModelAndView getGuestBookDeleteForm(@ModelAttribute("idx") String idx) {
		ModelAndView mv = new ModelAndView("guestbook/delete");
		// jsp 실제 삭제할때 비밀번호를 검사하기 위해서 getGuestBookOneList()를 실행하자
		GuestBookVO gvo = guestbookService.getGuestBookOneList(idx);
		mv.addObject("gvo", gvo);
		return mv;
	}
	
	// 삭제
	@PostMapping("/guestbook_delete.do")
	public ModelAndView getGuestBookDeleteOK(String idx) {
		ModelAndView mv = new ModelAndView("redirect:/guestbook_list.do");
		int result = guestbookService.getGuestBookDelete(idx);
		return mv;
	}
	
	// 수정 폼
	@PostMapping("/guestbook_edit_Form.do")
	public ModelAndView getGuestBookEditForm(String idx) {
		ModelAndView mv = new ModelAndView("guestbook/update");
		GuestBookVO gvo = guestbookService.getGuestBookOneList(idx);
		mv.addObject("gvo", gvo);
		return mv;
	}
	
	// 수정
	@PostMapping("/guestbook_edite.do")
	public ModelAndView getGuestBookEditOK(GuestBookVO gvo) {
		ModelAndView mv = new ModelAndView("redirect:guestbook_oneList.do?idx="+gvo.getIdx());
		int result = guestbookService.getGuestBookUpdate(gvo);
		return mv;
	}
}
