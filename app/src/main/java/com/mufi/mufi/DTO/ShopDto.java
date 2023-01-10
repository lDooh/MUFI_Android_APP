package com.mufi.mufi.DTO;

public class ShopDto {
    public ShopDto(String shop_id, String shop_name, String shop_address,
                   double north_latitude, double east_longitude,
                   String open_time, String close_time,
                   int number_booth, int number_using_booth) {
        this.shop_id = shop_id;
        this.shop_name = shop_name;
        this.shop_address = shop_address;
        this.north_latitude = north_latitude;
        this.east_longitude = east_longitude;
        this.open_time = open_time;
        this.close_time = close_time;
        this.number_booth = number_booth;
        this.number_using_booth = number_using_booth;
    }

    private String shop_id;

    private String shop_name;

    private String shop_address;

    private double north_latitude;

    private double east_longitude;

    private String open_time;

    private String close_time;

    private int number_booth;

    private int number_using_booth;

    public String getShop_id() {
        return shop_id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public String getShop_address() {
        return shop_address;
    }

    public double getNorth_latitude() {
        return north_latitude;
    }

    public double getEast_longitude() {
        return east_longitude;
    }

    public String getOpen_time() {
        return open_time;
    }

    public String getClose_time() {
        return close_time;
    }

    public int getNumber_booth() {
        return number_booth;
    }

    public int getNumber_using_booth() {
        return number_using_booth;
    }
}