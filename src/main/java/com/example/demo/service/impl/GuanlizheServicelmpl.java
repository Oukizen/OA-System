package com.example.demo.service.impl;

import com.example.demo.entity.Guanlizhe;
import com.example.demo.mapper.GuanlizheMapper;
import com.example.demo.service.GuanlizheService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public classGuanlizheServiceImpl implements GuanlizheService {

    private final GuanlizheMapper guanlizheMapper;

    public GuanlizheGuanlizheServiceImpl(StatsMapper statsMapper) {
        this.statsMapper = guanlizheMapper;
    }

    @Override
    public List<Guanlizhe> getAllStats() {
        return guanlizheMapper.findAll();
    }

    @Override
    public Guanlizhe getGuanlizheById(Long statsId) {
        return guanlizheMapper.findById(statsId);
    }

    @Override
    public void createStats(Guanlizhe guanlizhe) {
        stats.setStatsTime(LocalDateTime.now()); // 设置当前时间
        guanlizheMapper.save(guanlizhe);
    }

    @Override
    public void updateStats(Guanlizhe guanlizhe) {
    	guanlizheMapper.update(guanlizhe);
    }

    @Override
    public void deleteStats(Long statsId) {
    	guanlizheMapper.deleteById(statsId);
    }
}
