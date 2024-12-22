package com.example.demo.service;

import java.util.List;
import java.util.Map; // 确保导入了 Map

import com.example.demo.entity.User;

public interface UserService {

	List<User> findAll();

	User findById(int id);

	User save(User user);

	void deleteById(int id);

	// 获取用户总数
	int getTotalUserCount();
	
	// 获取性别比例
	Map<String, Integer> getGenderRatio();

}
