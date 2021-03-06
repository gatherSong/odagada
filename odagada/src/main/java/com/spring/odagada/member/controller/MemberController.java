package com.spring.odagada.member.controller;


import static com.spring.odagada.common.PageFactory.getpageBar;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.spring.odagada.carpool.model.service.CarpoolService;
import com.spring.odagada.member.model.service.MemberService;
import com.spring.odagada.member.model.vo.Member;

@SessionAttributes("logined")
@Controller
public class MemberController {
	
	private Logger logger=LoggerFactory.getLogger(MemberController.class);
	
	@Autowired
	MemberService service;
	
	@Autowired
	CarpoolService cService;
	
	//비밀번호 암호화 처리
	@Autowired
	BCryptPasswordEncoder pwEncoder;
	
	//email 중복확인
	@ResponseBody
	@RequestMapping("/member/checkEmail.do")
	public String checkEmail(String email) {
		logger.debug("받아오는 메일:"+email);
		int emailNum=service.checkEmail(email);
		String result="";
		if(emailNum==0) {
			result="ok";
		}else {
			result="no";
		}
		return result;
	}
			
	//아이디 중복확인
	@RequestMapping("/member/checkId.do")
	public ModelAndView checkId(String memberId, ModelAndView mv) throws UnsupportedEncodingException
	{
		Map map=new HashMap();
		boolean isId=service.checkId(memberId)==0?false:true;
		map.put("isId", isId);
		
		mv.addAllObjects(map);
		mv.addObject("char",URLEncoder.encode("문자열","UTF-8"));
		mv.addObject("num",1);
		mv.setViewName("jsonView");
		return mv;	
	}
	
	//회원가입페이지 전환
	@RequestMapping("/member/signUp.do")
	public String signUp() {
		return "member/signUpForm";
	}

	//회원가입
	@RequestMapping("/member/signUpEnd.do")
	public String signUpEnd(Model model, Member m, HttpServletRequest request, MultipartFile upFile) throws Exception {		
		logger.debug("뉴비: "+m);
		//암호화 전 패스워드
		String oriPw=m.getMemberPw();
		logger.debug("암호화 전:"+oriPw);
		logger.debug("암호화 후: "+pwEncoder.encode(oriPw));	
		m.setMemberPw(pwEncoder.encode(oriPw));		
		//메일주소
		String email1=request.getParameter("email1");
		String email2=request.getParameter("email2");
		String email=email1+"@"+email2;	
		m.setEmail(email);			
		//전화번호
		String phone1=request.getParameter("phone1");
		String phone2=request.getParameter("phone2");
		String phone=phone1+phone2;
		m.setPhone(phone);		
		//프로필 사진 저장되는 장소
		String sd=request.getSession().getServletContext().getRealPath("/resources/upload/profile");

		ModelAndView mv=new ModelAndView();
		
		if(!upFile.isEmpty()) {
			//파일명 생성(ReName)
			String oriFileName=upFile.getOriginalFilename();
			String ext=oriFileName.substring(oriFileName.lastIndexOf("."));
			
			//rename 규칙
			SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd_HHmmssSSS");
			int rdv=(int)(Math.random()*1000);
			String reName=sdf.format(System.currentTimeMillis())+"_"+rdv+ext;
			
			//profile 사진 저장
			try {
				upFile.transferTo(new File(sd+"/"+reName));
			}catch(IllegalStateException | IOException e){
				e.printStackTrace();
			}		
			m.setProfileImageOri(oriFileName);
			m.setProfileImageRe(reName);					
		}
		service.insertMember(m);

		String msg="인증 메일이 전송되었습니다. 인증 후 로그인 하실 수 있습니다.";
		String loc="/";
		model.addAttribute("msg", msg);
		model.addAttribute("loc", loc);
		return "common/msg";
		
	}
	
	 //이메일 인증 코드 검증
    @RequestMapping(value = "/emailConfirm.do", method = RequestMethod.GET)
    public ModelAndView emailConfirm(String memberId) {
    	Map<String, String>map=new HashMap();
    	map.put("isEmailAuth", "Y");
    	map.put("memberId", memberId);
               
        int result=service.updateStatus(map);  
        logger.debug("결과는?"+result);
        ModelAndView mv=new ModelAndView();
        
        String msg="";
		String loc="/";
        if(result>0) {
        	msg="회원가입 성공";
        }else {       	
        	mv.addObject("비정상적인 접근입니다.", msg);
        	mv.setViewName("redirect:/");
        }
        //System.out.println("usercontroller vo =" +vo);
       /* return "member/sucessAuth";*/
        mv.addObject("msg", msg);
		mv.addObject("loc", loc);
		mv.setViewName("member/successAuth");
		return mv;
    }
    

	//로그인 페이지
	@RequestMapping("/member/loginForm.do")
	public String loginForm() {
		return "member/loginForm";
	}
	
	//로그인
   @RequestMapping("/member/login.do")
   public ModelAndView login(String memberId, String memberPw, Model model) {
	   logger.debug("로그인 확인 memberId:"+memberId+"password:"+memberPw);
	   
	   Map<String, String>login=new HashMap();
	   login.put("memberId", memberId);
	   login.put("memberPw", memberPw);
	   
	   Map<String, String>result=service.login(login);
	   	   
	   ModelAndView mv=new ModelAndView();
	   
	   Member m=service.selectMember(memberId);   
	         
		if (m == null) {
			mv.addObject("msg", "등록된 정보가 없습니다.");
			mv.addObject("loc", "/member/loginForm.do");
			mv.setViewName("common/msg");
		} else {
			logger.debug("로그인 멤버 정보" + m);
			logger.debug("관리자 테스트" + m.getIsAdmin());

			if (result != null) {
				if (pwEncoder.matches(memberPw, result.get("MEMBERPW")) && m.getIsEmailAuth().equals("Y")) {
					mv.addObject("logined", m);
					mv.setViewName("redirect:/");
				}
				else if (pwEncoder.matches(memberPw, result.get("MEMBERPW")) && !m.getIsEmailAuth().equals("Y")) {
					mv.addObject("msg", "이메일 인증을 완료해주세요.");
					mv.setViewName("common/msg");
				} else {
					mv.addObject("msg", "패스워드가 일치하지 않습니다.");
					mv.addObject("loc", "/member/loginForm.do");
					mv.setViewName("common/msg");
				}
			}
		}
		return mv;
}

   
   //로그아웃(세션끊기)
   @RequestMapping("/member/logout.do")
   public String logout(SessionStatus status) {
	   	if(!status.isComplete()) {
	   		status.setComplete();
	   	}
	   	return "redirect:/";
   }
   
   //마이페이지 
   @RequestMapping("/member/myInfo.do")
   public String myInfo() {
	   return "member/myInfo";
   }
   
   //비밀번호 체크
   @ResponseBody
   @RequestMapping("/member/checkPw.do")
   public String checkPw(HttpServletResponse response, String answer, HttpSession session) {
	   logger.debug("받아오는 pw 값: "+answer);
	   
	   Member m = (Member)session.getAttribute("logined");
	   String result="";
  
	   if(pwEncoder.matches(answer, m.getMemberPw())) {
		   logger.debug("ok");
		   result = "ok";
	   }else {
		   logger.debug("no");
		   result = "no";
	   }	 
	   return result;
	/*   try {
		   if(pwEncoder.matches(answer, m.getMemberPw()))
		      {
		         response.setContentType("text/csv;charset=UTF-8");
		         logger.debug("ok");
		         
		         response.getWriter().println("ok");
		      }
		      else {
		         response.setContentType("text/csv;charset=UTF-8");
		         logger.debug("no");
		         response.getWriter().println("no");
		      }
	   }
	   catch(IOException e)
	   {
		   e.printStackTrace();
	   }*/
   }
 
   //내 정보 변경페이지
   @RequestMapping("/member/updateInfo.do")
   public String updateInfo(Model model) {
	   return "member/updateForm";
   }
   
   //내 정보 변경
   @RequestMapping("/member/updateInfoEnd.do")
   public String updateInfoEnd(Member m, HttpServletRequest request, MultipartFile upFile) {

		String phone1 = request.getParameter("phone1");
		String phone2 = request.getParameter("phone2");
		String phone = phone1 + phone2;
		m.setPhone(phone);

		String orPw = m.getMemberPw();
		m.setMemberPw(pwEncoder.encode(orPw));

		// 프로필 사진 저장되는 장소
		String sd = request.getSession().getServletContext().getRealPath("/resources/upload/profile");

		ModelAndView mv = new ModelAndView();

		if (!upFile.isEmpty()) {
			// 파일명 생성(ReName)
			String oriFileName = upFile.getOriginalFilename();
			String ext = oriFileName.substring(oriFileName.lastIndexOf("."));

			// rename 규칙
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmssSSS");
			int rdv = (int) (Math.random() * 1000);
			String reName = sdf.format(System.currentTimeMillis()) + "_" + rdv + ext;

			// profile 사진 저장
			try {
				upFile.transferTo(new File(sd + "/" + reName));
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
			}
			m.setProfileImageOri(oriFileName);
			m.setProfileImageRe(reName);
		}
		int result = service.updateMember(m);
		String msg = "";
		String loc = "/";
		if (result > 0) {
			msg = "정보수정이 완료되었습니다.";
		} else {
			msg = "정보수정 실패.";
		}
		mv.addObject("msg", msg);
		mv.addObject("loc", loc);
		return "common/msg";
	}

   

   
   @RequestMapping("/member/myCarpool")
   public ModelAndView myCarpool(HttpSession session, @RequestParam(value="cPage", required=false, defaultValue="0") int cPage) {
	   
	   int numPerPage = 5;
	   
	   ModelAndView mav = new ModelAndView("member/myCarpool");
	   
	   Member m = (Member)session.getAttribute("logined");
	   
	   List<Map<String, String>> list = cService.selectCarpoolList(m.getMemberNum(), cPage, numPerPage);
	   int totalCount = cService.selectCarpoolCount(m.getMemberNum());
	   
	   mav.addObject("carpoolList", list);
	   mav.addObject("pageBar", getpageBar(totalCount, cPage, numPerPage, "/odagada/member/myCarpool"));	   
	   
	   return mav;
   }
   
   

}
