package com.newly_dawn.app.wsn;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public Camera father_camera;
    public WebView father_webView;
    int camera_status = 0;
    public int current_menu = 0;
    private int[] activity_page_list = new int[]{R.id.activity_index_page,R.id.activity_taking_photo_page,
            R.id.activity_weather_page, R.id.activity_device_page,R.id.activity_setting_page,
            R.id.activity_setting_page,R.id.activity_login_page};
    private int[] activity_list = new int[]{R.layout.activity_index, R.layout.activity_taking_photo,
            R.layout.activity_weather,R.layout.activity_device,R.layout.activity_setting,
            R.layout.activity_setting,R.layout.activity_login};
    private int[] menu_id = new int[]{R.id.nav_index, R.id.nav_camera, R.id.nav_gallery,R.id.nav_slideshow,
            R.id.nav_manage, R.id.nav_share, R.id.nav_send};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Index index = new Index();
        index.build(MainActivity.this);
        build_page();
        build_drawer();
    }
    public void build_page(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
    public void build_drawer(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);
//        Log.i("CLASS_NAME", MainActivity.this + "");
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FrameLayout content_frame = (FrameLayout)findViewById(R.id.content_frame);
        int pre_current_menu = current_menu;
        CoordinatorLayout preLayout = getPreLayout();
        CoordinatorLayout nextLayout = getNextLayout(id);
        Log.i("TEST_BUG", preLayout + " : " + nextLayout);
        if(preLayout != null && nextLayout != null) {
            content_frame.removeView(preLayout);
            content_frame.addView(nextLayout);
            try{
                father_webView.destroy();
            }catch (Exception e){

            }
        }else{

            Toast.makeText(MainActivity.this, "敬请期待", Toast.LENGTH_SHORT).show();
            current_menu = pre_current_menu;
        }
        if(!updateLister(current_menu)){
            build_page();
        }
        build_drawer();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public CoordinatorLayout getPreLayout(){
        CoordinatorLayout preLayout = null;
        preLayout = (CoordinatorLayout)findViewById(activity_page_list[current_menu]);
        return preLayout;
    }
    public CoordinatorLayout getNextLayout(int id){
        LayoutInflater factorys = LayoutInflater.from(MainActivity.this);
        final View nextView;
        CoordinatorLayout nextLayout = null;
        for(int i = 0; i < menu_id.length; ++i){
            if(id == menu_id[i] && current_menu != i){
                nextView = factorys.inflate(activity_list[i], null);
                nextLayout = (CoordinatorLayout)nextView.findViewById(activity_page_list[i]);
                current_menu = i;
                break;
            }
        }
        return nextLayout;
    }
    public boolean updateLister(int id){
        switch (id){
            case 0:
                Index index = new Index();
                index.build(MainActivity.this);
                return true;
            case 1:
                TakingPhoto tk = new TakingPhoto();
                if(camera_status == 0){
                    father_camera = Camera.open();
                    camera_status = 1;
                }
                tk.build(MainActivity.this, father_camera);
                Log.i("zl_debug_camera_return", "" + camera_status);
                return true;
            case 2:
                Weather weather = new Weather();
                weather.build(MainActivity.this);
                return true;
            case 3:
                Device device = new Device();
                device.build(MainActivity.this);
                return true;
            case 4:
                Setting setting = new Setting();
                setting.build(MainActivity.this);
                return true;
            case 5:
                ProgressDialog dialog = new ProgressDialog(MainActivity.this);
                dialog.setTitle("提示信息");
                dialog.setMessage("loading......");
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.setCancelable(true);
                dialog.show();
                break;
            case 6:
                Login login = new Login();
                login.build(MainActivity.this);
                return true;
        }

        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (father_camera != null){
            father_camera.release();        // release the camera for other applications
            father_camera = null;
        }
    }


    @Override
    // 设置回退
    // 覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        try{
            if ((keyCode == KeyEvent.KEYCODE_BACK) && father_webView.canGoBack()) {
                father_webView.goBack(); // goBack()表示返回WebView的上一页面
                return true;
            }
        }catch (Exception e){

        }


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("确认退出吗？");
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 点击“确认”后的操作
                MainActivity.this.finish();
            }
        });
        builder.setNegativeButton("返回", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 点击“返回”后的操作,这里不设置没有任何操作
            }
        });
//        builder.setCancelable(false);
        builder.create().show();

        return super.onKeyDown(keyCode, event);
    }
}
