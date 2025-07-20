package com.example.foodtrucktracker.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class FoodTruck implements Serializable {
    @SerializedName("id")
    private int id;
    
    @SerializedName("name")
    private String name;
    
    @SerializedName("type")
    private String type;
    
    @SerializedName("description")
    private String description;
    
    @SerializedName("latitude")
    private double latitude;
    
    @SerializedName("longitude")
    private double longitude;
    
    @SerializedName("reportedBy")
    private String reportedBy;
    
    @SerializedName("reportedAt")
    private String reportedAt;
    
    @SerializedName("imageUrl")
    private String imageUrl;
    
    @SerializedName("isActive")
    private boolean isActive;
    
    @SerializedName("area")
    private String area;
    
    @SerializedName("landmark")
    private String landmark;
    
    @SerializedName("streetAddress")
    private String streetAddress;
    
    @SerializedName("operatingHours")
    private String operatingHours;
    
    @SerializedName("contactNumber")
    private String contactNumber;

    // Default constructor
    public FoodTruck() {}

    // Constructor with all parameters
    public FoodTruck(int id, String name, String type, String description, 
                     double latitude, double longitude, String reportedBy, 
                     String reportedAt, String imageUrl, boolean isActive,
                     String area, String landmark, String streetAddress,
                     String operatingHours, String contactNumber) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.reportedBy = reportedBy;
        this.reportedAt = reportedAt;
        this.imageUrl = imageUrl;
        this.isActive = isActive;
        this.area = area;
        this.landmark = landmark;
        this.streetAddress = streetAddress;
        this.operatingHours = operatingHours;
        this.contactNumber = contactNumber;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public String getReportedBy() { return reportedBy; }
    public void setReportedBy(String reportedBy) { this.reportedBy = reportedBy; }

    public String getReportedAt() { return reportedAt; }
    public void setReportedAt(String reportedAt) { this.reportedAt = reportedAt; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }

    public String getLandmark() { return landmark; }
    public void setLandmark(String landmark) { this.landmark = landmark; }

    public String getStreetAddress() { return streetAddress; }
    public void setStreetAddress(String streetAddress) { this.streetAddress = streetAddress; }

    public String getOperatingHours() { return operatingHours; }
    public void setOperatingHours(String operatingHours) { this.operatingHours = operatingHours; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    @Override
    public String toString() {
        return "FoodTruck{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", reportedBy='" + reportedBy + '\'' +
                ", reportedAt='" + reportedAt + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", isActive=" + isActive +
                ", area='" + area + '\'' +
                ", landmark='" + landmark + '\'' +
                ", streetAddress='" + streetAddress + '\'' +
                ", operatingHours='" + operatingHours + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                '}';
    }
}
