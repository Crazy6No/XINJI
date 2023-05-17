package com.example.xinbookkeeping.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.xinbookkeeping.bean.PayBean;
import com.example.xinbookkeeping.bean.RecordBean;

import java.util.ArrayList;
import java.util.List;


public class PaySqLiteHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "PaySlip";
    private static final String DB_NAME = "Pay.db";
    private static final int V = 1;

    public PaySqLiteHelper(@Nullable Context context) {
        super(context, DB_NAME, null, V);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME
                + "(Id integer primary key autoincrement" +
                ", UserId String" +
                ", CompanyId String" +
                ", Money String" +
                ", Year integer" +
                ", Month integer" +
                ", Day integer" +
                ", Time long" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * 新增
     *
     * @param UserId    用户id
     * @param CompanyId 公司id
     * @param Money     金额
     * @param Time      存入表的时间戳
     */
    public long insert(String UserId, String CompanyId, String Money, int Year, int Month, int Day, long Time) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("UserId", UserId);
        values.put("CompanyId", CompanyId);
        values.put("Money", Money);
        values.put("Year", Year);
        values.put("Month", Month);
        values.put("Day", Day);
        values.put("Time", Time);
        return database.insert(TABLE_NAME, null, values);
    }

    /**
     * 搜索某个用户的工资
     *
     * @param key1 用户id
     * @param key2 公司id
     */
    public List<PayBean> queryByUserId(String key1, String key2) {
        List<PayBean> list = new ArrayList<>();
        SQLiteDatabase database = this.getWritableDatabase();

        Cursor cursor = database.query(TABLE_NAME, null, "UserId like " + key1 + " and CompanyId like " + key2, null, null, null, "Time desc");
        while (cursor.moveToNext()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("Id"));
            @SuppressLint("Range") String UserId = cursor.getString(cursor.getColumnIndex("UserId"));
            @SuppressLint("Range") String CompanyId = cursor.getString(cursor.getColumnIndex("CompanyId"));
            @SuppressLint("Range") String Money = cursor.getString(cursor.getColumnIndex("Money"));
            @SuppressLint("Range") int Year = cursor.getInt(cursor.getColumnIndex("Year"));
            @SuppressLint("Range") int Month = cursor.getInt(cursor.getColumnIndex("Month"));
            @SuppressLint("Range") int Day = cursor.getInt(cursor.getColumnIndex("Day"));
            @SuppressLint("Range") long Time = cursor.getLong(cursor.getColumnIndex("Time"));

            list.add(new PayBean(id, UserId, CompanyId, Money, Year, Month, Day, Time));
        }

        cursor.close();
        return list;
    }

    /**
     *
     * 搜索公司年数据
     * */
    public List<PayBean> queryByCompanyYear(String key1, int key2) {
        List<PayBean> list = new ArrayList<>();
        SQLiteDatabase database = this.getWritableDatabase();

        Cursor cursor = database.query(TABLE_NAME, null, "CompanyId like " + key1 + " and Year like " + key2, null, null, null, "Time desc");
        while (cursor.moveToNext()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("Id"));
            @SuppressLint("Range") String UserId = cursor.getString(cursor.getColumnIndex("UserId"));
            @SuppressLint("Range") String CompanyId = cursor.getString(cursor.getColumnIndex("CompanyId"));
            @SuppressLint("Range") String Money = cursor.getString(cursor.getColumnIndex("Money"));
            @SuppressLint("Range") int Year = cursor.getInt(cursor.getColumnIndex("Year"));
            @SuppressLint("Range") int Month = cursor.getInt(cursor.getColumnIndex("Month"));
            @SuppressLint("Range") int Day = cursor.getInt(cursor.getColumnIndex("Day"));
            @SuppressLint("Range") long Time = cursor.getLong(cursor.getColumnIndex("Time"));

            list.add(new PayBean(id, UserId, CompanyId, Money, Year, Month, Day, Time));
        }

        cursor.close();
        return list;
    }

    /**
     *
     * 搜索公司月数据
     * */
    public List<PayBean> queryByCompanyMonth(String key1, int key2, int key3) {
        List<PayBean> list = new ArrayList<>();
        SQLiteDatabase database = this.getWritableDatabase();

        Cursor cursor = database.query(TABLE_NAME, null, "CompanyId like " + key1 + " and Year like " + key2 + " and Month like " + key3, null, null, null, "Time desc");
        while (cursor.moveToNext()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("Id"));
            @SuppressLint("Range") String UserId = cursor.getString(cursor.getColumnIndex("UserId"));
            @SuppressLint("Range") String CompanyId = cursor.getString(cursor.getColumnIndex("CompanyId"));
            @SuppressLint("Range") String Money = cursor.getString(cursor.getColumnIndex("Money"));
            @SuppressLint("Range") int Year = cursor.getInt(cursor.getColumnIndex("Year"));
            @SuppressLint("Range") int Month = cursor.getInt(cursor.getColumnIndex("Month"));
            @SuppressLint("Range") int Day = cursor.getInt(cursor.getColumnIndex("Day"));
            @SuppressLint("Range") long Time = cursor.getLong(cursor.getColumnIndex("Time"));

            list.add(new PayBean(id, UserId, CompanyId, Money, Year, Month, Day, Time));
        }

        cursor.close();
        return list;
    }
}
