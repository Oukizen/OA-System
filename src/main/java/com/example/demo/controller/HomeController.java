package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	@GetMapping("/home") // 映射路径 /home
	public String home() {
		return "index"; // 返回 index.html 模板
	}

}
