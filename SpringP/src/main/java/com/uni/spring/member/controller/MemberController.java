package com.uni.spring.member.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import com.uni.spring.member.model.dto.Member;
import com.uni.spring.member.model.service.MemberService;
import com.uni.spring.member.model.service.MemberServiceImpl2;


@SessionAttributes("loginUser") // Model에 Attribute 추가할때 자동으로 설정된 키값을 세션에 등록 시키는 기능
@Controller
public class MemberController {

	@Autowired
	private MemberService memberService;
	

	@Autowired
	private MemberServiceImpl2 memberServiceImpl2;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	/*@ExceptionHandler(value = Exception.class)
	public ModelAndView controllerExceptionHandler (Exception e) {
		
		e.printStackTrace();
		return new ModelAndView("common/errorPageServer").addObject("msg",e.getMessage());
		
	}
	
	
	*/
	
	
	
	
	//1.HttpServletRequest를 통해 전송받기 (기존 jsp/servlet 방식)
/*	@RequestMapping(value="login.do" , method=RequestMethod.POST)//@RequestMapping 을 붙여줌으로써  HandlerMapping 으로 등록
	public String  loginMember(HttpServletRequest request) {
		String userId = request.getParameter("userId");
		String userPwd = request.getParameter("userPwd");
		
		System.out.println(userId);
		System.out.println(userPwd);
		return "main"; //리턴되는 문자열을  servlet-context.xml 의 viewresolver 에 의해서 사용자가 보게될 뷰로 포워딩
	}
*/	
	
	//2.@RequestParam - 스프링에서 제공하는 파라미터를 받아오는 방식
	
/*	@RequestMapping(value="login.do" , method=RequestMethod.POST)
	public String  loginMember(@RequestParam("userId") String userId, @RequestParam("userPwd") String userPwd) {
		
		System.out.println(userId);
		System.out.println(userPwd);
		return "main"; 
	}
*/
	//3. @RequestParam 생략 - 매개변수를  name 과 동일하게 작성해야 자동으로 값이 주어진다.
/*	@RequestMapping(value="login.do" , method=RequestMethod.POST)
	public String  loginMember( String userId,  String userPwd) {
		
		System.out.println(userId);
		System.out.println(userPwd);
		return "main"; 
	}
*/
	
	//4. @ModelAttribute 를 이용한 방법 - 요청 파라미터가 많은경우 객체 타입으로 넘겨 받는데 기본 생성자와 setter 를 이용한 주입 방식 이므로 둘중하나라도 
	//없으면 에러 . 반드시 name 속성에 있는 값과 필드명이 동일 해야 하고 setter 작성하는 규칙에 맞게 작성되어야 한다. 
/*	@RequestMapping(value="login.do" , method=RequestMethod.POST)
	public String  loginMember(@ModelAttribute Member m) {
		
		System.out.println("Id :"+ m.getUserId());
		System.out.println("Pwd :"+ m.getUserPwd());
		return "main"; 
	}
	*/
	
	//5.@ModelAttribute 생략하고 객체바로 전달받는 방식
	/*@RequestMapping(value="login.do" , method=RequestMethod.POST)
	public String  loginMember(Member m , HttpSession session) {
		
		//System.out.println("Id :"+ m.getUserId());
		//System.out.println("Pwd :"+ m.getUserPwd());
		
		//MemberService memberService = new MemberServiceImpl();
		
		try {
			Member loginUser = memberService.loginMember(m);
			session.setAttribute("loginUser", loginUser);
			
			return "redirect:/"; 
		} catch (Exception e) {

			e.printStackTrace();
			return "common/errorPage";
			
		}
		
		
	}*/
	
	/*@RequestMapping("logout.do")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}*/
	
	
	//응답페이지에 응답할 데이터가 있는경우 
	//1.Model 객체 사용하는 방법  - 응답뷰로 전달하고자하는 Map(키, 벨류) 형식으로 값을 담을수있따. 
	
	/*@RequestMapping(value="login.do" , method=RequestMethod.POST)
	public String  loginMember(Member m , HttpSession session , Model model) {
		
		
		try {
			Member loginUser = memberService.loginMember(m);
			session.setAttribute("loginUser", loginUser);
			
			return "redirect:/"; 
		} catch (Exception e) {

			e.printStackTrace();
			model.addAttribute("msg", "로그인실패");
			return "common/errorPage";
			
		}
		
		
	}*/
	
	//2. ModelAndView 객체를 사용하는 방법 - 값과 뷰를 모두 지정 
	/*@RequestMapping(value="login.do" , method=RequestMethod.POST)
	public ModelAndView loginMember(Member m , HttpSession session , ModelAndView mv) {
		
		
		try {
			Member loginUser = memberService.loginMember(m);
			session.setAttribute("loginUser", loginUser);
			
			mv.setViewName( "redirect:/");
		} catch (Exception e) {

			e.printStackTrace();
			mv.addObject("msg", "로그인실패");
			mv.setViewName("common/errorPage");
			
		}
		
		return mv;
	}
	*/
	
	
	//3.session에 loginUser 를 저장할때 @SessionAttributes어노테이션 사용하기
/*	@RequestMapping(value="login.do" , method=RequestMethod.POST)
	public String  loginMember(Member m , Model model) {
			System.out.println(m.getUserId());
			
		try {
			Member loginUser = memberService.loginMember(m);
			model.addAttribute("loginUser", loginUser);
			return "redirect:/"; 
		} catch (Exception e) {

			e.printStackTrace();
			model.addAttribute("msg", "로그인실패");
			return "common/errorPage";
			
		}
		
		
	}*/
	
	//로그아웃 변경 (@SessionAttributes)
	@RequestMapping("logout.do")
	public String logout(SessionStatus status) {
		status.setComplete(); //현재 SessionAttributes 에 의해 저장된 오브젝트를 제거
		return "redirect:/";
	}

	@RequestMapping("enrollForm.do")
	public String enrollForm() {
		return "member/memberEnrollForm";
	}
	
	@RequestMapping("insertMember.do")
	public String insertMember(@ModelAttribute Member m, @RequestParam("post") String post,
			@RequestParam("address1") String address1, @RequestParam("address2") String address2 , HttpSession session) {
		
			
		m.setAddress(post+"/"+address1+"/"+ address2);

		System.out.println("암호화전: "+ m.getUserPwd());
		
		String encPwd = bCryptPasswordEncoder.encode(m.getUserPwd());
		
		System.out.println("암호화후: "+ encPwd);
		
		m.setUserPwd(encPwd);
		
		memberService.insertMember(m);
		
		session.setAttribute("msg", "회원가입성공");
		
		return "redirect:/";
	}
	
	//암호화 처리 후 로그인 
	@RequestMapping(value="login.do" , method=RequestMethod.POST)
	public String  loginMember(Member m , Model model) {
			
	
			Member loginUser = memberService.loginMember(bCryptPasswordEncoder, m);
			model.addAttribute("loginUser", loginUser);
			return "redirect:/"; 
	
		
		
		
	}
   
	@RequestMapping("myPage.do")
	public String myPage() {
		return "member/myPage";
	}
	
	@RequestMapping("updateMember.do")
	public String updateMember(@ModelAttribute Member m, @RequestParam("post") String post,
			@RequestParam("address1") String address1, @RequestParam("address2") String address2, Model model) throws Exception {
		
		
		m.setAddress(post+"/"+address1+"/"+ address2);
		Member userInfo = memberService.updateMember(m);
		//Member userInfo = memberServiceImpl2.updateMember(m);
		
		model.addAttribute("loginUser", userInfo);
		
		return "member/myPage";
	}
	
	
	@ResponseBody
	@RequestMapping("idCheck.do")
	public String idCheck(String userId) {
		
		int count = memberService.idCheck(userId);
		
		return String.valueOf(count);
	}
}





