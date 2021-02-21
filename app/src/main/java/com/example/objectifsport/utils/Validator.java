package com.example.objectifsport.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Validator {

    public static boolean isValid(String dateStr) {
        DateFormat sdf = new SimpleDateFormat();
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }
}
