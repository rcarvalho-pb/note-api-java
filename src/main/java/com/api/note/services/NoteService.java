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
        Note res = this.noteRepository.findAll().stream()
                .filter(note -> note.getId() == id)
                .findFirst()
                .orElseThrow(() -> { throw new NotFoundException("Note not found"); });
        System.out.println(res);
        return res;
    }

    public Note save(Note note) {
        return this.noteRepository.save(note);
    }

    public Note update(NoteDTO data, Integer noteId) {
        Optional<Note> note = this.noteRepository.findById(noteId);
        if (note.isEmpty() ){
            throw new NotFoundException("Note not found");
        }

        List<Link> links = this.linkRepository.findAll().stream()
                .filter(link -> link.getNote().getId() == noteId)
                .toList();
        List<Tag> tags = this.tagRepository.findAll().stream()
                .filter(tag -> tag.getNote().getId() == noteId)
                .toList();
        for(int i = 0; i < links.size(); i++) {
            links.get(i).setLink(data.links().get(i).getLink());
        }
        for (int i = 0; i < tags.size(); i++ ){
            tags.get(i).setUrl(data.tags().get(i).getUrl());
        }

        this.linkRepository.saveAll(links);
        this.tagRepository.saveAll(tags);

        return this.noteRepository.findById(noteId).get();
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
        Note note = new Note(data.title(), data.description());
        note.setLinks(data.links());
        note.setTags(data.tags());
        return note;
    }

    public Note saveUserNote(NoteDTO data, Integer userId) {
        Optional<User> user = this.userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        Note note = this.fromDTO(data);
        System.out.println(note);
        note.setUser(user.get());
        Note savedNote = this.noteRepository.save(note);
        System.out.println(savedNote);
        List<Link> links = data.links();
        List<Tag> tags = data.tags();

        links.forEach(link -> link.setNote(savedNote));
        tags.forEach(tag -> tag.setNote(savedNote));
        System.out.println(links);
        System.out.println(tags);

        this.linkRepository.saveAll(links);
        this.tagRepository.saveAll(tags);

        Note res = this.findById(savedNote.getId());
        System.out.println(res);
        return res;

    }

    public void deleteAllUserNotes(Integer userId) {
        this.noteRepository.deleteAll(
                this.noteRepository.findAll().stream()
                        .filter(note -> note.getUser().getId() == userId)
                        .toList()
        );
    }

    public List<Link> findAllLinksByNote(Integer noteId) {
        return this.linkRepository.findAll().stream()
                .filter(link -> link.getNote().getId() == noteId)
                .collect(Collectors.toUnmodifiableList());
    }

    public List<Tag> findAllTagsByNote(Integer noteId) {
        return this.tagRepository.findAll().stream()
                .filter(tag -> tag.getNote().getId() == noteId)
                .collect(Collectors.toUnmodifiableList());
    }
}
