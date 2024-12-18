package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    // ログインページを表示
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // templates/login.html を返す
    }

    // ログインリクエストを処理
    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        Model model) {
        // 用户名和密码验证
        Optional<User> optionalUser = userRepository.findByName(username);

        if (optionalUser.isPresent() && optionalUser.get().getPassword().equals(password)) {
            return "redirect:/home"; // 登录成功后重定向到主页 /home
        } else {
            model.addAttribute("error", "ユーザー名またはパスワードが正しくありません");
            return "login"; // 登录失败，返回 login 页面
        }
    }
}
