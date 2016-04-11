package com.newly_dawn.app.wsn;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class TakingPhoto{
    private AppCompatActivity context;
    private Camera camera;
    public void build(AppCompatActivity father_context, Camera father_camera){
        camera = father_camera;
        context = father_context;
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
        listener();
    }
    public void startPreview(){
        SurfaceView sv = (SurfaceView)context.findViewById(R.id.surfaceView1); // 获取SurfaceView组件，用于显示相机预览
        final SurfaceHolder sh = sv.getHolder();
        sh.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); // 设置该SurfaceHolder自己不维护缓冲
        camera.setDisplayOrientation(90);
        try {
            camera.setPreviewDisplay(sh); // 设置用于显示预览的SurfaceView
        } catch (IOException e) {
            e.printStackTrace();
        }
        Camera.Parameters parameters = camera.getParameters();    //获取相机参数
        parameters.setPictureSize(640, 480);    //设置预览画面的尺寸
        parameters.setPictureFormat(PixelFormat.JPEG);    //指定图片为JPEG图片
        parameters.set("jpeg-quality", 80);    //设置图片的质量
        parameters.setPictureSize(640, 480);    //设置拍摄图片的尺寸
        camera.setParameters(parameters);    //重新设置相机参数
        camera.startPreview();    //开始预览
        camera.autoFocus(null); // 设置自动对焦

    }
    public void listener(){
        Button startpreview = (Button)context.findViewById(R.id.startPreview);
        startpreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPreview();
            }
        });
        Button takePhoto = (Button)context.findViewById(R.id.takephoto); // 获取“预览”按钮
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.takePicture(null, null, jpeg); // 进行拍照
                startPreview();
            }
        });
    }
    final Camera.PictureCallback jpeg = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            // 根据拍照所得的数据创建位图
            final Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
            // 加载layout/save.xml文件对应的布局资源
            View saveView = context.getLayoutInflater().inflate(R.layout.save, null);
            final EditText photoName = (EditText) saveView.findViewById(R.id.phone_name);
            // 获取对话框上的ImageView组件
            ImageView show = (ImageView) saveView.findViewById(R.id.show);
            show.setImageBitmap(bm);			// 显示刚刚拍得的照片
            show.setRotation(90);
//            camera.stopPreview();		//停止预览
//            isPreview = false;

            // 使用对话框显示saveDialog组件
            new AlertDialog.Builder(context).setView(saveView).setPositiveButton("保存", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    File file = new File("/sdcard/pictures/" + photoName
                            .getText().toString() + ".jpg");//创建文件对象
                    try {
                        file.createNewFile();                                //创建一个新文件
                        FileOutputStream fileOS = new FileOutputStream(file);    //创建一个文件输出流对象
                        //将图片内容压缩为JPEG格式输出到输出流对象中
                        bm.compress(Bitmap.CompressFormat.JPEG, 100, fileOS);
                        fileOS.flush();                                    //将缓冲区中的数据全部写出到输出流中
                        fileOS.close();                                    //关闭文件输出流对象
//                                isPreview = true;
//                                resetCamera();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
//                    isPreview = true;
//                    resetCamera();	//重新预览

                }
            }).show();
        }
    };
}
