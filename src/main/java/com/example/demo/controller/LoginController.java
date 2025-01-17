package com.example.demo.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

	@Autowired
	private UserMapper userRepository;

	// ログインページを表示
	@GetMapping("/login")
	public String loginPage() {
		return "login"; // templates/login.html を返す
	}

	// ログインリクエストを処理
	@PostMapping("/login")
	public String login(HttpServletRequest request, @RequestParam("email") String email,
			@RequestParam("password") String password, Model model) {
		// 邮箱和密码验证
		Optional<User> optionalUser = userRepository.findByEmail(email); // 改为用 email 查询

		if (optionalUser.isPresent() && optionalUser.get().getPassword().equals(password)) {
			HttpSession session = request.getSession();
			session.setAttribute("Syokui", optionalUser.get().getSyokui());
			model.addAttribute("Syokui", optionalUser.get().getSyokui());
			return "redirect:/home"; // 登录成功后重定向到主页 /home
		} else {
			model.addAttribute("error", "メールアドレスまたはパスワードが正しくありません");
			return "login"; // 登录失败，返回 login 页面
		}
	}
}
