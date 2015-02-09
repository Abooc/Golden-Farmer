package org.lee.android.app.JanbanMaker.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.lee.android.app.JanbanMaker.AppApplication;
import org.lee.android.app.JanbanMaker.R;
import org.lee.android.util.Toast;

/**
 * Created by author:李瑞宇
 * email:allnet@live.cn
 * on 14-09-05.
 *
 * 关于页面
 */
public class AboutActivity extends Activity implements View.OnClickListener{

    public static void launch(Context context){
        Intent intent = new Intent(context, AboutActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayShowHomeEnabled(false);
        setContentView(R.layout.activity_about);



        Tracker t = ((AppApplication)getApplication()).getTracker(
                AppApplication.TrackerName.GLOBAL_TRACKER);
        // Set screen name.
        // Where path is a String representing the screen name.
        t.setScreenName("AboutActivity");

        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());

        init();
    }

    private void init(){
        int versionCode = getCurrentVersion(this);
        String versionName = getVersionName(this);
        TextView textView = (TextView) findViewById(R.id.VersionCode);
        textView.setText(versionName + "(build:" + versionCode + ")");

        findViewById(R.id.Update).setOnClickListener(this);
    }

    /**
     * 检查更新事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        Toast.show("建设中...");
    }

    public static String getVersionName(Context context) {
        PackageManager packagemanager = context.getPackageManager();
        String packName = context.getPackageName();
        try {
            PackageInfo packageinfo = packagemanager.getPackageInfo(packName, 0);
            return packageinfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getCurrentVersion(Context context) {
        PackageManager packagemanager = context.getPackageManager();
        String packName = context.getPackageName();
        try {
            PackageInfo packageinfo = packagemanager.getPackageInfo(packName, 0);
            return packageinfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
