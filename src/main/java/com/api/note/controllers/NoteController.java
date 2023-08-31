package com.api.note.controllers;

import com.api.note.model.Link;
import com.api.note.model.Note;
import com.api.note.model.Tag;
import com.api.note.model.dto.NoteDTO;
import com.api.note.repositories.NoteRepository;
import com.api.note.services.LinkService;
import com.api.note.services.NoteService;
import com.api.note.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;
    private final LinkService linkService;
    private final TagService tagService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Note> findAllUserNotes(@PathVariable Integer id) {
        return this.noteService.findAllUserNotes(id);
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Note findNoteById(@PathVariable(name = "id") Integer userId, @RequestParam Integer noteId) {
        return this.noteService.findUserNotesById(userId, noteId);
    }

    @PostMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Note createNote(@PathVariable Integer userId, @RequestBody NoteDTO data) {
        return this.noteService.saveUserNote(data, userId);

    }


}
