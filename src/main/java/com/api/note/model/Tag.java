package com.api.note.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tags")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String url;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "note_id", referencedColumnName = "id")
    private Note note;

    public Tag(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return this.url;
    }
}
