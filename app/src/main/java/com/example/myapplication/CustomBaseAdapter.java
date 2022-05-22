package com.example.myapplication;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CustomBaseAdapter extends BaseAdapter {

    ArrayList<String> appName;
    ArrayList<Drawable> appIcon;
    Context ctx;
    LayoutInflater inflater;

    CustomBaseAdapter(Context ctx , ArrayList<String> appName , ArrayList<Drawable> appIcon){
        this.appIcon = appIcon;
        this.appName = appName;
        this.ctx = ctx;
        this.inflater = LayoutInflater.from(ctx);
        Log.d("AAAAAA",String.valueOf(appName));
    }

    @Override
    public int getCount() {
        return appName.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.custom_list_view,null);
        TextView textView = (TextView)convertView.findViewById(R.id.appName);
        ImageView imgView = (ImageView)convertView.findViewById(R.id.appIcon);
//        Button button = (Button)convertView.findViewById(R.id.infoButton);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(ctx, "Clicked", Toast.LENGTH_SHORT).show();
//            }
//        });
        textView.setText(appName.get(position));
        imgView.setImageDrawable(appIcon.get(position));
        return convertView ;
    }
}
