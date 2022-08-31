package com.example.wordle;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {
    Button loginbtn;
    TextView signin, forgotpasswd, regacc;
    EditText emailke, passwd;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailke =findViewById(R.id.email);
        passwd= findViewById(R.id.password);
        forgotpasswd= findViewById(R.id.forgot_password);
        fAuth= FirebaseAuth.getInstance();
        loginbtn= findViewById(R.id.login_btn);
        signin = findViewById(R.id.sign_in);
        regacc = findViewById(R.id.Register);

        regacc.setOnClickListener(v -> {
            regacc.setTextColor(Color.RED);
            startActivity(new Intent(login.this,register.class));
        });

        if(fAuth.getCurrentUser() != null){
            finish();
            return;
        }

        //check email and password

        loginbtn.setOnClickListener(view -> {
            String email = emailke.getText().toString().trim();
            String password = passwd.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                emailke.setError("Email is Required.");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                passwd.setError("Password is Required.");
                return;
            }
            if (password.length() < 4) {
                passwd.setError("Minimum length is >=4.");
                return;
            }



            //authenticate user in firebase
            fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    //if correct
                    Toast.makeText(login.this, "LOGIN SUCCESSFUL", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), Preference.class));
                } else {
                    //incorrect
                    Toast.makeText(login.this, "WELCOME TO THE GAME OF WINNERS!!" , Toast.LENGTH_LONG).show();

                }
            });
            loginbtn.setOnClickListener(view1 -> startActivity(new Intent(getApplicationContext(), Preference.class)));

        });
        forgotpasswd.setOnClickListener(view -> {
            EditText resetMail= new EditText(view.getContext());
            AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(view.getContext());
            passwordResetDialog.setTitle("Reset Password?");
            passwordResetDialog.setMessage("Enter your email to receive reset link");
            passwordResetDialog.setView(resetMail);

            passwordResetDialog.setPositiveButton("Yes", (dialogInterface, i) -> {
                //extract the email and send reset link

                String mail = resetMail.getText().toString();
                fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(unused -> Toast.makeText(login.this, "Reset link sent to your email", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(login.this, "Error! Reset link is not sent" + e.getMessage(), Toast.LENGTH_SHORT).show());
            });
            passwordResetDialog.setNegativeButton("No", (dialogInterface, i) -> {
                //close the dialog
            });
            passwordResetDialog.create().show();

        });

    }
}