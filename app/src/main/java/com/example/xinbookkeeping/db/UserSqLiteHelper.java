package com.example.xinbookkeeping.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.example.xinbookkeeping.bean.CompanyBean;
import com.example.xinbookkeeping.bean.UserBean;

import java.util.ArrayList;
import java.util.List;

public class UserSqLiteHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "Userinfo";
    private static final String DB_NAME = "User.db";
    private static final int V = 1;

    public UserSqLiteHelper(@Nullable Context context) {
        super(context, DB_NAME, null, V);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME
                + "(Id integer primary key autoincrement" +
                ", Uid String unique" +
                ", Nickname String" +
                ", TeleNum String" +
                ", Upwd String" +
                ", CompanyId String" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * 新增用户
     *
     * @param Nickname 昵称
     * @param Uid      用户名
     * @param TeleNum  电话号码
     * @param Upwd     登录密码
     */
    public long insert(String Nickname, String Uid, String TeleNum, String Upwd) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Nickname", Nickname);
        values.put("Uid", Uid);
        values.put("TeleNum", TeleNum);
        values.put("Upwd", Upwd);
        values.put("CompanyId", "");
        return database.insert(TABLE_NAME, null, values);
    }

    /**
     * 查找用户
     *
     * @param key 用户名
     */
    public UserBean query(Context context, String key) {
        UserBean data = null;
        SQLiteDatabase database = this.getWritableDatabase();

        Cursor cursor = database.query(TABLE_NAME, null, "Uid like '" + key + "'", null, null, null, null);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("Id"));
            @SuppressLint("Range") String Nickname = cursor.getString(cursor.getColumnIndex("Nickname"));
            @SuppressLint("Range") String Uid = cursor.getString(cursor.getColumnIndex("Uid"));
            @SuppressLint("Range") String TeleNum = cursor.getString(cursor.getColumnIndex("TeleNum"));
            @SuppressLint("Range") String Upwd = cursor.getString(cursor.getColumnIndex("Upwd"));
            @SuppressLint("Range") String CompanyId = cursor.getString(cursor.getColumnIndex("CompanyId"));

            if (!TextUtils.isEmpty(CompanyId)) {
                CompanySqLiteHelper companySqLiteHelper = new CompanySqLiteHelper(context);
                CompanyBean bean = companySqLiteHelper.queryById(Integer.parseInt(CompanyId));
                data = new UserBean(id, Nickname, Uid, TeleNum, Upwd, bean.getUnitname(), CompanyId);
            } else {
                data = new UserBean(id, Nickname, Uid, TeleNum, Upwd);
            }
        }

        cursor.close();
        return data;
    }

    /**
     * 查找用户
     *
     * @param key 用户主键id
     */
    public UserBean queryById(Context context, int key) {
        UserBean data = null;
        SQLiteDatabase database = this.getWritableDatabase();

        Cursor cursor = database.query(TABLE_NAME, null, "Id like " + key, null, null, null, null);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("Id"));
            @SuppressLint("Range") String Nickname = cursor.getString(cursor.getColumnIndex("Nickname"));
            @SuppressLint("Range") String Uid = cursor.getString(cursor.getColumnIndex("Uid"));
            @SuppressLint("Range") String TeleNum = cursor.getString(cursor.getColumnIndex("TeleNum"));
            @SuppressLint("Range") String Upwd = cursor.getString(cursor.getColumnIndex("Upwd"));
            @SuppressLint("Range") String CompanyId = cursor.getString(cursor.getColumnIndex("CompanyId"));

            if (!TextUtils.isEmpty(CompanyId)) {
                CompanySqLiteHelper companySqLiteHelper = new CompanySqLiteHelper(context);
                CompanyBean bean = companySqLiteHelper.queryById(Integer.parseInt(CompanyId));
                data = new UserBean(id, Nickname, Uid, TeleNum, Upwd, bean.getUnitname(), CompanyId);
            } else {
                data = new UserBean(id, Nickname, Uid, TeleNum, Upwd);
            }
        }

        cursor.close();
        return data;
    }

    /**
     * 公司搜索用户
     *
     * @param key 模糊搜索
     */
    public List<UserBean> queryByNickname(Context context, String key) {
        List<UserBean> list = new ArrayList<>();
        SQLiteDatabase database = this.getWritableDatabase();

        Cursor cursor = database.query(TABLE_NAME, null, "Nickname like '%" + key + "%' or Uid like '" + key + "'", null, null, null, null);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("Id"));
            @SuppressLint("Range") String Nickname = cursor.getString(cursor.getColumnIndex("Nickname"));
            @SuppressLint("Range") String Uid = cursor.getString(cursor.getColumnIndex("Uid"));
            @SuppressLint("Range") String TeleNum = cursor.getString(cursor.getColumnIndex("TeleNum"));
            @SuppressLint("Range") String Upwd = cursor.getString(cursor.getColumnIndex("Upwd"));
            @SuppressLint("Range") String CompanyId = cursor.getString(cursor.getColumnIndex("CompanyId"));
            if (!TextUtils.isEmpty(CompanyId)) {
                CompanySqLiteHelper companySqLiteHelper = new CompanySqLiteHelper(context);
                CompanyBean bean = companySqLiteHelper.queryById(Integer.parseInt(CompanyId));
                list.add(new UserBean(id, Nickname, Uid, TeleNum, Upwd, bean.getUnitname(), CompanyId));
            } else {
                list.add(new UserBean(id, Nickname, Uid, TeleNum, Upwd));
            }

        }

        cursor.close();
        return list;
    }

    /**
     * 修改昵称
     */
    public long updateNickname(String Id, String Nickname) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Nickname", Nickname);
        return database.update(TABLE_NAME, values, "Id like ?", new String[]{Id});
    }

    /**
     * 修改手机号
     */
    public long updateTeleNum(String Id, String TeleNum) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TeleNum", TeleNum);
        return database.update(TABLE_NAME, values, "Id like ?", new String[]{Id});
    }

    /**
     * 修改密码
     */
    public long updateUpwd(String Id, String Upwd) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Upwd", Upwd);
        return database.update(TABLE_NAME, values, "Id like ?", new String[]{Id});
    }

    /**
     * 绑定公司
     */
    public long updateCompanyId(String Id, String CompanyId) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("CompanyId", CompanyId);
        return database.update(TABLE_NAME, values, "Id like ?", new String[]{Id});
    }
}
