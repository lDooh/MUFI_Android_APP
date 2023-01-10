package com.mufi.mufi.model;

import android.util.Log;

import com.mufi.mufi.DTO.ShopDto;
import com.mufi.mufi.DTO.ShopInfo;
import com.mufi.mufi.MufiTag;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ShopModel {
    private JSONObject jsonObject;

    public ShopModel() {

    }

    // 리턴받는 JSON을 매개변수로 받음, 로그인 성공 시 true 반환
    public boolean setResultString(String resultString) {
        boolean isGetShopLocationSuccess = false;

        try {
            jsonObject = new JSONObject(resultString);
            isGetShopLocationSuccess = true;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return isGetShopLocationSuccess;
    }

    public ArrayList<ShopInfo> getShopLocation() {
        ArrayList<ShopInfo> arrayList = new ArrayList<>();

        try {
            JSONArray jsonArray = (JSONArray) jsonObject.get("locations");
            JSONObject tmp;
            ShopInfo shopInfo;

            for (int i = 0; i < jsonArray.length(); i++) {
                tmp = (JSONObject) jsonArray.get(i);
                shopInfo = new ShopInfo(tmp.getString("shop_id"),
                        tmp.getDouble("north_latitude"),
                        tmp.getDouble("east_longitude"));

                arrayList.add(shopInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return arrayList;
    }

    public ShopDto getShopDto() {
        ShopDto shopDto;

        try {
            shopDto = new ShopDto(jsonObject.getString("shop_id"),
                    jsonObject.getString("shop_name"),
                    jsonObject.getString("shop_address"),
                    jsonObject.getDouble("north_latitude"),
                    jsonObject.getDouble("east_longitude"),
                    jsonObject.getString("open_time"),
                    jsonObject.getString("close_time"),
                    jsonObject.getInt("number_booth"),
                    jsonObject.getInt("number_using_booth"));
        } catch (JSONException e) {
            shopDto = new ShopDto("null", "null", "null",
                    0.0, 0.0, "null", "null", 0, 0);
            Log.e(MufiTag.DEBUG_TAG_JSON, "JSON 예외: " + e.getMessage());
        }

        return shopDto;
    }
}
