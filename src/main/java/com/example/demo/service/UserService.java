package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.User;

public interface UserService {

	List<User> findAll();

	User findById(int id);

	User save(User user);

	void deleteById(int id);

}