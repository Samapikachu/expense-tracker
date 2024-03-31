package com.example.expense;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.UUID;

public class SignUpActivity extends AppCompatActivity {
    private EditText editTextUsername, editTextPassword;
    private UserDBHelper userDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        userDBHelper = new UserDBHelper(this);
    }

    public void signUp(View view) {
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userDBHelper.checkUser(username)) {
            Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generate a unique user ID (you can use UUID.randomUUID().toString() or any other method)
        String userId = generateUserId();

        long result = userDBHelper.insertUser(userId, username, password);
        if (result != -1) {
            Toast.makeText(this, "Sign up successful", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to sign up", Toast.LENGTH_SHORT).show();
            Log.e("SignUpActivity", "Failed to insert user data into the database");
        }
    }
    private String generateUserId() {
        return UUID.randomUUID().toString();
    }
}
