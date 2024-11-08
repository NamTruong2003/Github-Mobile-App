package com.example.githubbrowser;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignInWithGithubActivity extends AppCompatActivity {
    EditText username;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in_with_github);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button signInWithGithubButton = findViewById(R.id.signInButton);
        Button signInWithAPasskeyButton = findViewById(R.id.signInWithAPasskeyButton);

        username = findViewById(R.id.usernameOrEmail);
        password = findViewById(R.id.password);

        signInWithGithubButton.setOnClickListener(v -> {
            if(username.getText().toString().trim().matches("")){
                username.setError("Username required!");
                username.requestFocus();
                return;
            }
            if(password.getText().toString().trim().matches("")){
                password.setError("Password required!");
                password.requestFocus();
                return;
            }

            Intent intent01 = new Intent(SignInWithGithubActivity.this, MainActivity.class);
            startActivity(intent01);
            username.setText("");
            password.setText("");
        });

        signInWithAPasskeyButton.setOnClickListener(view -> {
            Toast.makeText(this, "Passkey sign-in is coming soon!\nStay tuned for updates."
                    , Toast.LENGTH_SHORT).show();
        });
    }
}