package com.nailSalon.model.view;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TodaysAppointmentView {

    private String createBy;

    private String time;

    private String service;

    private String price;


    public TodaysAppointmentView() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(LocalDateTime dateTime) {
        String hours = dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        String day = dateTime.format(DateTimeFormatter.ofPattern("dd.MM"));
        this.time = "at " + hours + " on " + day;
    }


    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
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
