package com.nailSalon.model.view;

public class PendingAppointmentView {

    private Long id;

    private String createBy;

    private String createOn;

    private String madeFor;

    private String service;

    private String price;

    private String takenBy;

    public PendingAppointmentView() {
    }

    public String getTakenBy() {
        return takenBy;
    }

    public void setTakenBy(String takenBy) {
        this.takenBy = takenBy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateOn() {
        return createOn;
    }

    public void setCreateOn(String createOn) {
        this.createOn = createOn;
    }

    public String getMadeFor() {
        return madeFor;
    }

    public void setMadeFor(String madeFor) {
        this.madeFor = madeFor;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
