package app.witkey.live.utils.sharedpreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

/**
 * created by developer on 10/03/2017.
 */

public class ObjectSharedPreference {


    public static void saveObject(Object object, String key) {
        final Gson gson = new Gson();
        String serializedObject = gson.toJson(object);
        SharedPref.save(key, serializedObject);
    }

    public static <GenericClass> GenericClass getObject(Class<GenericClass> classType, String key) {
        String data = SharedPref.read(key, null);
        if (data != null) {
            final Gson gson = new Gson();
            return gson.fromJson(data, classType);
        }
        return null;
    }

    public static Object getObject(String key) {
        String data = SharedPref.read(key, null);
        try {
            if (data != null) {
                return new Gson().fromJson(data, new TypeToken<HashMap<String, HashMap<String, Boolean>>>() {
                }.getType());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}
