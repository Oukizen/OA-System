package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GuanlizheController {

	@GetMapping("/guanlizhe") // 映射路径 /home
	public String guanlizhe() {
		return "guanlizhe"; // 返回 index.html 模板
	}

}
