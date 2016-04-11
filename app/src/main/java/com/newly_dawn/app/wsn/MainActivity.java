package com.newly_dawn.app.wsn;

import android.content.Intent;
import android.graphics.CornerPathEffect;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
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
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private int current_menu = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        build_page();
        buidl_drawer();
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
    public void buidl_drawer(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        if(preLayout != null && nextLayout != null) {
            content_frame.removeView(preLayout);
            content_frame.addView(nextLayout);
            TakingPhoto tk = new TakingPhoto();
            tk.build(LayoutInflater.from(MainActivity.this), MainActivity.this);
            build_page();
        }else{
            Toast.makeText(MainActivity.this, "敬请期待", Toast.LENGTH_SHORT).show();
            current_menu = pre_current_menu;
        }
        buidl_drawer();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public CoordinatorLayout getPreLayout(){
        CoordinatorLayout preLayout = null;
        switch (current_menu){
            case 0:
                preLayout = (CoordinatorLayout)findViewById(R.id.activity_index_page);
                break;
            case 1:
                preLayout = (CoordinatorLayout)findViewById(R.id.activity_taking_photo_page);
                break;
            case 2:
                preLayout = (CoordinatorLayout)findViewById(R.id.activity_setting_page);
                break;
            case 3:
                preLayout = (CoordinatorLayout)findViewById(R.id.activity_setting_page);
                break;
            case 4:
                preLayout = (CoordinatorLayout)findViewById(R.id.activity_setting_page);
                break;
            case 5:
                preLayout = (CoordinatorLayout)findViewById(R.id.activity_setting_page);
                break;
            case 6:
                preLayout = (CoordinatorLayout)findViewById(R.id.activity_setting_page);
                break;
            default:
                preLayout = (CoordinatorLayout)findViewById(R.id.activity_index_page);
                break;
        }
        return preLayout;
    }
    public CoordinatorLayout getNextLayout(int id){
        LayoutInflater factorys = LayoutInflater.from(MainActivity.this);
        final View nextView;
        CoordinatorLayout nextLayout = null;
        if(id == R.id.nav_index && current_menu != 0){
            // turn to the Home
            nextView = factorys.inflate(R.layout.activity_index, null);
            nextLayout = (CoordinatorLayout)nextView.findViewById(R.id.activity_index_page);
            current_menu = 0;
        }
        else if (id == R.id.nav_camera && current_menu != 1) {
            // Handle the camera action
            nextView = factorys.inflate(R.layout.activity_taking_photo, null);
            nextLayout = (CoordinatorLayout)nextView.findViewById(R.id.activity_taking_photo_page);
            current_menu = 1;
        } else if (id == R.id.nav_gallery && current_menu != 2) {
            current_menu = 2;
        } else if (id == R.id.nav_slideshow && current_menu != 3) {
            current_menu = 3;
        } else if (id == R.id.nav_manage && current_menu != 4) {
            nextView = factorys.inflate(R.layout.activity_setting, null);
            nextLayout = (CoordinatorLayout)nextView.findViewById(R.id.activity_setting_page);
            current_menu = 4;
        } else if (id == R.id.nav_share && current_menu != 5) {
            current_menu = 5;
        } else if (id == R.id.nav_send && current_menu != 6) {
            current_menu = 6;
        }
        return nextLayout;
    }
    public void update_listener(int id){
        TakingPhoto tk = new TakingPhoto();
        tk.build(LayoutInflater.from(MainActivity.this), MainActivity.this);
    }
}
