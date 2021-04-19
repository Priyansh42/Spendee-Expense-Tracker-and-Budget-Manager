package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class resetpassword extends AppCompatActivity {
    private EditText passwordEmail;
    private Button resetpassword;
    private FirebaseAuth firebaseAuth;

    public resetpassword() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpassword);

        passwordEmail=(EditText)findViewById(R.id.pass_email);
        resetpassword=(Button)findViewById(R.id.reset_pass);
        firebaseAuth=FirebaseAuth.getInstance();

        resetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String useremail=passwordEmail.getText().toString().trim();

                if(useremail.equals(""))
                {
                  passwordEmail.setError("Email Required...",null);
                  return;
                }
                else
                {
                    firebaseAuth.sendPasswordResetEmail(useremail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(resetpassword.this,"Email sent successfully..",Toast.LENGTH_LONG).show();
                                finish();
                                startActivity(new Intent(resetpassword.this,home_screen.class));
                            }
                            else
                            {
                                Toast.makeText(resetpassword.this,"Error sending email..",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}