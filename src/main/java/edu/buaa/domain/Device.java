package edu.buaa.domain;

import java.util.Date;

public class Device {
    private String Name;
    private int Id;
    private String lastime;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getLastime() {
        return lastime;
    }

    public void setLastime(String lastime) {
        this.lastime = lastime;
    }

    @Override
    public String toString() {
        return "Device{" +
            "Name='" + Name + '\'' +
            ", Id=" + Id +
            ", lastime=" + lastime +
            '}';
    }
}
