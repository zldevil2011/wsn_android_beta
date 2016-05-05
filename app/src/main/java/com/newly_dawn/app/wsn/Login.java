package com.newly_dawn.app.wsn;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.newly_dawn.app.wsn.objects.News;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Login{
    private MainActivity myContext;
    private ProgressDialog dialog;
    public void build(MainActivity context){
        myContext = context;
        dialog = new ProgressDialog(myContext);
        final View nextView;
        Log.i("zl_debug", "Login");
        Toolbar toolbar = (Toolbar) context.findViewById(R.id.toolbar);
        context.setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) context.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Login Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        listener(context);
    }
    public void listener(AppCompatActivity context){
        TextView tv = (TextView)context.findViewById(R.id.imageViewBackground);
        tv.setBackgroundResource(R.drawable.wireless);
        String loginUrl = "http://www.xiaolong.party" + "/api/send_mail/";
//        String loginUrl = "http://123.56.77.193/api/v1/send_mail/";
        new UserAsyncLogin().execute(loginUrl);
    }
    public class UserAsyncLogin extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute(){
            dialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            String code = "404";
            try {
                code = readHttp(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return code;
        }
        protected void onPostExecute(String code){
            dialog.dismiss();
            if(code.equals("200")){
                Toast.makeText(myContext, "success", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(myContext, "XXXXXXX", Toast.LENGTH_SHORT).show();
            }
        }
    }
    /**
     * 连接HTTP获取返回结果
     * @param urlPath
     * @throws Exception
     */
    public String readHttp(String urlPath) throws Exception {
        TextView usernameT = (TextView)myContext.findViewById(R.id.username),
                passwordT = (TextView)myContext.findViewById(R.id.password);
        String username = usernameT.getText().toString(),
                password = passwordT.getText().toString();
        URL url = new URL(urlPath);
        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();	//创建一个HTTP连接
        urlConn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        urlConn.setRequestProperty("Accept", "application/json");

        urlConn.setRequestMethod("POST");
        urlConn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        urlConn.setDoOutput(true);

        JSONObject object = new JSONObject();
        object.put("email", username);
        object.put("password", password);
        byte[] data = object.toString().getBytes("UTF-8");

        OutputStream outputStream = urlConn.getOutputStream();
        outputStream.write(data);
        outputStream.flush();
        outputStream.close();

        String result = String.valueOf(urlConn.getResponseCode());
        Log.i("CODE_TEST_", String.valueOf(urlConn.getResponseCode()));
        urlConn.disconnect();	//断开连接
        return result;  //返回登陆结果
    }
}
