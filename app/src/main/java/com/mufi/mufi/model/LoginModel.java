package com.mufi.mufi.model;

import android.util.Log;

import com.mufi.mufi.DTO.AppUserDTO;
import com.mufi.mufi.MufiTag;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginModel {
    private JSONObject jsonObject;
    private StringBuffer sb = new StringBuffer();
    private String kioskIp, shopId, kioskNumber;
    private AppUserDTO appUserDTO;

    public LoginModel() {

    }

    // 로그인 시 리턴받는 JSON을 매개변수로 받음, 로그인 성공 시 true 반환
    public boolean setResultString(String resultString) {
        boolean isLoginSuccess = false;
        try {
            jsonObject = new JSONObject(resultString);

            // 로그인 성공 시 1
            if ((jsonObject.getString("isLoginSuccess")).equals("1")) {
                isLoginSuccess = true;
            } else {

            }
        } catch (JSONException e) {
            Log.d(MufiTag.DEBUG_TAG_JSON, "setResultString JSON 예외: " + e.getMessage());
        }

        return isLoginSuccess;
    }

    public AppUserDTO getAppUserDTO(String id) {
        try {
            AppUserDTO appUserDTO = AppUserDTO.getInstance();

            appUserDTO.setId(id);
            appUserDTO.setName(jsonObject.getString("name"));
            appUserDTO.setCallNumber(jsonObject.getString("call_number"));
            appUserDTO.setBirthday(jsonObject.getString("birth"));
            appUserDTO.setGender(jsonObject.getInt("gender"));
        } catch (JSONException e) {
            Log.d(MufiTag.DEBUG_TAG_JSON, "getAppUserDTO JSON 예외: " + e.getMessage());
        }

        return appUserDTO;
    }
}
