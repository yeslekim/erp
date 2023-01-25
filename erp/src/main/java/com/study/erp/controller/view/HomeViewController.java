package com.study.erp.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class HomeViewController {

	@GetMapping("/")
	public String home() throws Exception {
		return "login";
	}
}
