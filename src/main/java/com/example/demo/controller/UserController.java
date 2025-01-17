package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ui.Model;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.example.demo.service.UploadedFileService;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UploadedFileService uploadedFileService;

    @GetMapping("/yonghu")
    public String showUserPage(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false); // 获取当前会话（如果有）

        if (session == null || session.getAttribute("Syokui") == null) {
            // 如果没有会话或会话中没有登录信息，返回提示消息
            model.addAttribute("message", "未登录，请重新登录");
            return "redirect:/login"; // 返回登录页面
        }

        Object syokui = session.getAttribute("Syokui");
        if (!"1".equals(String.valueOf(syokui))) {
            // 如果 Syokui 不是 "1"，跳转到权限不足页面
            model.addAttribute("message", "権限が不足しており、このページにアクセスできません。");
            return "accessDenied"; // 跳转到权限不足页面
        }

        return "yonghu"; // 正常显示用户页面
    }


    @GetMapping("/yonghuxq")
    public String showUserPage2(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // 获取当前会话（如果有）
        if (session == null || session.getAttribute("Syokui") == null) {            // 如果没有会话或会话中没有登录信息，重定向到登录页面
            return "redirect:/login";
        }
        return "yonghuxq"; //
    }
    @PostMapping("/logout")
    @ResponseBody
    public Map<String, String> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // 获取当前会话（如果有）
        if (session != null) {
            session.invalidate(); // 使会话无效
        }
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        return response;
    }

    @PostMapping("/yonghu/save")
    @ResponseBody
    public User saveUser(@RequestBody User user) {
        return userService.save(user);
    }

    @GetMapping("/yonghu/list")
    @ResponseBody
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @DeleteMapping("/yonghu/delete/{id}")
    @ResponseBody
    public void deleteUser(@PathVariable int id) {
        userService.deleteById(id);
    }

    @GetMapping("/yonghu/get/{id}")
    @ResponseBody
    public ResponseEntity<User> getUser(@PathVariable int id) {
        User user = userService.findById(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/yonghu/photo/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> getUserPhoto(@PathVariable int id) {
        User user = userService.findById(id);
        if (user != null && user.getPhoto() != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(user.getPhoto(), headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/yonghu/upload-photo")
    @ResponseBody
    public ResponseEntity<?> uploadPhoto(@RequestParam("photo") MultipartFile file,
                                         @RequestParam("employeeId") String employeeId) {
        try {
            int id = Integer.parseInt(employeeId);
            User user = userService.findById(id);
            if (user != null) {
                user.setPhoto(file.getBytes());
                userService.save(user);
                return ResponseEntity.ok().body(Map.of("photoUrl", "/yonghu/photo/" + id));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload photo: " + e.getMessage());
        }
    }

    // 新增 API：获取人员总数
    @GetMapping("/api/employee-count")
    @ResponseBody
    public Map<String, Integer> getEmployeeCount() {
        try {
            int totalCount = userService.getTotalUserCount();
            return Map.of("total_count", totalCount);
        } catch (Exception e) {
            return Map.of("total_count", 0);
        }
    }

    // 新增 API：获取性别比例
    @GetMapping("/api/gender-ratio")
    @ResponseBody
    public Map<String, Integer> getGenderRatio() {
        try {
            return userService.getGenderRatio(); // 调用 Service 方法获取性别比例
        } catch (Exception e) {
            return Map.of("male", 0, "female", 0);
        }
    }

    // 新增 API：获取文档总数
    @GetMapping("/api/document-count")
    @ResponseBody
    public Map<String, Long> getDocumentCount() {
        try {
            long documentCount = uploadedFileService.getFileCount(); // 调用 UploadedFileService
            return Map.of("document_count", documentCount);
        } catch (Exception e) {
            return Map.of("document_count", 0L);
        }
    }
}

