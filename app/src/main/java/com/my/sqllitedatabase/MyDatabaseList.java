package com.my.sqllitedatabase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by AOW on 2018/4/8.
 */

public class MyDatabaseList extends Activity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wordlist);
        ListView listView=findViewById(R.id.listv);

        List<Map<String,String>> map= (List<Map<String, String>>) getIntent().getExtras().getSerializable("key");

        SimpleAdapter simpleAdapter=new SimpleAdapter(this,map,R.layout.listitem,new String[]{"word","detail"},new int[]{R.id.word_tv,R.id.detail_tv});

        listView.setAdapter(simpleAdapter);
    }

}
