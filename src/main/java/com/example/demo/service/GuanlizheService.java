package com.example.demo.service;

import com.example.demo.entity.Guanlizhe;

import java.util.List;

public interface GuanlizheService {
    // 保存公告
	GuanlizheService saveAnnouncement(GuanlizheService stats);

    // 获取所有公告
    List<GuanlizheService> getAllAnnouncements();

    // 根据ID删除公告
    void deleteAnnouncement(Long id);
}
