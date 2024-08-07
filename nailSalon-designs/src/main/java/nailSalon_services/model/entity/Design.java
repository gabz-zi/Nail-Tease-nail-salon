package nailSalon_services.model.entity;

import jakarta.persistence.*;
import nailSalon_services.model.enums.Category;


@Entity
@Table(name = "designs")
public class Design {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column
    @Enumerated(EnumType.STRING)
    private Category category;


    @ManyToOne(optional = false)
    private User madeBy;


    @Column(name = "image_url", nullable = false)
    private String imageUrl;


    public Design() {
    }

    public void setMadeBy(User madeBy) {
        this.madeBy = madeBy;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public User getMadeBy() {
        return madeBy;
    }

}
