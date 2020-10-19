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

    private Button mCancelButton;
    private EditText mEmailLogin, mPasswordLogin;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        TextView mForgetPassword = (TextView) findViewById(R.id.forgetPassword);
        mForgetPassword.setPaintFlags(mForgetPassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        mAuth = FirebaseAuth.getInstance();

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null){
                    Intent intent = new Intent(LoginActivity.this, StartActivity.class);
                    Toast.makeText(LoginActivity.this,"Zalogowano poprawnie",Toast.LENGTH_LONG).show();
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
        Button mLoginButton = (Button) findViewById(R.id.loginButton);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailLogin.getText().toString();
                String password = mPasswordLogin.getText().toString();
                if(email.equals("") || password.equals(""))
                    Toast.makeText(LoginActivity.this,"Niepoprawny email lub hasło.", Toast.LENGTH_LONG).show();
                else {
                    mAuth.signInWithEmailAndPassword(email,password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                //Toast.makeText(LoginActivity.this, "Błąd logowania", Toast.LENGTH_SHORT).show();
                                try
                                {
                                    throw task.getException();
                                }
                                catch (Exception e) {
                                    System.out.println(e.getMessage().toString());
                                   // Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                    switch (e.getMessage()) {
                                        case "There is no user record corresponding to this identifier. The user may have been deleted.":
                                            Toast.makeText(LoginActivity.this, getResources().getString(R.string.notFoundEmail), Toast.LENGTH_SHORT).show();
                                            break;
                                        case "The password is invalid or the user does not have a password.":
                                            Toast.makeText(LoginActivity.this, getResources().getString(R.string.wrongPassword), Toast.LENGTH_SHORT).show();
                                            break;
                                        case "A network error (such as timeout, interrupted connection or unreachable host) has occurred.":
                                            Toast.makeText(LoginActivity.this, getResources().getString(R.string.networkError), Toast.LENGTH_SHORT).show();
                                            break;
                                    }
                                }
                            }
                        }
                    });
                }
            }
        });

        mCancelButton = (Button) findViewById(R.id.cancelButton);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(v.getContext(),WelcomeActivity.class);

                mCancelButton.setText("Zarejestruj");
                Intent intent = new Intent(v.getContext(),RegisterActivity.class);

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