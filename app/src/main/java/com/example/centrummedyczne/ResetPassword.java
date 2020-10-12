package com.example.centrummedyczne;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {

    private EditText mEmail;
    private Button mSendResetEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

    }

    public void onClickSendReset(View view){

        mEmail = (EditText) findViewById(R.id.emailReset);
        mSendResetEmail = (Button) findViewById(R.id.sendResetEmail);
        String email = mEmail.getText().toString();
        if (email.equals(null)){
            Toast.makeText(ResetPassword.this, "Podaj adres email.", Toast.LENGTH_LONG).show();
        }
        else {
            FirebaseAuth auth = FirebaseAuth.getInstance();

            auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(ResetPassword.this, "Wysłano email.", Toast.LENGTH_LONG).show();
                        mSendResetEmail.setText("@string/send_again");
                    }
                    else {
                        Toast.makeText(ResetPassword.this, "Błąd wysyłania.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }
}