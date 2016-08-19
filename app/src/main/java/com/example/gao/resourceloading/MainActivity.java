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
     * ��Ҫ�滻����Ŀؼ�
     * TextView,ImageView
     */
    private TextView textV;
    private ImageView imgV;

    /**
     * �����ؼ��İ�ť
     */
    private Button btnChange;
    /**
     * �������
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
         * ͨ�������ť������TextView��ImageView�����ؼ�
         */
        btnChange.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View arg0) {

        /**ʹ��PathClassLoader����������*/

        //����һ����ͼ�������ҵ�ָ����apk�������"com.example.gao.resourceloaderapk1"��ָ��apk����AndroidMainfest.xml�ļ��ж����<action name="com.example.gao.resourceloaderapk1"/>
        Intent intent = new Intent("com.example.gao.resourceloaderapk1", null);
        //��ð�������
        PackageManager pm = getPackageManager();
        List<ResolveInfo> resolveinfoes =  pm.queryIntentActivities(intent, 0);
        //���ָ����activity����Ϣ
        ActivityInfo actInfo = resolveinfoes.get(0).activityInfo;
        //���apk��Ŀ¼����jar��Ŀ¼
        final String apkPath = "/data/app/com.example.gao.resourceloaderapk1-2.apk";

        //native�����Ŀ¼
        String libPath = actInfo.applicationInfo.nativeLibraryDir;
        //���������������dex���ص��������
        //��һ����������ָ��apk��װ��·�������·��Ҫע��ֻ����ͨ��actInfo.applicationInfo.sourceDir����ȡ
        //�ڶ�����������C/C++�����ı��ؿ��ļ�Ŀ¼,����Ϊnull
        //����������������һ�����������
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
             * ͨ���������UIUtil���е�getTextString����
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