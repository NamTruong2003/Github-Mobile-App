package com.example.githubbrowser.profile;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.githubbrowser.R;

import java.util.ArrayList;

public class RepoVerticalAdapter extends ArrayAdapter<Repo> {
    private final Activity context;
    private ArrayList<Repo> repoItemList;
    private int resources;

    public RepoVerticalAdapter(Activity context, int resources, ArrayList<Repo> repoItemList) {
        super(context, resources, repoItemList);
        this.context = context;
        this.resources = resources;
        this.repoItemList = repoItemList;
    }

    @NonNull
    public View getView(int position, View convertView, ViewGroup parent) {
        // get function item
        Repo item = repoItemList.get(position);
        //
        LayoutInflater inflater = context.getLayoutInflater();
        View currentView = inflater.inflate(resources, parent, false);
        // find view
        TextView repoName = currentView.findViewById(R.id.repo_name_1);
        TextView repoDesc = currentView.findViewById(R.id.repoDesc);
        TextView repoStar = currentView.findViewById(R.id.repoStar1);
        TextView repoLang = currentView.findViewById(R.id.repoLang);
        // set attribute
        repoName.setText(item.getRepoName());
        repoDesc.setText(item.getRepoDesc());
        repoStar.setText(String.valueOf(item.getRepoStar()));
        repoLang.setText(item.getRepoLang());

        if (item.getRepoDesc().equals("null")){
            repoDesc.setText("");
            repoDesc.setVisibility(View.GONE);
        }

        return currentView;
    }
}
