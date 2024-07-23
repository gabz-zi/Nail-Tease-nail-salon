package com.nailSalon.model.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "styles")
public class Style {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private StyleName name;

    @Column(nullable = false)
    private String description;

    @OneToMany
    private List<Design> designs;

    public Style() {
        this.designs = new ArrayList<>();
    }

    public void setName(StyleName name) {
        this.name = name;
        this.setDescription(name);
    }

    public void setDescription(StyleName name) {
        String description = "";
        switch (name) {
            case IMPRESSIONISM -> description = "Impressionism is a painting style most commonly associated with the 19th century where small brush strokes are used to build up a larger picture.";
            case ABSTRACT -> description = "Abstract art does not attempt to represent recognizable subjects in a realistic manner.";
            case EXPRESSIONISM -> description = "Expressionism is a style of art that doesn't concern itself with realism.";
            case SURREALISM -> description = "Surrealism is characterized by dreamlike, fantastical imagery that often defies logical explanation.";
            case REALISM -> description = "Also known as naturalism, this style of art is considered as 'real art' and has been the dominant style of painting since the Renaissance.";
        }
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public StyleName getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Design> getDesigns() {
        return designs;
    }

    public void setDesigns(List<Design> designs) {
        this.designs = designs;
    }
}
