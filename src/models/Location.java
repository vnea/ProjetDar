package models;


public class Location {

    private double latitude;
    private double longitude;

    private static final double DIV_FACTOR = 1500;
    private static final double MUL_FACTOR = -.5;

    
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
    
    public String toJsonApprox() {
        return "{" +
                    "lat: " + latitude  + (Math.random() * MUL_FACTOR) / DIV_FACTOR + "," +
                    "lng: " + longitude + (Math.random() * MUL_FACTOR) / DIV_FACTOR +
               "}";
    }

}
