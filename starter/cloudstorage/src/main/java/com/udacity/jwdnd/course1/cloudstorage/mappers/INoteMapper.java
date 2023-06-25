package com.udacity.jwdnd.course1.cloudstorage.mappers;

import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface INoteMapper {
    @Select("SELECT * FROM NOTES")
    List<Note> getAll();

    @Select("SELECT * FROM NOTES WHERE userid = #{userId}")
    List<Note> getAllByUserId(int userId);

    @Insert("INSERT INTO NOTES (notetitle, notedescription, userid) " +
            "VALUES(#{noteTitle}, #{noteDescription}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    int insert(Note note);

    @Select("SELECT * FROM NOTES WHERE noteid = #{noteId}")
    Note findById(int noteId);

    @Delete("DELETE FROM NOTES WHERE noteid=#{noteId}")
    int deleteById(int noteId);

    @Update("UPDATE NOTES set notetitle=#{noteTitle}, notedescription=#{noteDescription} where noteid=#{noteId}")
    int update(Note note);
}
