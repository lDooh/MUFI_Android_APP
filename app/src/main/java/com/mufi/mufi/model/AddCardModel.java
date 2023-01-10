package com.mufi.mufi.model;

import android.util.Log;

import com.mufi.mufi.MufiTag;

import org.json.JSONException;
import org.json.JSONObject;

public class AddCardModel {
    private JSONObject jsonObject;

    public AddCardModel() {
    }

    // 카드 등록 시 리턴받는 JSON을 매개변수로 받음, 카드 등록 성공 시 true 반환
    public boolean setResultString(String resultString) {
        boolean isRegistrationSuccess = false;
        try {
            jsonObject = new JSONObject(resultString);

            // 카드 등록 성공 시 1
            if ((jsonObject.getString("isRegistrationSuccess")).equals("1")) {
                isRegistrationSuccess = true;
            } else {

            }
        } catch (JSONException e) {
            Log.d(MufiTag.DEBUG_TAG_JSON, "setResultString JSON 예외: " + e.getMessage());
        }

        return isRegistrationSuccess;
    }
}
