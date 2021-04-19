package com.example.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class first_home_page extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;

    private DashboardFragment dashboardFragment;
    private IncomeFragment incomeFragment;
    private ExpenseFragment expenseFragment;

    private FirebaseAuth mAuth;

    ArrayList<HashMap<String,Object>> items;
    PackageManager pm ;
    List<PackageInfo> packs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_home_page);

        Toolbar toolbar=findViewById(R.id.my_toolbar);
        toolbar.setTitle("Spendee");
        toolbar.setTitleTextColor(Color.BLACK);

        setSupportActionBar(toolbar);

        bottomNavigationView=findViewById(R.id.bottomNavigationbar);
        frameLayout=findViewById(R.id.main_frame);
        DrawerLayout drawerLayout=findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(
                this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView=findViewById(R.id.naView);
        navigationView.setNavigationItemSelectedListener(this);

        dashboardFragment=new DashboardFragment();
        incomeFragment=new IncomeFragment();
        expenseFragment=new ExpenseFragment();
        setFragment(dashboardFragment);


        mAuth=FirebaseAuth.getInstance();
        FirebaseUser mUser=mAuth.getCurrentUser();
        String uid = mUser.getUid();
        DatabaseReference mUserInfoDatabase = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(uid);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userEmail = dataSnapshot.child("Email").getValue().toString();
                TextView email=findViewById(R.id.user_email);
                email.setText(userEmail);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        mUserInfoDatabase.addListenerForSingleValueEvent(valueEventListener);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.dashboard:
                        setFragment(dashboardFragment);
                        bottomNavigationView.setItemBackgroundResource(R.color.dashboard_color);
                        return true;

                    case R.id.income:
                        setFragment(incomeFragment);
                        bottomNavigationView.setItemBackgroundResource(R.color.income_color);
                        return true;

                    case R.id.income_search:
                        Intent intent=new Intent(getApplicationContext(),searchdata.class);
                        startActivity(intent);
                        bottomNavigationView.setItemBackgroundResource(R.color.expense_color);
                        return true;

                    case R.id.expense:
                        setFragment(expenseFragment);
                        bottomNavigationView.setItemBackgroundResource(R.color.expense_color);
                        return true;

                    case R.id.expense_search:
                        Intent intent1=new Intent(getApplicationContext(),searchdata2.class);
                        startActivity(intent1);
                        bottomNavigationView.setItemBackgroundResource(R.color.expense_color);
                        return true;

                    default:
                        return false;

                }

            }
        });

        items =new  ArrayList<HashMap<String,Object>>();
        pm = getPackageManager();
        packs = pm.getInstalledPackages(0);
        for (PackageInfo pi : packs)
        {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("appName", pi.applicationInfo.loadLabel(pm));
            map.put("packageName", pi.packageName);
            items.add(map);
        }

    }

    private void setFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed(){
        DrawerLayout drawerLayout=findViewById(R.id.drawer_layout);
        if(drawerLayout.isDrawerOpen(GravityCompat.END)){
            drawerLayout.closeDrawer(GravityCompat.END);
        }
        else{
            super.onBackPressed();
        }

    }
    public void displaySelectedListener(int itemId){
        Fragment fragment=null;
        switch(itemId){
            case R.id.profile:
                Intent profile_intent=new Intent(getApplicationContext(),Profile.class);
                startActivity(profile_intent);
                break;

            case R.id.dashboard:
                fragment=new DashboardFragment();
                break;

            case R.id.income:
                fragment=new IncomeFragment();
                break;

            case R.id.search_income:
                Intent intent_inc=new Intent(getApplicationContext(),searchdata.class);
                startActivity(intent_inc);
                break;

            case R.id.expense:
                fragment=new ExpenseFragment();
                break;

            case R.id.search_expense:
                Intent intent_exp=new Intent(getApplicationContext(),searchdata2.class);
                startActivity(intent_exp);
                break;

            case R.id.income_tax_emi:
                Intent intent3=new Intent(getApplicationContext(),inc_emi.class);
                startActivity(intent3);
                break;

            case R.id.calculator:
                int d=0;
                if(items.size()>=1)
                {
                    int j=0;
                    for(j=0;j<items.size();j++){
                        String AppName = (String) items.get(j).get("appName");
                        if(AppName.matches("Calculator"))
                        {
                            d=j;
                            break;
                        }
                    }
                    String packageName = (String) items.get(d).get("packageName");

                    Intent i = pm.getLaunchIntentForPackage(packageName);
                    if (i != null)
                    {
                        Toast.makeText(getApplicationContext(),"Opening Calculator..",Toast.LENGTH_SHORT).show();
                        startActivity(i);
                    }
                    else
                    {
                        Intent intent=new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.sec.android.app.popupcalculator"));
                        startActivity(intent);
                    }
                }
                else
                {
                    Intent intent1=new Intent(Intent.ACTION_VIEW);
                    intent1.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.sec.android.app.popupcalculator"));
                    startActivity(intent1);
                }
                break;

            case R.id.feedback:
                Intent intent2=new Intent(getApplicationContext(),feedback.class);
                startActivity(intent2);
                break;

            case R.id.about:
                Intent intent4=new Intent(getApplicationContext(),about.class);
                startActivity(intent4);
                break;

            case R.id.logout:
                AlertDialog.Builder builder=new AlertDialog.Builder(first_home_page.this);
                builder.setTitle("Logout");
                builder.setMessage("Do you really want to Logout?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                        startActivity(new Intent(getApplicationContext(),home_screen.class));
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
                break;
        }
        if(fragment!=null){
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.main_frame,fragment);
            ft.commit();
        }

        DrawerLayout drawerLayout=findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        displaySelectedListener(item.getItemId());
        return true;
    }
}