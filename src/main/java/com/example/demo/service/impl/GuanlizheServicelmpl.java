package com.example.demo.service.impl;

import com.example.demo.entity.Guanlizhe;
import com.example.demo.mapper.GuanlizheMapper;
import com.example.demo.service.GuanlizheService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GuanlizheServicelmpl implements GuanlizheService {

    private final GuanlizheMapper guanlizheMapper;

    public GuanlizheServicelmpl(GuanlizheMapper guanlizheMapper) {
        this.guanlizheMapper = guanlizheMapper;
    }

    @Override
    public List<Guanlizhe> getAllStats() {
        return guanlizheMapper.findAll();
    }

    @Override
    public Guanlizhe getGuanlizheById1(Long statsId) {
        return guanlizheMapper.findById(statsId);
    }

    @Override
    public void createStats(Guanlizhe guanlizhe) {
        guanlizhe.setStatsTime(LocalDateTime.now()); // 设置当前时间
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

	@Override
	public Guanlizhe getGuanlizheById(Long statsId) {
		// TODO Auto-generated method stub
		return null;
	}
}
