package com.newly_dawn.app.wsn;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Index {
    public void build(AppCompatActivity context){

        final View nextView;
        Log.i("zl_debug", "Index");
        Toolbar toolbar = (Toolbar) context.findViewById(R.id.toolbar);
        context.setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) context.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Index Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        listener(context);
    }
    public void listener(AppCompatActivity context){
        ListView listview = (ListView)context.findViewById(R.id.index_list);
        String[] title = new String[]{ "郑州生态廊道内铸铜雕塑遭疯狂盗窃",
                "河北美院与村民起冲突 让学生围场子",
                "河北鸟贩冰库 300只野生鸟类尸横满地",
                "男童为完作业摸螺蛳落水 父亲救其身亡",
                "阿富汗7.3级地震 印度等地震感明显",
                "上海外地临牌限行 市民排长队购牌",
                "中瑞双方支持两国企业、高校和科研机构开展创新合作", "贵州一中学教学楼楼顶护墙垮塌致一名学生死亡" };
        String[] time = new String[]{ "2016-04-11 16:05:51", "2016-04-11 16:05:51", "2016-04-11 16:38:05", "2016-04-11 16:38:05",
                "2016-04-11 16:38:05","2016-04-11 16:38:05","2016-04-11 16:38:05","2016-04-11 16:38:05"};
        List<Map<String, String>> listItems = new ArrayList<Map<String, String>>();
        for(int i = 0 ; i < title.length; ++i){
            Map<String, String> map = new HashMap<String, String>();
            map.put("title", title[i]);
            map.put("time", time[i]);
            listItems.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(context, listItems, R.layout.index_list_item, new String[]{"title",
                "time"}, new int[]{R.id.title, R.id.time});
        listview.setAdapter(adapter);
    }
}
