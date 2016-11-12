package models;

public class Location {

    private double latitude;
    private double longitude;

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
    
    public String toJson() {
        return "{" +
                    "lat: " + latitude + "," +
                    "lng: " + longitude +
               "}";
    }

}
