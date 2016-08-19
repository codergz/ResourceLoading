package com.example.gao.resourceloading;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import dalvik.system.PathClassLoader;

/**
 * Created by gao on 2016/8/10.
 */
public class MainActivity extends BaseActivity {

    /**
     * 需要替换主题的控件
     * TextView,ImageView
     */
    private TextView textV;
    private ImageView imgV;

    /**
     * 更换控件的按钮
     */
    private Button btnChange;
    /**
     * 类加载器
     */
    protected PathClassLoader pc1 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textV = (TextView)findViewById(R.id.text);
        imgV = (ImageView)findViewById(R.id.imageview);

        btnChange = (Button)findViewById(R.id.btn1);
        /**
         * 通过点击按钮，更新TextView和ImageView两个控件
         */
        btnChange.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View arg0) {

        /**使用PathClassLoader方法加载类*/

        //创建一个意图，用来找到指定的apk：这里的"com.example.gao.resourceloaderapk1"是指定apk中在AndroidMainfest.xml文件中定义的<action name="com.example.gao.resourceloaderapk1"/>
        Intent intent = new Intent("com.example.gao.resourceloaderapk1", null);
        //获得包管理器
        PackageManager pm = getPackageManager();
        List<ResolveInfo> resolveinfoes =  pm.queryIntentActivities(intent, 0);
        //获得指定的activity的信息
        ActivityInfo actInfo = resolveinfoes.get(0).activityInfo;
        //获得apk的目录或者jar的目录
        final String apkPath = "/data/app/com.example.gao.resourceloaderapk1-2.apk";

        //native代码的目录
        String libPath = actInfo.applicationInfo.nativeLibraryDir;
        //创建类加载器，把dex加载到虚拟机中
        //第一个参数：是指定apk安装的路径，这个路径要注意只能是通过actInfo.applicationInfo.sourceDir来获取
        //第二个参数：是C/C++依赖的本地库文件目录,可以为null
        //第三个参数：是上一级的类加载器
                pc1 = new PathClassLoader(apkPath,libPath, MainActivity.this.getClassLoader());
                //loadResources(apkPath);
                setContent();

            }});


    }


    @SuppressLint("NewApi")
    private void setContent(){
        try{
            Class clazz = pc1.loadClass("com.example.gao.resourceloaderapk1.UIUtil");
            /**
             * 通过反射调用UIUtil类中的getTextString方法
             */
            Method method = clazz.getMethod("getTextString", Context.class);
            String str = (String)method.invoke(null, this);
            textV.setText(str);
            method = clazz.getMethod("getImageDrawable", Context.class);
            Drawable drawable = (Drawable)method.invoke(null, this);
            imgV.setImageDrawable(drawable);

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}