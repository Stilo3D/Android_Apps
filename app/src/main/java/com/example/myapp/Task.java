package com.example.myapp;

import java.util.Date;
import java.util.UUID;

public class Task {
    private UUID id;
    private String name;
    private Date date;
    private boolean done;

    private Category category;

    public Task(){
        id = UUID.randomUUID();
        date = new Date();
        category = Category.DOM;
    }

    public void setName(String s) {
        this.name = s;
    }

    public void setDone(boolean b) {
        this.done = b;
    }

    public Date getDate(){
        return this.date;
    }
    public void setDate(Date dat) { this.date = dat;}
    public UUID getId(){
        return this.id;
    }

    public boolean isDone(){
        return done;
    }

    public String getName() {return name;}

    public void setCategory(Category cat) {
        this.category = cat;}

    public Category getCategory() {return this.category;}



}
