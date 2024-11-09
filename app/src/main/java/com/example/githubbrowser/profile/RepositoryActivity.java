package com.example.githubbrowser.profile;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.githubbrowser.R;
import com.example.githubbrowser.UserInformation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RepositoryActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private ArrayList<Repo> repoArrayList;
    private ListView repoListView;
    RepoVerticalAdapter repoVerticalAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_repository);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.repo_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Setting up the back button
        Button backButton = findViewById(R.id.backToProfile);
        backButton.setOnClickListener(v -> finish());
        //
        requestQueue =  Volley.newRequestQueue(this);
        fetchRepo();
    }

    // fetch Repository
    public void fetchRepo() {
        String userUrl = "https://api.github.com/user/repos";

        // listener
        Response.Listener<JSONArray> responseListener = (JSONArray response) -> {
            Log.d("RESPONSE",  response.toString());

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
                // find ListView
                repoListView = findViewById(R.id.list_repo_1);

                // adapter
                RepoVerticalAdapter repoVerticalAdapter = new RepoVerticalAdapter(this, R.layout.item_repo,repoArrayList);

                // set adapter
                repoListView.setAdapter(repoVerticalAdapter);

                // set listener
                repoListView.setOnItemClickListener((parent, view, position, id) -> {
                    Intent intent = new Intent(this, RepoDetailsActivity.class);
                    intent.putExtra("repo", repoVerticalAdapter.getItem(position));
                    startActivity(intent);
                });

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
}