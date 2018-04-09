package com.my.xutilsdatabase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.my.sqllitedatabase.MyDatabaseList;
import com.my.sqllitedatabase.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by AOW on 2018/4/9.
 * XUtils是git上比较活跃 功能比较完善的一个框架，是基于afinal开发的，比afinal稳定性提高了不少，
 * 目前xUtils主要有四大模块：
       DbUtils模块：
         android中的orm框架，一行代码就可以进行增删改查；
         支持事务，默认关闭；
         可通过注解自定义表名，列名，外键，唯一性约束，NOT NULL约束，CHECK约束等（需要混淆的时候请注解表名和列名）；
         支持绑定外键，保存实体时外键关联实体自动保存或更新；
         自动加载外键关联实体，支持延时加载；
         支持链式表达查询，更直观的查询语义，参考下面的介绍或sample中的例子。
 */

public class DbUtilesActivity extends Activity {

    @ViewInject(R.id.word_et)
    EditText word_et;
    @ViewInject(R.id.detail_et)
    EditText detail_et;
    @ViewInject(R.id.search_et)
    EditText search_et;


    @OnClick({R.id.add_bt,R.id.search_bt,R.id.update_bt,R.id.delete_bt,R.id.seleteall_bt})
    public void OnCLick(View view) {
        DbUtils db=DbUtils.create(this);
        switch (view.getId()) {
            case R.id.add_bt:
                DbUtils db2=DbUtils.create(this);
                add(db2);
                break;
            case R.id.search_bt:
                String search = search_et.getText().toString();
//                queryData(myDatabaseHelper.getReadableDatabase(),search);
                break;
            case R.id.update_bt:
                String add_text1 = word_et.getText().toString();
                String detail_text1 = detail_et.getText().toString();
//                updateData(myDatabaseHelper.getReadableDatabase(),add_text1,detail_text1);
                break;
            case R.id.delete_bt:
                String word = search_et.getText().toString();
//                deleteData(myDatabaseHelper.getReadableDatabase(),word);
                break;
            case R.id.seleteall_bt:
                DbUtils db1=DbUtils.create(this);
                select(db1);
            default:
                break;
        }
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dbutils);
        ViewUtils.inject(this);

    }

    private void add(DbUtils db){
        String add_text = word_et.getText().toString();
        String detail_text = detail_et.getText().toString();
        Person p=new Person();
        p.setName(add_text);
        p.setAge(detail_text);
        try {
            db.save(p);// 使用saveBindingId保存实体时会为实体的id赋值
            Toast.makeText(this,"保存保存",Toast.LENGTH_LONG).show();
        } catch (DbException e) {
            e.printStackTrace();
        }
        word_et.setText("");
        detail_et.setText("");
    }

    private void select(DbUtils db){
        try {
            //查询所有数据
            List<Person> list=db.findAll(Person.class);

            List<Map<String,String>> list1=new ArrayList<Map<String,String>>();
            for(int i=0;i<list.size();i++){
                Map<String,String> map=new HashMap<>();
                map.put("word",list.get(i).getName());
                map.put("detail",list.get(i).getAge());
                list1.add(map);
            }
//             IS NULL 查询name为空的数据
//            Parent Parent = db.findFirst(Selector.from(Parent.class).where("name","=", null));
//              IS NOT NULL
//            Parent Parent = db.findFirst(Selector.from(Parent.class).where("name","!=", null));
//
//// WHERE id<54 AND (age>20 OR age<30) ORDER BY id LIMIT pageSize OFFSET pageOffset
//            List<Parent> list = db.findAll(Selector.from(Parent.class)
//                    .where("id" ,"<", 54)
//                    .and(WhereBuilder.b("age", ">", 20).or("age", " < ", 30))
//                    .orderBy("id")
//                    .limit(pageSize)
//                    .offset(pageSize * pageIndex));
            Toast.makeText(this,"数据总数："+list.size(),Toast.LENGTH_LONG).show();
            Intent intent=new Intent(this, MyDatabaseList.class);
            Bundle bundle=new Bundle();
            bundle.putSerializable("key", (Serializable) list1);
            intent.putExtras(bundle);
            startActivity(intent);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
