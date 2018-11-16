package com.example.henrylopez.fettapp.metodos;

public class mRestaurantes {
    private String Image;
    private String RestaurantName;
    private String RestaurantDire;
    private float RestaurantRating;
    private Double Lat;
    private Double Lng;

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getRestaurantName() {
        return RestaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        RestaurantName = restaurantName;
    }

    public String getRestaurantDire() {
        return RestaurantDire;
    }

    public void setRestaurantDire(String restaurantDire) {
        RestaurantDire = restaurantDire;
    }

    public float getRestaurantRating() {
        return RestaurantRating;
    }

    public void setRestaurantRating(float restaurantRating) {
        RestaurantRating = restaurantRating;
    }

    public mRestaurantes(String image, String restaurantName, String restaurantDire, int restaurantRating) {
        Image = image;
        RestaurantName = restaurantName;
        RestaurantDire = restaurantDire;
        RestaurantRating = restaurantRating;
    }

    public mRestaurantes(Double lat, Double lng) {
        Lat = lat;
        Lng = lng;
    }

    public Double getLat() {
        return Lat;
    }

    public void setLat(Double lat) {
        Lat = lat;
    }

    public Double getLng() {
        return Lng;
    }

    public void setLng(Double lng) {
        Lng = lng;
    }

    public  mRestaurantes(){

    }
}
