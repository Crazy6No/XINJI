package com.example.xinbookkeeping.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.xinbookkeeping.bean.RecordBean;

import java.util.ArrayList;
import java.util.List;

public class RecordSqLiteHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "Record";
    private static final String DB_NAME = "Record.db";
    private static final int V = 1;

    public RecordSqLiteHelper(@Nullable Context context) {
        super(context, DB_NAME, null, V);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME
                + "(Id integer primary key autoincrement" +
                ", UserId String" +
                ", Type String" +
                ", Money String" +
                ", State String" +
                ", RecordDesc String" +
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
     * @param UserId 用户id
     * @param Type   类型 收入/支出
     * @param Money  金额
     * @param State  收入/支出的分类(比如购物)
     * @param Time   存入表的时间戳
     */
    public long insert(String UserId, String Type, String Money, String State, String RecordDesc, int Year, int Month, int Day, long Time) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("UserId", UserId);
        values.put("Type", Type);
        values.put("Money", Money);
        values.put("State", State);
        values.put("RecordDesc", RecordDesc);
        values.put("Year", Year);
        values.put("Month", Month);
        values.put("Day", Day);
        values.put("Time", Time);
        return database.insert(TABLE_NAME, null, values);
    }

    /**
     *
     * 修改账单
     * */
    public long update(String Id, String Type, String Money, String State, String RecordDesc) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Type", Type);
        values.put("Money", Money);
        values.put("State", State);
        values.put("RecordDesc", RecordDesc);

        return database.update(TABLE_NAME, values, "Id like ?", new String[]{Id});
    }

    /**
     *
     * 删除
     * */
    public long delete(int Id) {
        SQLiteDatabase database = this.getWritableDatabase();
        return database.delete(TABLE_NAME, "Id like ?", new String[]{String.valueOf(Id)});
    }


    /**
     * 搜索某个用户的账簿记录
     *
     * @
     */
    public List<RecordBean> queryByUserId(String key) {
        List<RecordBean> list = new ArrayList<>();
        SQLiteDatabase database = this.getWritableDatabase();

        Cursor cursor = database.query(TABLE_NAME, null, "UserId like " + key, null, null, null, "Time desc");
        while (cursor.moveToNext()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("Id"));
            @SuppressLint("Range") String UserId = cursor.getString(cursor.getColumnIndex("UserId"));
            @SuppressLint("Range") String Type = cursor.getString(cursor.getColumnIndex("Type"));
            @SuppressLint("Range") String Money = cursor.getString(cursor.getColumnIndex("Money"));
            @SuppressLint("Range") String State = cursor.getString(cursor.getColumnIndex("State"));
            @SuppressLint("Range") String RecordDesc = cursor.getString(cursor.getColumnIndex("RecordDesc"));
            @SuppressLint("Range") int Year = cursor.getInt(cursor.getColumnIndex("Year"));
            @SuppressLint("Range") int Month = cursor.getInt(cursor.getColumnIndex("Month"));
            @SuppressLint("Range") int Day = cursor.getInt(cursor.getColumnIndex("Day"));
            @SuppressLint("Range") long Time = cursor.getLong(cursor.getColumnIndex("Time"));

            list.add(new RecordBean(id, UserId, Type, Money, State, RecordDesc, Year, Month, Day, Time));
        }

        cursor.close();
        return list;
    }

    /**
     *
     * 根据账单id搜索
     */
    public RecordBean queryById(String key) {
        RecordBean bean = null;
        SQLiteDatabase database = this.getWritableDatabase();

        Cursor cursor = database.query(TABLE_NAME, null, "Id like " + key, null, null, null, "Time desc");
        while (cursor.moveToNext()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("Id"));
            @SuppressLint("Range") String UserId = cursor.getString(cursor.getColumnIndex("UserId"));
            @SuppressLint("Range") String Type = cursor.getString(cursor.getColumnIndex("Type"));
            @SuppressLint("Range") String Money = cursor.getString(cursor.getColumnIndex("Money"));
            @SuppressLint("Range") String State = cursor.getString(cursor.getColumnIndex("State"));
            @SuppressLint("Range") String RecordDesc = cursor.getString(cursor.getColumnIndex("RecordDesc"));
            @SuppressLint("Range") int Year = cursor.getInt(cursor.getColumnIndex("Year"));
            @SuppressLint("Range") int Month = cursor.getInt(cursor.getColumnIndex("Month"));
            @SuppressLint("Range") int Day = cursor.getInt(cursor.getColumnIndex("Day"));
            @SuppressLint("Range") long Time = cursor.getLong(cursor.getColumnIndex("Time"));

            bean = new RecordBean(id, UserId, Type, Money, State, RecordDesc, Year, Month, Day, Time);
        }

        cursor.close();
        return bean;
    }

    /**
     * 搜索某个用户的账簿记录
     */
    public List<RecordBean> query(String key, String y, String m) {
        List<RecordBean> list = new ArrayList<>();
        SQLiteDatabase database = this.getWritableDatabase();

        Cursor cursor = database.query(TABLE_NAME, null, "UserId like " + key + " and Year like " + y + " and Month like " + m, null, null, null, "Time desc");
        while (cursor.moveToNext()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("Id"));
            @SuppressLint("Range") String UserId = cursor.getString(cursor.getColumnIndex("UserId"));
            @SuppressLint("Range") String Type = cursor.getString(cursor.getColumnIndex("Type"));
            @SuppressLint("Range") String Money = cursor.getString(cursor.getColumnIndex("Money"));
            @SuppressLint("Range") String State = cursor.getString(cursor.getColumnIndex("State"));
            @SuppressLint("Range") String RecordDesc = cursor.getString(cursor.getColumnIndex("RecordDesc"));
            @SuppressLint("Range") int Year = cursor.getInt(cursor.getColumnIndex("Year"));
            @SuppressLint("Range") int Month = cursor.getInt(cursor.getColumnIndex("Month"));
            @SuppressLint("Range") int Day = cursor.getInt(cursor.getColumnIndex("Day"));
            @SuppressLint("Range") long Time = cursor.getLong(cursor.getColumnIndex("Time"));

            list.add(new RecordBean(id, UserId, Type, Money, State, RecordDesc, Year, Month, Day, Time));
        }

        cursor.close();
        return list;
    }
}
