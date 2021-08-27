package com.udacity.jwdnd.course1.cloudstorage.mappers;

import com.udacity.jwdnd.course1.cloudstorage.models.User;
import org.apache.ibatis.annotations.*;

import java.sql.SQLException;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM USERS WHERE username = #{username}")
    User getUser(String userName) throws SQLException;

    @Select("SELECT COUNT(1) AS count FROM USERS WHERE username = #{username}")
    @Result(column = "count", javaType = Long.class)
    long checkUser(String userName) throws SQLException;

    @Insert("INSERT INTO USERS (username, salt, password, firstname, lastname)" +
            "VALUES (#{username}, #{salt}, #{password}, #{firstName}, #{lastName})")
    @Options(useGeneratedKeys = true, keyColumn = "userid", keyProperty = "userId")
    int addUser(User user) throws SQLException;
}
