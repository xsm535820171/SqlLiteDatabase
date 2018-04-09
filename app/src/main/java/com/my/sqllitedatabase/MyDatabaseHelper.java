package com.my.sqllitedatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by AOW on 2018/4/8.
 *
 * SQLiteOpenHelper是一个用于管理数据库的工具类，用于管理数据库的创建和更新
 */

public class MyDatabaseHelper extends SQLiteOpenHelper{

    final String CREATE_TABLE="create table dict(_id integer primary key autoincrement,word varchar(255),detail varchar(255))";

    /**
     *
     * @param context 上下文
     * @param name 数据库名字
     * @param factory
     * @param version
     */
    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    /**
     * 第一次创建数据库时回调该方法
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    /**
     * 当数据库版本需要更新时回调该方法
     * @param sqLiteDatabase
     * @param i  老版本号
     * @param i1 新版本号
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        System.out.println("oldversion=="+i+",,newversion=="+i1);
    }

    /**
     * getReadableDatabase以读的方式打开数据库，
     * @return
     */
    @Override
    public SQLiteDatabase getReadableDatabase() {
        return super.getReadableDatabase();
    }

    /**
     * 以读写的方式打开数据库，一旦数据库的磁盘空间满了，数据库只能读不能写
     *  如果getWritableDatabase打开数据库失败，getReadableDatabase会先已读的方式打开数据库，
     */
    @Override
    public SQLiteDatabase getWritableDatabase() {
        return super.getWritableDatabase();
    }

    /**
     * 关闭所有打开的数据库SQLiteDatabase
     */
    @Override
    public synchronized void close() {
        super.close();
    }
}
