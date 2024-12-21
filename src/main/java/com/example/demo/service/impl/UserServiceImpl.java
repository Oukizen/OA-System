package com.example.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;

	// 获取所有用户
	@Override
	public List<User> findAll() {
		return userMapper.findAll(); // 调用 UserMapper 中的 findAll 方法
	}

	// 根据ID查找用户
	@Override
	public User findById(int id) {
		return userMapper.findById((long) id).orElse(null); // 根据ID查找用户
	}

	// 保存用户
	@Override
	public User save(User user) {
		userMapper.save(user);
		return user;
	}

	// 根据ID删除用户
	@Override
	public void deleteById(int id) {
		userMapper.deleteById((long) id);
	}
}
