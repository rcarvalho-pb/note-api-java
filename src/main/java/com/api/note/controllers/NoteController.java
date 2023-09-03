package com.api.note.controllers;

import com.api.note.model.Link;
import com.api.note.model.Note;
import com.api.note.model.Tag;
import com.api.note.model.dto.NoteDTO;
import com.api.note.services.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<Note> findAllUserNotes(@PathVariable Integer id) {
        return this.noteService.findAllUserNotes(id);
    }

    @GetMapping(value = "/find")
    @ResponseStatus(HttpStatus.OK)
    public Note findUserNoteById(@RequestParam Integer noteId) {
        return this.noteService.findById(noteId);
    }

    @PostMapping(value = "/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Note createNote(@PathVariable(name = "userId") Integer userId, @RequestBody NoteDTO data) {
        System.out.println("NoteController - Criando Nota");
        return this.noteService.saveUserNote(data, userId);

    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Note updateUserNote(@RequestParam Integer noteId, @RequestBody NoteDTO data) {
        return this.noteService.update(data, noteId);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteUserNote(@RequestParam Integer noteId){
        this.noteService.delete(noteId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAllUserNotes(@PathVariable(name = "id") Integer userId) {
        this.noteService.deleteAllUserNotes(userId);
    }

    @GetMapping("/links")
    @ResponseStatus(HttpStatus.OK)
    public List<Link> getAllNoteLinks(@RequestParam Integer noteId) {
        return this.noteService.findAllLinksByNote(noteId);
    }

    @DeleteMapping("/links")
    @ResponseStatus(HttpStatus.OK)
    public void deleteNoteLink(@RequestParam Integer linkId) {
        this.noteService.deleteLinkById(linkId);
    }

    @GetMapping("/tags/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<Tag> getAllNoteTags(@RequestParam Integer noteId) {
        return this.noteService.findAllTagsByNote(noteId);
    }

    @DeleteMapping("/tags")
    @ResponseStatus(HttpStatus.OK)
    public void deleteNoteTag(@RequestParam Integer tagId) {
        this.noteService.deleteTagById(tagId);
    }
}
