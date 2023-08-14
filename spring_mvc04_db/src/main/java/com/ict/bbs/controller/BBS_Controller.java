package com.ict.bbs.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.events.Comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ict.bbs.model.service.BBS_Service;
import com.ict.bbs.model.vo.BBS_VO;
import com.ict.bbs.model.vo.Comment_VO;
import com.ict.common.Paging;
import com.ict.model.vo.GuestBook2VO;

@Controller
public class BBS_Controller {

	@Autowired
	private BBS_Service bBs_Service;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private Paging paging;

	// 리스트
	// RequestMapping은 get방식도 받고 post방식도 받음.
	@RequestMapping("/bbs_list.do")
	public ModelAndView bbsList(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("bbs/list");

		// 전체 게시물의 수
		int count = bBs_Service.getTotalCount();
		paging.setTotalRecord(count); 

		// 전체 페이지의 수
		if (paging.getTotalRecord() <= paging.getNumPerPage()) {
			paging.setTotalPage(1);
		} else {
			paging.setTotalPage(paging.getTotalRecord() / paging.getNumPerPage());
			if (paging.getTotalRecord() % paging.getNumPerPage() != 0) {
				paging.setTotalPage(paging.getTotalPage() + 1);
			}
		}

		// 현재 페이지
		String cPage = request.getParameter("cPage");
		if (cPage == null) {
			paging.setNowPage(1);
		} else {
			// integer 쓰는 이유는 paging에서 vo 만들 때 int를 줬기 때문에
			paging.setNowPage(Integer.parseInt(cPage));
		}

		// begin, end 대신 limit, offset
		// limit = paging.getNumPerPage();

		// offset = limit * (현재 페이지-1);
		paging.setOffset(paging.getNumPerPage() * (paging.getNowPage() - 1));

		// 시작블록과 끝블록 구하기
		// BeginBlock은 EndBlock을 구하고 난 후 구한다.
		paging.setBeginBlock(
				(int) ((paging.getNowPage() - 1) / paging.getPagePerBlock()) * paging.getPagePerBlock() + 1);
		paging.setEndBlock(paging.getBeginBlock() + paging.getPagePerBlock() - 1);

		// 주의사항
		if (paging.getEndBlock() > paging.getTotalPage()) {
			paging.setEndBlock(paging.getTotalPage());
		}

		List<BBS_VO> bbs_list = bBs_Service.getList(paging.getOffset(), paging.getNumPerPage());
		
		
		
		mv.addObject("bbs_list", bbs_list);
		mv.addObject("paging", paging);
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
			if (file.isEmpty()) {
				bvo.setF_name("");
			} else {
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
			if (res > 0) { // 성공
				return mv;
			} else { // 실패
				return null;
			}
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}

	// 상세보기
	@GetMapping("/bbs_onelist.do")
	public ModelAndView bbsOneList(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("bbs/onelist");
		String b_idx = request.getParameter("b_idx");
		String cPage = request.getParameter("cPage");

		// 조회수 업데이트
		int result = bBs_Service.getHitUpdate(b_idx);

		// 상세보기
		BBS_VO bvo = bBs_Service.getOneList(b_idx);

		// 댓글 가져오기
		List<Comment_VO> c_list = bBs_Service.getCommList(b_idx);

		mv.addObject("c_list", c_list);
		mv.addObject("bvo", bvo);
		mv.addObject("cPage", cPage);
		return mv;
	}

	// 방법 1) jsp 방식으로 파라미터값 받아서 넘기는 거.
//	@PostMapping("/com_insert.do")
//	public ModelAndView commentInsert(Comment_VO cvo,
//			HttpServletRequest request) {
//		String cPage = request.getParameter("cPage");
//		String b_idx = request.getParameter("b_idx");
//		//String b_idx = cvo.getB_idx();  이렇게도 쓸 수 있다.
//		ModelAndView mv = new ModelAndView("redirect:/bbs_onelist.do?b_idx="+b_idx+"&cPage="+cPage);
//		int result = bBs_Service.getCommInsert(cvo);
//		return mv;
//	}

	// 방법 2) Spring 방식으로 파라미터값 받아서 넘기는 거(@ModelAttribute 사용)
	// @ModelAttribute("cPage")String cPage,
	// 파라미터 cPage를 받아서 model에 cPage라는 이름으로 저장된다.
	// 다음에 넘어갈 페이지에게 전달
	@PostMapping("/com_insert.do")
	public ModelAndView commentInsert(
			Comment_VO cvo, 
			@ModelAttribute("cPage") String cPage,
			@ModelAttribute("b_idx") String b_idx) {
		ModelAndView mv = new ModelAndView("redirect:/bbs_onelist.do");
		int result = bBs_Service.getCommInsert(cvo);
		return mv;
	}

	// 댓글 삭제
	@PostMapping("/com_delete.do")
	public ModelAndView commentDelete(
			// 필요한 파라미터값 3개 받았음.
			@RequestParam("c_idx") String c_idx, 
			@ModelAttribute("cPage") String cPage,
			@ModelAttribute("b_idx") String b_idx) {
		ModelAndView mv = new ModelAndView("redirect:/bbs_onelist.do");
		int result = bBs_Service.getCommDelete(c_idx);
		return mv;
	}

	// 삭제 폼
	@PostMapping("/bbs_deleteForm.do")
	public ModelAndView deleteForm(
			@ModelAttribute("cPage") String cPage, 
			@ModelAttribute("b_idx") String b_idx) {
		ModelAndView mv = new ModelAndView("bbs/delete");
		return mv;
	}

	// 삭제
	@PostMapping("/bbs_delete.do")
	public ModelAndView bbsDelete(
			@RequestParam("pwd") String pwd, 
			@ModelAttribute("cPage") String cPage,
			@ModelAttribute("b_idx") String b_idx) {

		ModelAndView mv = new ModelAndView("redirect:/bbs_list.do");

		// 비밀번호가 맞는지 체크하기
		// DB에서 암호 얻기
		BBS_VO bvo = bBs_Service.getOneList(b_idx);
		String dbpwd = bvo.getPwd(); // db 패스워드

		// passwordEncoder.matches(암호화 되지 않은 것, 암호화 된 것)
		if (!passwordEncoder.matches(pwd, dbpwd)) {
			// post방식은 redirect 못 감.
			// mv.setViewName("redirect:/bbs_deleteForm.do");
			mv.setViewName("bbs/delete");
			mv.addObject("pwchk", "fail");
			return mv;
		} else {
			// 원글 삭제 시 상태값을 0 -> 1로 변경 시킨다.
			int result = bBs_Service.getDelete(b_idx);
			mv.setViewName("redirect:/bbs_list.do");
			return mv;
		}
	}

	// 수정 폼
	@PostMapping("/bbs_updateForm.do")
	public ModelAndView bbsUpdateForm(
			@ModelAttribute("cPage") String cPage, 
			@ModelAttribute("b_idx") String b_idx) {
		ModelAndView mv = new ModelAndView("bbs/update");
		// b_idx로 onelist 정보 가져오기
		BBS_VO bvo = bBs_Service.getOneList(b_idx);
		mv.addObject("bvo", bvo);
		return mv;
	}

	// 수정
	@PostMapping("/bbs_update.do")
	public ModelAndView bbsUpdate(
			BBS_VO bvo, HttpServletRequest request, 
			@ModelAttribute("cPage") String cPage,
			@ModelAttribute("b_idx") String b_idx) {
		
		ModelAndView mv = new ModelAndView();

		// 비밀번호가 맞는지 체크하기
		// DB에서 암호 얻기
		BBS_VO bvo2 = bBs_Service.getOneList(b_idx);
		String dbpwd = bvo2.getPwd(); // db 패스워드

		// passwordEncoder.matches(암호화 되지 않은 것, 암호화 된 것)
		if (!passwordEncoder.matches(bvo.getPwd(), dbpwd)) {
			// 파라미터 값 bvo로 받기 때문에 암호화 되지않은거 bvo.getPwd()로 받는다)
			mv.setViewName("bbs/update");
			mv.addObject("pwchk", "fail");
			mv.addObject("bvo", bvo);
			bvo.setF_name(bvo.getOld_f_name());
			
			return mv;
		} else {
			try {
				String path = request.getSession().getServletContext().getRealPath("/resources/images");
				MultipartFile file = bvo.getFile();
				if (file.isEmpty()) {
					bvo.setF_name(bvo.getOld_f_name());
				} else {
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
				int res = bBs_Service.getUpdate(bvo);
				if (res > 0) { // 성공
					mv.setViewName("redirect:/bbs_onelist.do");
					return mv;
				} else { // 실패
					return null;
				}
			} catch (Exception e) {
				System.out.println(e);
				return null;
			}
		}
	}
	
	// 다운로드
	@GetMapping("/down.do")
	public void down(
			@RequestParam("f_name")String f_name, 
			HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String path = request.getSession().getServletContext().getRealPath("/resources/images/"+f_name);
			String r_path = URLEncoder.encode(path,"utf-8");
			response.setContentType("application/x-msdownload");
			response.setHeader("Content-Disposition", "attachment; filename="+r_path);
			// 여기 까진 모양만 나오고 실제 다운은 안됨.
			// 실제 다운로드 하려면 밑에를 해야함.
			File file = new File(new String(path.getBytes()));
			FileInputStream in = new FileInputStream(file);
			OutputStream out = response.getOutputStream();
			FileCopyUtils.copy(in, out);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}