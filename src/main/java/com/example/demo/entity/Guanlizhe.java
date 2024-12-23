package com.example.demo.entity;

import java.time.LocalDateTime;

public class Guanlizhe {
    private Long statsId;       // stats_id 对应的 Java 字段
    private LocalDateTime statsTime; // stats_time 对应的 Java 字段
    private String statsNr;     // stats_nr 对应的 Java 字段

    // Getters and Setters
    public Long getStatsId() {
        return statsId;
    }

    public void setStatsId(Long statsId) {
        this.statsId = statsId;
    }

    public LocalDateTime getStatsTime() {
        return statsTime;
    }

    public void setStatsTime(LocalDateTime statsTime) {
        this.statsTime = statsTime;
    }

    public String getStatsNr() {
        return statsNr;
    }

    public void setStatsNr(String statsNr) {
        this.statsNr = statsNr;
    }
}
