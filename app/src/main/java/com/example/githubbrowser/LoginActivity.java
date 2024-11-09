package com.example.githubbrowser;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;



import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
//        if(UserInformation.getAccessToken() != null){
//            Intent intent01 = new Intent(LoginActivity.this, MainActivity.class);
//            startActivity(intent01);
//            Toast.makeText(this, "Welcome back", Toast.LENGTH_SHORT).show();
//        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button signInWithGithubButton = findViewById(R.id.signInWithGithubButton);
        signInWithGithubButton.setOnClickListener(view -> startGithubLogin());
        Button button = findViewById(R.id.loginButtonEnterprise);
        button.setOnClickListener(v -> {
            Intent intent02 = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent02);
        });

    }
    private void startGithubLogin() {

        String redirectUri = "usthgithub://jmaqhuy.id.vn";
        String scope = "repo user";
        String authUrl = "https://github.com/login/oauth/authorize" +
                "?client_id=" + Constant.CLIENT_ID +
                "&redirect_uri=" + redirectUri +
                "&scope=" + scope;

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl));
        startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        Uri uri = intent.getData();
        if (uri != null) {
            Log.i("URI", uri.toString());
            if (uri.getScheme().equals("usthgithub") && uri.getHost().equals("jmaqhuy.id.vn")) {
                String code = uri.getQueryParameter("code");
                fetchAccessToken(code);
            }
        }
    }

    private void fetchAccessToken(String code) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("client_id", Constant.CLIENT_ID)
                .add("client_secret", Constant.CLIENT_SECRET)
                .add("code", code)
                .build();

        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .addHeader("Accept", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    JSONObject jsonObject = null;

                    try {
                        jsonObject = new JSONObject(responseData);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        String accessToken = jsonObject.getString("access_token");
                        UserInformation.setAccessToken(accessToken);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    Intent intent01 = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent01);
                }
            }
        });
    }




}