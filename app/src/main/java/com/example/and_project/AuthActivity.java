package com.example.and_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.util.regex.Pattern;

public class AuthActivity extends AppCompatActivity {
    private UserViewModel viewModel;
    private Pattern emailPattern;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        emailPattern = Pattern.compile("\\d*@via.dk");
        viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        setupFrabmentTab();
        if (isLoggedIn()) {
            observeUser();
        }
    }

    public void signIn(View view) {
        TextView tvUsername = (TextView)findViewById(R.id.username);
        TextView tvPassword = (TextView)findViewById(R.id.password);

        String email = tvUsername.getText().toString();
        String password = tvPassword.getText().toString();
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in both email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        viewModel.signIn(email, password).addOnCompleteListener(task -> {
            if (!task.isSuccessful())
                Toast.makeText(this, "Unable to sign in", Toast.LENGTH_SHORT).show();
            else {
                viewModel.updateUserLiveData();
                goToMainActivity();
            }
            });
    }

    public void signUp(View view) {
        TextView tvUsername = findViewById(R.id.signUpUsername);
        TextView tvPassword = findViewById(R.id.signUpPassword);
        TextView tvPasswordRepeat = findViewById(R.id.signUpPasswordRepeat);
        TextView tvDescription = findViewById(R.id.signUpDescription);
        TextView tvName = findViewById(R.id.signUpName);

        if (!emailPattern.matcher(tvUsername.getText()).matches()) {
            Toast.makeText(this, "Please use a @via.dk mail", Toast.LENGTH_SHORT).show();
            return;
        }

        if (tvPassword.getText().toString().length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!tvPassword.getText().toString().equals(tvPasswordRepeat.getText().toString())) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }
        viewModel.signUp(tvUsername.getText().toString(), tvPassword.getText().toString()).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Toast.makeText(this, "Unable to sign up", Toast.LENGTH_SHORT).show();
            } else {
                goToMainActivity();
                viewModel.addUserDetails(tvName.getText().toString(), tvDescription.getText().toString());
                viewModel.updateUserLiveData();
            }
        });
    }

    private boolean isLoggedIn() {
        return viewModel.getCurrentUser() != null;
    }

    private void observeUser() {
        viewModel.getUserLiveData().observe(this, user -> {
            if (user != null) {
                goToMainActivity();
            }
        });
    }

    private void goToMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void setupFrabmentTab() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, new LoginFragment()).commit();

        ((TabLayout) findViewById(R.id.tabLayout)).addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment = tab.getPosition() == 1
                        ? new SignUpFragment()
                        : new LoginFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, fragment).commit();
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }
}