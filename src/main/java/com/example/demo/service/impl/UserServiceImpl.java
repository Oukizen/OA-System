package com.example.demo.service.impl;

import java.util.List;
import java.util.Map;

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

    // 新增方法：获取用户总数
    @Override
    public int getTotalUserCount() {
        return userMapper.countAllUsers();
    }

    // 新增方法：获取性别比例
    @Override
    public Map<String, Integer> getGenderRatio() {
        int maleCount = userMapper.countMaleUsers();
        int femaleCount = userMapper.countFemaleUsers();

        // 打印日志查看结果
        System.out.println("Male Count: " + maleCount);
        System.out.println("Female Count: " + femaleCount);

        return Map.of("male", maleCount, "female", femaleCount);
    }

}
