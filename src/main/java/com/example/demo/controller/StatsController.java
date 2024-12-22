package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StatsController {
	@GetMapping("/stats")
	public String statsPage() {
	return"stats";
	}

}
