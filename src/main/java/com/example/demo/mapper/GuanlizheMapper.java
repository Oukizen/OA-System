package com.example.demo.mapper;

import com.example.demo.entity.Guanlizhe;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface GuanlizheMapper {

    @Select("SELECT * FROM stats_table")
    List<Guanlizhe> findAll();

    @Select("SELECT * FROM stats_table WHERE stats_id = #{statsId}")
    Guanlizhe findById(Long statsId);

    @Insert("INSERT INTO stats_table (stats_time, stats_nr) VALUES (#{statsTime}, #{statsNr})")
    @Options(useGeneratedKeys = true, keyProperty = "statsId")
    void save(Guanlizhe stats_nr);

    @Update("UPDATE stats_table SET stats_time = #{statsTime}, stats_nr = #{statsNr} WHERE stats_id = #{statsId}")
    void update(Guanlizhe statsTime);

    @Delete("DELETE FROM stats_table WHERE stats_id = #{statsId}")
    void deleteById(Long statsId);
}
