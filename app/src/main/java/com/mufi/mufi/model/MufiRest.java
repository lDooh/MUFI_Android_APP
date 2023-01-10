package com.mufi.mufi.model;

import android.os.NetworkOnMainThreadException;
import android.util.Log;

import com.mufi.mufi.MufiTag;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MufiRest {
    private static final String SERVER_URL = "http://10.0.2.2:8000/app/";
    private static final String QR_URL = "";
    private static final String LOGIN_URL = "user/login/";
    private static final String SIGN_UP_URL = "user/signup/";
    private static final String ID_INSPECTION_URL = "user/inspection/";
    private static final String CARD_ADD_URL = "card/register/";
    private static final String CARD_GET_URL = "card/info/";
    private static final String CARD_DELETE_URL = "card/delete/";
    private static final String PHOTO_FEED_URL = "photo/feed/";
    private static final String PHOTO_PAYMENT_URL = "photo/payment/";
    private static final String PHOTO_ORIGINAL_URL = "photo/original/";
    private static final String PHOTO_DELETE_URL = "photo/delete/";
    private static final String NEW_TOKEN_URL = "update/token/";
    private static final String SHOP_LOCATION_URL = "shop/location/";
    private static final String SHOP_INFO_URL = "shop/info/";
    private HttpURLConnection conn = null;
    private String inputLine = null;

    private static MufiRest instance;

    // Singleton
    private MufiRest() {
    }

    public static MufiRest getInstance() {
        if (instance == null) {
            instance = new MufiRest();
        }
        return instance;
    }

    public synchronized String send(String param, int connect_tag) {
        inputLine = null;
        StringBuffer sbUrl = new StringBuffer(SERVER_URL);
        StringBuffer outResult = new StringBuffer();

        // 요청 동작에 따라 다른 URL 적용
        switch (connect_tag) {
            case MufiTag.REST_QR: {
                sbUrl.delete(0, sbUrl.length());
                sbUrl.append("http://192.168.1.112:5000/");
                sbUrl.append(QR_URL);
                break;
            }
            case MufiTag.REST_LOGIN: {
                sbUrl.append(LOGIN_URL);
                break;
            }
            case MufiTag.REST_SIGN_UP: {
                sbUrl.append(SIGN_UP_URL);
                break;
            }
            case MufiTag.REST_ID_DUPLICATED: {
                sbUrl.append(ID_INSPECTION_URL);
                break;
            }
            case MufiTag.REST_ADD_CARD: {
                sbUrl.append(CARD_ADD_URL);
                break;
            }
            case MufiTag.REST_GET_CARD: {
                sbUrl.append(CARD_GET_URL);
                break;
            }
            case MufiTag.REST_DELETE_CARD: {
                sbUrl.append(CARD_DELETE_URL);
                break;
            }
            case MufiTag.REST_GET_PHOTO_FEED: {
                sbUrl.append(PHOTO_FEED_URL);
                break;
            }
            case MufiTag.REST_GET_PHOTO_PAYMENT: {
                sbUrl.append(PHOTO_PAYMENT_URL);
                break;
            }
            case MufiTag.REST_GET_PHOTO_ORIGINAL: {
                sbUrl.append(PHOTO_ORIGINAL_URL);
                break;
            }
            case MufiTag.REST_DELETE_PHOTO: {
                sbUrl.append(PHOTO_DELETE_URL);
                break;
            }
            case MufiTag.REST_GET_SHOP_LOCATION: {
                sbUrl.append(SHOP_LOCATION_URL);
                break;
            }
            case MufiTag.REST_GET_SHOP_INFO: {
                sbUrl.append(SHOP_INFO_URL);
                break;
            }
        }

        Thread thread = new Thread() {
            @Override
            public void run() {
                synchronized (this) {
                    try {
                        sbUrl.append(param);
                        URL url = new URL(sbUrl.toString());

                        Log.d(MufiTag.DEBUG_TAG_REST, "REST API Start");
                        conn = (HttpURLConnection) url.openConnection();
                        //conn.setDoOutput(true);
                        conn.setRequestMethod("GET");
                        conn.setRequestProperty("Content-Type", "application/json");
                        conn.setRequestProperty("Accept-Charset", "UTF-8");
                        conn.setConnectTimeout(10000);
                        conn.setReadTimeout(10000);

                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        while ((inputLine = in.readLine()) != null) {
                            outResult.append(inputLine);
                        }

                    } catch (NetworkOnMainThreadException e) {
                        Log.d(MufiTag.DEBUG_TAG_REST, "NetworkOnMainThreadException: " + e.getMessage());
                        e.printStackTrace();
                    } catch (IOException e) {
                        Log.d(MufiTag.DEBUG_TAG_REST, "IO Exception: " + e.getMessage());
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        conn.disconnect();
                        Log.d(MufiTag.DEBUG_TAG_REST, "REST API End");
                        notify();
                    }
                }
            }
        };

        thread.start();

        synchronized (thread) {
            try {
                thread.wait();
            } catch (InterruptedException e) {
                Log.d(MufiTag.DEBUG_TAG_REST, "thread 예외: " + e.getMessage());
            }
        }

        return outResult.toString();
    }
}