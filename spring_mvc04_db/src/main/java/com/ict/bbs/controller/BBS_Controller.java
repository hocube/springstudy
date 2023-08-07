package com.ict.bbs.controller;

import java.io.File;
import java.util.List;
import java.util.UUID;

import javax.management.loading.PrivateClassLoader;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ict.bbs.model.service.BBS_Service;
import com.ict.bbs.model.vo.BBS_VO;

@Controller
public class BBS_Controller {
	
	@Autowired
	private BBS_Service bBs_Service;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	// 리스트
	@GetMapping("/bbs_list.do")
	public ModelAndView getList(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("bbs/list");
		List<BBS_VO> bbs_list = bBs_Service.getList();
		mv.addObject("bbs_list", bbs_list);
		return mv;
	}
	
	@GetMapping("/bbs_insertForm.do")
	public ModelAndView bbsInsertForm(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("bbs/write");
		return mv;
	}
	
	// 글쓰기
	@PostMapping("/bbs_insert.do")
	public ModelAndView bbsInsert(BBS_VO bvo, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("redirect:/bbs_list.do");
		try {
			String path = request.getSession().getServletContext().getRealPath("/resources/images");
			MultipartFile file = bvo.getFile();
			if(file.isEmpty()) {
				bvo.setF_name("");
			}else {
				// 같은 이름의 파일이 없도록 UUID 사용
				UUID uuid = UUID.randomUUID();
				String f_name = uuid.toString() + "_" + bvo.getFile().getOriginalFilename();
				// DB에 저장
				bvo.setF_name(f_name); 
				
				// 이미지 저장
				byte[] in = bvo.getFile().getBytes();
				File out = new File(path, f_name);
				FileCopyUtils.copy(in, out);
			}
			// 패스워드 암호화
			// 한줄로 바꿔서 쓸 수도 있음.
			bvo.setPwd(passwordEncoder.encode(bvo.getPwd()));
			int res = bBs_Service.getInsert(bvo);
			if(res > 0) { // 성공
				return mv;
			}else { //실패
				return null;
			}
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}
}