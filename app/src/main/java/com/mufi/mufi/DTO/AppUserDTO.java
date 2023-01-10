package com.mufi.mufi.DTO;

import java.io.Serializable;

// 현재 로그인 되어있는 유저의 정보 저장
public class AppUserDTO implements Serializable {
    private static AppUserDTO instance;
    private static final long serialVersionUID = 1L;
    private static String id, password, name, callNumber, birthday;
    private static int gender;     // 0 == female, 1 == male

    public static AppUserDTO getInstance() {
        if (instance == null) {
            instance = new AppUserDTO();
        }
        return instance;
    }

    private AppUserDTO() {

    }

    public static void setAppUserDTO(String id, String password, String name, String callNumber, String birthday, int gender) {
        setId(id);
        setPassword(password);
        setName(name);
        setCallNumber(callNumber);
        setBirthday(birthday);
        setGender(gender);
    }

    public static String getId() {
        return id;
    }
    public static void setId(String id) {
        instance.id = id;
    }

    public static String getPassword() {
        return password;
    }
    public static void setPassword(String password) {
        instance.password = password;
    }

    public static String getName() {
        return name;
    }
    public static void setName(String name) {
        instance.name = name;
    }

    public static String getCallNumber() {
        return callNumber;
    }
    public static void setCallNumber(String callNumber) {
        instance.callNumber = callNumber;
    }

    public static String getBirthday() {
        return birthday;
    }
    public static void setBirthday(String birthday) {
        // yyyymmdd 형태로 저장
        String[] birthdaySplit = birthday.split("-");

        if (birthdaySplit.length == 1) {    // yyyymmdd 형태로 전달됐다면 그대로 저장
            instance.birthday = birthdaySplit[0];
        } else {    // yyyy-mm-dd 형태로 전달됐다면 "-" 빼기
            StringBuffer birthdayBuffer = new StringBuffer();
            birthdayBuffer.append(birthdaySplit[0]);

            if (birthdaySplit[1].length() == 1) {
                birthdayBuffer.append("0");
            }
            birthdayBuffer.append(birthdaySplit[1]);

            if (birthdaySplit[2].length() == 1) {
                birthdayBuffer.append("0");
            }
            birthdayBuffer.append(birthdaySplit[2]);
            instance.birthday = birthdayBuffer.toString();
        }
    }

    public static int getGender() {
        return gender;
    }
    public static void setGender(int gender) {
        instance.gender = gender;
    }
}
