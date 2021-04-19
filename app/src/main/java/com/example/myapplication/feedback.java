package com.example.myapplication;

import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import java.util.HashMap;

public class feedback extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mFeedbackDatabase;

    String s1;

    RatingBar ratingBar;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        mAuth= FirebaseAuth.getInstance();
        FirebaseUser mUser=mAuth.getCurrentUser();
        if(mAuth!=null) {
            String uid = mUser.getUid();

            mFeedbackDatabase = FirebaseDatabase.getInstance().getReference().child("Feedback").child(uid);
        }

        ratingBar=findViewById(R.id.rating_bar);
        btnSubmit=findViewById(R.id.rating_btn);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s1=String.valueOf(ratingBar.getRating());
                HashMap hashMap=new HashMap();
                hashMap.put("Rating",s1);

                if(ratingBar.getRating()!=0.0)
                {
                    String id = mFeedbackDatabase.push().getKey();
                    mFeedbackDatabase.child("Feedback").updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            Toast.makeText(getApplicationContext(),"Thanks for Rating us..",Toast.LENGTH_SHORT).show();
                            ratingBar.setRating(0);
                        }
                    });
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Please Rate us!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        ImageView back_arrow=findViewById(R.id.back);
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}