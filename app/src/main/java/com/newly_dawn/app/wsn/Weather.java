package com.newly_dawn.app.wsn;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Weather{
    private Spinner spinnerTopProvince = null;
    private Spinner spinnerSubCitys = null;
    private String[][] citys ={{"朝阳" ,  "阳台" ,  "紫金" ,  "海淀"},
            { "抚顺" ,  "大连" ,  "青岛" ,  "烟台"},
            {"济南" ,  "菏泽" , "威海", "单县" },
            {"开封" ,  "安阳" ,  "洛阳" ,  "南阳"}};
    private ArrayAdapter<CharSequence> arrayAdapter = null;
    private AppCompatActivity myContext;
    public void build(AppCompatActivity context){
        myContext = context;
        final View nextView;
        Log.i("zl_debug", "Weater");
        Toolbar toolbar = (Toolbar) context.findViewById(R.id.toolbar);
        context.setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) context.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Weather Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        listener(context);

    }
    public void listener(final AppCompatActivity context){
        spinnerTopProvince=(Spinner)context.findViewById(R.id.Province);
        spinnerSubCitys=(Spinner)context.findViewById(R.id.city);
        spinnerTopProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Weather.this.arrayAdapter = new ArrayAdapter<CharSequence>(context,
                        android.R.layout.simple_spinner_item, citys[position]);
                spinnerSubCitys.setAdapter(arrayAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayList<City> ans = ParseXml(getXMLFromResXml());
        for(int i = 0; i < ans.size(); ++i){
            City tmp = ans.get(i);
            Log.i("cityCode", "" +tmp.getProvice() + " " +tmp.getCity() + " " +tmp.getCounty() + " " + tmp.getID());
        }
    }
    public ArrayList<City> ParseXml(XmlPullParser parser){
        ArrayList<City> CityArray = new ArrayList<City>();
        City CityTemp = null;
        //开始解析事件
        int eventType = 0;
        try {
            eventType = parser.getEventType();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        int full = 0;
        //处理事件，不碰到文档结束就一直处理
        while (eventType != XmlPullParser.END_DOCUMENT) {
            //因为定义了一堆静态常量，所以这里可以用switch
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG:
                //给当前标签起个名字
                    String tagName = parser.getName();
                    if(tagName.equals("province")){
                        CityTemp = new City();
                        String Provice = (parser.getAttributeValue(1));
//                        Log.i("cityCode_p", Provice);
                        CityTemp.setProvice(Provice);
                        full = 1;
                    }else if(tagName.equals("city")){
                        String city = parser.getAttributeValue(1);
//                        Log.i("cityCode_c", city);
                        String provice = CityTemp.getProvice();
                        CityTemp = new City();
                        CityTemp.setProvice(provice);
                        CityTemp.setCity(city);
                        full = 2;
                    }else if(tagName.equals("county")){
                        String provice = CityTemp.getProvice();
                        String city = CityTemp.getCity();
                        CityTemp = new City();
                        CityTemp.setProvice(provice);
                        CityTemp.setCity(city);
                        CityTemp.setCounty(parser.getAttributeValue(1));
                        CityTemp.setID(Integer.parseInt(parser.getAttributeValue(2)));
                        full = 3;
                    }
                    if(full == 3){
                        CityArray.add(CityTemp);
                        full = 0;
                    }
                    break;
                case XmlPullParser.END_TAG:
                    break;
                case XmlPullParser.END_DOCUMENT:
                    break;
            }
            //别忘了用next方法处理下一个事件，忘了的结果就成死循环#_#
            try {
                eventType = parser.next();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return CityArray;
    }
    public XmlResourceParser getXMLFromResXml(){
        XmlResourceParser xmlParser = null;
        try {
            xmlParser = myContext.getResources().getXml(R.xml.citycodes);
            return xmlParser;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return xmlParser;
    }
}
