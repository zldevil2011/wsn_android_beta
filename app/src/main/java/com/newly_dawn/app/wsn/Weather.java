package com.newly_dawn.app.wsn;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
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
import android.widget.TextView;

import com.newly_dawn.app.wsn.objects.City;
import com.newly_dawn.app.wsn.objects.WeatherInfo;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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

    private String http_str = "";
    private Handler handler;
    private AppCompatActivity myContext;
    private ProgressDialog dialog;
    public void build(AppCompatActivity context){
        myContext = context;
        dialog = new ProgressDialog(myContext);
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

//        cityArr = (String[]) citys.get(provinceArr[0]).toArray(arr_T_C);
//        cityAdapter = new ArrayAdapter<String>(myContext, android.R.layout.simple_spinner_item, cityArr);
//        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerSubCitys.setAdapter(cityAdapter);
//
//        countyArr = (String[]) countys.get(cityArr[0]).toArray(arr_T_CY);
//        countyAdapter = new ArrayAdapter<String>(myContext, android.R.layout.simple_spinner_item, countyArr);
//        countyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerSubCountys.setAdapter(countyAdapter);

        spinnerProvince.setOnItemSelectedListener(new myProvinceItemSelectedListener());
        spinnerSubCitys.setOnItemSelectedListener(new myCityItemSelectedListener());
        spinnerSubCountys.setOnItemSelectedListener(new myCountyItemSelectedListener());
//        spinnerSubCountys.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String cityCode = CODE.get(countyArr[position]);
//                Log.i("CODE_TEST", cityCode);
//
//                if (!isNetworkAvailable(myContext)) {
////                Log.i("zl_debug_", "Internet error");
//                    alerNetErr();
//                }
//                final String http = "http://wthrcdn.etouch.cn/weather_mini?citykey=" + cityCode;
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            readHttp(http);
//                            Log.i("CODE---","success");
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            Log.i("CODE---", "something wrong");
//                        }
//                        Message m = handler.obtainMessage(); // 获取一个Message
//                        handler.sendMessage(m); // 发送消息
//                    }
//                }).start();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//        handler = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                if (http_str != null) {
//                    Log.i("ABT", http_str);
//                    parseWeatherInfo(http_str);
//                }
//                super.handleMessage(msg);
//            }
//        };
    }
    public class MyAsyncTask extends AsyncTask<String, Void, List<WeatherInfo>>{

        @Override
        protected void onPreExecute(){
            dialog.show();
        }
        @Override
        protected List<WeatherInfo> doInBackground(String... params) {
            List<WeatherInfo> weatherInfos = new ArrayList<WeatherInfo>();
            try {
                readHttp(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.i("MY_TEST", "here");
            weatherInfos = parseWeatherInfo(http_str);
            Log.i("MY_TEST_CHECK", weatherInfos + "");
            Log.i("MY_TEST", "ABC");
            return weatherInfos;
        }
        protected void onPostExecute(List<WeatherInfo> result){
            TextView temperature = (TextView)myContext.findViewById(R.id.temperature);
            Log.i("MY_TEST", "before write");
            for(int i = 0; i < result.size(); ++i){
                Log.i("MY_TEST", result.get(i).getHighTemperature() + " " + result.get(i).getLowTemperature());
            }
            temperature.setText(result.get(0).getTemperature() + "℃");
            Log.i("MY_TEST", "UPDATE UI");
            dialog.dismiss();
        }
    }
//    public class MyAsyncTask extends AsyncTask<String, Void, List<String>>
//    {
//        @Override
//        protected void onPreExecute()
//        {
//            dialog.show();
//        }
//        @Override
//        protected List<String> doInBackground(String... params)
//        {
//            List<String> cities = new ArrayList<String>();
//            String citiesString = HttpUtils.sendPostMessage(params[0], "utf-8");
//            //    解析服务器端的json数据
//            cities = JsonUtils.parseCities(citiesString);return cities;
//        }
//        @Override
//        protected void onPostExecute(List<String> result)
//        {
//            adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, result);
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            spinner.setAdapter(adapter);
//            dialog.dismiss();
//        }
//    }
    public static List<WeatherInfo> parseWeatherInfo(String citiesString)
    {
        List<WeatherInfo> weatherInfos = new ArrayList<WeatherInfo>();
        try
        {
            JSONObject jsonObject = new JSONObject(citiesString);
            Log.i("MY_TEST_JSON_OBJECT", "" + jsonObject);
            JSONObject data = jsonObject.getJSONObject("data");
            String temperature = data.getString("wendu");
            JSONArray forecast = data.getJSONArray("forecast");
            for(int i = 0; i < forecast.length(); i++){
                Log.i("MY_TEST_PARSE","1");
                WeatherInfo tmp_wea = new WeatherInfo();
                Log.i("MY_TEST_PARSE","2");
                JSONObject tmp = forecast.getJSONObject(i);
                Log.i("MY_TEST_PARSE", "2+1");
                tmp_wea.setPlace(data.getString("city"));
                Log.i("MY_TEST_PARSE", "3");
                tmp_wea.setDate(tmp.getString("date"));
                Log.i("MY_TEST_PARSE", "4");
                tmp_wea.setType(tmp.getString("type"));
                tmp_wea.setTemperature(data.getString("wendu"));
                Log.i("MY_TEST_PARSE", "5");
                tmp_wea.setLowTemperature(tmp.getString("low").substring(2));
                Log.i("MY_TEST_PARSE", "6");
                tmp_wea.setHighTemperature(tmp.getString("high").substring(2));
                Log.i("MY_TEST_PARSE", "7");
                tmp_wea.setFengli(tmp.getString("fengli"));
                Log.i("MY_TEST_PARSE", "8");
                tmp_wea.setFengxiang(tmp.getString("fengxiang"));

                Log.i("MY_TEST_PARSE",""+tmp_wea);
                weatherInfos.add(tmp_wea);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return weatherInfos;
    }
    private class myProvinceItemSelectedListener implements AdapterView.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            cityArr = (String[]) citys.get(provinceArr[position]).toArray(arr_T_C);
            cityAdapter = new ArrayAdapter<String>(myContext, android.R.layout.simple_spinner_item, cityArr);
            cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerSubCitys.setAdapter(cityAdapter);

//            countyArr = (String[]) countys.get(cityArr[0]).toArray(arr_T_CY);
//            countyAdapter = new ArrayAdapter<String>(myContext, android.R.layout.simple_spinner_item, countyArr);
//            countyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            spinnerSubCountys.setAdapter(countyAdapter);
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
            final String http = "http://wthrcdn.etouch.cn/weather_mini?citykey=" + cityCode;
            dialog.setTitle("提示信息");
            dialog.setMessage("loading......");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(false);
//            dialog.show();
            new MyAsyncTask().execute(http);
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        readHttp(http);
////                        Log.i("CODE---","success");
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        Log.i("CODE---", "something wrong");
//                    }
//                    Message m = handler.obtainMessage(); // 获取一个Message
//                    handler.sendMessage(m); // 发送消息
//                }
//            }).start();
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
    public void readHttp(String urlPath) throws Exception {
        Log.i("CODE_TEST_URL", urlPath);
        Log.i("CODE_TEST_", "1");
        URL url = new URL(urlPath);
        Log.i("CODE_TEST_", "2");
        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();	//创建一个HTTP连接
        Log.i("CODE_TEST_", "3");
        InputStreamReader in = new InputStreamReader(urlConn.getInputStream()); // 获得读取的内容
        Log.i("CODE_TEST_", "4");
        BufferedReader buffer = new BufferedReader(in); // 获取输入流对象
        String inputLine = null;
        //通过循环逐行读取输入流中的内容
        http_str = "";
        while ((inputLine = buffer.readLine()) != null) {
            http_str += inputLine + "\n";
        }
        Log.i("CODE_TEST_", "" + http_str);
        in.close();	//关闭字符输入流对象
        urlConn.disconnect();	//断开连接
//        URL url = new URL(urlPath);
//
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//
//        InputStreamReader inStream = new InputStreamReader(conn.getInputStream());
//
//        BufferedReader buffer = new BufferedReader(inStream);
//        String result = "", inputLine = null;
//        while ((inputLine = buffer.readLine()) != null) {
//            result += inputLine + "\n";
//        }
//
//        inStream.close();
//        Log.i("CODE_TEST_", "" + outStream.toByteArray());
//        return new String(outStream.toByteArray());//通过out.Stream.toByteArray获取到写的数据
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
