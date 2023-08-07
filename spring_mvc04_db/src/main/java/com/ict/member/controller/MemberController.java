package com.ict.member.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ict.member.model.service.MemberService;
import com.ict.member.model.vo.MemberVO;

@Controller
public class MemberController {
	@Autowired
	private MemberService memberService;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@GetMapping("/member_reg.do")
	public ModelAndView getMemberRegForm() {
		return new ModelAndView("members/addForm");
	}
	
	//파라미터값은 정보들 다 들고와서 vo만들기.
	//db를 가야해서 서비스 만들자.
	@PostMapping("/member_add.do")
	public ModelAndView getMemberAdd(MemberVO m2vo) {
		ModelAndView mv = new ModelAndView("redirect:/");
		//  "/" : root 이고, 첫페이지를 뜻하기 때문에. 이렇게만쓰면 첫 페이지인 인덱스로.
		// 그리고 흔적이 남거나 정보가 남아질수있기때문에 redirect 하면 응답요청을 깨긋하게 하기때문에 그렇다.
			
		// 디비에 집어넣기 전에 패스워드 암호화해야한다.
		// 1. 그래서 먼저 autowired 를 BC해준다. 
		// (=>passwordEncoder 라고 이름 지정한 것은 spring-security에서 썼기때문에 여기서 이름을 그렇게 쓸수있다.)
			
		// 2. 작성화면에서 넣은 비번을 암호화처리하고 setter로 넣어준다. 
		
		// 패스워드 암호화 하자 
		// 이거의 길이는 db에 chear & 60으로 최소 지정하자.
		m2vo.setM_pw(passwordEncoder.encode(m2vo.getM_pw()));
		
		// 3. 그리고 디비 다녀오는일 처리 시키자.
		int result = memberService.getMemberAdd(m2vo);
		return mv;
	}
	@PostMapping("/member_login.do")
	// 비번이있기 때문에 post 
	public ModelAndView getMemberLodgIn(MemberVO m2vo, HttpSession session) {
		ModelAndView mv = new ModelAndView("redirect:/");
		
		// 입력한 id의 패스워드를 DB에 가져와서 입력한 pwd와 비교해서 맞으면 성공 틀리면 실패
		// id로 DB에 저장된 pwd 가져오기 
		
		MemberVO mvo = memberService.getMemberPwd(m2vo.getM_id());
		if(! passwordEncoder.matches(m2vo.getM_pw(), mvo.getM_pw())) {
			session.setAttribute("loginChk", "fail");
			return mv;
		}else {
			session.setAttribute("mvo", mvo);
			session.setAttribute("loginChk", "ok");
			return mv;
		}
	}
	
	@GetMapping("/member_logout.do")
	public ModelAndView getLogout(HttpSession session) {
		// 세션 초기화
		// session.invalidate();
		session.removeAttribute("mvo");
		session.removeAttribute("loginChk");
		return new ModelAndView("redirect:/");
	}
}