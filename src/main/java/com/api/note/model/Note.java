package com.api.note.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "notes")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(of = "id")
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String description;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @JsonManagedReference
    @OneToMany(
            mappedBy = "note",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Link> links = new ArrayList<>();
    @JsonManagedReference
    @OneToMany(
            mappedBy = "note",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Tag> tags = new ArrayList<>();

    public Note(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
