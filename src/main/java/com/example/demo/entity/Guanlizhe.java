package com.example.demo.entity;


import java.time.LocalDateTime;



public class Guanlizhe {


    private Long id;


    private LocalDateTime statsTime;

    private String statsNr;

    // 默认构造方法
    public Guanlizhe() {}

    // 构造方法
    public Guanlizhe(LocalDateTime statsTime, String statsNr) {
        this.statsTime = statsTime;
        this.statsNr = statsNr;
    }

    // Getter 和 Setter 方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
