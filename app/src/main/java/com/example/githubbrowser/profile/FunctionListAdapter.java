package com.example.githubbrowser.profile;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.githubbrowser.R;

import java.util.List;

public class FunctionListAdapter extends ArrayAdapter<FunctionItem> {

    private final Activity context;
    private List<FunctionItem> functionItemList;
    private int resources;

    public FunctionListAdapter(Activity context, int resources, List<FunctionItem> functionItemList) {
        super(context, resources, functionItemList);
        this.context = context;
        this.resources = resources;
        this.functionItemList = functionItemList;
    }

    @NonNull
    public View getView(int position, View convertView, ViewGroup parent) {
        // get function item
        FunctionItem item = functionItemList.get(position);
        //
        LayoutInflater inflater = context.getLayoutInflater();
        View currentView = inflater.inflate(resources, parent, false);
        // find view
        TextView leadingText = currentView.findViewById(R.id.leadingText);
        TextView trailingText = currentView.findViewById(R.id.trailingText);
        ImageView imageView = currentView.findViewById(R.id.image_setting);
        // set attribute
        leadingText.setText(item.getLeadingText());
        trailingText.setText(String.valueOf(item.getTrailingNumber()));
        imageView.setImageResource(item.getFuncImg());
        if (!functionItemList.isEmpty()) {
            if (item.getFuncImg() == R.drawable.repository) {
                imageView.setBackground(ContextCompat.getDrawable(context, R.drawable.background_repositories_icon));
                imageView.setColorFilter(Color.argb(255, 255, 255, 255));
            } else if (item.getFuncImg() == R.drawable.star) {
                imageView.setBackground(ContextCompat.getDrawable(context, R.drawable.background_starred_icon));
                imageView.setColorFilter(Color.argb(255, 255, 255, 255));
            } else if (item.getFuncImg() == R.drawable.orgisation) {
                imageView.setBackground(ContextCompat.getDrawable(context, R.drawable.background_organizations_icon));
                imageView.setColorFilter(Color.argb(255, 255, 255, 255));
            } else if (item.getFuncImg() == R.drawable.project_icon) {
                imageView.setBackground(ContextCompat.getDrawable(context, R.drawable.background_project_icon));
                imageView.setColorFilter(Color.argb(255, 255, 255, 255));
            }
        }

        return currentView;
    }

}
