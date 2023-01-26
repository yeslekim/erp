package com.study.erp.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/view/user")
public class UserViewController {
	
	@GetMapping("/main")
	public String home() throws Exception {
		return "user/main";
	}
	
	@GetMapping("/board")
	public String board() throws Exception {
		return "user/board";
	}

}
