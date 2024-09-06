package com.example.githubbrowser.profile;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.githubbrowser.R;

public class SettingListAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] leadingText;
    private final String[] trailingText;
    private final Integer[] imgid;
    private Integer listViewItemLayoutId;

    public SettingListAdapter(Integer listViewItemLayoutId, Activity context, String[] leadingText, String[] trailingText, Integer[] imgid) {
        super(context, listViewItemLayoutId, leadingText);
        this.context = context;
        this.leadingText = leadingText;
        this.trailingText = trailingText;
        this.imgid = imgid;
        this.listViewItemLayoutId = listViewItemLayoutId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View currentView = inflater.inflate(listViewItemLayoutId, null, true);
        TextView leadingText = currentView.findViewById(R.id.leadingText);
        TextView trailingText = currentView.findViewById(R.id.trailingText);
        ImageView imageView = currentView.findViewById(R.id.image_setting);
        leadingText.setText(this.leadingText[position]);
        trailingText.setText(this.trailingText[position]);
        if(imgid.length > 0){
            imageView.setImageResource(this.imgid[position]);
        }

        return currentView;
    }
}
