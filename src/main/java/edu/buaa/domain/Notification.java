package edu.buaa.domain;

import java.util.Arrays;

public class Notification {
    private String owner;
    private int[][] map;
    private String body;
    private int ownerId;
    private int targetId;
    private String target;
    private String type;

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

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
            ", ownerId=" + ownerId +
            ", targetId=" + targetId +
            ", target=" + target +
            ", type='" + type + '\'' +
            '}';
    }
}
