package com.api.note.services;

import com.api.note.exceptions.NotFoundException;
import com.api.note.model.Link;
import com.api.note.model.Note;
import com.api.note.repositories.LinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LinkService {

    private final LinkRepository linkRepository;

    public List<Link> findAll() {
        return Collections.unmodifiableList(this.linkRepository.findAll());
    }

    public Link findById(Integer id) {
        return this.linkRepository.findAll().stream()
                .filter(user -> user.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public Link save(Link link) {
        return this.linkRepository.save(link);
    }

    public Link update(Link link, Integer id) {
        Optional<Link> searchedLink = this.linkRepository.findById(id);
        searchedLink.ifPresentOrElse(l -> {
            link.setId(id);
            this.linkRepository.save(link);
        }, () -> {
            throw new NotFoundException("Note not found");
        });
        return link;
    }

    public void delete(Integer id) {
        Optional<Link> link = this.linkRepository.findById(id);
        link.ifPresent(this.linkRepository::delete);
    }
}
