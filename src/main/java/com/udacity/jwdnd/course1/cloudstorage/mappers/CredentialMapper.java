package com.udacity.jwdnd.course1.cloudstorage.mappers;

import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {

    @Select("SELECT * FROM CREDENTIALS")
    List<Credential> getCredentials();

    @Select("SELECT * FROM CREDENTIALS WHERE credentialid = #{credentialId}")
    Credential getCredential(int credentialId);

    @Select("SELECT COUNT(1) AS count FROM CREDENTIALS WHERE credentialid = #{credentialId}")
    @Result(column = "count", javaType = Long.class)
    long checkCredential(int credentialId);

    @Insert("INSERT INTO CREDENTIALS (url, username, key, password)" +
            "VALUES (#{url}, #{username}, #{key}, #{password})")
    @Options(useGeneratedKeys = true, keyColumn = "credentialid", keyProperty = "credentialId")
    int createCredential(Credential credential);

    @Update("UPDATE CREDENTIALS SET url = #{url}, username = #{username}, key = #{key}, password = #{password}" +
            "WHERE credentialid = #{credentialId}")
    int editCredential(Credential credential);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialid = #{credentialId}")
    boolean deleteCredential(int credentialId);

}
