package com.udacity.jwdnd.course1.cloudstorage.mappers;

import com.udacity.jwdnd.course1.cloudstorage.models.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface IFileMapper {
    @Select("SELECT * FROM FILES")
    List<File> getAll();

    @Select("SELECT * FROM FILES WHERE userid = #{userId}")
    List<File> getAllByUser(int userId);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) " +
            "VALUES(#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insert(File file);

    @Select("SELECT * FROM FILES WHERE fileid = #{fileId}")
    File findById(int fileId);

    @Delete("DELETE FROM FILES WHERE fileid=#{fileId}")
    int deleteById(int fileId);

    @Update("UPDATE FILES set filename=#{fileName}, " +
            " contenttype=#{contentType}, filesize=#{fileSize}, userid=#{userId}, filedata=#{fileData} where fileid=#{fileId}")
    int update(File file);

    @Select("SELECT * FROM FILES WHERE filename = #{fileName}")
    File findByName(String fileName);
}
