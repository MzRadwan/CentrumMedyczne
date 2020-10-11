package com.example.centrummedyczne;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private Button mLoginButton, mCancelButton;
    private EditText mEmailLogin, mPasswordLogin;
    private TextView mForgetPassword;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mForgetPassword = (TextView) findViewById(R.id.forgetPassword);
        mForgetPassword.setPaintFlags(mForgetPassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        mAuth = FirebaseAuth.getInstance();

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null){
                    Intent intent = new Intent(LoginActivity.this, PatientAccountActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }

            }
        };

        final FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

        mForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentUser == null){
                    Intent intent = new Intent (LoginActivity.this, ResetPassword.class);
                    startActivity(intent);
                }
            }
        });

        mEmailLogin = (EditText) findViewById(R.id.emailEditText);
        mPasswordLogin = (EditText) findViewById(R.id.passwordEditText);
        mLoginButton = (Button) findViewById(R.id.loginButton);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailLogin.getText().toString();
                String password = mPasswordLogin.getText().toString();
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "Błąd logowania", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        mCancelButton = (Button) findViewById(R.id.cancelButton);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),WelcomeActivity.class);
                startActivity(intent);
            }
        });
    }

    //Change UI according to user data.
    public void  updateUI(FirebaseUser account){
        if(account != null){
            Toast.makeText(this,"Zalogowano poprawnie",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,PatientAccountActivity.class));
        }else {
            //Toast.makeText(this,"U Didnt signed in",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthListener);
    }

}