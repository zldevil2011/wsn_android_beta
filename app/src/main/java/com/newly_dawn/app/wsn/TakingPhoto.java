package com.newly_dawn.app.wsn;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class TakingPhoto{
    public void build(AppCompatActivity context, Camera father_camera){
        final View nextView;
        Log.i("zl_debug", "TAKINGPHOTO");
        Toolbar toolbar = (Toolbar) context.findViewById(R.id.toolbar);
        context.setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) context.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "TAKEING Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        listener(context, father_camera);
    }
    public void listener(AppCompatActivity context, final Camera camera){
        Button takePhoto = (Button)context.findViewById(R.id.takephoto); // 获取“预览”按钮
        SurfaceView sv = (SurfaceView)context.findViewById(R.id.surfaceView1); // 获取SurfaceView组件，用于显示相机预览
        final SurfaceHolder sh = sv.getHolder();
        sh.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); // 设置该SurfaceHolder自己不维护缓冲
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    camera.setDisplayOrientation(90);
                    camera.setPreviewDisplay(sh); // 设置用于显示预览的SurfaceView
                    Camera.Parameters parameters = camera.getParameters();    //获取相机参数
                    parameters.setPictureSize(640, 480);    //设置预览画面的尺寸
                    parameters.setPictureFormat(PixelFormat.JPEG);    //指定图片为JPEG图片
                    parameters.set("jpeg-quality", 80);    //设置图片的质量
                    parameters.setPictureSize(640, 480);    //设置拍摄图片的尺寸
                    camera.setParameters(parameters);    //重新设置相机参数
                    camera.startPreview();    //开始预览
                    camera.autoFocus(null); // 设置自动对焦
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    final Camera.PictureCallback jpeg = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

        }
    };
}
