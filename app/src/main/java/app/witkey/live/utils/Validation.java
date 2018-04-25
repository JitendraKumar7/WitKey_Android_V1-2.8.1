package app.witkey.live.utils;

import android.util.Patterns;


public class Validation {

    public static boolean isEmpty(String keyword) {

        return keyword.length() == 0;
    }

    public static boolean isNonEmpty(String value) {
        return !(value == null || value.isEmpty() || value.equalsIgnoreCase("") || value.equalsIgnoreCase("null"));
    }

    public static boolean isURLValid(String link) {
        return Patterns.WEB_URL.matcher(link).matches();
    }

    public static boolean isDoubleValid(String value) {
        return !(value.equalsIgnoreCase("."));
    }

    // METHOD TO VALIDATE EMAIL
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
}
