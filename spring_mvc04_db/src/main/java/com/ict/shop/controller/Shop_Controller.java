package com.ict.shop.controller;

import java.io.File;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.log.UserDataHelper.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ict.common.Paging;
import com.ict.member.model.vo.MemberVO;
import com.ict.shop.model.service.Shop_Service;
import com.ict.shop.model.vo.CartVO;
import com.ict.shop.model.vo.ShopVO;

@Controller
public class Shop_Controller {
	@Autowired
	private Shop_Service shop_Service;
	
	@Autowired
	private Paging paging;
	
	@GetMapping("/shop_list.do")
	public ModelAndView getShopList(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("shop/product_list");
		String category = request.getParameter("category");
		// list에 처음 오면 category가 null이다.
		// 다음부터는 lsit에 올때 category를 지정해주자
		if(category == null || category == "") {
			category = "ele002";
		}
		try {
			List<ShopVO> shoplist = shop_Service.getShopList(category);
			mv.addObject("shoplist", shoplist);
			return mv;
		} catch (Exception e) {
			e.printStackTrace();
			// 오류나면 오류페이지로 갈 수 있도록 해준다.
			return new ModelAndView("shop/error");
		}
	}
	
	// 상세보기
	@GetMapping("/shop_onelist.do")
	public ModelAndView getShopOneList(@RequestParam("idx")String idx) {
		ModelAndView mv = new ModelAndView("shop/product_content");
		try {
			ShopVO svo = shop_Service.getShopOneList(idx);
			mv.addObject("svo", svo);
			return mv;
		} catch (Exception e) {
			e.printStackTrace();
			return new ModelAndView("shop/error");
		}
	}
	 
	@GetMapping("/shop_addCart.do")
	public ModelAndView getShopAddCart(@ModelAttribute("idx")String idx, 
			HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("redirect:/shop_onelist.do");
		try {
			// 내 카트에 있는지 없는지 검사 
			// (1)로그인한 정보 (2) 제품정보. 두개를 가지고 카트에 있는지 알아보자
			
			// 로그인한 정보 가져오기
			String m_id = ((MemberVO) request.getSession().getAttribute("mvo")).getM_id();
			
			// idx를 이용해서 제품 정보를 가져오기
			ShopVO svo = shop_Service.getProductOneList(idx);
			
			// 제품정보와 로그인정보를 가지고 cart 정보를 구한다.
			CartVO cartVO = shop_Service.getCartOneList(m_id, svo.getP_num());
			
			if(cartVO == null) {
			// 카트 정보에 제품정보가 없으면 카트에 추가
				CartVO cvo = new CartVO();
				cvo.setP_num(svo.getP_num());
				cvo.setP_name(svo.getP_name());
				cvo.setP_price(String.valueOf(svo.getP_price()));
				cvo.setP_saleprice(String.valueOf(svo.getP_saleprice()));
				cvo.setM_id(m_id);
				int res = shop_Service.getCartInsert(cvo);
			}else {
			// 카트 정보에 제품정보가 있으면 제품의 개수를 1 증가하는 업데이트
				int res = shop_Service.getCartUpdate(cartVO);
			}
			return mv;
		} catch (Exception e) {
			e.printStackTrace();
			return new ModelAndView("shop/error");
		}
	}
	
	@GetMapping("/shop_showCart.do")
	public ModelAndView getShopShowCart(HttpServletRequest request) {
								// HttpServletRequest request 말고 세션으로 해도 됨.
		// 세션에서 로그인한 m_id를 가지고 있어야함.
		ModelAndView mv = new ModelAndView("shop/cartList");
		try {
			// 로그인한 정보 가져오기
			String m_id = ((MemberVO) request.getSession().getAttribute("mvo")).getM_id();			
			List<CartVO> cartList = shop_Service.getCartList(m_id);
			mv.addObject("cartList", cartList);
			return mv;
		} catch (Exception e) {
			e.printStackTrace();
			return new ModelAndView("shop/error");
		}
	}
	
	@PostMapping("/shop_cartedit.do")
	public ModelAndView getShopCartEdit(CartVO cartVO) {
		ModelAndView mv = new ModelAndView("redirect:/shop_showCart.do");
		try {
			int res = shop_Service.getCartEdit(cartVO);
			return mv;
		} catch (Exception e) {
			e.printStackTrace();
			return new ModelAndView("shop/error");
		}
	}
	
	@GetMapping("/shop_cartdelete.do")
	public ModelAndView getCartDelete(@RequestParam("idx")String idx) {
		ModelAndView mv = new ModelAndView("redirect:/shop_showCart.do");
		try {
			int res = shop_Service.getCartDelete(idx);
			return mv;
		} catch (Exception e) {
			e.printStackTrace();
			return new ModelAndView("shop/error");
		}
	}
	
	@GetMapping("/shop_product_insertForm.do")
	public ModelAndView getProductInsertForm() {
		return new ModelAndView("shop/admin");
	}
	
	@PostMapping("/shop_productInsert.do")
	public ModelAndView getProductInsert(ShopVO svo, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("shop/product_list");
		try {
			String path = request.getSession().getServletContext().getRealPath("/resources/images");
			MultipartFile file_s = svo.getFile_s();
			MultipartFile file_l = svo.getFile_l();
			
			UUID uuid = UUID.randomUUID();
			String p_image_s2 = uuid.toString() + "_" + svo.getFile_s().getOriginalFilename();
			String p_image_l2 = uuid.toString() + "_" + svo.getFile_l().getOriginalFilename();
			svo.setP_image_l(p_image_l2);
			svo.setP_image_s(p_image_s2);
			
			// 이미지 저장
			byte[] in = svo.getFile_l().getBytes();
			byte[] in2 = svo.getFile_s().getBytes();
			File out = new File(path, p_image_l2);
			File out2 = new File(path, p_image_s2);
			FileCopyUtils.copy(in, out);
			FileCopyUtils.copy(in2, out2);
			
			int res = shop_Service.getProductInsert(svo);
			return mv;
					
		} catch (Exception e) {
			e.printStackTrace();
			return new ModelAndView("shop/error");
		}
	}
}