package com.ict.board.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ict.board.model.service.Board_Service;
import com.ict.board.model.vo.Board_VO;
import com.ict.common.Paging;

@Controller
public class Board_Controller {
	@Autowired
	private Board_Service board_Service;
	@Autowired
	private Paging paging;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@RequestMapping("/board_list.do")
	public ModelAndView boardList(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("board/board_list");

		// 페이징 기법
		// 전체 게시물의 수
		int count = board_Service.getTotalCount();
		paging.setTotalRecord(count);

		// 전체 페이지의 수
		if (paging.getTotalRecord() <= paging.getNumPerPage()) { // 10개까지는 1페이지
			paging.setTotalPage(1);
		} else { // 넘어가는 경우는
			paging.setTotalPage(paging.getTotalRecord() / paging.getNumPerPage());
			// 나머지가 있으면 1페이지 증가시켜라. (원래꺼에서 1 증가)
			if (paging.getTotalRecord() % paging.getNumPerPage() != 0) {
				paging.setTotalPage(paging.getTotalPage() + 1);
			}
		}

		// 현재 페이지
		String cPage = request.getParameter("cPage");
		if (cPage == null) {
			paging.setNowPage(1);
		} else {
			// integer 쓰는 이유는 paging에서 vo만들 때 int를 줬기 때문에
			paging.setNowPage(Integer.parseInt(cPage));
		}

		// begin, end 대신 limit, offset
		// limit = paging.getNumPerPage()
		// offset = limit * (paging.getNowPage() -1)

		paging.setOffset(paging.getNumPerPage() * (paging.getNowPage() - 1));

		// 시작블록과 끝블록
		paging.setBeginBlock(
				(int) ((paging.getNowPage() - 1) / paging.getPagePerBlock()) * paging.getPagePerBlock() + 1);
		paging.setEndBlock(paging.getBeginBlock() + paging.getPagePerBlock() - 1);

		// 주의사항
		// EndBlock이 TotalPage보다 클때가 있다.
		if (paging.getEndBlock() > paging.getTotalPage()) {
			paging.setEndBlock(paging.getTotalPage());
		}

		List<Board_VO> board_list = board_Service.getList(paging.getOffset(), paging.getNumPerPage());

		mv.addObject("board_list", board_list);
		mv.addObject("paging", paging);
		return mv;
	}

	@GetMapping("/board_insertForm.do")
	public ModelAndView boardInsertForm() {
		return new ModelAndView("board/board_write");
	}

	@PostMapping("/board_insert.do")
	public ModelAndView boardInsert(Board_VO bv, HttpServletRequest request) {

		ModelAndView mv = new ModelAndView("redirect:/board_list.do");
		try {
			// 올릴때는 폴더이름만 써줘도 됨.
			String path = request.getSession().getServletContext().getRealPath("/resources/images");
			MultipartFile file = bv.getFile();
			// 첨부파일이 있을 수도 있고 없을 수도 있기 때문에 if로 처리한다.
			// 무조건 있어야 하면 if를 안 하면 된다.
			// 만약 프로필 첨부파일을 안 넣었을 때 기본 프로필 사진을 제공할거면 여기다 넣어줘도 된다.
			if (file.isEmpty()) {
				bv.setF_name("");
			} else {
				// 같은 이름 없도록 UUID사용
				UUID uuid = UUID.randomUUID();
				String f_name = uuid.toString() + "_" + bv.getFile().getOriginalFilename();
				bv.setF_name(f_name);

				// 이미지 저장
				byte[] in = bv.getFile().getBytes();
				File out = new File(path, f_name);
				FileCopyUtils.copy(in, out);
			}

			// 패스워드 암호화
			bv.setPwd(passwordEncoder.encode(bv.getPwd()));
			int res = board_Service.getInsert(bv);
			return mv;
		} catch (Exception e) {
		}
		return null;
	}

	// 상세보기
	@GetMapping("/board_onelist.do")
	// 내가 한거
	//public ModelAndView boardOneList(HttpServletRequest request) {
	//	ModelAndView mv = new ModelAndView("board/board_onelist");
	//	String idx = request.getParameter("idx");
	//	String cPage = request.getParameter("cPage");
	// 선생님이 한거
	public ModelAndView boardOneList(@ModelAttribute("cPage")String cPage,
			@ModelAttribute("idx")String idx) {
		ModelAndView mv = new ModelAndView("board/board_onelist");

		// DB처리 할 것들
		// 한번에 DB를 두번 이상 가야 한다면 한번에 묶어서 처리해주는것 = 트랜잭션
		// 첫번째 두번째꺼 두개다 잘되야지만 넘어가진다. 하지만 첫번째꺼가 잘못되면 다시 rollback됨.
		
		// 조회수 업데이트  
		int res = board_Service.getHitUpdate(idx);
		// 상세보기
		Board_VO bv = board_Service.getOneList(idx);

		mv.addObject("bv", bv);
		// 파라미터로 했기 때문에 안써도 된다.
		// mv.addObject("cPage", cPage);
		return mv;
	}
	
	// 첨부파일 다운로드
	// a링크 걸린거면 get방식으로.
	// 다운로드니깐 void. ModelAndView 아님
	@GetMapping("/board_down.do")
	public void boarDown(
			@RequestParam("f_name")String f_name,
			HttpServletRequest request, 
			HttpServletResponse response) {
		try {
			// 다운로드는 폴더와 + 다운로드 해야할 파일 이름까지 써줘야함.
			String path = request.getSession().getServletContext().getRealPath("/resources/images/" + f_name);
			String r_path = URLEncoder.encode(path,"utf-8");
			response.setContentType("application/x-msdownload");
			response.setHeader("Content-Disposition", "attachment; filename= "+r_path);
			// 여기까지 하면 브라우저 모양은 성공
			
			File file = new File(new String(path.getBytes()));
			FileInputStream in = new FileInputStream(file);
			OutputStream out = response.getOutputStream();
			FileCopyUtils.copy(in, out);
		} catch (Exception e) {
		}
	}
	
	// 덧글 폼
 	@PostMapping("/board_ans_insertForm.do")
	public ModelAndView boardAnsInsertForm(@ModelAttribute("cPage")String cPage,
			@ModelAttribute("idx")String idx) {
		return new ModelAndView("board/board_ans_write");	
	}
	
 	// 덧글 삽입
	@PostMapping("/board_ans_insert.do")
	public ModelAndView boardAnsInsert(@ModelAttribute("cPage")String cPage,
			@ModelAttribute("idx")String idx,
			Board_VO bv, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("redirect:/board_list.do");
		// 여기서부터 핵심!!!!!
		try {
			// 상세보기에서 groups, step, lev를 가져온다.
			Board_VO bvo = board_Service.getOneList(idx);
			
			int groups = Integer.parseInt(bvo.getGroups());
			int step = Integer.parseInt(bvo.getStep());
			int lev = Integer.parseInt(bvo.getLev());
			
			// step, lev를 하나씩 올리자(증가) 
			step ++;
			lev ++;
			
			// DB에 groups, lev를 업데이트하자
			// ★groups와 같은 원글을 찾아서 lev이 같거나 크면 lev을 증가시키자★
			Map<String, Integer> map = new HashMap<String, Integer>();
			map.put("groups", groups);
			map.put("lev", lev);
			
			int result = board_Service.getLevUpdate(map); 
			
			// 업데이트 이후부터는 댓글 쓴게 필요하니깐 bv를 가져와야 한다.
			bv.setGroups(String.valueOf(groups));
			bv.setStep(String.valueOf(step));
			bv.setLev(String.valueOf(lev));
			
			// 첨부파일 처리
			String path = request.getSession().getServletContext().getRealPath("/resources/images");
			MultipartFile file = bv.getFile();
			// 첨부파일이 있을 수도 있고 없을 수도 있기 때문에 if로 처리한다.
			// 무조건 있어야 하면 if를 안 하면 된다.
			// 만약 프로필 첨부파일을 안 넣었을 때 기본 프로필 사진을 제공할거면 여기다 넣어줘도 된다.
			if (file.isEmpty()) {
				bv.setF_name("");
			} else {
				// 같은 이름 없도록 UUID사용
				UUID uuid = UUID.randomUUID();
				String f_name = uuid.toString() + "_" + bv.getFile().getOriginalFilename();
				bv.setF_name(f_name);

				// 이미지 저장
				byte[] in = bv.getFile().getBytes();
				File out = new File(path, f_name);
				FileCopyUtils.copy(in, out);
			}

			// 패스워드 암호화
			bv.setPwd(passwordEncoder.encode(bv.getPwd()));
			// 삽입
			int res = board_Service.getAnsInsert(bv);
			
			return mv;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
}