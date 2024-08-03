package com.nailSalon.model.view;

public class MyAppointmentView {
    private String createOn;

    private String madeFor;

    private String service;

    private String status;

    private String price;

    private Long id;

    public MyAppointmentView() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
