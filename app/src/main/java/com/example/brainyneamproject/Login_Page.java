package com.example.brainyneamproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

public class Login_Page extends AppCompatActivity {

    EditText email, password;
    Button btnSignIn;
    TextView forgotPassword, createAccount;
    UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_login_page);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userInfo = new UserInfo(this);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password_toggle);
        btnSignIn = findViewById(R.id.btnSignIn);
        forgotPassword = findViewById(R.id.forgotPassword);
        createAccount = findViewById(R.id.createAccount);

        //forget/sign up password ke baad email automatic display hoga
        String prefillEmail = getIntent().getStringExtra("email");
        if (prefillEmail != null) {
            email.setText(prefillEmail);
            password.requestFocus();
        }

        btnSignIn.setOnClickListener(view -> attemptLogin());

        forgotPassword.setOnClickListener(view -> {
            Intent i = new Intent(Login_Page.this, Forget_Page.class);
            i.putExtra("email", email.getText().toString().trim());
            startActivity(i);
        });

        createAccount.setOnClickListener(view ->
                startActivity(new Intent(Login_Page.this, Signup_Page.class)));
    }

    private void attemptLogin() {
        String emailText = email.getText().toString().trim();
        String passText = password.getText().toString();

        //main velidation code
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
        if (passText.isEmpty()) {
            password.setError("Password is required");
            password.requestFocus();
            return;
        }
        if (passText.length() < 6) {
            password.setError("Password must be at least 6 characters");
            password.requestFocus();
            return;
        }

        //user ke credantial check
        if (!userInfo.userExists(emailText)) {
            email.setError("No account found with this email");
            email.requestFocus();
            return;
        }

        if (userInfo.checkLogin(emailText, passText)) {
            Toast.makeText(this,
                    "Welcome " + userInfo.getName(emailText), Toast.LENGTH_SHORT).show();

            Intent home = new Intent(Login_Page.this, MainActivity.class);
            home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(home);
            finish();
        } else {
            password.setError("Incorrect password");
            password.requestFocus();
        }
    }
}
