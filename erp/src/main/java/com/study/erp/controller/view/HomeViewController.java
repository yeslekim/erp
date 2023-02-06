package com.study.erp.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class HomeViewController {

	@GetMapping("/")
	public String home(HttpServletRequest request) throws Exception {
		return "login";
	}
	
}
