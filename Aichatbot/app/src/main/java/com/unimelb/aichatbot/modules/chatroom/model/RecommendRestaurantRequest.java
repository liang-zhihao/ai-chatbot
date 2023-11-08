package com.unimelb.aichatbot.modules.chatroom.model;

public class RecommendRestaurantRequest {
    //     {
//     "latitude":-37.800045,
//     "longitude":144.965310
// }
    private double latitude;
    private double longitude;

    public RecommendRestaurantRequest(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
