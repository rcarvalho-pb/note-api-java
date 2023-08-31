package com.api.note.services;

import com.api.note.exceptions.NotFoundException;
import com.api.note.model.Note;
import com.api.note.model.Tag;
import com.api.note.repositories.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public List<Tag> findAll() {
        return Collections.unmodifiableList(this.tagRepository.findAll());
    }

    public Tag findById(Integer id) {
        return this.tagRepository.findAll().stream()
                .filter(tag -> tag.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public Tag save(Tag tag) {
        return this.tagRepository.save(tag);
    }

    public Tag update(Tag tag, Integer id) {
        Optional<Tag> searchedTag = this.tagRepository.findById(id);
        searchedTag.ifPresentOrElse(n -> {
            tag.setId(id);
            this.tagRepository.save(tag);
        }, () -> {
            throw new NotFoundException("Note not found");
        });
        return tag;
    }

    public void delete(Integer id) {
        Optional<Tag> tag = this.tagRepository.findById(id);
        tag.ifPresent(this.tagRepository::delete);
    }
}
