package edu.buaa.domain.messaging;

import java.util.Objects;

public class TargetNotification {


    private String currentTime;
    private String category;
    private String ip;
    private String name;
    private String owner;
    private String level;
    private  double longitude;
    private  double latitude;

    private  double selfLongitude;
    private  double selfLatitude;

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getSelfLongitude() {
        return selfLongitude;
    }

    public void setSelfLongitude(double selfLongitude) {
        this.selfLongitude = selfLongitude;
    }

    public double getSelfLatitude() {
        return selfLatitude;
    }

    public void setSelfLatitude(double selfLatitude) {
        this.selfLatitude = selfLatitude;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TargetNotification that = (TargetNotification) o;

        if (Double.compare(that.longitude, longitude) != 0) return false;
        if (Double.compare(that.latitude, latitude) != 0) return false;
        if (Double.compare(that.selfLongitude, selfLongitude) != 0) return false;
        if (Double.compare(that.selfLatitude, selfLatitude) != 0) return false;
        if (!currentTime.equals(that.currentTime)) return false;
        if (!category.equals(that.category)) return false;
        if (!ip.equals(that.ip)) return false;
        if (!name.equals(that.name)) return false;
        if (!owner.equals(that.owner)) return false;
        return level.equals(that.level);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = currentTime.hashCode();
        result = 31 * result + category.hashCode();
        result = 31 * result + ip.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + owner.hashCode();
        result = 31 * result + level.hashCode();
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(latitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(selfLongitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(selfLatitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "TargetNotification{" +
            "currentTime='" + currentTime + '\'' +
            ", category='" + category + '\'' +
            ", ip='" + ip + '\'' +
            ", name='" + name + '\'' +
            ", owner='" + owner + '\'' +
            ", level='" + level + '\'' +
            ", longitude=" + longitude +
            ", latitude=" + latitude +
            ", selfLongitude=" + selfLongitude +
            ", selfLatitude=" + selfLatitude +
            '}';
    }
}
