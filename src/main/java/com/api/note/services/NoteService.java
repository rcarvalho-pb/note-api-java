package com.api.note.services;

import com.api.note.exceptions.NotFoundException;
import com.api.note.model.Link;
import com.api.note.model.Note;
import com.api.note.model.Tag;
import com.api.note.model.User;
import com.api.note.model.dto.NoteDTO;
import com.api.note.repositories.LinkRepository;
import com.api.note.repositories.NoteRepository;
import com.api.note.repositories.TagRepository;
import com.api.note.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    private final LinkRepository linkRepository;
    private final TagRepository tagRepository;

    public List<Note> findAll() {
        return Collections.unmodifiableList(this.noteRepository.findAll());
    }

    public List<Note> findAllUserNotes(Integer userId) {
        return this.noteRepository.findAll().stream()
                .filter(note -> note.getUser().getId() == userId)
                .collect(Collectors.toUnmodifiableList());
    }

    public Note findById(Integer id) {
        return this.noteRepository.findAll().stream()
                .filter(note -> note.getId() == id)
                .findFirst()
                .orElseThrow(() -> { throw new NotFoundException("Note not found"); });
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

    public Note findUserNotesById(Integer userId, Integer noteId) {
        return this.noteRepository.findAll().stream()
                .filter(note -> note.getUser().getId() == userId)
                .filter(note -> note.getId() == noteId)
                .findFirst()
                .orElseThrow(() -> { throw new NotFoundException("Note not Found"); });
    }

    public Note fromDTO(NoteDTO data) {
        return new Note(data.title(), data.description());
    }

    public Note saveUserNote(NoteDTO data, Integer userId) {
        Optional<User> user = this.userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        Note note = this.fromDTO(data);
        note.setUser(user.get());
        Note savedNote = this.noteRepository.save(note);
        List<Link> links = data.links();
        List<Tag> tags = data.tags();

        links.forEach(link -> link.setNote(savedNote));
        tags.forEach(tag -> tag.setNote(savedNote));

        this.linkRepository.saveAll(links);
        this.tagRepository.saveAll(tags);

        return savedNote;
    }
}
