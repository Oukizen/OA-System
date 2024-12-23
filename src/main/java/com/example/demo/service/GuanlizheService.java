package com.example.demo.service;

import com.example.demo.entity.Guanlizhe;

import java.util.List;

public interface GuanlizheService {
    List<Guanlizhe> getAllStats();
    Guanlizhe getGuanlizheById1(Long statsId);
    void createStats(Guanlizhe stats);
    void updateStats(Guanlizhe stats);
    void deleteStats(Long statsId);

}
