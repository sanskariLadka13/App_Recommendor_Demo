package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

        String[] mobileArray = {"Recent Apps"};

        ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(mobileArray));

        final UsageStatsManager usageStatsManager=(UsageStatsManager)getSystemService(Context.USAGE_STATS_SERVICE);
        final Map<String,UsageStats> queryUsageStats=usageStatsManager.queryAndAggregateUsageStats(time - 1000*100, time);
        for (UsageStats name : queryUsageStats.values()) {
            // iterating over usage stats and considering only those apps which have appeared atleast once in foreground
            if (name.getTotalTimeInForeground() > 0) {
                Log.d("PACKAGE NAME", name.getPackageName() + " " + String.valueOf(name.getTotalTimeInForeground()));
                arrayList.add((name.getPackageName()));
            }
        }

        Log.d("TOTAL APPS", String.valueOf(queryUsageStats.size()));

        // Code for extracting App Name from package name... Work in progress ,, need to wrap it in loop
        final PackageManager pm = getApplicationContext().getPackageManager();
        ApplicationInfo ai;
        try {
            ai = pm.getApplicationInfo( "com.example.myapplication", 0);
        } catch ( PackageManager.NameNotFoundException e) {
            ai = null;
        }
        final String applicationName = (String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)");
        Log.d("APK NAME",applicationName);



        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_recent_list_holder, arrayList);

        ListView listView = (ListView) findViewById(R.id.recentAppListView);
        listView.setAdapter(adapter);

    }
}