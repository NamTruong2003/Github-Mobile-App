package com.example.githubbrowser.homepage;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.githubbrowser.Handler;
import com.example.githubbrowser.R;
import com.example.githubbrowser.UserInformation;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.concurrent.atomic.AtomicBoolean;

public class RepositoriesActivity extends AppCompatActivity {

    private AtomicBoolean isfirstItem1 = new AtomicBoolean(true);
    private LinearLayout holdContentLayout;
    private static String userName;
    private static String GIT_TOKEN;
    private ProgressDialog progressDialog;
    private RelativeLayout relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_repositories);
        holdContentLayout = findViewById(R.id.holdContentLayout_repositories);
        relativeLayout = findViewById(R.id.center_repositories);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton backButton = findViewById(R.id.backButton_repositories);
        backButton.setOnClickListener(view -> {
            Toast.makeText(getApplicationContext(),R.string.back,Toast.LENGTH_SHORT).show();
            onBackPressed();
        });
        Spinner spinner1 = findViewById(R.id.open_menu_repositories);

        String[] items1 = {"All", "Archived", "Fork","Mirror", "Private", "Public", "Source", "Template"};

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items1);

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_item);

        spinner1.setAdapter(adapter1);
        setupSpinnerListener(spinner1,items1,isfirstItem1);
        GIT_TOKEN = UserInformation.getAccessToken();
    }
    public void setupSpinnerListener(Spinner spinner, String[] items,final AtomicBoolean isfirstselection) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!isfirstselection.get()) {
                    String selectedItem = items[i];
                    Toast.makeText(getApplicationContext(), "Spinner: " + selectedItem, Toast.LENGTH_SHORT).show();
                } else {
                    isfirstselection.set(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        new updateContent().execute();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    public class updateContent extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            Toast.makeText(getApplicationContext(), "Data loaded successfully", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(RepositoriesActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Handler handler = new Handler();
            String contentString = null;
            String contributorString = null;
            userName = getUserName(GIT_TOKEN);
            String url = "https://api.github.com/users/"+userName+"/repos";
            try {
                contentString = handler.httpServiceCall(url, GIT_TOKEN);
                JSONArray jsonArray = new JSONArray(contentString);
                if (jsonArray != null && jsonArray.length()>0){

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String repo = jsonObject.getString("name");
                        JSONObject jsonObject1 = jsonObject.getJSONObject("owner");
                        String login_name = jsonObject1.getString("login");
                        String avatar = jsonObject1.getString("avatar_url");
                        runOnUiThread(() -> addViewContent(repo, login_name, avatar));
                    }
                } else {
                    runOnUiThread(() -> {
                        relativeLayout.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), "No data found in the API response", Toast.LENGTH_SHORT).show();
                    });
                }

            } catch (JSONException | MalformedURLException e) {
                e.printStackTrace();
            }
            return null;
        }
        public void addViewContent(String repo, String des, String image_api) {
            runOnUiThread(() -> {
                View viewContent = getLayoutInflater().inflate(R.layout.api_createissue, null);
                TextView textViewRepo = viewContent.findViewById(R.id.name_repo);
                TextView textViewLoginName = viewContent.findViewById(R.id.login_name);
                ImageView image = viewContent.findViewById(R.id.iamge_api);

                textViewRepo.setText(repo);
                textViewLoginName.setText(des);
                Picasso.get().load(image_api).into(image);
                holdContentLayout.addView(viewContent);
            });
        }
        public String getUserName(String token){
            Handler handler = new Handler();
            String jsonString;
            String name = null;
            try {
                String url ="https://api.github.com/user";
                jsonString = handler.httpServiceCall(url,token);
                Log.i("reponse",jsonString);
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
}