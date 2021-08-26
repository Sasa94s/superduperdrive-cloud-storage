package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.exceptions.ArgAlreadyExistsException;
import com.udacity.jwdnd.course1.cloudstorage.exceptions.ArgNotFoundException;
import com.udacity.jwdnd.course1.cloudstorage.exceptions.NoRowsAffectedException;
import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/home/note")
public class NotesController {

    private final NoteService noteService;
    private final UserService userService;

    public NotesController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @PostMapping("add")
    public String add(@ModelAttribute Note note, Authentication authentication, RedirectAttributes model) {
        try {
            int userId = userService.getUser(authentication.getName()).getUserId();
            note.setUserId(userId);
            noteService.createNote(note);
        } catch (ArgAlreadyExistsException | NoRowsAffectedException | ArgNotFoundException e) {
            model.addFlashAttribute("resultError", e.getMessage());
        }

        return "redirect:/home/result";
    }

    @PostMapping("edit")
    public String edit(@ModelAttribute Note note, RedirectAttributes model) {
        try {
            noteService.editNote(note);
        } catch (NoRowsAffectedException e) {
            model.addFlashAttribute("resultError", e.getMessage());
        }
        return "redirect:/home/result";
    }

    @PostMapping("delete")
    public String delete(@ModelAttribute("noteId") Integer noteId, RedirectAttributes model) {
        try {
            noteService.deleteNote(noteId);
        } catch (ArgNotFoundException | NoRowsAffectedException e) {
            model.addFlashAttribute("resultError", e.getMessage());
        }
        return "redirect:/home/result";
    }
}