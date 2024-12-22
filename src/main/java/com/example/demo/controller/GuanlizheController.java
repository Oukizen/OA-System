package com.example.demo.controller;

import com.example.demo.entity.Guanlizhe;
import com.example.demo.service.GuanlizheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/announcements")
public class GuanlizheController {

    private final GuanlizheService statsService;

    @Autowired
    public GuanlizheController(GuanlizheService GuanlizheService) {
        this.GuanlizheService = GuanlizheService;
    }

    // 创建公告
    @PostMapping
    public Guanlizhe createAnnouncement(@RequestBody Guanlizhe stats) {
        return statsService.saveAnnouncement(stats);
    }

    // 获取所有公告
    @GetMapping
    public List<Guanlizhe> getAnnouncements() {
        return GuanlizheService.getAllAnnouncements();
    }

    // 删除公告
    @DeleteMapping("/{id}")
    public void deleteAnnouncement(@PathVariable Long id) {
        statsService.deleteAnnouncement(id);
    }
}
