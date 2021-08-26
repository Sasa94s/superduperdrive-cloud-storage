package com.udacity.jwdnd.course1.cloudstorage.mappers;

import com.udacity.jwdnd.course1.cloudstorage.models.FileModel;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.BlobTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

@Mapper
public interface FileMapper {

    @Results({
            @Result(property = "fileData", column = "filedata", jdbcType = JdbcType.BLOB, typeHandler = BlobTypeHandler.class)
    })
    @Select("SELECT * FROM FILES")
    List<FileModel> getFiles();

    @Results({
            @Result(property = "fileData", column = "filedata", jdbcType = JdbcType.BLOB, typeHandler = BlobTypeHandler.class)
    })
    @Select("SELECT * FROM FILES WHERE filename = #{fileName}")
    FileModel getFile(String fileName);

    @Select("SELECT COUNT(1) AS count FROM FILES WHERE filename = #{fileName}")
    @Result(column = "count", javaType = Long.class)
    long checkFile(String fileName);

    @Insert("INSERT INTO FILES (fileid, filename, contenttype, filesize, userid, filedata)" +
            "VALUES (#{fileId}, #{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyColumn = "fileid", keyProperty = "fileId")
    int createFile(FileModel file);

    @Delete("DELETE FROM FILES WHERE filename = #{fileName}")
    boolean deleteFile(String fileName);
}
