package com.udacity.jwdnd.course1.cloudstorage.mappers;

import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {

    @Select("SELECT * FROM NOTES")
    List<Note> getNotes();

    @Select("SELECT * FROM NOTES WHERE noteid = #{noteId}")
    Note getNote(int noteId);

    @Select("SELECT COUNT(1) AS count FROM NOTES WHERE noteid = #{noteId}")
    @Result(column = "count", javaType = Long.class)
    long checkNote(int noteId);

    @Insert("INSERT INTO NOTES (notetitle, notedescription, userid)" +
            "VALUES (#{noteTitle}, #{noteDescription}, #{userId})")
    @Options(useGeneratedKeys = true, keyColumn = "noteid", keyProperty = "noteId")
    int createNote(Note note);

    @Update("UPDATE NOTES SET notetitle = #{noteTitle}, notedescription = #{noteDescription}" +
            "WHERE noteid = #{noteId}")
    int editNote(Note note);

    @Delete("DELETE FROM NOTES WHERE noteid = #{noteId}")
    boolean deleteNote(int noteId);

}
