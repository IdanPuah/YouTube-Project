package com.example.youtube_android.ui.login;

import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.youtube_android.R;
import com.example.youtube_android.data.model.UserViewModel;
import com.example.youtube_android.ui.main.MainActivity;
import com.example.youtube_android.ui.signup.SignUpActivity;
import com.example.youtube_android.util.LoggedManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private Button btnLogIn, btnSignUp;
    private ImageView loadingSpinner;
    private UserViewModel userViewModel;
    private Animation animRotate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        edtEmail = findViewById(R.id.editTextEmail);
        edtPassword = findViewById(R.id.editTextTextPassword);
        btnLogIn = findViewById(R.id.logInButton);
        btnSignUp = findViewById(R.id.signUpButton);

        loadingSpinner = findViewById(R.id.loadingSpinner);

        animRotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);

        // Log in button click listener
        btnLogIn.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Show the loading spinner and start animation
            loadingSpinner.setVisibility(View.VISIBLE);
            loadingSpinner.startAnimation(animRotate);

            // Trigger the login process
            userViewModel.validateLogin(email, password);
        });

        // Observe the login result
        userViewModel.getLoginResult().observe(this, loginResult -> {
            // Hide the loading spinner and stop animation
            loadingSpinner.clearAnimation();
            loadingSpinner.setVisibility(View.GONE);

            switch (loginResult) {
                case 0:
                    LoggedManager.getInstance().setLogged(true);
                    Intent mainActivity = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(mainActivity);
                    finish(); // Finish the LoginActivity after starting MainActivity
                    break;
                case 1:
                    Toast.makeText(LoginActivity.this, "Login failed. Please check your Email and Password.", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(LoginActivity.this, "Unable to process image due to memory limitations." +
                            "Please login to user with lighter profile image", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(LoginActivity.this, "Network error. Please check your connection.", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(LoginActivity.this, "An unknown error occurred. Please try again with diffrenet user", Toast.LENGTH_SHORT).show();
                    break;
            }
        });

        btnSignUp.setOnClickListener(v -> {
            Intent signUpIntent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(signUpIntent);
        });
    }
}

