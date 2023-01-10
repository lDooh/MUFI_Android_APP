package com.mufi.mufi;

import java.util.regex.Pattern;

public class Validation {
    private static String regExpID = "^[a-zA-Z0-9]{4,20}$";
    private static String regExpPW = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,16}$";
    private static String regExpNick = "^[a-zA-Z0-9가-힇]{3,15}$";
    private static String regExpCall = "^[0][1][0][0-9]{8}$";

    // ID 유효성 검사
    public static boolean validateID(String id) {
        if (Pattern.matches(regExpID, id)) {
            return true;
        }
        else {
            return false;
        }
    }

    // PW 유효성 검사
    public static boolean validatePW(String pw) {
        if (Pattern.matches(regExpPW, pw)) {
            return true;
        }
        else {
            return false;
        }
    }

    // TODO: 이름 유효성 검사
    public static boolean validateName(String name) {
        if (true) {
            return true;
        }
        else {
            return false;
        }
    }

    // 전화번호 유효성 검사
    public static boolean validateCallNumber(String callNumber) {
        if (Pattern.matches(regExpCall, callNumber)) {
            return true;
        }
        else {
            return false;
        }
    }

    static String regExpCardNumber = "^[0-9]{4}$";
    static String regExpExMonth = "^[0-2][0-9]$";
    static String regExpExYear = "^[2-3][0-9]$";
    static String regExpCvc = "^[0-9]{3}$";
    static String regExpCardPW = "^[0-9]{2}$";
    static String regExpBirthdaySix = "^[0-9][0-9][0-2][0-9][0-3][0-9]$";

    // 카드번호 유효성 검사
    public static boolean validateCardNumber(String cardNumber) {
        if (Pattern.matches(regExpCardNumber, cardNumber)) {
            return true;
        }
        else {
            return false;
        }
    }

    // 카드 유효기간(월) 유효성 검사
    public static boolean validateExMonth(String exMonth) {
        if (Pattern.matches(regExpExMonth, exMonth)) {
            return true;
        }
        else {
            return false;
        }
    }

    // 카드 유효기간(년) 유효성 검사
    public static boolean validateExYear(String exYear) {
        if (Pattern.matches(regExpExYear, exYear)) {
            return true;
        }
        else {
            return false;
        }
    }

    // CVC 코드 유효성 검사3
    public static boolean validateCvc(String cvc) {
        if (Pattern.matches(regExpCvc, cvc)) {
            return true;
        }
        else {
            return false;
        }
    }

    // 카드 비밀번호 앞 두 자리 유효성 검사
    public static boolean validateCardPW(String cardPW) {
        if (Pattern.matches(regExpCardPW, cardPW)) {
            return true;
        }
        else {
            return false;
        }
    }

    // 생년월일 6자리 유효성 검사
    public static boolean validateBirthdaySix(String birthday) {
        if (Pattern.matches(regExpBirthdaySix, birthday)) {
            return true;
        }
        else {
            return false;
        }
    }
}
