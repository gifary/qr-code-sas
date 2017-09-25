package hospitality.sas.gifary.absenkaryawan.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import hospitality.sas.gifary.absenkaryawan.Constants;

/**
 * Created by gifary on 25/09/17.
 */

public class UserUtil {
    private static UserUtil instance;

    private SharedPreferences preferences;

    public static UserUtil getInstance(Context context) {
        if (instance == null) {
            instance = new UserUtil();
            instance.preferences = PreferenceManager.getDefaultSharedPreferences(context);
        }

        return instance;
    }

    public String getStringProperty(String key) {
        return preferences.getString(key, "");
    }

    public void setStringProperty(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value).apply();
    }

    public boolean getBooleanProperty(String key) {
        return preferences.getBoolean(key, false);
    }

    public void setBooleanProperty(String key, boolean value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value).apply();
    }

    public int getIntProperty(String key) {
        return preferences.getInt(key, 0);
    }

    public void setIntProperty(String key, int value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value).apply();
    }

    public float getFloatProperty(String key) {
        return preferences.getFloat(key, 0.0F);
    }

    public void setFloatProperty(String key, float value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat(key, value).apply();
    }

    /**
     * Clearing all saved preferences, used for logging out
     */
    public void reset() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear().apply();
        setBooleanProperty("SHOW_WIDGET", true);
    }

    public boolean isLoggedIn() {
        return !getEmail().isEmpty();
    }

    public void signIn(JSONObject data, String token) throws JSONException {
        setIntProperty(Constants.USER_ID, data.getInt("user_id"));
        setStringProperty(Constants.USER_FULL_NAME, data.getString("nama"));
        setStringProperty(Constants.USER_EMAIL, data.getString("email_perusahaan"));
        setStringProperty(Constants.USER_NIK, data.getString("nik"));
    }

    public void signOut() {
        reset();
    }

    public String getEmail() {
        return getStringProperty(Constants.USER_EMAIL);
    }

    public String getId() {
        return getStringProperty(Constants.USER_ID);
    }

    public String getFullName() {
        return getStringProperty(Constants.USER_FULL_NAME);
    }

    public String getNIK() {
        return getStringProperty(Constants.USER_NIK);
    }

}
