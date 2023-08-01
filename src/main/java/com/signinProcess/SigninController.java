package com.signinProcess;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class SigninController {
	
	@RequestMapping("/")
	public String index() {
		return "index.html";
	}
	
	@RequestMapping("/SigninProcess")
	public String getDetails(@ModelAttribute ZACredentials credentials,HttpServletRequest req,HttpServletResponse res,HttpSession session) {
		String Code = (String) req.getParameter("code");
		String name = credentials.First_Name;
		
		credentials.setCode(Code);		
		credentials.createAccessToken(Code);
		credentials.getUserDetails();
		credentials.storeDetails();
		session.setAttribute("Name", name);
//		System.out.println("Code"+Code);
		return "success.html";

	}	
	@RequestMapping("/logout")
	public String logout(@ModelAttribute ZACredentials credentials,HttpSession session) {
		String name = credentials.First_Name;
		
		session.removeAttribute(name);
		session.invalidate();
		return "index.html";
	}
}
