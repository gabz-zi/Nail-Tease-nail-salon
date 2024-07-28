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

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Appointment> appointments;

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
        this.appointments = new ArrayList<>();
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


    public void addFavourite(Design design) {
        this.favouriteDesigns.add(design);
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public List<Design> getAddedDesigns() {
        return addedDesigns;
    }

    public void setAddedDesigns(List<Design> addedDesigns) {
        this.addedDesigns = addedDesigns;
    }

    public List<Design> getFavouriteDesigns() {
        return favouriteDesigns;
    }

    public void setFavouriteDesigns(List<Design> favouriteDesigns) {
        this.favouriteDesigns = favouriteDesigns;
    }

    public List<Design> getRatedDesigns() {
        return ratedDesigns;
    }

    public void setRatedDesigns(List<Design> ratedDesigns) {
        this.ratedDesigns = ratedDesigns;
    }

}
