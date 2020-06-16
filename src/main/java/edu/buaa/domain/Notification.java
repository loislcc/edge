package edu.buaa.domain;

import java.util.Arrays;

public class Notification {
    private String owner;
    private int[][] map;
    private String body;

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int[][] getMap() {
        return map;
    }

    public void setMap(int[][] map) {
        this.map = map;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Notification{" +
            "owner='" + owner + '\'' +
            ", map=" + Arrays.toString(map) +
            ", body='" + body + '\'' +
            '}';
    }
}
