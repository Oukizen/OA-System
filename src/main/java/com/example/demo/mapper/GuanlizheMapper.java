package com.example.demo.mapper;

import com.example.demo.entity.Stats;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatsRepository extends JpaRepository<Stats, Long> {
    // 这里你可以根据需要定义更多的查询方法，例如根据时间获取公告等。
}
