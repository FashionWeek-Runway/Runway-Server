package com.example.runway.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationRegex {

    public static boolean validationPassword(String password) {
        // 비밀번호 포맷 확인(영문, 숫자 포함 8자 이상)
        Pattern pattern = Pattern.compile("^[A-Za-z[0-9]]{8,16}$");
        Matcher matcher = pattern.matcher(password);

        return matcher.find();
    }

    public static boolean validationPhoneNumber(String phone) {
        Pattern pattern = Pattern.compile("\\d{11}");
        Matcher matcher = pattern.matcher(phone);

        return matcher.matches();
    }


}
