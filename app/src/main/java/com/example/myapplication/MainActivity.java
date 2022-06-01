package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!Python.isStarted()){
            Python.start(new AndroidPlatform(this));
        }

        Python py = Python.getInstance();
        PyObject pyobj = py.getModule("helloworld");
        PyObject obj = pyobj.callAttr("hello",5,8);
        Log.d("PYTHON",obj.toString());
        // Checking for permission , if it's not allowed then prompt a permission screen
        boolean granted = false;
        AppOpsManager appOps = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), getPackageName());

        if (mode == AppOpsManager.MODE_DEFAULT) {
            granted = (checkCallingOrSelfPermission(android.Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED);
        } else {
            granted = (mode == AppOpsManager.MODE_ALLOWED);
        }
        Log.d("PERMISSION GRANTED", String.valueOf(granted));
        if(!granted) {
            // Permission screen
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        }

        long time = System.currentTimeMillis();
        Log.d("TIME", String.valueOf(time));

        String[] mobileArray = {};
        String[] pckgArr = {};
        String[] apkName = {};
        Drawable[] apkIcon = {};

        ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(mobileArray));
        ArrayList<String> pckgArray = new ArrayList<String>(Arrays.asList(pckgArr));
        ArrayList<String> apkNameArray = new ArrayList<String>(Arrays.asList(apkName));
        ArrayList<Drawable> appIcon = new ArrayList<Drawable>(Arrays.asList(apkIcon));

        final UsageStatsManager usageStatsManager=(UsageStatsManager)getSystemService(Context.USAGE_STATS_SERVICE);
        final Map<String,UsageStats> queryUsageStats=usageStatsManager.queryAndAggregateUsageStats(time - 1000*10, time);
        for (UsageStats name : queryUsageStats.values()) {
            // iterating over usage stats and considering only those apps which have appeared atleast once in foreground
            if (name.getTotalTimeInForeground() > 0) {

                // Code for extracting App Name from package name... Work in progress ,, need to wrap it in loop
                final PackageManager pm = getApplicationContext().getPackageManager();
                ApplicationInfo ai;
                try {
                    ai = pm.getApplicationInfo( name.getPackageName(), 0);
                } catch ( PackageManager.NameNotFoundException e) {
                    ai = null;
                }
                final String applicationName = (String) (ai != null ? pm.getApplicationLabel(ai) : "Unknown");

                if(applicationName == "Unknown") continue;

                Log.d("PACKAGE NAME", name.getPackageName()+" " + applicationName+ " " + String.valueOf(name.getTotalTimeInForeground()));
                arrayList.add((applicationName +"--"+name.getPackageName()));
                pckgArray.add(name.getPackageName());
                apkNameArray.add(applicationName);

                try
                {
                    Drawable icon = getPackageManager().getApplicationIcon(name.getPackageName());
                    appIcon.add(icon);
                }
                catch (PackageManager.NameNotFoundException e)
                {
                    Drawable icon = null;
                    appIcon.add(icon);
                    e.printStackTrace();
                }

            }
        }

        Log.d("TOTAL APPS", String.valueOf(queryUsageStats.size()));

       CustomBaseAdapter customAdapter = new CustomBaseAdapter(getApplicationContext(),apkNameArray,appIcon);

        ListView listView = (ListView) findViewById(R.id.recentAppListView);
        listView.setAdapter(customAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Intent launchIntent = getPackageManager().getLaunchIntentForPackage(pckgArray.get(position));
                    startActivity( launchIntent );
                }catch (Exception e){
                    Toast.makeText(MainActivity.this, "No launch activity available "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void launchApp(String pkgName){

    }
}