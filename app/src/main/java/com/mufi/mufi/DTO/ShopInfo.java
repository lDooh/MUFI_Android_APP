package com.mufi.mufi.DTO;

public class ShopInfo {
    private String shopId;
    private double north_latitude;
    private double east_longitude;

    public ShopInfo(String shopId, double north_latitude, double east_longitude) {
        this.shopId = shopId;
        this.north_latitude = north_latitude;
        this.east_longitude = east_longitude;
    }

    public String getShopId() {
        return shopId;
    }

    public double getNorth_latitude() {
        return north_latitude;
    }

    public double getEast_longitude() {
        return east_longitude;
    }
}