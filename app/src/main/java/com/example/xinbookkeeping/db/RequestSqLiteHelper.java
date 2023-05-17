package com.example.xinbookkeeping.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.xinbookkeeping.bean.CompanyBean;
import com.example.xinbookkeeping.bean.RequestBean;
import com.example.xinbookkeeping.bean.UserBean;

import java.util.ArrayList;
import java.util.List;


public class RequestSqLiteHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "Request";
    private static final String DB_NAME = "Request.db";
    private static final int V = 1;

    public RequestSqLiteHelper(@Nullable Context context) {
        super(context, DB_NAME, null, V);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME
                + "(Id integer primary key autoincrement" +
                ", UserId String" +
                ", CompanyId String" +
                ", Post String" +
                ", RequestTime long" +
                ", Time long" +
                ", Operate String" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * 新增
     *
     * @param UserId      用户id
     * @param CompanyId   公司id
     * @param RequestTime 公司id
     * @param Time        存入表的时间戳
     *                    Operate 操作 1 申请中 2 已拒绝 3 已同意 4 已处理（申请多家公司后 加入某一家公司 其余的申请都会被处理）
     */
    public long insert(String UserId, String CompanyId, String Post, long RequestTime, long Time) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("UserId", UserId);
        values.put("CompanyId", CompanyId);
        values.put("Post", Post);
        values.put("RequestTime", RequestTime);
        values.put("Time", Time);
        values.put("Operate", "1");
        return database.insert(TABLE_NAME, null, values);
    }

    public RequestBean query(String key1, String key2) {
        RequestBean bean = null;
        SQLiteDatabase database = this.getWritableDatabase();

        Cursor cursor = database.query(TABLE_NAME, null, "UserId like " + key1 + " and CompanyId like " + key2, null, null, null, "Time desc");
        while (cursor.moveToNext()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("Id"));
            @SuppressLint("Range") String UserId = cursor.getString(cursor.getColumnIndex("UserId"));
            @SuppressLint("Range") String CompanyId = cursor.getString(cursor.getColumnIndex("CompanyId"));
            @SuppressLint("Range") String Post = cursor.getString(cursor.getColumnIndex("Post"));
            @SuppressLint("Range") long RequestTime = cursor.getLong(cursor.getColumnIndex("RequestTime"));
            @SuppressLint("Range") long Time = cursor.getLong(cursor.getColumnIndex("Time"));
            @SuppressLint("Range") String Operate = cursor.getString(cursor.getColumnIndex("Operate"));
            bean = new RequestBean(id, UserId, CompanyId, Post, RequestTime, Time, Operate);
        }
        cursor.close();
        return bean;
    }

    public List<RequestBean> queryUserOtherIng(String key) {
        List<RequestBean> list = new ArrayList<>();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.query(TABLE_NAME, null, "UserId like " + key +" and Operate like '1'", null, null, null, "Time desc");
        while (cursor.moveToNext()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("Id"));
            @SuppressLint("Range") String UserId = cursor.getString(cursor.getColumnIndex("UserId"));
            @SuppressLint("Range") String CompanyId = cursor.getString(cursor.getColumnIndex("CompanyId"));
            @SuppressLint("Range") String Post = cursor.getString(cursor.getColumnIndex("Post"));
            @SuppressLint("Range") long RequestTime = cursor.getLong(cursor.getColumnIndex("RequestTime"));
            @SuppressLint("Range") long Time = cursor.getLong(cursor.getColumnIndex("Time"));
            @SuppressLint("Range") String Operate = cursor.getString(cursor.getColumnIndex("Operate"));

            list.add(new RequestBean(id, UserId, CompanyId, Post, RequestTime, Time, Operate));
        }
        cursor.close();
        return list;
    }

    public List<RequestBean> queryUser(String key, Context context) {
        List<RequestBean> list = new ArrayList<>();
        SQLiteDatabase database = this.getWritableDatabase();
        CompanySqLiteHelper companySqLiteHelper = new CompanySqLiteHelper(context);
        Cursor cursor = database.query(TABLE_NAME, null, "UserId like " + key, null, null, null, "Time desc");
        while (cursor.moveToNext()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("Id"));
            @SuppressLint("Range") String UserId = cursor.getString(cursor.getColumnIndex("UserId"));
            @SuppressLint("Range") String CompanyId = cursor.getString(cursor.getColumnIndex("CompanyId"));
            @SuppressLint("Range") String Post = cursor.getString(cursor.getColumnIndex("Post"));
            @SuppressLint("Range") long RequestTime = cursor.getLong(cursor.getColumnIndex("RequestTime"));
            @SuppressLint("Range") long Time = cursor.getLong(cursor.getColumnIndex("Time"));
            @SuppressLint("Range") String Operate = cursor.getString(cursor.getColumnIndex("Operate"));

            CompanyBean companyBean = companySqLiteHelper.queryById(Integer.parseInt(CompanyId));
            list.add(new RequestBean(id, UserId, CompanyId, Post, RequestTime, Time, Operate, companyBean.getUnitname() + " 电话：" + companyBean.getTeleNum()));
        }
        cursor.close();
        return list;
    }

    public List<RequestBean> queryCompany(String key, Context context) {
        List<RequestBean> list = new ArrayList<>();
        SQLiteDatabase database = this.getWritableDatabase();
        UserSqLiteHelper userSqLiteHelper = new UserSqLiteHelper(context);
        Cursor cursor = database.query(TABLE_NAME, null, "CompanyId like " + key + " and Operate like '1'", null, null, null, "Time desc");
        while (cursor.moveToNext()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("Id"));
            @SuppressLint("Range") String UserId = cursor.getString(cursor.getColumnIndex("UserId"));
            @SuppressLint("Range") String CompanyId = cursor.getString(cursor.getColumnIndex("CompanyId"));
            @SuppressLint("Range") String Post = cursor.getString(cursor.getColumnIndex("Post"));
            @SuppressLint("Range") long RequestTime = cursor.getLong(cursor.getColumnIndex("RequestTime"));
            @SuppressLint("Range") long Time = cursor.getLong(cursor.getColumnIndex("Time"));
            @SuppressLint("Range") String Operate = cursor.getString(cursor.getColumnIndex("Operate"));

            UserBean userBean = userSqLiteHelper.queryById(context, Integer.parseInt(UserId));
            list.add(new RequestBean(id, UserId, CompanyId, Post, RequestTime, Time, Operate, userBean.getNickname() + " 电话：" + userBean.getTeleNum()));
        }
        cursor.close();
        return list;
    }

    /**
     * 修改手机号
     */
    public long updateOperate(String Id, String Operate) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Operate", Operate);
        return database.update(TABLE_NAME, values, "Id like ?", new String[]{Id});
    }
}
