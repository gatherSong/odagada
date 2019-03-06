package com.spring.odagada.carpool.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.spring.odagada.carpool.model.service.CarpoolService;
import com.spring.odagada.carpool.model.vo.Carpool;
import com.spring.odagada.carpool.model.vo.Option;

@Controller
public class CarpoolController {

	@Autowired
	CarpoolService service;
	
	
	private Logger l = LoggerFactory.getLogger(CarpoolController.class);
			
	@RequestMapping("/carpool/register")
	public ModelAndView carpoolRegister() {
		ModelAndView mav = new ModelAndView("/carpool/register");	
		
		return mav;
	}

	
	@RequestMapping("/carpool/registerEnd")
	public String carpoolRegisterEnd(Carpool carpool, Option option) {
		
		l.debug(carpool.toString());
		l.debug(option.toString());
				
		return "redirect:/";
	}
	
	@RequestMapping("/carpool/search.do")
	public ModelAndView carpoolSearch() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("carpool/search");
		return mav;
	}
	@RequestMapping("/carpool/searchEnd.do")
	public ModelAndView wcarpoolSearchEnd(String startSearch, String endSearch, String startDate, String startLon, String startLat, String endLon, String endLat){
		ModelAndView mav = new ModelAndView();
		l.debug("죄표값");
		
		Map<String,String> m = new HashMap<String, String>();
		m.put("startCity", startSearch);
		m.put("endCity",endSearch);
		m.put("startDate", startDate);
		m.put("startLong", startLon);
		m.put("startLat",startLat);
		m.put("destLong",endLon);
		m.put("destLat",endLat);
		
		
		List<Map<String,String>> list = service.selectCarpoolList(m);
		
		mav.addObject("startSearch",startSearch);
		mav.addObject("endSearch",endSearch);
		mav.addObject("startDate",startDate);
		mav.setViewName("carpool/searchEnd");
		return mav;
		
	}
	
	@RequestMapping("/carpool/oneSearch.do")
	public ModelAndView carpoolOne(int carpoolnum) {
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName("carpool/oneSearch");
		return mav;
	}
}
