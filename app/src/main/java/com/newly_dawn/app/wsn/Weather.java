package com.newly_dawn.app.wsn;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Weather{
    private Spinner spinnerProvince = null;
    private Spinner spinnerSubCitys = null;
    private Spinner spinnerSubCountys = null;
    private ArrayList<City> cityCode = null;
    private Set<String> provinces = new HashSet<>();
    private Map<String, HashSet> citys = new HashMap<>();
    private Map<String, HashSet> countys = new HashMap<>();
    private Map<String, String> CODE = new HashMap<>();
    ArrayAdapter<String> provinceAdapter = null;
    ArrayAdapter<String> cityAdapter = null;
    ArrayAdapter<String> countyAdapter = null;
    final String[] arr_T_P = new String[]{};
    String[] provinceArr = null;
    final String[] arr_T_C = new String[]{};
    String[] cityArr = null;
    final String[] arr_T_CY = new String[]{};
    String[] countyArr = null;

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
        spinnerProvince=(Spinner)context.findViewById(R.id.Province);
        spinnerSubCitys=(Spinner)context.findViewById(R.id.City);
        spinnerSubCountys=(Spinner)context.findViewById(R.id.County);

        readCityCode();
//        for(int i = 0; i < cityCode.size(); ++i){
//            City tmp = cityCode.get(i);
//            Log.i("cityCode", "" +tmp.getProvice() + " " +tmp.getCity() + " " +tmp.getCounty() + " " + tmp.getID());
//        }
        initUI();
    }
    public void initUI(){
        provinceArr = (String[]) provinces.toArray(arr_T_P);
        provinceAdapter = new ArrayAdapter<String>(myContext, android.R.layout.simple_spinner_item, provinceArr);
        provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProvince.setAdapter(provinceAdapter);

        cityArr = (String[]) citys.get(provinceArr[0]).toArray(arr_T_C);
        cityAdapter = new ArrayAdapter<String>(myContext, android.R.layout.simple_spinner_item, cityArr);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubCitys.setAdapter(cityAdapter);

        countyArr = (String[]) countys.get(cityArr[0]).toArray(arr_T_CY);
        countyAdapter = new ArrayAdapter<String>(myContext, android.R.layout.simple_spinner_item, countyArr);
        countyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubCountys.setAdapter(countyAdapter);

        spinnerProvince.setOnItemSelectedListener(new myProvinceItemSelectedListener());
        spinnerSubCitys.setOnItemSelectedListener(new myCityItemSelectedListener());
        spinnerSubCountys.setOnItemSelectedListener(new myCountyItemSelectedListener());
    }
    private class myProvinceItemSelectedListener implements AdapterView.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            cityArr = (String[]) citys.get(provinceArr[position]).toArray(arr_T_C);
            cityAdapter = new ArrayAdapter<String>(myContext, android.R.layout.simple_spinner_item, cityArr);
            cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerSubCitys.setAdapter(cityAdapter);

            countyArr = (String[]) countys.get(cityArr[0]).toArray(arr_T_CY);
            countyAdapter = new ArrayAdapter<String>(myContext, android.R.layout.simple_spinner_item, countyArr);
            countyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerSubCountys.setAdapter(countyAdapter);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
    private class myCityItemSelectedListener implements AdapterView.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            countyArr = (String[]) countys.get(cityArr[position]).toArray(arr_T_CY);
            countyAdapter = new ArrayAdapter<String>(myContext, android.R.layout.simple_spinner_item, countyArr);
            countyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerSubCountys.setAdapter(countyAdapter);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
    private class myCountyItemSelectedListener implements AdapterView.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String cityCode = CODE.get(countyArr[position]);
            Log.i("CODE_TEST", cityCode);

            if (!isNetworkAvailable(myContext)) {
//                Log.i("zl_debug_", "Internet error");
                alerNetErr();
            }
            String http = "http://wthrcdn.etouch.cn/weather_mini?citykey=" + cityCode;
            try {
                String httpString = readHttp(http);
                Log.i("CODE_TEST", httpString);
            } catch (Exception e) {
                Log.i("CODE_TEST", "CONNECTION FAILED");
                e.printStackTrace();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
    public void readCityCode(){
        cityCode = ParseXml(getXMLFromResXml());
        getList();
    }
    public void getList(){
        int len = cityCode.size();
        for(int i = 0; i < len; ++i){
            provinces.add(cityCode.get(i).getProvice());
        }
//        Log.i("provices_len", "" + provices.size());
        int len_provice = provinces.size();
        int step = 0;
        for(String provice : provinces){
            HashSet<String> tmp = new HashSet<String>();
            for(step = 0; step < len; ++step){
                if(cityCode.get(step).getProvice().equals(provice)){
                    tmp.add(cityCode.get(step).getCity());
                }
            }
            Log.i("_len__", provice + " " + tmp);
            citys.put(provice, tmp);
        }
//        Log.i("citys_len", "" + citys.size());
        step = 0;
        for(HashSet<String> value : citys.values()){
            for(String city : value){
                HashSet<String> tmp = new HashSet<>();
                for(step = 0; step < len; ++step){
                    if(cityCode.get(step).getCity().equals(city)){
                        tmp.add(cityCode.get(step).getCounty());
                    }
                }
                Log.i("_len_", city + " " + tmp);
                countys.put(city, tmp);
            }
        }
//        Log.i("countys_len", "" + countys.size());
        for(HashSet<String> value : countys.values()){
            for(String county : value){
                int code = 0;
                for(step = 0; step < len; ++step){
                    if(cityCode.get(step).getCounty().equals(county)){
                        code = cityCode.get(step).getID();
                        break;
                    }
                }
                CODE.put(county, String.valueOf(code));
            }
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
                        CityTemp.setProvice(Provice);
                        full = 1;
                    }else if(tagName.equals("city")){
                        String city = parser.getAttributeValue(1);
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
    /**
     * 从指定的URL中获取数组
     * @param urlPath
     * @return
     * @throws Exception
     */
    public static String readHttp(String urlPath) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        Log.i("CODE_TEST_", "1");
        byte[] data = new byte[1024];
        int len = 0;
        URL url = new URL(urlPath);
        Log.i("CODE_TEST_", "2");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        Log.i("CODE_TEST_", "3");
        InputStreamReader inStream = new InputStreamReader(conn.getInputStream());
        Log.i("CODE_TEST_", "4");
        BufferedReader buffer = new BufferedReader(inStream);
        String result = "", inputLine = null;
        while ((inputLine = buffer.readLine()) != null) {
            result += inputLine + "\n";
        }
        Log.i("CODE_TEST_", "5");
        inStream.close();
        Log.i("CODE_TEST_", "" + outStream.toByteArray());
        return new String(outStream.toByteArray());//通过out.Stream.toByteArray获取到写的数据
    }
    public boolean isNetworkAvailable(Activity activity)
    {
        Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null)
        {
            return false;
        }
        else
        {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0)
            {
                for (int i = 0; i < networkInfo.length; i++)
                {
                    System.out.println(i + "===状态===" + networkInfo[i].getState());
                    System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public void alerNetErr() {

        // 对话框
        AlertDialog.Builder ab = new AlertDialog.Builder(myContext);
        ab.setTitle("网络错误");
        ab.setMessage("请检查手机数据连接");
        // 设置操作对象
        ab.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 取消对话框
                        dialog.cancel();
                        // 打开网络设置Activity
                        Intent it = new Intent(
                                android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                        myContext.startActivity(it);
                    }
                });
        ab.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 取消对话框
                        dialog.cancel();
                        // 退出程序
                        // exitApp(context);
                    }
                });
        // 显示
        ab.create().show();
    }
}
