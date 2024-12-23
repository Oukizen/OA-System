package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Guanlizhe;

public interface GuanlizheService {
	List<Guanlizhe> getAllStats();

	Guanlizhe getGuanlizheById1(Long statsId);

	void createStats(Guanlizhe stats);

	void updateStats(Guanlizhe stats);

	void deleteStats(Long statsId);

	Guanlizhe getGuanlizheById(Long statsId);

}
