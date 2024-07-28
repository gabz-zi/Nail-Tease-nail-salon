package com.nailSalon.model.entity;

import javax.persistence.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "nail_services")
public class NailService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private String duration; // duration in minutes

    @Column
    private String description;


    @OneToMany(mappedBy = "service", cascade = CascadeType.REMOVE)
    private List<Appointment> appointments;

    public NailService() {
        this.appointments = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public String getPriceFormatted() {
        boolean isWholeNumber = (this.price == Math.round(this.price));
        String pattern = isWholeNumber ? "#.##" : "#.00";
        DecimalFormat df = new DecimalFormat(pattern);
        return df.format(this.price);
    }


    public void setPrice(double price) {
        this.price = price;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }
}
