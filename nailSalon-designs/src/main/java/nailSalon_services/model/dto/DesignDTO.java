package nailSalon_services.model.dto;

public class DesignDTO {
    private Long id;
    private String name;
    private String category;
    private String imageUrl;
    private String madeByUsername; // Only include relevant user info

    public DesignDTO() {
    }

    public DesignDTO(Long id, String name, String category, String imageUrl, String madeByUsername) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.imageUrl = imageUrl;
        this.madeByUsername = madeByUsername;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMadeByUsername() {
        return madeByUsername;
    }

    public void setMadeByUsername(String madeByUsername) {
        this.madeByUsername = madeByUsername;
    }
}

