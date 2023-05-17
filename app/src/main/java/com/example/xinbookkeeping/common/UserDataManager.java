package com.example.xinbookkeeping.common;


import android.content.Context;
import android.content.SharedPreferences;

import com.example.xinbookkeeping.ui.MyApp;


/**
 * 用户管理 存储一些用户登录的基本信息
 */
public class UserDataManager {

    private static UserDataManager manager;

    private UserDataManager() {
    }

    public static UserDataManager getInstance() {
        if (manager == null) {
            synchronized (UserDataManager.class) {
                if (manager == null) {
                    manager = new UserDataManager();
                }
            }
        }
        return manager;
    }

    private SharedPreferences getSp() {
        return MyApp.getInstance().getSharedPreferences("user-local", Context.MODE_PRIVATE);
    }

    public void setUserData(int id, String uid) {
        this.id = id;
        this.uid = uid;
        SharedPreferences sp = getSp();
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("id", id);
        editor.putString("uid", uid);
        editor.commit();
    }

    private int id = ID_DEFAULT;

    private static final int ID_DEFAULT = -99;
    private String uid;

    public int getId() {
        if (id == ID_DEFAULT) {
            SharedPreferences sp = getSp();
            id = sp.getInt("id", ID_DEFAULT);
        }
        return id;
    }

    public String getUid() {
        if (uid == null) {
            SharedPreferences sp = getSp();
            uid = sp.getString("uid", null);
        }
        return uid;
    }

    public void clearData() {
        SharedPreferences sp = getSp();
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("id", ID_DEFAULT);
        editor.putString("uid", null);
        id = -ID_DEFAULT;
        uid = null;
        editor.commit();
    }

}
