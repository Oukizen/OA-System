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

	@Override
	public List<User> findAll() {
		return userMapper.findAll();
	}

	@Override
	public User findById(int id) {
		return userMapper.findById((long) id).orElse(null);
	}

	@Override
	public User save(User user) {
		if (user.getId() == null) {
			userMapper.save(user); // 新增
		} else {
			userMapper.update(user); // 更新
		}
		return user;
	}

	@Override
	public void deleteById(int id) {
		userMapper.deleteById((long) id);
	}
}
