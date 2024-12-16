package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserController {
  @Autowired
  private UserService userService;

  @GetMapping("/yonghu")
  public String showUserPage() {
    return "yonghu";
  }

  @GetMapping("/yonghu/list")
  @ResponseBody
  public List<User> getAllUsers() {
    return userService.findAll();
  }

  @PostMapping("/yonghu/save")
  @ResponseBody
  public User saveUser(@RequestBody User user) {
    return userService.save(user);
  }

  @DeleteMapping("/yonghu/delete/{id}")
  @ResponseBody
  public void deleteUser(@PathVariable int id) {
    userService.deleteById(id);
  }

  @GetMapping("/yonghu/get/{id}")
  @ResponseBody
  public User getUser(@PathVariable int id) {
    return userService.findById(id);
  }
}