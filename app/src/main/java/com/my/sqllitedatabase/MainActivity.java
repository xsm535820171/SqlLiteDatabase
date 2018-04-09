package com.my.sqllitedatabase;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.my.xutilsdatabase.DbUtilesActivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * DDL(Data Definition Language):数据定义语言，用于定义和管理 SQL 数据库中的所有对象的语言
        1.CREATE - to create objects in the database 创建
        2.ALTER - alters the structure of the database 修改
        3.DROP - delete objects from the database 删除
        4.TRUNCATE - remove all records from a table, including all spaces allocated for the records are removed
            TRUNCATE TABLE [Table Name]。
 * DML(Data Manipulation Language):数据操作语言,SQL中处理数据等操作统称为数据操纵语言
        1.SELECT - retrieve data from the a database 查询
        2.INSERT - insert data into a table 添加
        3.UPDATE - updates existing data within a table 更新
        4.DELETE - deletes all records from a table, the space for the records remain 删除
        5.CALL - call a PL/SQL or Java subprogram
        6.EXPLAIN PLAN - explain access path to data
 *DCL(Data Control Language):数据控制语言，用来授予或回收访问数据库的某种特权，并控制数据库操纵事务发生的时间及效果，对数据库实行监视等
        1.COMMIT - save work done 提交
        2.SAVEPOINT - identify a point in a transaction to which you can later roll back 保存点
        3.ROLLBACK - restore database to original since the last COMMIT 回滚
        4.SET TRANSACTION - Change transaction options like what rollback segment to use 设置当前事务的特性，它对后面的事务没有影响．
 使用SqlLiteDatabase操作数据库的步骤如下：
        1,获取SqlLiteDatabase对象，他代表了与数据库的链接
        2，调用SqlLiteDatabase的方法，来执行SQL语句
        3，操作SQL语句的执行结果，比如用SimpleCursorAdapter封装Cursor
        4,关闭SqlLiteDatabase，回收资源


 ViewUtils模块：

  android中的ioc框架，完全注解方式就可以进行UI，资源和事件绑定；
      新的事件绑定方式，使用混淆工具混淆后仍可正常工作；
      目前支持常用的20种事件绑定，参见ViewCommonEventListener类和
      包com.lidroid.xutils.view.annotation.event。
 */

public class MainActivity extends AppCompatActivity {

    MyDatabaseHelper myDatabaseHelper;

    @ViewInject(R.id.word_et)
    EditText word_et;
    @ViewInject(R.id.detail_et)
    EditText detail_et;
    @ViewInject(R.id.search_et)
    EditText search_et;


    @OnClick({R.id.add_bt,R.id.other_bt,R.id.search_bt,R.id.update_bt,R.id.delete_bt,R.id.seleteall_bt})
    public void OnCLick(View view){
        switch (view.getId()){
            case R.id.add_bt:
                String add_text = word_et.getText().toString();
                String detail_text = detail_et.getText().toString();
                //第一种方法添加数据
                insertData(myDatabaseHelper.getReadableDatabase(),add_text,detail_text);
                word_et.setText("");
                detail_et.setText("");
                //第二种方法添加数据
//                ContentValues contentValues=new ContentValues();
//                contentValues.put("word",add_text);
//                contentValues.put("detail",detail_text);
//                myDatabaseHelper.getReadableDatabase().insert("dict",null,contentValues);
                break;
            case R.id.search_bt:
                String search=search_et.getText().toString();
                queryData(myDatabaseHelper.getReadableDatabase(),search);
                break;
            case R.id.update_bt:
                String add_text1 = word_et.getText().toString();
                String detail_text1 = detail_et.getText().toString();
                updateData(myDatabaseHelper.getReadableDatabase(),add_text1,detail_text1);
                break;
            case R.id.delete_bt:
                String word=search_et.getText().toString();
                deleteData(myDatabaseHelper.getReadableDatabase(),word);
                break;
            case R.id.seleteall_bt:
                queryAllData(myDatabaseHelper.getReadableDatabase());
            case R.id.other_bt:
                Intent intent=new Intent(this, DbUtilesActivity.class);
                startActivity(intent);
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewUtils.inject(this);

        myDatabaseHelper=new MyDatabaseHelper(MainActivity.this,"dict.db3",null,1);

    }

    //添加数据
    private void insertData(SQLiteDatabase sql,String word,String detail){
        sql.execSQL("insert into dict values(null,?,?)",new String[]{word,detail});
        Toast.makeText(this,"添加成功",Toast.LENGTH_LONG).show();
    }

    //以下包含两种方法 更新数据
    private void updateData(SQLiteDatabase sql,String word,String detail){
        sql.execSQL("update dict set detail=? where word=?",new String[]{detail,word});
//        ContentValues contentValues=new ContentValues();
//        contentValues.put("detail",detail);
//        /**
//         * update(String table,ContentValues values,String Where,String[] whereArgs)的参数意义
//         * param table:表名
//         * param values:代表想更新的数据
//         * param WhereClause:满足该WhereClause子句的记录将会被更新
//         * param whereArgs:用于为WhereClause子句传入参数
//         * update(String table,ContentValues values,String Where,String[] whereArgs)
//         */
//        sql.update("dict",contentValues,"word=?",new String[]{word});
        Toast.makeText(this,"更新成功",Toast.LENGTH_LONG).show();
    }
//    以下包含两种方法 删除数据
    private void deleteData(SQLiteDatabase sql,String word){
        sql.execSQL("delete from dict where word=?",new String[]{word});
        sql.delete("dict","word=?",new String[]{word});
        Toast.makeText(this,"删除成功",Toast.LENGTH_LONG).show();
    }

    //查找数据
    private void queryData(SQLiteDatabase sql,String word){
        Cursor cursor = sql.rawQuery("select * from dict where word like ? or detail like ?",new String[]{"%"+word+"%","%"+word+"%"});
        Bundle bundle=new Bundle();
        bundle.putSerializable("key",convertCursorToList(cursor));
        Intent intent=new Intent(MainActivity.this,MyDatabaseList.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //显示所有数据
//    query(boolean distinct, String table, String[] columns, String selection,
//              String[] selectionArgs, String groupBy, String having,
//                  String orderBy, String limit) {
    private void queryAllData(SQLiteDatabase sql){
        Cursor cursor = sql.query(false,"dict",null,null,null,null,null,null,null);
        Bundle bundle=new Bundle();
        bundle.putSerializable("key",convertCursorToList(cursor));
        Intent intent=new Intent(MainActivity.this,MyDatabaseList.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public ArrayList<Map<String,String>> convertCursorToList(Cursor cursor){
        ArrayList<Map<String ,String>> result=new ArrayList<Map<String ,String>>();
        while (cursor.moveToNext()) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("word",cursor.getString(1));
            map.put("detail",cursor.getString(2));
            result.add(map);
        }
        return result;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myDatabaseHelper!=null){
            myDatabaseHelper.close();
        }
    }
}
