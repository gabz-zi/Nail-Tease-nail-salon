package nailSalon_services.model.entity;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String username;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "madeBy")
    private List<Design> addedDesigns;


    public User() {
        this.addedDesigns = new ArrayList<>();
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

    public List<Design> getAddedDesigns() {
        return addedDesigns;
    }

    public void setAddedDesigns(List<Design> addedDesigns) {
        this.addedDesigns = addedDesigns;
    }
}

