package com.example.myapplication;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Finalinc extends AppCompatActivity {
    TextView T1,T2,T3,T4,T5,T6,T7;
    Button back,backmain;
    String base,LTA,HRA,SA, A,B,C;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_finalinc);
        back=(Button)findViewById(R.id.goback);
        backmain=(Button)findViewById(R.id.backtomain);
        backmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Finalinc.this,first_home_page.class);
                startActivity(i);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Finalinc.this,income2.class);
                startActivity(i);
            }
        });
        T1=(TextView)findViewById(R.id.t1);

        T2=(TextView)findViewById(R.id.t2);

        T3=(TextView)findViewById(R.id.t3);

        T4=(TextView)findViewById(R.id.t4);

        T6=(TextView)findViewById(R.id.t6);

        T7=(TextView)findViewById(R.id.t7);
        Intent i=getIntent();
        HRA=i.getStringExtra("hra");
        LTA=i.getStringExtra("lta");
        base=i.getStringExtra("base");
        SA=i.getStringExtra("sa");
        A=i.getStringExtra("80A");
        B=i.getStringExtra("80B");
        C=i.getStringExtra("80C");
        int baseInt=Integer.parseInt(base);
        double HRAdouble=Double.parseDouble(HRA);
        double LTAdouble=Double.parseDouble(LTA);
        double SAdouble=Double.parseDouble(SA);
        double Adouble=Double.parseDouble(A);
        double Bdouble=Double.parseDouble(B);
        double Cdouble=Double.parseDouble(C);
        double totalIncome=baseInt+HRAdouble+LTAdouble+SAdouble;
        T1.setText(base);
        int forT2=(int)HRAdouble+(int)LTAdouble+(int)SAdouble;
        T2.setText(""+forT2);
        int totalG=baseInt+(int)HRAdouble+(int)LTAdouble+(int)SAdouble;
        T3.setText(""+totalG);
        double ded= (int)Adouble+(int)Bdouble+(int)Cdouble;
        T4.setText(""+ded);

        double grossdeduction=ded+50000;
        int totaltaxable=(int)totalIncome-(int)grossdeduction;
        T6.setText(""+totaltaxable);
        double tax=0;
        if(totaltaxable<250000){
            tax=0;
        }else if(totaltaxable>250000 && totaltaxable<500000){
            tax=0.05*totaltaxable;

        }
        else if(totaltaxable>500000 && totaltaxable<1000000){
            tax=0.20*totaltaxable;
        }
        else if(totaltaxable>1000000){
            tax=0.30*totaltaxable;
        }
        T7.setText("Total Tax: "+tax );




    }
}