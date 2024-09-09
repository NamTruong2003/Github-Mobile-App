package com.example.githubbrowser.profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.githubbrowser.R;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // setting up setting button
        ImageButton settingButton = view.findViewById(R.id.settingButton);
        if(settingButton != null){
            settingButton.setOnClickListener(v -> {
                Intent intent = new Intent(
                        requireContext(), SettingActivity.class
                );
                startActivity(intent);
            });
        }

        // Setting up follower button
        Button followerButton = view.findViewById(R.id.followerButton);
        if(followerButton != null){
            followerButton.setOnClickListener(v -> {
                Intent intent = new Intent(
                        requireContext(), FollowersActivity.class
                );
                startActivity(intent);
            });
        }

        // Setting up trophy button
        Button trophyButton = view.findViewById(R.id.trophyButton);
        if(trophyButton != null){
            trophyButton.setOnClickListener(v -> {
                Intent intent = new Intent(
                        requireContext(), TrophyActivity.class
                );
                startActivity(intent);
            });
        }

        // CREATE LIST VIEW
        // 1st listing view
        String[] leadingText = {"Repositories", "Started", "Organizations", "Projects"};
        String[] trailingText = {"0", "0", "0", "0"};
        Integer[] imgid = {R.drawable.repository, R.drawable.star, R.drawable.orgisation,R.drawable.project_icon};
        SettingListAdapter adapter = new SettingListAdapter(R.layout.listview_function_item, getActivity(), leadingText, trailingText, imgid);
        ListView listView = view.findViewById(R.id.functionListView);
        listView.setAdapter(adapter);
        setListViewHeightBasedOnItems(listView);

        // Inflate the layout for this fragment
        return view;
    }

    public static void setListViewHeightBasedOnItems(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}