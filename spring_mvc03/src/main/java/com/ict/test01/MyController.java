package com.ict.test01;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ict.model.MyService;
import com.ict.model.VO;

@Controller
public class MyController {
	@Autowired
	private MyService service;

	public MyService getService() {
		return service;
	}

	public void setService(MyService service) {
		this.service = service;
	}

	@GetMapping(value = "/")
	public ModelAndView exec01(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("test01/result");
//		String msg = "Hello World";
//		mv.addObject("msg", msg);

//		MyService service = new MyService();
		String msg = service.getGreeting();
		mv.addObject("msg", msg);
		return mv;
	}
	
	
	
	// calc
	/*
	@PostMapping(value = "/calc.do")
	public ModelAndView exec02(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("test01/result2");
		String s1 = request.getParameter("s1");
		String s2 = request.getParameter("s2");
		String op = request.getParameter("op");
		
		String cPage = request.getParameter("cPage");
		String[] hobby = request.getParameterValues("hobby");
		
		String str = service.getCalc(s1, s2, op);
		mv.addObject("s1", s1);
		mv.addObject("s2", s2);
		mv.addObject("op", op);
		mv.addObject("str", str);
		mv.addObject("cPage", cPage);
		mv.addObject("hobby", hobby);
		return mv;
	}
	*/
	
	
	// calc 다른 방법으로 1.
	/*
	// 파라미터를 넣어서 해보기
	@PostMapping(value = "/calc.do")
	// vo의 변수이름과 파라미터이름이 같으면 자동으로 vo에 파라미터가 저장된다.
	public ModelAndView exec02(VO vo) {
		ModelAndView mv = new ModelAndView("test01/result2");
		
		String str = service.getCalc(vo.getS1(), vo.getS2(), vo.getOp());
		
		mv.addObject("vo", vo);
		mv.addObject("str", str);
		return mv;
	}
	*/
	
	
	// calc 다른 방법으로 2. 
	@PostMapping(value = "/calc.do")
	// VO vo => 파라미터 받아서 vo에 저장
	// 그대로 model에 저장한다. vo란 이름으로 => jsp에서 vo를 사용할 수 있다.
	public ModelAndView exec02(@ModelAttribute("vo")VO vo) {
							// Model이 나오면 저장해서 다음에 쓰겠다는 뜻
		ModelAndView mv = new ModelAndView("test01/result2");
		
		String str = service.getCalc(vo.getS1(), vo.getS2(), vo.getOp());
		
		// mv.addObject("vo", vo); //위에서 @ModelAttribute("vo") 이걸 했기 때문에 안해도 됨.
		mv.addObject("str", str);
		return mv;
	}
}