package com.example.githubbrowser;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.Objects;

public class Trending extends Fragment {
    private LinearLayout holdContentLayout;
    private static String url = "https://api.github.com/search/repositories?q=created";


    private static String GITHUB_TOKEN ;
    private ProgressDialog progressDialog;
    public Trending(){

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trending, container, false);

        holdContentLayout = view.findViewById(R.id.content_view);
        Toolbar trending_toolbar = view.findViewById(R.id.xml_toolbar_trending);
        trending_toolbar.setTitle("Trending");
        trending_toolbar.setTitleTextColor(Color.BLACK);

        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(trending_toolbar);
            Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }
        final Drawable upArrow = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_arrow_back_24);
        upArrow.setColorFilter(ContextCompat.getColor(requireContext(), android.R.color.black), PorterDuff.Mode.SRC_ATOP);

        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(upArrow);
        }

        trending_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity()!=null){
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });

        GITHUB_TOKEN = UserInformation.getAccessToken();
        new updateContent().execute();

        return view;
    }
    public class updateContent extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Handler handler = new Handler();
            String contentString = null;
            String contributorString =null;
            try {
                contentString = handler.httpServiceCall(url,GITHUB_TOKEN);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            if (contentString != null){
                try {
                    JSONObject jsonObject = new JSONObject(contentString);
                    JSONArray items = jsonObject.getJSONArray("items");
                    for (int i =0;i<10;i++){
                        JSONObject jsonObject1 = items.getJSONObject(i);
                        String repo = jsonObject1.getString("full_name");
                        String des = jsonObject1.getString("description");
                        int star = jsonObject1.getInt("stargazers_count");
                        String contributor_url=jsonObject1.getString("contributors_url");
                        int contributor = 0;
                        try {
                            contributorString = handler.httpServiceCall(contributor_url,GITHUB_TOKEN);

                        }catch (MalformedURLException e){
                            e.printStackTrace();
                        }
                        if(contributorString != null){
                            JSONArray jsonArray = new JSONArray(contributorString);
                            contributor = jsonArray.length();

                        }
                        final int final_contributors = contributor;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                addViewContent(repo,des,star,final_contributors);
                            }
                        });


                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);

                }


            }
            return null;
        }
    }
    public void addViewContent(String repo,String des, int star,int contributors ){
        View viewContent = LayoutInflater.from(requireContext()).inflate(R.layout.infor_card,null);
        TextView textViewRepo = viewContent.findViewById(R.id.repo);
        TextView textViewDes = viewContent.findViewById(R.id.description);
        TextView textViewStar = viewContent.findViewById(R.id.star);
        TextView textViewContributors = viewContent.findViewById(R.id.contributors);
        textViewRepo.setText(repo);
        textViewDes.setText(des);
        textViewStar.setText(String.valueOf(star)+" Star");
        textViewContributors.setText(String.valueOf(contributors)+" Contributors");
        holdContentLayout.addView(viewContent);

    }
}