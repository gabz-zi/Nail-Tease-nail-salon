package com.nailSalon.model.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @OneToMany(mappedBy = "madeBy")
    private List<Design> addedDesigns;

    @ManyToMany
    private List<Design> favouriteDesigns;

    @ManyToMany
    private List<Design> ratedDesigns;


    public User() {
        this.addedDesigns = new ArrayList<>();
        this.favouriteDesigns = new ArrayList<>();
        this.ratedDesigns = new ArrayList<>();
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Design> getAddedPaintings() {
        return addedDesigns;
    }

    public void setAddedPaintings(List<Design> addedDesigns) {
        this.addedDesigns = addedDesigns;
    }

    public List<Design> getFavouritePaintings() {
        return favouriteDesigns;
    }

    public void setFavouritePaintings(List<Design> favouriteDesigns) {
        this.favouriteDesigns = favouriteDesigns;
    }

    public List<Design> getRatedPaintings() {
        return ratedDesigns;
    }

    public void setRatedPaintings(List<Design> ratedDesigns) {
        this.ratedDesigns = ratedDesigns;
    }

    public void addFavourite(Design design) {
        this.favouriteDesigns.add(design);
    }

}
