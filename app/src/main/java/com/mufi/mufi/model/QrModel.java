package com.mufi.mufi.model;

import android.util.Log;

import com.mufi.mufi.MufiTag;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.StringTokenizer;

public class QrModel {
    private JSONObject jsonObject;
    private StringBuffer sb = new StringBuffer();
    private String kioskIp, shopId, kioskNumber;

    // QR 코드 파싱 (기기 유동 IP, 매장 ID, 키오스크 Number
    public QrModel(String qrURL) {
        jsonObject = new JSONObject();
        StringTokenizer st = new StringTokenizer(qrURL, "/", false);

        kioskIp = st.nextToken();
        shopId = st.nextToken();
        kioskNumber = st.nextToken();
    }

    // 파싱된 키오스크 IP 반환
    public String getKioskIp() {
        return kioskIp;
    }
    // 파싱된 매장 ID 반환
    public String getShopId() {
        return shopId;
    }
    // 파싱된 키오스크 number 반환
    public String getKioskNumber() {
        return kioskNumber;
    }

    // 키오스크 QR 코드 읽어서 파싱, JSON에 ID와 함께 저장
    @Deprecated
    public void createQRJson(String id) {
        try {
            jsonObject.put("id", id);
        } catch (JSONException e) {
            Log.e(MufiTag.DEBUG_TAG_JSON, "JSON 예외: " + e.getMessage());
        }
    }

    @Deprecated
    public JSONObject getQRJSon() {
        return jsonObject;
    }
}
