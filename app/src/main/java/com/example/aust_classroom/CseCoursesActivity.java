package com.example.aust_classroom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;

import java.util.ArrayList;

public class CseCoursesActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    private Button softDevBtn;
    private Button numericalBtn;
    private Button numericalLabBtn;
    private Button algorithmsBtn;
    private Button algorithmsLabBtn;
    private Button deptBtn;
    private Button deptLabBtn;
    private Button compArchBtn;
    private Button assemblyLabBtn;
    private Button math4Btn;

    private BoomMenuButton bmb;
    ArrayList<Integer> imageIdlist;
    ArrayList<String> tittleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cse_courses);

        firebaseAuth = FirebaseAuth.getInstance();

        softDevBtn = findViewById(R.id.softDev3Btn);
        numericalBtn = findViewById(R.id.numericalBtn);
        numericalLabBtn = findViewById(R.id.numericalLabBtn);
        algorithmsBtn = findViewById(R.id.algorithmsBtn);
        algorithmsLabBtn = findViewById(R.id.algorithmsLabBtn);
        deptBtn = findViewById(R.id.deptBtn);
        deptLabBtn = findViewById(R.id.deptLabBtn);
        compArchBtn = findViewById(R.id.compArchBtn);
        assemblyLabBtn = findViewById(R.id.assemblyLabBtn);
        math4Btn = findViewById(R.id.math4Btn);


        imageIdlist = new ArrayList<>();
        tittleList = new ArrayList<>();
        setInitialData();


        bmb = findViewById(R.id.bmb);

        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            TextOutsideCircleButton.Builder builder = new TextOutsideCircleButton.Builder()
                    .normalImageRes(imageIdlist.get(i))
                    .normalText(tittleList.get(i))
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            // When the boom-button corresponding this builder is clicked.
                            if (index == 0) {
                                if (haveNetwork()) {
                                    Intent home = new Intent(CseCoursesActivity.this, HomeActivity2.class);
                                    startActivity(home);
                                    finish();
                                }
                                else if (!haveNetwork()){
                                    Toast.makeText(CseCoursesActivity.this, "Network connection unavailable!!\nCheck your 'internet' connection and try again", Toast.LENGTH_LONG).show();
                                }
                            }
                            if (index == 1) {
                                if (haveNetwork()) {
                                    Intent profile = new Intent(CseCoursesActivity.this, UserProfileActivity.class);
                                    startActivity(profile);
                                }
                                else if (!haveNetwork()){
                                    Toast.makeText(CseCoursesActivity.this, "Network connection unavailable!!\nCheck your 'internet' connection and try again", Toast.LENGTH_LONG).show();
                                }
                            }
                            if (index == 2) {
                                Toast.makeText(CseCoursesActivity.this, "Settings will open", Toast.LENGTH_LONG).show();
                            }
                            if (index == 3) {
                                if (haveNetwork()) {
                                    firebaseAuth.signOut();
                                    finish();
                                    startActivity(new Intent(CseCoursesActivity.this, LoginActivity.class));
                                }
                                else if (!haveNetwork()){
                                    Toast.makeText(CseCoursesActivity.this, "Network connection unavailable!!\nCheck your 'internet' connection and try again", Toast.LENGTH_LONG).show();
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
