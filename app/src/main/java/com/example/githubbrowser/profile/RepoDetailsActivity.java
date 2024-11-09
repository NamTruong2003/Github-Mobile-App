package com.example.githubbrowser.profile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.githubbrowser.R;
import com.google.android.material.imageview.ShapeableImageView;

public class RepoDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_repo_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // back btn
        ImageButton imageButton = findViewById(R.id.backBtnRepoDetail);
        imageButton.setOnClickListener(v -> finish());
        // get intent
        Intent intent= getIntent();
        Repo repo = (Repo) intent.getSerializableExtra("repo");
        if(repo != null){
            // get attribute
            String userImg = repo.getOwnImgUrl();
            String username = repo.getUsername();
            String repoName = repo.getRepoName();
            int repoStar = repo.getRepoStar();
            int repoFork = repo.getRepoFork();
            // find view
            ShapeableImageView repoUserImg = findViewById(R.id.repoDetailUserImg);
            TextView repoDetailUser = findViewById(R.id.repoDetailUser);
            TextView repoDetailTitle = findViewById(R.id.repoDetailTitle);
            TextView repoDetailStar = findViewById(R.id.repoDetailStar);
            TextView repoDetailFork = findViewById(R.id.repoDetailFork);
            // set content
            Glide.with(this)
                    .load(userImg)
                    .into(repoUserImg);
            repoDetailUser.setText(username);
            repoDetailTitle.setText(repoName);
            repoDetailStar.setText(repoStar+ " stars");
            repoDetailFork.setText(repoFork+ " forks");

        }
    }
}