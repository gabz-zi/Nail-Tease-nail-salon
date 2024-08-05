package com.nailSalon.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;


import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private LocalDateTime createOn;

    @Column(nullable = false)
    @Future
    private LocalDateTime madeFor;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    private NailService service;

    @Column(nullable = false)
    private Integer status = 0;

    @Column
    private boolean cancelled;


    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    private User takenBy;  // the employee who took the appointment

    public Appointment() {
    }

    public boolean getCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public User getTakenBy() {
        return takenBy;
    }

    public void setTakenBy(User takenBy) {
        this.takenBy = takenBy;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getCreateOn() {
        return createOn;
    }

    public void setCreateOn(LocalDateTime createOn) {
        this.createOn = createOn;
    }

    public LocalDateTime getMadeFor() {
        return madeFor;
    }

    public void setMadeFor(LocalDateTime madeFor) {
        this.madeFor = madeFor;
    }

    public NailService getService() {
        return service;
    }

    public void setService(NailService service) {
        this.service = service;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

