package com.mufi.mufi.model;

import android.util.Log;

import com.mufi.mufi.DTO.PhotoInfo;
import com.mufi.mufi.MufiTag;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PhotoModel {
    private JSONObject jsonObject;

    public PhotoModel() {

    }

    // 리턴받는 JSON을 매개변수로 받음, 로그인 성공 시 true 반환
    public boolean setResultString(String resultString) {
        boolean isGetPhotoSuccess = false;
        try {
            jsonObject = new JSONObject(resultString);
            isGetPhotoSuccess = true;
        } catch (JSONException e) {
            isGetPhotoSuccess = false;
            Log.d(MufiTag.DEBUG_TAG_JSON, "setResultString JSON 예외: " + e.getMessage());
        }

        return isGetPhotoSuccess;
    }

    public boolean setDeletePhotoResultString(String resultString) {
        boolean isDeletePhotoSuccess = false;
        try {
            jsonObject = new JSONObject(resultString);

            if ((jsonObject.getInt("isDeletePhotoSuccess")) > 0) {
                isDeletePhotoSuccess = true;
            } else {

            }
        } catch (JSONException e) {
            isDeletePhotoSuccess = false;
            Log.d(MufiTag.DEBUG_TAG_JSON, "setResultString JSON 예외: " + e.getMessage());
        }

        return isDeletePhotoSuccess;
    }

    public ArrayList<PhotoInfo> getPhotoInfoArrayList() {
        ArrayList<PhotoInfo> arrayList = new ArrayList<>();

        try {
            // 받은 result JSON에서 photo 배열을 가져옴
            JSONArray jsonArray = (JSONArray) jsonObject.get("photo");

            JSONObject tmp;
            PhotoInfo photoInfo;

            // 등록되어있는 사진 모두 ArrayList에 넣어서 반환
            for (int i = 0; i < jsonArray.length(); i++) {
                tmp = (JSONObject) jsonArray.get(i);
                photoInfo = new PhotoInfo(tmp.getString("payment_id"),
                        tmp.getString("payment_date"),
                        tmp.getInt("photo_number"),
                        tmp.getString("image_content"));
                arrayList.add(photoInfo);
            }

        } catch (JSONException e) {
            Log.d(MufiTag.DEBUG_TAG_JSON, "getPhotoInfoArrayList JSON 예외: " + e.getMessage());
        }

        return arrayList;
    }

    public double getPhotoSize() {
        try {
            return jsonObject.getDouble("photo_size");
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public String getShopName() {
        try {
            return jsonObject.getString("shop_name");
        } catch (JSONException e) {
            Log.d(MufiTag.DEBUG_TAG_JSON, "getShopName JSON 예외: " + e.getMessage());
            return "null";
        }
    }
}
