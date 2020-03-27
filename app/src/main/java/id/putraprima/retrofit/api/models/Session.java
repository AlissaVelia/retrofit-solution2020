package id.putraprima.retrofit.api.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Session {

    private static final String DATA_KEY_APP = "data_app";
    private static final String DATA_KEY_VERSION = "data_version";
    private static final String TOKEN_KEY = "key_token";
    private  static final String SAVE_DATA = "key_keep_app";
    private  static  final String TOKEN_TYPE = "token_type";

    private SharedPreferences preferences;

    public Session(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String getDataApp() {
        return preferences.getString(DATA_KEY_APP, null);
    }

    public void setDataApp(String app) {
        preferences.edit().putString(DATA_KEY_APP, app)
                .apply();
    }

    public String getDataVersion() {
        return preferences.getString(DATA_KEY_VERSION, null);
    }

    public void setDataVersion(String version) {
        preferences.edit().putString(DATA_KEY_VERSION, version)
                .apply();
    }

    public boolean isKeepData(){
        return preferences.getBoolean(SAVE_DATA,false);
    }

    public String getToken() {
        return preferences.getString(TOKEN_KEY, null);
    }

    public void setToken(String token) {
        preferences.edit().putString(TOKEN_KEY, token)
                .apply();
    }

    public String getTokenType() {
        return preferences.getString(TOKEN_TYPE, null);
    }

    public void setTokenType(String tokenType) {
        preferences.edit().putString(TOKEN_TYPE, tokenType)
                .apply();
    }

}