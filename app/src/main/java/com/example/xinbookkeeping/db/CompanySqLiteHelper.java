package com.example.xinbookkeeping.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.example.xinbookkeeping.bean.CompanyBean;
import com.example.xinbookkeeping.bean.StaffBean;
import com.example.xinbookkeeping.bean.UserBean;

import java.util.ArrayList;
import java.util.List;

public class CompanySqLiteHelper extends SQLiteOpenHelper {

    private static final String INFO_TABLE_NAME = "Companyinfo";
    private static final String STAFF_TABLE_NAME = "Staffinfo";
    private static final String DB_NAME = "Company.db";
    private static final int V = 1;

    public CompanySqLiteHelper(@Nullable Context context) {
        super(context, DB_NAME, null, V);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + INFO_TABLE_NAME
                + "(Id integer primary key autoincrement" +
                ", Uid String unique" +
                ", UnitName String unique" +
                ", TeleNum String" +
                ", Upwd String" +
                ")");

        db.execSQL("create table " + STAFF_TABLE_NAME
                + "(Id integer primary key autoincrement" +
                ", Post String " +
                ", UserId String" +
                ", CompanyId String" +
                ", Time long" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * 新增公司
     *
     * @param UnitName 公司名
     * @param Uid      用户名
     * @param TeleNum  电话号码
     * @param Upwd     登录密码
     */
    public long insert(String UnitName, String Uid, String TeleNum, String Upwd) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("UnitName", UnitName);
        values.put("Uid", Uid);
        values.put("TeleNum", TeleNum);
        values.put("Upwd", Upwd);
        return database.insert(INFO_TABLE_NAME, null, values);
    }

    /**
     * 查找公司
     *
     * @param key
     */
    public CompanyBean query(String key) {
        CompanyBean bean = null;
        SQLiteDatabase database = this.getWritableDatabase();

        Cursor cursor = database.query(INFO_TABLE_NAME, null, "Uid like '" + key + "'", null, null, null, null);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("Id"));
            @SuppressLint("Range") String UnitName = cursor.getString(cursor.getColumnIndex("UnitName"));
            @SuppressLint("Range") String Uid = cursor.getString(cursor.getColumnIndex("Uid"));
            @SuppressLint("Range") String TeleNum = cursor.getString(cursor.getColumnIndex("TeleNum"));
            @SuppressLint("Range") String Upwd = cursor.getString(cursor.getColumnIndex("Upwd"));


            bean = new CompanyBean(id, UnitName, Uid, TeleNum, Upwd);
        }

        cursor.close();
        return bean;
    }

    /**
     * 用户搜索公司
     *
     * @param key 模糊搜索
     */
    public List<CompanyBean> queryList(String key) {
        List<CompanyBean> list = new ArrayList<>();
        SQLiteDatabase database = this.getWritableDatabase();

        Cursor cursor = database.query(INFO_TABLE_NAME, null, "UnitName like '%" + key + "%'", null, null, null, null);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("Id"));
            @SuppressLint("Range") String UnitName = cursor.getString(cursor.getColumnIndex("UnitName"));
            @SuppressLint("Range") String Uid = cursor.getString(cursor.getColumnIndex("Uid"));
            @SuppressLint("Range") String TeleNum = cursor.getString(cursor.getColumnIndex("TeleNum"));
            @SuppressLint("Range") String Upwd = cursor.getString(cursor.getColumnIndex("Upwd"));
            list.add(new CompanyBean(id, UnitName, Uid, TeleNum, Upwd));
        }
        cursor.close();
        return list;
    }

    /**
     * 查找公司
     *
     * @param key 公司主键id
     */
    public CompanyBean queryById(int key) {
        CompanyBean bean = null;
        SQLiteDatabase database = this.getWritableDatabase();

        Cursor cursor = database.query(INFO_TABLE_NAME, null, "Id like " + key, null, null, null, null);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("Id"));
            @SuppressLint("Range") String UnitName = cursor.getString(cursor.getColumnIndex("UnitName"));
            @SuppressLint("Range") String Uid = cursor.getString(cursor.getColumnIndex("Uid"));
            @SuppressLint("Range") String TeleNum = cursor.getString(cursor.getColumnIndex("TeleNum"));
            @SuppressLint("Range") String Upwd = cursor.getString(cursor.getColumnIndex("Upwd"));

            bean = new CompanyBean(id, UnitName, Uid, TeleNum, Upwd);
        }

        cursor.close();
        return bean;
    }

    /**
     * 修改公司名
     */
    public long updateUnitName(String Id, String UnitName) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("UnitName", UnitName);
        return database.update(INFO_TABLE_NAME, values, "Id like ?", new String[]{Id});
    }

    /**
     * 修改手机号
     */
    public long updateTeleNum(String Id, String TeleNum) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TeleNum", TeleNum);
        return database.update(INFO_TABLE_NAME, values, "Id like ?", new String[]{Id});
    }

    /**
     * 修改密码
     */
    public long updateUpwd(String Id, String Upwd) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Upwd", Upwd);
        return database.update(INFO_TABLE_NAME, values, "Id like ?", new String[]{Id});
    }

    /**
     * 新增员工
     *
     * @param UserId    用户id
     * @param CompanyId 公司id
     * @param Post      岗位
     * @param Time      入职时间
     */
    public long insertStaff(String UserId, String CompanyId, String Post, long Time) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("UserId", UserId);
        values.put("CompanyId", CompanyId);
        values.put("Post", Post);
        values.put("Time", Time);
        return database.insert(STAFF_TABLE_NAME, null, values);
    }

    /**
     * 搜索员工
     */
    public List<StaffBean> query(String key, Context context) {
        SQLiteDatabase database = this.getWritableDatabase();
        List<StaffBean> list = new ArrayList<>();
        UserSqLiteHelper userSqLiteHelper = new UserSqLiteHelper(context);

        Cursor cursor = database.query(STAFF_TABLE_NAME, null, "CompanyId like " + key, null, null, null, null);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("Id"));
            @SuppressLint("Range") String UserId = cursor.getString(cursor.getColumnIndex("UserId"));
            @SuppressLint("Range") String Post = cursor.getString(cursor.getColumnIndex("Post"));
            @SuppressLint("Range") long Time = cursor.getLong(cursor.getColumnIndex("Time"));
            UserBean userBean = userSqLiteHelper.queryById(context, Integer.parseInt(UserId));
            list.add(new StaffBean(id, userBean.getNickname(), userBean.getTeleNum(), Post, UserId, Time));
        }

        cursor.close();
        return list;
    }

    /**
     * 搜索员工
     */
    public StaffBean queryOne(String key, Context context) {
        SQLiteDatabase database = this.getWritableDatabase();
        StaffBean bean = null;
        UserSqLiteHelper userSqLiteHelper = new UserSqLiteHelper(context);

        Cursor cursor = database.query(STAFF_TABLE_NAME, null, "UserId like " + key, null, null, null, null);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("Id"));
            @SuppressLint("Range") String UserId = cursor.getString(cursor.getColumnIndex("UserId"));
            @SuppressLint("Range") String Post = cursor.getString(cursor.getColumnIndex("Post"));
            @SuppressLint("Range") long Time = cursor.getLong(cursor.getColumnIndex("Time"));
            UserBean userBean = userSqLiteHelper.queryById(context, Integer.parseInt(UserId));
            bean = new StaffBean(id, userBean.getNickname(), userBean.getTeleNum(), Post, UserId, Time);
        }

        cursor.close();
        return bean;
    }

    /**
     *
     * 删除
     * */
    public long delete(int Id) {
        SQLiteDatabase database = this.getWritableDatabase();
        return database.delete(STAFF_TABLE_NAME, "Id like ?", new String[]{String.valueOf(Id)});
    }

    public long updatePost(String Id, String Post) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Post", Post);
        return database.update(STAFF_TABLE_NAME, values, "Id like ?", new String[]{Id});
    }
}
