package com.example.brainyneamproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Forget_Page extends AppCompatActivity {

    private EditText email, passwordNew, passwordConfirm;
    private Button btnChangePassword;
    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_page);

        userInfo = new UserInfo(this);

        email = findViewById(R.id.email);
        passwordNew = findViewById(R.id.password_new);
        passwordConfirm = findViewById(R.id.password_confirm);
        btnChangePassword = findViewById(R.id.btnchangepassword);

        // Pre-fill email if user typed it on the Login page
        String prefillEmail = getIntent().getStringExtra("email");
        if (prefillEmail != null && !prefillEmail.isEmpty()) {
            email.setText(prefillEmail);
        }

        btnChangePassword.setOnClickListener(v -> attemptChangePassword());
    }

    private void attemptChangePassword() {
        String emailText = email.getText().toString().trim();
        String pass = passwordNew.getText().toString();
        String confirm = passwordConfirm.getText().toString();


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
        if (!userInfo.userExists(emailText)) {
            email.setError("No account found with this email");
            email.requestFocus();
            return;
        }


        if (pass.isEmpty()) {
            passwordNew.setError("New password is required");
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

        //update ke baad login page pe jayega
        userInfo.updatePassword(emailText, pass);
        Toast.makeText(this, "Password changed successfully. Please login.",
                Toast.LENGTH_LONG).show();

        Intent i = new Intent(Forget_Page.this, Login_Page.class);
        i.putExtra("email", emailText);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }
}
