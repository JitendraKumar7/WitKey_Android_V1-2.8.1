package app.witkey.live.utils;

/**
 * created by developer on 6/9/2017.
 */

public class StringValidations {

    public static boolean isEmailValid(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
}
