package com.newly_dawn.app.wsn;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
//        FloatingActionButton fab = (FloatingActionButton) context.findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Login Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        listener(context);
    }
    public void listener(AppCompatActivity context){
        TextView tv = (TextView)context.findViewById(R.id.imageViewBackground);
//        tv.setBackgroundResource(R.drawable.wireless);
        Button loginBtn = (Button)myContext.findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new LoginButtonListener());
        TextView register = (TextView)context.findViewById(R.id.registerTextView);
        register.setOnClickListener(new RegisterTextViewListener());
    }
    public class LoginButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            String loginUrl = "http://www.xiaolong.party" + "/api/login/";
            new UserAsyncLogin().execute(loginUrl);
        }
    }
    public class RegisterTextViewListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(myContext, register.class);
//            myContext.startActivityForResult(myContext, register.class);
            myContext.startActivity(intent);
        }
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
                code = readHttp(params[0]).get(0);
                String info = readHttp(params[0]).get(1);
                Log.i("TTTTT", info);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return code;
        }
        protected void onPostExecute(String code){
            dialog.dismiss();
            if(code.equals("200")){
//                String info = "not found";
//                try {
//                    info = readHttp("http://www.xiaolong.party/api/user_info/?access_token=UniJZqeuI3cF1BNCqGWQYAljhyS6nF").get(1);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                Log.i("user_info", info);
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
    public ArrayList<String> readHttp(String urlPath) throws Exception {
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
        object.put("username", username);
        object.put("password", password);
        byte[] data = object.toString().getBytes("UTF-8");
        Log.i("CODE_TEST_", object.toString());
        OutputStream outputStream = urlConn.getOutputStream();
        outputStream.write(data);
        outputStream.flush();
        outputStream.close();

        InputStreamReader in = new InputStreamReader(urlConn.getInputStream()); // 获得读取的内容
        Log.i("CODE_TEST_", "4");
        BufferedReader buffer = new BufferedReader(in); // 获取输入流对象
        String inputLine = null;
        //通过循环逐行读取输入流中的内容
        String reponseText = "";
        while ((inputLine = buffer.readLine()) != null) {
            reponseText += inputLine + "\n";
        }
        JSONObject tmp = new JSONObject(reponseText);
        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;
        sharedPreferences = myContext.getSharedPreferences("wsnSharedPreferences", Context.MODE_WORLD_READABLE);
        editor = sharedPreferences.edit();
        editor.apply();
        String result = String.valueOf(urlConn.getResponseCode());
        String info = String.valueOf(urlConn.getResponseMessage());
        Log.i("CODE_TEST_", String.valueOf(urlConn.getResponseCode()));
        urlConn.disconnect();	//断开连接
        ArrayList<String> result_list = new ArrayList<>();
        result_list.add(result);
        result_list.add(reponseText);
        return result_list;  //返回登陆结果
    }
}
