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
    private String brief;

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

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
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
            ", brief='" + brief + '\'' +
            '}';
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
        if (currentTime != null ? !currentTime.equals(that.currentTime) : that.currentTime != null) return false;
        if (category != null ? !category.equals(that.category) : that.category != null) return false;
        if (ip != null ? !ip.equals(that.ip) : that.ip != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (owner != null ? !owner.equals(that.owner) : that.owner != null) return false;
        if (level != null ? !level.equals(that.level) : that.level != null) return false;
        return brief != null ? brief.equals(that.brief) : that.brief == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = currentTime != null ? currentTime.hashCode() : 0;
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (ip != null ? ip.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        result = 31 * result + (level != null ? level.hashCode() : 0);
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(latitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(selfLongitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(selfLatitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (brief != null ? brief.hashCode() : 0);
        return result;
    }
}
