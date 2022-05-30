package com.example.aust_classroom;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;




import android.view.View;

import androidx.annotation.NonNull;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextInsideCircleButton;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class HomeActivity2 extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    // private FirebaseDatabase firebaseDatabase;
    // private FirebaseUser user;

    private Button eduNotice;
    private Button genNotice;
    private Button iums;
    private Button cgpa;

    private BoomMenuButton bmb;

    ArrayList<Integer> imageIdlist;
    ArrayList<String> tittleList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        firebaseAuth = FirebaseAuth.getInstance();


        eduNotice = findViewById(R.id.educationalNoticeBtn);
        genNotice = findViewById(R.id.generalNoticeBtn);
        iums = findViewById(R.id.iumsBtn);
        cgpa = findViewById(R.id.logoutBtn);

        imageIdlist = new ArrayList<>();
        tittleList = new ArrayList<>();
        setInitialData();

        eduNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (haveNetwork()) {
                    Intent intent = new Intent(HomeActivity2.this, EduNoticeActivity.class);
                    startActivity(intent);
                }
                else if (!haveNetwork()){
                    Toast.makeText(HomeActivity2.this, "Network connection unavailable!!\nCheck your 'internet' connection and try again", Toast.LENGTH_LONG).show();
                }
            }
        });

        genNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (haveNetwork()) {
                    Intent intent = new Intent(HomeActivity2.this, GenNoticeActivity.class);
                    startActivity(intent);
                }
                else if (!haveNetwork()){
                    Toast.makeText(HomeActivity2.this, "Network connection unavailable!!\nCheck your 'internet' connection and try again", Toast.LENGTH_LONG).show();
                }
            }
        });

        iums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (haveNetwork()) {
                    Intent intent = new Intent(HomeActivity2.this, IumsActivity.class);
                    startActivity(intent);
                }
                else if (!haveNetwork()){
                    Toast.makeText(HomeActivity2.this, "Network connection unavailable!!\nCheck your 'internet' connection and try again", Toast.LENGTH_LONG).show();
                }
            }
        });

        cgpa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (haveNetwork()) {
                    startActivity(new Intent(HomeActivity2.this, CalculateGPAActivity.class));
                }
                else if (!haveNetwork()){
                    Toast.makeText(HomeActivity2.this, "Network connection unavailable!!\nCheck your 'internet' connection and try again", Toast.LENGTH_LONG).show();
                }
            }
        });


        bmb = findViewById(R.id.bmb);

        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            TextOutsideCircleButton.Builder builder = new TextOutsideCircleButton.Builder()
                    .normalImageRes(imageIdlist.get(i))
                    .normalText(tittleList.get(i))
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            // When the boom-button corresponding this builder is clicked.
                            if (index == 0)
                            {
                                Toast.makeText(HomeActivity2.this, "Tumi Home ei Aso -_- , baki gula tipo", Toast.LENGTH_LONG).show();
                            }
                            if (index == 1)
                            {
                                if (haveNetwork()) {
                                    Intent profile = new Intent(HomeActivity2.this, UserProfileActivity.class);
                                    startActivity(profile);
                                }
                                else if (!haveNetwork()){
                                    Toast.makeText(HomeActivity2.this, "Network connection unavailable!!\nCheck your 'internet' connection and try again", Toast.LENGTH_LONG).show();
                                }
                            }
                            if (index == 2)
                            {
                                Toast.makeText(HomeActivity2.this, "Settings will open", Toast.LENGTH_LONG).show();
                            }
                            if (index == 3)
                            {
                                if (haveNetwork()) {
                                    firebaseAuth.signOut();
                                    finish();
                                    startActivity(new Intent(HomeActivity2.this, LoginActivity.class));
                                }
                                else if (!haveNetwork()){
                                    Toast.makeText(HomeActivity2.this, "Network connection unavailable!!\nCheck your 'internet' connection and try again", Toast.LENGTH_LONG).show();
                                }
                            }

                        }

                    });
            bmb.addBuilder(builder);

        }

    }

    private void setInitialData()
    {
        imageIdlist.add(R.drawable.home3);
        imageIdlist.add(R.drawable.userprofile);
        imageIdlist.add(R.drawable.settings2);
        imageIdlist.add(R.drawable.logout);

        tittleList.add("HomePage");
        tittleList.add("User Profile");
        tittleList.add("Settings");
        tittleList.add("Logout");
    }

    private boolean haveNetwork(){
        boolean have_WIFI = false;
        boolean have_MobileData = false;

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();

        for (NetworkInfo info : networkInfos)
        {
            if (info.getTypeName().equalsIgnoreCase("WIFI"))
                if (info.isConnected())
                {
                    have_WIFI = true;
                }
            if (info.getTypeName().equalsIgnoreCase("MOBILE"))
                if (info.isConnected())
                {
                    have_MobileData = true;
                }
        }
        return have_WIFI || have_MobileData;
    }
}