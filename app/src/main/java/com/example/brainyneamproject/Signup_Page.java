package com.example.brainyneamproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Signup_Page extends AppCompatActivity {

    private EditText name, contact, email, passwordNew, passwordConfirm;
    private Button btnSignup;
    private TextView btnSignIn;
    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);

        userInfo = new UserInfo(this);

        name = findViewById(R.id.Name);
        contact = findViewById(R.id.Contact);
        email = findViewById(R.id.email);
        passwordNew = findViewById(R.id.password_new);
        passwordConfirm = findViewById(R.id.password_confirm);
        btnSignup = findViewById(R.id.btnSignup);
        btnSignIn = findViewById(R.id.btnSignIn);

        btnSignup.setOnClickListener(v -> attemptSignup());

        btnSignIn.setOnClickListener(v -> {
            startActivity(new Intent(Signup_Page.this, Login_Page.class));
            finish();
        });
    }

    private void attemptSignup() {
        String nameText = name.getText().toString().trim();
        String contactText = contact.getText().toString().trim();
        String emailText = email.getText().toString().trim();
        String pass = passwordNew.getText().toString();
        String confirm = passwordConfirm.getText().toString();


        if (nameText.isEmpty()) {
            name.setError("Name is required");
            name.requestFocus();
            return;
        }
        if (nameText.length() < 3) {
            name.setError("Name must be at least 3 characters");
            name.requestFocus();
            return;
        }


        if (contactText.isEmpty()) {
            contact.setError("Contact number is required");
            contact.requestFocus();
            return;
        }
        if (!contactText.matches("^[6-9][0-9]{9}$")) {
            contact.setError("Enter a valid 10 digit mobile number");
            contact.requestFocus();
            return;
        }


        if (emailText.isEmpty()) {
            email.setError("Email is required");
            email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            email.setError("Enter a valid email address");
            email.requestFocus();
            return;
        }
        if (userInfo.userExists(emailText)) {
            email.setError("This email is already registered");
            email.requestFocus();
            return;
        }


        if (pass.isEmpty()) {
            passwordNew.setError("Password is required");
            passwordNew.requestFocus();
            return;
        }
        if (pass.length() < 6) {
            passwordNew.setError("Password must be at least 6 characters");
            passwordNew.requestFocus();
            return;
        }
        if (!pass.matches(".*[A-Za-z].*") || !pass.matches(".*[0-9].*")) {
            passwordNew.setError("Password must contain letters and numbers");
            passwordNew.requestFocus();
            return;
        }
        if (confirm.isEmpty()) {
            passwordConfirm.setError("Please confirm your password");
            passwordConfirm.requestFocus();
            return;
        }
        if (!pass.equals(confirm)) {
            passwordConfirm.setError("Passwords do not match");
            passwordConfirm.requestFocus();
            return;
        }


        userInfo.saveUser(nameText, contactText, emailText, pass);
        Toast.makeText(this, "Account created successfully. Please login.",
                Toast.LENGTH_LONG).show();

        Intent i = new Intent(Signup_Page.this, Login_Page.class);
        i.putExtra("email", emailText);
        startActivity(i);
        finish();
    }
}
