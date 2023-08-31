package com.api.note.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "links")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Link {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String link;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "note_id", referencedColumnName = "id")
    private Note note;

    public Link(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return this.link;
    }
}
