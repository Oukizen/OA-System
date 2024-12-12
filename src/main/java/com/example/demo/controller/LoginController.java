package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

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
        // 固定のユーザー名とパスワード：admin/admin
        if ("admin".equals(username) && "admin".equals(password)) {
        	return "redirect:/"; // ログイン成功後、ダッシュボードにリダイレクト
        } else {
            model.addAttribute("error", "ユーザー名またはパスワードが正しくありません");
            return "login"; // ログイン失敗の場合、ログインページに戻る
        }
    }
}
