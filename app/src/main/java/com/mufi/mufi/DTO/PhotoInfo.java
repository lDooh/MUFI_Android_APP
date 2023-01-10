package com.mufi.mufi.DTO;

public class PhotoInfo {
    private String payment_id;
    private String payment_date;
    private int photo_number;
    private String image_content;

    public PhotoInfo(String payment_id, String payment_date, int photo_number, String image_content) {
        this.payment_id = payment_id;
        this.payment_date = payment_date;
        this.photo_number = photo_number;
        this.image_content = image_content;
    }

    public String getPayment_id() {
        return payment_id;
    }

    public String getPayment_date() {
        return payment_date;
    }

    public int getPhoto_number() {
        return photo_number;
    }

    public String getImage_content() {
        return image_content;
    }
}