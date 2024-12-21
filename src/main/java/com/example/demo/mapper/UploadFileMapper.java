package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.demo.entity.UploadFile;

@Mapper
public interface UploadFileMapper {

	@Insert("INSERT INTO file (name, url, is_public) VALUES (#{name}, #{url}, #{isPublic})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	void insertFile(UploadFile file);

	@Select("SELECT * FROM file")
	List<UploadFile> getAllFiles();

	@Select("<script>" + "SELECT * FROM file " + "<where>"
			+ "  <if test='name != null and name != \"\"'>AND name LIKE CONCAT('%', #{name}, '%')</if>" + "</where>"
			+ "ORDER BY name ASC " + "LIMIT #{offset}, #{limit}" + "</script>")
	List<UploadFile> getFiles(@Param("offset") int offset, @Param("limit") int limit, @Param("name") String name);

	@Select("<script>" + "SELECT COUNT(*) FROM file " + "<where>"
			+ "  <if test='name != null and name != \"\"'>AND name LIKE CONCAT('%', #{name}, '%')</if>" + "</where>"
			+ "</script>")
	long getTotalCount(@Param("name") String name);

	@Delete("DELETE FROM file WHERE id = #{fileId}")
	void deleteFile(Long fileId);

	@Select("SELECT * FROM file WHERE id = #{fileId}")
	UploadFile getFileById(Long fileId);

	@Update("UPDATE file SET is_public = #{isPublic}, name = #{name}, url = #{url} WHERE id = #{fileId}")
	int updateFileStatus(@Param("fileId") Long fileId, @Param("isPublic") int i, @Param("name") String name,
			@Param("url") String url);

	@Select("SELECT name FROM file WHERE id = #{fileId}")
	String getFileNameById(Long fileId);

	@Select("SELECT url FROM file WHERE id = #{fileId}")
	String getFileUrlById(Long fileId);

	@Delete("<script>" + "DELETE FROM file WHERE id IN "
			+ "<foreach item='id' collection='ids' open='(' separator=',' close=')'>" + "#{id}" + "</foreach>"
			+ "</script>")
	int batchDeleteFiles(@Param("ids") List<Long> ids);
}
