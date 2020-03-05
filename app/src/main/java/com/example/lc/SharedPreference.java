package com.example.lc;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class  SharedPreference{
    private final Context context;
    private final SharedPreferences pref;
    private static Editor editor;

    private static final String PREF_NAME = "lcpref";
    private static int PRIVATE_MODE = 0;
    private static final String KEY_LOGIN = "loginstatus";
    private static final String KEY_NAME = "adminname";

    public SharedPreference(Context c) {
        this.context = c;
        pref=c.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor = pref.edit();
    }

    public Boolean getLoginStatus() {
        return pref.getBoolean(KEY_LOGIN, false);
    }

    public void setLoginStatus(Boolean str) {
        try {
            editor.putBoolean(KEY_LOGIN, str);
            editor.commit();
        } catch (Exception e) {
        }
    }

    public String getusername(){
        return pref.getString(KEY_NAME,"");
    }

    public void setusername(String str){
        try{
            editor.putString(KEY_NAME,str);
            editor.commit();
        }catch (Exception e){
        }
    }

    public void ClearLogin(){
        editor.putBoolean(KEY_LOGIN,false);
        editor.remove(KEY_NAME);
        editor.commit();
    }
}
