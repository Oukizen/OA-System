package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/yonghu")
    public String showUserPage() {
        return "yonghu";
    }
    @GetMapping("/yonghuxq")
    public String showUserPage2() {
        return "yonghuxq";
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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // 返回404和null
        }
        return ResponseEntity.ok(user);  // 返回用户数据
    }

    @GetMapping("/yonghu/photo/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> getUserPhoto(@PathVariable int id) {
        User user = userService.findById(id);
        if (user != null && user.getPhoto() != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);  // 设置图片类型，假设是JPEG格式
            return new ResponseEntity<>(user.getPhoto(), headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 没有找到图片时返回404
        }
    }


}
