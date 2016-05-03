package com.newly_dawn.app.wsn.tools;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.newly_dawn.app.wsn.R;

public class Browser{

    private AppCompatActivity myContext;
    private ProgressDialog dialog;
    public void build(AppCompatActivity context, String url){
        myContext = context;
        dialog = new ProgressDialog(myContext);
        final View nextView;
        Log.i("zl_debug", "Browser");
        Toolbar toolbar = (Toolbar) context.findViewById(R.id.toolbar);
        context.setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) context.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Browser Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        listener(context, url);
    }
    public void listener(AppCompatActivity context, String url){
        Log.i("AAAAA", "AAAAAA");

        WebView webView = (WebView) context.findViewById(R.id.webBrowser);
        Log.i("AAAAA", "" + webView);
        Log.i("AAAAA", "12345");
        //WebView加载web资源
        try {
            webView.loadUrl(url);
        }catch (Exception e){
            Log.i("AAAAA", "" + e);
        }

        Log.i("AAAAA", "67890");
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
    }

}
