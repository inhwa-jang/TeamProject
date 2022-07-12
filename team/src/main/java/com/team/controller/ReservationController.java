package com.team.controller;

import java.util.List;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.team.domain.FieldDTO;
import com.team.domain.MemberDTO;
import com.team.domain.ReservDTO;
import com.team.service.ReservationService;

@Controller //주소맵핑
public class ReservationController {

	@Inject
	private ReservationService reservationService;

	@RequestMapping(value = "/reservation/reservation", method = RequestMethod.GET)
	public String reservation(HttpServletRequest request,HttpSession session, Model model) {
		// 로그인한 사용자 정보 가져오기
			// id 세션값 가져오기
			int select03 = Integer.parseInt(request.getParameter("select03"));
			String id = (String)session.getAttribute("id");
		if(select03 == 0) {
				return "reservation/sel_msg";
			}else {

				FieldDTO fieldDTO = reservationService.getFiled(select03);
				// phone 값 가져오기
				MemberDTO memberDTO = reservationService.getPhone(id);
				System.out.println(fieldDTO.getF_name());
				System.out.println(fieldDTO.getF_num());
				System.out.println(fieldDTO.getPrice());
				model.addAttribute("fieldDTO",fieldDTO);
				model.addAttribute("memberDTO", memberDTO);
				return "reservation/reservation";
			}
	}
	
	@RequestMapping(value = "/reservation/select", method = RequestMethod.GET)
	public String reserSelect(HttpServletRequest request,Model model) throws Exception{
		model.addAttribute("getFieldList_sel",reservationService.getFieldList_sel());
		
		return "/reservation/reservSelect";
	}
	
	@RequestMapping(value = "/reservation/reservationPro", method = RequestMethod.POST)
	public String reservationPro(HttpServletRequest request,Model model) throws Exception{
		ReservDTO reservDTO = new ReservDTO();
		reservDTO.setF_num(Integer.parseInt(request.getParameter("f_num")));
//		reservDTO.setF_name(request.getParameter("f_name"));
		reservDTO.setId(request.getParameter("id"));
		reservDTO.setR_date(request.getParameter("r_date"));
		reservDTO.setTime(request.getParameter("time"));
		reservDTO.setTotal_price(request.getParameter("price"));
		
		reservationService.insertReserv(reservDTO);
		
		return "redirect:/reservation/reservResult";
	}
	
	@RequestMapping(value = "/reservation/reservResult", method = RequestMethod.GET)
	public String reserResult(HttpServletRequest request,HttpSession session,Model model) throws Exception{
		
		String s_id = (String)session.getAttribute("id");
		ReservDTO reservDTO = reservationService.getReservation(s_id);
		model.addAttribute("reservDTO", reservDTO);
		
		System.out.println(reservDTO.getTime());
		System.out.println(reservDTO.getTotal_price());
		
		return "/reservation/reservResult";
	}

	@RequestMapping(value = "/reservation/list", method = RequestMethod.GET)
	public String reservList(HttpSession session,HttpServletRequest request,Model model) throws Exception{
		String id = (String)session.getAttribute("id");
		System.out.println(id);
		List<ReservDTO> reservList = reservationService.getReservList(id);
		model.addAttribute("reservList",reservList);
		return "/reservation/reservList";
	}
}

