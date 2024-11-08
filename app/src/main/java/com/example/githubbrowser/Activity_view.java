package com.example.githubbrowser;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class Activity_view extends Fragment {
    private LinearLayout holdContentLayout;
    private static  String userName;

    private static String GITHUB_TOKEN;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_activity_view, container, false);

        holdContentLayout = view.findViewById(R.id.content_view);

        GITHUB_TOKEN = UserInformation.getAccessToken();

        new updateContent().execute();

        return view;
    }
    public class updateContent extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            if(progressDialog != null && progressDialog.isShowing()){
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
            userName = getUserName(GITHUB_TOKEN);
            String url = "https://api.github.com/users/"+userName+"/received_events";
            try {
                contentString = handler.httpServiceCall(url,GITHUB_TOKEN);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            if (contentString != null){
                try {
                    JSONArray jsonArray = new JSONArray(contentString);

                    for (int i =0;i< jsonArray.length();i++){

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        JSONObject actor = jsonObject1.getJSONObject("actor");
                        String name = actor.getString("login");

                        String repo ;
                        int star = 0;
                        if(jsonObject1.getString("type").equals("ForkEvent")) {
                            JSONObject payload = jsonObject1.getJSONObject("payload");
                            JSONObject forkee = payload.getJSONObject("forkee");
                            repo = forkee.getString("full_name");
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    addForkEventContent(name,repo,star);
                                }
                            });
                        } else if (jsonObject1.getString("type").equals("PushEvent")) {
                                JSONObject repo1 = jsonObject1.getJSONObject("repo");
                                repo = repo1.getString("name");
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                    addPushEventContent(name,repo,star);
                                }
                            });

                        }





                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);

                }


            }
            return null;
        }
    }
    public void addForkEventContent(String name,String repo,int star){
        View viewContent = LayoutInflater.from(requireContext()).inflate(R.layout.activity_infor_card,null);
        TextView textViewActivity = viewContent.findViewById(R.id.name);
        TextView textViewRepo = viewContent.findViewById(R.id.repo);

        TextView textViewStar = viewContent.findViewById(R.id.star);

        textViewActivity.setText(name+" forked a repository");
        textViewRepo.setText(repo);

        textViewStar.setText(String.valueOf(star)+" Star");

        holdContentLayout.addView(viewContent);

    }
    public void addPushEventContent(String name,String repo,int star){
        View viewContent = LayoutInflater.from(requireContext()).inflate(R.layout.activity_infor_card,null);
        TextView textViewActivity = viewContent.findViewById(R.id.name);
        TextView textViewRepo = viewContent.findViewById(R.id.repo);

        TextView textViewStar = viewContent.findViewById(R.id.star);

        textViewActivity.setText(name+" push a commits");
        textViewRepo.setText("to"+repo);

        textViewStar.setText(String.valueOf(star)+" Star");

        holdContentLayout.addView(viewContent);

    }
    public String getUserName(String token){
        Handler handler = new Handler();
        String jsonString;
        String name = null;
        try {
            String url ="https://api.github.com/user";
            jsonString = handler.httpServiceCall(url,token);

            if(jsonString != null){
                JSONObject jsonObject = new JSONObject(jsonString);
                name = jsonObject.getString("login");
            }



        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return name;
    }
}