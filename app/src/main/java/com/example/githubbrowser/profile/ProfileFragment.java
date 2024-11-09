package com.example.githubbrowser.profile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.githubbrowser.R;
import com.example.githubbrowser.UserInformation;
import com.google.android.material.imageview.ShapeableImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileFragment extends Fragment implements ItemClickListener {

    private RequestQueue requestQueue;
    private List<Repo> repoArrayList;
    private RepoAdapter repoAdapter;
    private RecyclerView repoRecycleView;
    private List<FunctionItem> functionItemList;
    //
    private int totalRepo;
    private int totalStarred;
    private int totalOrg;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        functionItemList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(requireContext());
        View currentView = inflater.inflate(R.layout.fragment_profile, container, false);
        // fetch user
        fetchUser(this.requireContext(), currentView);
        // fetch repo
        fetchRepo(this.requireContext(), currentView);


//        CardView Card1 = currentView.findViewById(R.id.Card1);
//        CardView Card3 = currentView.findViewById(R.id.Card3);
//        CardView Card2 = currentView.findViewById(R.id.Card2);

//        Card1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent_card1 = new Intent(requireContext(), com.example.githubbrowser.profile.Card1.class);
//                startActivity(intent_card1);
//            }
//        });
//        Card2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent_card2 = new Intent(requireContext(), com.example.githubbrowser.profile.Card2.class);
//                startActivity(intent_card2);
//            }
//        });
//        Card3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent_card3 = new Intent(requireContext(), com.example.githubbrowser.profile.Card3.class);
//                startActivity(intent_card3);
//            }
//        });


        // setting up setting button
        ImageButton settingButton = currentView.findViewById(R.id.settingButton);
        if(settingButton != null){
            settingButton.setOnClickListener(v -> {
                Intent intent = new Intent(
                        requireContext(), SettingActivity.class
                );
                startActivity(intent);
            });
        }

        // Setting up follower button
        Button followerButton = currentView.findViewById(R.id.followerButton);
        if(followerButton != null){
            followerButton.setOnClickListener(v -> {
                Intent intent = new Intent(
                        requireContext(), FollowersActivity.class
                );
                startActivity(intent);
            });
        }

        // Setting up trophy button
        Button trophyButton = currentView.findViewById(R.id.trophyButton);
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
//        FunctionListAdapter adapter = new FunctionListAdapter( getActivity(), R.layout.listview_function_item, functionItemList);
//        ListView listView = currentView.findViewById(R.id.functionListView);
//        listView.setAdapter(adapter);
//        setListViewHeightBasedOnItems(listView);

//        // set click on items
//        listView.setOnItemClickListener((parent, view, position, id) -> {
//            Intent intent;
//            switch (position){
//                case 0:
//                    intent = new Intent(
//                            requireContext(), RepositoryActivity.class
//                    );
//                    startActivity(intent);
//                    break;
//                case 1:
//                    intent = new Intent(
//                            requireContext(), StarredRepoActivity.class
//                    );
//                    startActivity(intent);
//                    break;
//                case 2:
//                    intent = new Intent(
//                            requireContext(), OrganizationActivity.class
//                    );
//                    startActivity(intent);
//                    break;
//                case 3:
//                    intent = new Intent(
//                            requireContext(), ProjectsActivity.class
//                    );
//                    startActivity(intent);
//                    break;
//            }
//        });

        // Inflate the layout for this fragment
        return currentView;
    }

    // set click on items
    public static void setClickOnItem(ListView listView, Context context) {
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent;
            switch (position){
                case 0:
                    intent = new Intent(
                            context, RepositoryActivity.class
                    );
                    context.startActivity(intent);
                    break;
                case 1:
                    intent = new Intent(
                            context, StarredRepoActivity.class
                    );
                    context.startActivity(intent);
                    break;
                case 2:
                    intent = new Intent(
                            context, OrganizationActivity.class
                    );
                    context.startActivity(intent);
                    break;
                case 3:
                    intent = new Intent(
                            context, ProjectsActivity.class
                    );
                    context.startActivity(intent);
                    break;
            }
        });
    }

    public  void setListViewHeightBasedOnItems(ListView listView) {
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

    // send request
    public void fetchUser(Context context, View view) {
        String userUrl = "https://api.github.com/user";

        // listener
        Response.Listener<JSONObject> responseListener = (JSONObject response) -> {
            Log.d("RESPONSE",  response.toString());
            try {
                JSONObject jsonObject = new JSONObject(response.toString());
                // set username
                String username = jsonObject.getString("login");
                TextView user = view.findViewById(R.id.username);
                user.setText(username);

                // set avatar
                ShapeableImageView avatar = view.findViewById(R.id.avatar); // find widget
                String avatarUrl = jsonObject.getString("avatar_url"); // retrieve img url
                // load the image
                Glide.with(context)
                        .load(avatarUrl)
                        .into(avatar);

                // set follower
                Button followerBtn = view.findViewById(R.id.followerButton);
                String followers = jsonObject.getString("followers");
                followerBtn.setText(followers + " follower");

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };

        // error listener
        Response.ErrorListener errorListener = (VolleyError error) ->{
            Log.d("RESPONSE",  error.toString());
        };

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.GET, userUrl,
                null,
                responseListener,
                errorListener) {

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("X-GitHub-Api-Version", "2022-11-28");
                headers.put("Authorization", "Bearer "+ UserInformation.getAccessToken());
                return headers;
            }

        };

        // Add the request to the RequestQueue.
        requestQueue.add(jsonObjReq);
    }

    // send repo request
    public void fetchRepo(Context context, View view) {
        String userUrl = "https://api.github.com/user/repos";

        // listener
        Response.Listener<JSONArray> responseListener = (JSONArray response) -> {
            Log.d("RESPONSE",  response.toString());
            // create function item entity
            totalRepo = response.length();
            String leadingText = "Repositories";
            int funcImg = R.drawable.repository;
            FunctionItem repoFunc = new FunctionItem(funcImg, leadingText, totalRepo);
            functionItemList.add(repoFunc);

            //
            repoArrayList = new ArrayList<>();
            try {
                for(int i = 0; i< response.length(); i++){

                    JSONObject jsonObject = response.getJSONObject(i);
                    // get params
                    String repoName = jsonObject.getString("name");
                    String repoDesc = jsonObject.getString("description");
                    String repoLang = jsonObject.getString("language");
                    int repoStar = jsonObject.getInt("stargazers_count");
                    JSONObject owner = jsonObject.getJSONObject("owner");
                    String ownerName = owner.getString("login");
                    String ownerImg = owner.getString("avatar_url");
                    int repoFork = jsonObject.getInt("forks_count");

                    repoLang = repoLang.equals("null") ? "" : repoLang;
                    // create Repo instance
                    Repo repo = new Repo(repoName, repoDesc, repoStar,
                            repoLang, ownerName, ownerImg, repoFork);
                    repoArrayList.add(repo);
                }
                // find RecyclerView
                repoRecycleView = view.findViewById(R.id.repoRecyclerView);
                // set layout management
                LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                repoRecycleView.setLayoutManager(layoutManager);
                // adapter
                repoAdapter = new RepoAdapter(repoArrayList);
                repoRecycleView.setAdapter(repoAdapter); // link ListView vs adapter

                // set listener
                repoAdapter.setItemClickListener(this);
                // fetch starred
                fetchStarred(context, view);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };

        // error listener
        Response.ErrorListener errorListener = (VolleyError error) ->{
            Log.d("RESPONSE",  error.toString());
        };

        JsonArrayRequest jsonObjReq = new JsonArrayRequest(
                Request.Method.GET, userUrl,
                null,
                responseListener,
                errorListener) {

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("X-GitHub-Api-Version", "2022-11-28");
                headers.put("Authorization", "Bearer "+ UserInformation.getAccessToken());
                return headers;
            }

        };

        // Add the request to the RequestQueue.
        requestQueue.add(jsonObjReq);
    }

    /*
     * Send started request
     *   get all starred
     */
    public void fetchStarred(Context context, View view) {
        String userUrl = "https://api.github.com/user/starred";

        // listener
        Response.Listener<JSONArray> responseListener = (JSONArray response) -> {
            Log.d("RESPONSE",  response.toString());
            // create FunctionItem entity
            totalStarred = response.length();
            String leadingText = "Starred";
            int funcImg = R.drawable.star;
            FunctionItem starredFunc = new FunctionItem(funcImg, leadingText, totalStarred);
            functionItemList.add(starredFunc);
            // fetch org
            fetchOrg(context, view);
        };

        // Error
        Response.ErrorListener errorListener = (VolleyError error) ->{
            Log.d("RESPONSE",  error.toString());
        };

        JsonArrayRequest jsonObjReq = new JsonArrayRequest(
                Request.Method.GET, userUrl,
                null,
                responseListener,
                errorListener) {

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("X-GitHub-Api-Version", "2022-11-28");
                headers.put("Authorization", "Bearer "+ UserInformation.getAccessToken());
                return headers;
            }

        };

        // Add the request to the RequestQueue.
        requestQueue.add(jsonObjReq);
    }

    /*
     * Send organizations request
     *   get all organizations
     */
    public void fetchOrg(Context context, View view) {
        String userUrl = "https://api.github.com/user/orgs";

        // listener
        Response.Listener<JSONArray> responseListener = (JSONArray response) -> {
            Log.d("RESPONSE",  response.toString());
            // create FunctionItem entity
            totalOrg = response.length();
            String leadingText = "Organizations";
            int funcImg = R.drawable.orgisation;
            FunctionItem orgFunc = new FunctionItem(funcImg, leadingText, totalStarred);
            functionItemList.add(orgFunc);
            // create FunctionItem entity
            int totalProject = response.length();
            String leadingTextPro = "Projects";
            int funcImgPro = R.drawable.project_icon;
            FunctionItem proFunc = new FunctionItem(funcImgPro, leadingTextPro, totalProject);
            functionItemList.add(proFunc);

            // set list view
            FunctionListAdapter adapter = new FunctionListAdapter( getActivity(), R.layout.listview_function_item, functionItemList);
            ListView listView = view.findViewById(R.id.functionListView);
            listView.setAdapter(adapter);
            setListViewHeightBasedOnItems(listView);

            // set on click item
            setClickOnItem(listView, view.getContext());
        };

        // Error
        Response.ErrorListener errorListener = (VolleyError error) ->{
            Log.d("RESPONSE",  error.toString());
        };

        JsonArrayRequest jsonObjReq = new JsonArrayRequest(
                Request.Method.GET, userUrl,
                null,
                responseListener,
                errorListener) {

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("X-GitHub-Api-Version", "2022-11-28");
                headers.put("Authorization", "Bearer "+ UserInformation.getAccessToken());
                return headers;
            }

        };

        // Add the request to the RequestQueue.
        requestQueue.add(jsonObjReq);
    }

    @Override
    public void onClick(View view, int position) {
        Intent intent = new Intent(requireContext(), RepoDetailsActivity.class);
        intent.putExtra("repo", repoArrayList.get(position));
        startActivity(intent);
    }
}