package com.mufi.mufi;

public final class MufiTag {
    public final static String DEBUG_TAG_LOGIN = "LOGIN DEBUG";
    public final static String DEBUG_TAG_ADD_CARD = "ADD CARD DEBUG";
    public final static String DEBUT_TAG_GET_CARD = "GET CARD DEBUG";
    public final static String DEBUG_TAG_JSON = "JSON DEBUG";
    public final static String DEBUG_TAG_REST = "REST API DEBUG";
    public final static String DEBUG_TAG_FCM = "FCM DEBUG";
    public final static String DEBUG_FILE_DOWNLOAD = "FILE DOWNLOAD DEBUG";

    // REST API 통신 태그
    public final static int REST_QR = 1;            // QR 코드 찍기
    public final static int REST_LOGIN = 2;         // 로그인
    public final static int REST_SIGN_UP = 3;       // 회원가입
    public final static int REST_ID_DUPLICATED = 4;    // ID 중복검사 (회원가입 중)
    public final static int REST_ADD_CARD = 5;      // 카드 등록
    public final static int REST_GET_CARD = 6;      // 등록되어있는 카드 정보 받아오기
    public final static int REST_NEW_TOKEN = 7;     // 토큰 갱신 시 서버에 전달
    public final static int REST_GET_PHOTO_THUMB = 8;       // 사진 썸네일 받아오기
    public final static int REST_DELETE_CARD = 9;   // 등록되어있는 카드 삭제
    
    public final static int REST_GET_PHOTO_FEED = 10;       // 사진 피드 받아오기
    public final static int REST_GET_PHOTO_PAYMENT = 11;    // 사진 10장 받아오기
    public final static int REST_GET_PHOTO_ORIGINAL = 12;   // 원본 사진, 사진 상세정보 받아오기
    public final static int REST_GET_SHOP_LOCATION = 13;    // 매장 위치 정보 받아오기
    public final static int REST_GET_SHOP_INFO = 14;        // 매장 상세 정보 받아오기
    public final static int REST_DELETE_PHOTO = 15;         // 사진 삭제

    // 액티비티, 프래그먼트 전환 시 사용할 태그
    public final static int RESULT_QR_SCAN = 1;     // QR 코드 스캔 액티비티, QR 코드를 스캔했다면
    public final static int RESULT_ADD_CARD = 2;    // 카드 추가 액티비티, 카드를 추가했다면
}
