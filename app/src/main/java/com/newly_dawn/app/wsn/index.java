package com.newly_dawn.app.wsn;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.newly_dawn.app.wsn.objects.News;
import com.newly_dawn.app.wsn.objects.WeatherInfo;
import com.newly_dawn.app.wsn.tools.Browser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Index {
    private AppCompatActivity myContext;
    private ProgressDialog dialog;
    private String http_str;
    private ListView listview;
    public void build(AppCompatActivity context){
        myContext = context;
        dialog = new ProgressDialog(myContext);
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
        //read news from WangYiTouTiao
        final String http = "http://c.m.163.com/nc/article/headline/T1348647853363/0-20.html";
        dialog.setTitle("提示信息");
        dialog.setMessage("loading......");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        new NewsAsyncTask().execute(http);
    }
    public class NewsAsyncTask extends AsyncTask<String, Void, List<News>> {
        List<Map<String,String>> listItems = new ArrayList<>();
        @Override
        protected void onPreExecute(){
            dialog.show();
        }
        @Override
        protected List<News> doInBackground(String... params) {
            List<News> newsList = new ArrayList<News>();
            try {
                readHttp(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.i("MY_TEST", "here");
            newsList = parseNewsInfo(http_str);
            Log.i("MY_TEST_CHECK", newsList + "");
            Log.i("MY_TEST", "ABC");
            for(int i = 0; i < newsList.size(); ++i){
                News tmp = newsList.get(i);
                Map<String, String> map = new HashMap<>();
                map.put("title", tmp.getTitle());
                map.put("time",tmp.getPtime());
                map.put("url",tmp.getUrl());
                listItems.add(map);
            }
            return newsList;
        }
        protected void onPostExecute(List<News> result){
            Log.i("MY_TEST", "before write");
            for(int i = 0; i < result.size(); ++i){
                Log.i("MY_TEST", result.get(i).getTitle() + " " + result.get(i).getPtime());
            }
            listview = (ListView) myContext.findViewById(R.id.index_list);
            SimpleAdapter adapter = new SimpleAdapter(myContext, listItems, R.layout.index_list_item, new String[]{"title",
                    "time", "url"}, new int[]{R.id.title, R.id.time, R.id.url});
            listview.setAdapter(adapter);
            listview.setOnItemClickListener(new NewsItemClickListener());
            Log.i("MY_TEST", "UPDATE UI");
            dialog.dismiss();
        }
    }
    public class NewsItemClickListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            HashMap<String, String> currentItem = (HashMap<String, String>) listview.getItemAtPosition(position);
            String newsUrl = currentItem.get("url");
            Log.i("TED---", currentItem.get("url"));
            Toast.makeText(myContext, currentItem.get("url"), Toast.LENGTH_SHORT).show();

            FrameLayout content_frame = (FrameLayout)myContext.findViewById(R.id.content_frame);
            CoordinatorLayout preLayout = (CoordinatorLayout)myContext.findViewById(R.id.activity_index_page);
            CoordinatorLayout nextLayout = null;

            LayoutInflater factorys = LayoutInflater.from(myContext);
            final View nextView;
            nextView = factorys.inflate(R.layout.activity_browser, null);
            nextLayout = (CoordinatorLayout)nextView.findViewById(R.id.activity_browser_page);
            Log.i("browser_debug", "" + preLayout + " : " + nextLayout);
            if(preLayout != null && nextLayout != null) {
                content_frame.removeView(preLayout);
                content_frame.addView(nextLayout);
            }
//            build_page();
            Browser b = new Browser();
            b.build(myContext, newsUrl);
            buidl_drawer();
//            DrawerLayout drawer = (DrawerLayout) myContext.findViewById(R.id.drawer_layout);
//            drawer.closeDrawer(GravityCompat.START);

        }
    }
    public void build_page(){
        Toolbar toolbar = (Toolbar) myContext.findViewById(R.id.toolbar);
        myContext.setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) myContext.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Browser with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
    public void buidl_drawer(){
        Toolbar toolbar = (Toolbar) myContext.findViewById(R.id.toolbar);
        DrawerLayout drawer = (DrawerLayout) myContext.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                myContext, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) myContext.findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(myContext);   //big problem
    }
    /**
     * read from html and get the JSON data
     * @param newsString
     * @return
     */
    public static List<News> parseNewsInfo(String newsString)
    {
        List<News> newsList = new ArrayList<News>();
        try
        {
            JSONObject jsonObject = new JSONObject(newsString);
            Log.i("MY_TEST_JSON_OBJECT", "" + jsonObject);
            JSONArray data = jsonObject.getJSONArray("T1348647853363");
            Log.i("data_LENGTH", data.length() + "");
            for(int i = 1; i < data.length(); i++){
                Log.i("data_LENGTH-------", i + "");
                News tmp_news = new News();
                Log.i("data_LENGTH-------", "A" + i);
                JSONObject tmp = data.getJSONObject(i);
                Log.i("data_LENGTH-------", "B" + i);
                tmp_news.setTitle(tmp.getString("title"));
                Log.i("data_LENGTH-------", "C" + i);
                try {
                    tmp_news.setUrl(tmp.getString("url_3w"));
                }catch (Exception e){
                    tmp_news.setUrl("");
                }
                Log.i("data_LENGTH-------", "D" + i);
                tmp_news.setSource("");
                Log.i("data_LENGTH-------", "E" + i);
                tmp_news.setLmodify("");
                Log.i("data_LENGTH-------", "F" + i);
                tmp_news.setImgSrc("");
                Log.i("data_LENGTH-------", "G" + i);
                tmp_news.setSubtitle("");
                Log.i("data_LENGTH-------", "H" + i);
                tmp_news.setPtime(tmp.getString("ptime"));
                Log.i("data_LENGTH-------", "I" + i);
                newsList.add(tmp_news);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return newsList;
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
    }
}
