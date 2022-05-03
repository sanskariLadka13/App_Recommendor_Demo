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

       // String topPackageName ;
       // if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            UsageStatsManager mUsageStatsManager = (UsageStatsManager)getSystemService(Context.USAGE_STATS_SERVICE);
//            long time = System.currentTimeMillis();
//            Log.d("TIME", String.valueOf(time));
//            // We get usage stats for the last 10 seconds
//            List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000*10, time);
//            Log.d("STATS", String.valueOf(stats));
//            // Sort the stats by the last time used
//            if(stats != null) {
//                SortedMap<Long,UsageStats> mySortedMap = new TreeMap<Long,UsageStats>();
//                for (UsageStats usageStats : stats) {
//
//                    mySortedMap.put(usageStats.getLastTimeUsed(),usageStats);
//                }
//                if(mySortedMap != null && !mySortedMap.isEmpty()) {
//                    topPackageName =  mySortedMap.get(mySortedMap.lastKey()).getPackageName();
//                    Log.d("FINAL OUTPUT",topPackageName);
//                }
//            }
      //  }
        long time = System.currentTimeMillis();
        Log.d("TIME", String.valueOf(time));

        final UsageStatsManager usageStatsManager=(UsageStatsManager)getSystemService(Context.USAGE_STATS_SERVICE);
        final Map<String,UsageStats> queryUsageStats=usageStatsManager.queryAndAggregateUsageStats(time - 1000*100, time);
        for (UsageStats name : queryUsageStats.values()) {
            // iterating over usage stats and considering only those apps which have appeared atleast once in foreground
            if (name.getTotalTimeInForeground() > 0) {
                Log.d("PACKAGE NAME", name.getPackageName() + " " + String.valueOf(name.getTotalTimeInForeground()));
            }
        }

        Log.d("TOTAL APPS", String.valueOf(queryUsageStats.size()));

        // Code for extracting App Name from package name... Work in progress ,, need to wrap it in loop
        final PackageManager pm = getApplicationContext().getPackageManager();
        ApplicationInfo ai;
        try {
            ai = pm.getApplicationInfo( "com.example.myapplication", 0);
        } catch (Error | PackageManager.NameNotFoundException e) {
            ai = null;
        }
        final String applicationName = (String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)");
        Log.d("APK NAME",applicationName);

    }
}