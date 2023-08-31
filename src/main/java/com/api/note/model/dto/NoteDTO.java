package com.api.note.model.dto;

import com.api.note.model.Link;
import com.api.note.model.Tag;

import java.util.List;

public record NoteDTO(String title, String description, List<Link> links, List<Tag> tags) {
}
