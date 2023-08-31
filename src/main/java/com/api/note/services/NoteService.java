package com.api.note.services;

import com.api.note.exceptions.NotFoundException;
import com.api.note.model.Note;
import com.api.note.model.User;
import com.api.note.repositories.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;

    public List<Note> findAll() {
        return Collections.unmodifiableList(this.noteRepository.findAll());
    }

    public Note findById(Integer id) {
        return this.noteRepository.findAll().stream()
                .filter(note -> note.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public Note save(Note note) {
        return this.noteRepository.save(note);
    }

    public Note update(Note note, Integer id) {
        Optional<Note> searchedNote = this.noteRepository.findById(id);
        searchedNote.ifPresentOrElse(n -> {
            note.setId(id);
            this.noteRepository.save(note);
        }, () -> {
            throw new NotFoundException("Note not found");
        });
        return note;
    }

    public void delete(Integer id) {
        Optional<Note> note = this.noteRepository.findById(id);
        note.ifPresent(this.noteRepository::delete);
    }
}
