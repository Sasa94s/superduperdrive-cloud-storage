package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.exceptions.ArgAlreadyExistsException;
import com.udacity.jwdnd.course1.cloudstorage.exceptions.ArgNotFoundException;
import com.udacity.jwdnd.course1.cloudstorage.exceptions.NoRowsAffectedException;
import com.udacity.jwdnd.course1.cloudstorage.mappers.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private final NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public boolean isNoteIdFound(int noteId) {
        return noteMapper.checkNote(noteId) == 1;
    }

    public int createNote(Note note) throws ArgAlreadyExistsException, NoRowsAffectedException {
        int rows = noteMapper.createNote(note);
        if (rows < 1) {
            throw new NoRowsAffectedException(String.format("%d note(s) created", rows));
        }

        return note.getNoteId();
    }

    public List<Note> getNotes() {
        return noteMapper.getNotes();
    }

    public Note getNote(int noteId) throws ArgNotFoundException {
        if (!isNoteIdFound(noteId)) {
            throw new ArgNotFoundException(String.format("%s note not found", noteId));
        }

        return noteMapper.getNote(noteId);
    }

    public int editNote(Note note) throws NoRowsAffectedException {
        int rows = noteMapper.editNote(note);
        if (rows < 1) {
            throw new NoRowsAffectedException(String.format("%d note(s) modified", rows));
        }

        return rows;
    }

    public void deleteNote(int noteId) throws ArgNotFoundException, NoRowsAffectedException {
        if (!isNoteIdFound(noteId)) {
            throw new ArgNotFoundException(String.format("%s note not found", noteId));
        }

        if (!noteMapper.deleteNote(noteId)) {
            throw new NoRowsAffectedException("Failed to delete note");
        }
    }
}
