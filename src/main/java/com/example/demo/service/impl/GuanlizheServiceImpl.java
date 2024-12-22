package com.example.demo.service.impl;

import com.example.demo.entity.Guanlizhe;
import com.example.demo.mapper.StatsRepository;
import com.example.demo.service.GuanlizheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuanlizheServiceImpl implements GuanlizheService {

    private final StatsRepository statsRepository;

    @Autowired
    public StatsServiceImpl(StatsRepository statsRepository) {
        this.statsRepository = statsRepository;
    }

    @Override
    public Stats saveAnnouncement(Stats stats) {
        return statsRepository.save(stats);
    }

    @Override
    public List<Stats> getAllAnnouncements() {
        return statsRepository.findAll();
    }

    @Override
    public void deleteAnnouncement(Long id) {
        statsRepository.deleteById(id);
    }
}
