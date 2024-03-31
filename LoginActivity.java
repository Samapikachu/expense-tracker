package com.example.expense;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextUsername, editTextPassword;
    private UserDBHelper userDBHelper;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        userDBHelper = new UserDBHelper(this);
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
    }

    public void login(View view) {
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Authenticate the user using the unique user ID instead of the username
        String userId = userDBHelper.authenticateUser(username, password);
        if (userId != null) {
            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();

            // Save the user ID in SharedPreferences
            saveCurrentUser(userId);

            startActivity(new Intent(this, OptionsActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveCurrentUser(String userId) {
        sharedPreferences.edit().putString("userId", userId).apply();
    }
}
