package com.mufi.mufi.model;

import android.util.Log;

import com.mufi.mufi.DTO.AppCardDTO;
import com.mufi.mufi.MufiTag;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CardModel {
    private JSONObject jsonObject;

    public CardModel() {
    }

    // 서버로부터 카드 정보를 받기, 카드가 하나라도 있으면 1, 없거나 실패 시 0
    public boolean setResultString(String resultString) {
        boolean isGetCardSuccess = false;
        try {
            jsonObject = new JSONObject(resultString);

            // 카드 정보 가져오기 성공 시
            if((jsonObject.getInt("isGetCardsSuccess")) > 0) {
                isGetCardSuccess = true;
            } else {

            }
        } catch (JSONException e) {
            Log.d(MufiTag.DEBUG_TAG_JSON, "setResultString JSON 예외: " + e.getMessage());
        }

        return isGetCardSuccess;
    }

    public boolean setDeleteCardResultString(String resultString) {
        boolean isDeleteCardSuccess = false;
        try {
            jsonObject = new JSONObject(resultString);

            // 카드 정보 가져오기 성공 시
            if((jsonObject.getInt("isDeleteCardSuccess")) > 0) {
                isDeleteCardSuccess = true;
            } else {

            }
        } catch (JSONException e) {
            Log.d(MufiTag.DEBUG_TAG_JSON, "setResultString JSON 예외: " + e.getMessage());
        }

        return isDeleteCardSuccess;
    }

    public ArrayList<AppCardDTO> getAppCardArrayList() {
        ArrayList<AppCardDTO> arrayList = new ArrayList<>();

        try {
            // 받은 result JSON에서 cards 배열을 가져옴
            JSONArray jsonArray = (JSONArray) jsonObject.get("cards");

            JSONObject tmp;
            AppCardDTO appCardInfo;

            // 등록되어있는 카드를 모두 ArrayList에 넣어서 반환
            for (int i = 0; i < jsonArray.length(); i++) {
                tmp = (JSONObject) jsonArray.get(i);
                appCardInfo = new AppCardDTO(tmp.getString("card_number"),
                        tmp.getString("card_id"));
                arrayList.add(appCardInfo);
            }

        } catch (JSONException e) {
            Log.d(MufiTag.DEBUG_TAG_JSON, "getAppCardNumber JSON 예외: " + e.getMessage());
        }

        return arrayList;
    }
}
