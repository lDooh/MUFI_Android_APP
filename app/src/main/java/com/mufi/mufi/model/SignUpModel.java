package com.mufi.mufi.model;

import android.util.Log;

import com.mufi.mufi.MufiTag;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpModel {
    public SignUpModel() {

    }
    
    // ID가 중복되지 않으면(회원가입 가능) true 반환
    public boolean isNotDuplicated(String resultString) {
        boolean signUpAble = false;
        
        try {
            JSONObject jsonObject = new JSONObject(resultString);
            if ((jsonObject.getString("isNotDuplicated")).equals("1")) {
                signUpAble = true;
            }
        } catch (JSONException e) {
            Log.d(MufiTag.DEBUG_TAG_JSON, "ID 중복검사 JSON 예외: " + e.getMessage());
            e.printStackTrace();
        }
        
        return signUpAble;
    }

    // 회원가입 요청 할 URL 파라미터 반환
    public String getSignUpString(String id, String password, String name, String callNumber, String birthday, int gender) {
        StringBuffer DTOBuffer = new StringBuffer();

        DTOBuffer.append(id);
        DTOBuffer.append("/");
        DTOBuffer.append(password);
        DTOBuffer.append("/");
        DTOBuffer.append(name);
        DTOBuffer.append("/");
        DTOBuffer.append(callNumber);
        DTOBuffer.append("/");
        DTOBuffer.append(birthday);
        DTOBuffer.append("/");
        DTOBuffer.append(gender);

        return DTOBuffer.toString();
    }

    // 회원가입 성공 여부 반환
    public boolean isSignUpSuccess(String resultString) {
        boolean success = false;

        try {
            JSONObject jsonObject = new JSONObject(resultString);
            if ((jsonObject.getString("isSignUpSuccess")).equals("1")) {
                success = true;
            }
        } catch (JSONException e) {
            Log.d(MufiTag.DEBUG_TAG_JSON, "회원가입 JSON 예외: " + e.getMessage());
            e.printStackTrace();
        }

        return success;
    }
}
