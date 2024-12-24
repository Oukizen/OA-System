package com.example.demo.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.demo.entity.User;

@Mapper
public interface UserMapper {

	@Select("SELECT * FROM user")
	List<User> findAll();

	@Select("SELECT * FROM user WHERE name = #{name}")
	Optional<User> findByName(String name);

	@Select("SELECT * FROM user WHERE id = #{id}")
	Optional<User> findById(Long id);
	
	@Select("SELECT * FROM user WHERE email = #{email}")
	Optional<User> findByEmail(String email);

	@Insert("INSERT INTO user(name, password, email, phone, sex, syokui, age, address, position_name, photo) "
			+ "VALUES(#{name}, #{password}, #{email}, #{phone}, #{sex}, #{syokui}, #{age}, #{address}, #{positionName}, #{photo})")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	void save(User user);

	@Update("UPDATE user SET name = #{name}, password = #{password}, email = #{email}, phone = #{phone}, sex = #{sex}, syokui = #{syokui}, "
			+ "age = #{age}, address = #{address}, position_name = #{positionName}, photo = #{photo} WHERE id = #{id}")
	void update(User user);

	@Delete("DELETE FROM user WHERE id = #{id}")
	void deleteById(Long id);

	// 新增方法：统计用户总数
	@Select("SELECT COUNT(*) FROM user")
	int countAllUsers();

	@Select("SELECT COUNT(*) FROM user WHERE sex = '男'")
	int countMaleUsers();

	@Select("SELECT COUNT(*) FROM user WHERE sex = '女'")
	int countFemaleUsers();

}
