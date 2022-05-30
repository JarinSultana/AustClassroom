package com.example.aust_classroom;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;

import java.util.ArrayList;

public class CseY2S2CgActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    EditText softDevMarks;
    EditText numericalMarks;
    EditText numericalLabMarks;
    EditText algorithmsMarks;
    EditText algorithmsLabMarks;
    EditText deptMarks;
    EditText deptLabMarks;
    EditText computerArchMarks;
    EditText assemblyLabMarks;
    EditText math4Marks;
    Button calculateCgBtn;
    TextView calculatedCgShow;

    float credit075 = 0.75f;
    float credit15 = 1.5f;
    float credit3 = 3.0f;

    float val01;
    float val02;
    float val03;
    float val04;
    float val05;
    float val06;
    float val07;
    float val08;
    float val09;
    float val010;


    private BoomMenuButton bmb;
    ArrayList<Integer> imageIdlist;
    ArrayList<String> tittleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scroll_cse_y2_s2_cg_calculation);

        firebaseAuth = FirebaseAuth.getInstance();

        softDevMarks = findViewById(R.id.editSdLab);
        numericalMarks = findViewById(R.id.editNumMethods);
        numericalLabMarks = findViewById(R.id.editNumMethodsLab);
        algorithmsMarks = findViewById(R.id.editAlgorithms);
        algorithmsLabMarks = findViewById(R.id.editAlgorithmsLab);
        deptMarks = findViewById(R.id.editDeptMarks);
        deptLabMarks = findViewById(R.id.editDeptMarksLab);
        computerArchMarks = findViewById(R.id.editComputerArchitechMarks);
        assemblyLabMarks = findViewById(R.id.editAssemblyLabMarks);
        math4Marks = findViewById(R.id.editMath4Marks);
        calculateCgBtn = findViewById(R.id.calculateCgBtn);
        calculatedCgShow = findViewById(R.id.calculatedGPANum);

        /*String sdmarks = softDevMarks.getText().toString().trim();
        String numerical = numericalMarks.getText().toString().trim();
        String numericalLab = numericalLabMarks.getText().toString().trim();
        String algorithms = algorithmsMarks.getText().toString().trim();
        String algorithmsLab = algorithmsLabMarks.getText().toString().trim();
        String dept = deptMarks.getText().toString().trim();
        String deptLab = deptLabMarks.getText().toString().trim();
        String computerArchitecture = computerArchMarks.getText().toString().trim();
        String assemblyLab = assemblyLabMarks.getText().toString().trim();
        String mathematics4 = math4Marks.getText().toString().trim();*/

        calculateCgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculateCGPA();
            }
        });




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
                                    Intent home = new Intent(CseY2S2CgActivity.this, HomeActivity2.class);
                                    startActivity(home);
                                    finish();
                                }
                                else if (!haveNetwork()){
                                    Toast.makeText(CseY2S2CgActivity.this, "Network connection unavailable!!\nCheck your 'internet' connection and try again", Toast.LENGTH_LONG).show();
                                }
                            }
                            if (index == 1) {
                                if (haveNetwork()) {
                                    Intent profile = new Intent(CseY2S2CgActivity.this, UserProfileActivity.class);
                                    startActivity(profile);
                                }
                                else if (!haveNetwork()){
                                    Toast.makeText(CseY2S2CgActivity.this, "Network connection unavailable!!\nCheck your 'internet' connection and try again", Toast.LENGTH_LONG).show();
                                }
                            }
                            if (index == 2) {
                                Toast.makeText(CseY2S2CgActivity.this, "Settings will open", Toast.LENGTH_LONG).show();
                            }
                            if (index == 3) {
                                if (haveNetwork()) {
                                    firebaseAuth.signOut();
                                    finish();
                                    startActivity(new Intent(CseY2S2CgActivity.this, LoginActivity.class));
                                }
                                else if (!haveNetwork()){
                                    Toast.makeText(CseY2S2CgActivity.this, "Network connection unavailable!!\nCheck your 'internet' connection and try again", Toast.LENGTH_LONG).show();
                                }
                            }

                        }

                    });
            bmb.addBuilder(builder);
        }
    }

    private void calculateCGPA() {
        String sdmarks = softDevMarks.getText().toString().trim();
        String numerical = numericalMarks.getText().toString().trim();
        String numericalLab = numericalLabMarks.getText().toString().trim();
        String algorithms = algorithmsMarks.getText().toString().trim();
        String algorithmsLab = algorithmsLabMarks.getText().toString().trim();
        String dept = deptMarks.getText().toString().trim();
        String deptLab = deptLabMarks.getText().toString().trim();
        String computerArchitecture = computerArchMarks.getText().toString().trim();
        String assemblyLab = assemblyLabMarks.getText().toString().trim();
        String mathematics4 = math4Marks.getText().toString().trim();

        try {
            if (sdmarks != null && numerical != null && numericalLab != null && algorithms != null && algorithmsLab != null && dept != null && deptLab != null && computerArchitecture != null && assemblyLab != null && mathematics4 != null) {
                float val1 = Float.parseFloat(sdmarks);
                float val2 = Float.parseFloat(numerical);
                float val3 = Float.parseFloat(numericalLab);
                float val4 = Float.parseFloat(algorithms);
                float val5 = Float.parseFloat(algorithmsLab);
                float val6 = Float.parseFloat(dept);
                float val7 = Float.parseFloat(deptLab);
                float val8 = Float.parseFloat(computerArchitecture);
                float val9 = Float.parseFloat(assemblyLab);
                float val10 = Float.parseFloat(mathematics4);

                if ((val1 >= 0 && val1 <= 100) && (val2 >= 0 && val2 <= 100) && (val3 >= 0 && val3 <= 100) && (val4 >= 0 && val4 <= 100) && (val5 >= 0 && val5 <= 100) && (val6 >= 0 && val6 <= 100) && (val7 >= 0 && val7 <= 100) && (val8 >= 0 && val8 <= 100) && (val9 >= 0 && val9 <= 100) && (val10 >= 0 && val10 <= 100)) {
                    if (val1 <= 100 && val1 >= 80) {
                        val01 = 4.00f;
                    } else if (val1 <= 79 && val1 >= 75) {
                        val01 = 3.75f;
                    } else if (val1 <= 74 && val1 >= 70) {
                        val01 = 3.50f;
                    } else if (val1 <= 69 && val1 >= 65) {
                        val01 = 3.25f;
                    } else if (val1 <= 64 && val1 >= 60) {
                        val01 = 3.00f;
                    } else if (val1 <= 59 && val1 >= 55) {
                        val01 = 2.75f;
                    } else if (val1 <= 54 && val1 >= 50) {
                        val01 = 2.50f;
                    } else if (val1 <= 49 && val1 >= 45) {
                        val01 = 2.25f;
                    } else if (val1 <= 44 && val1 >= 40) {
                        val01 = 2.00f;
                    } else if (val1 <= 39 && val1 >= 0) {
                        val01 = 0.00f;
                    }


                    if (val2 <= 100 && val2 >= 80) {
                        val02 = 4.00f;
                    } else if (val2 <= 79 && val2 >= 75) {
                        val02 = 3.75f;
                    } else if (val2 <= 74 && val2 >= 70) {
                        val02 = 3.50f;
                    } else if (val2 <= 69 && val2 >= 65) {
                        val02 = 3.25f;
                    } else if (val2 <= 64 && val2 >= 60) {
                        val02 = 3.00f;
                    } else if (val2 <= 59 && val2 >= 55) {
                        val02 = 2.75f;
                    } else if (val2 <= 54 && val2 >= 50) {
                        val02 = 2.50f;
                    } else if (val2 <= 49 && val2 >= 45) {
                        val02 = 2.25f;
                    } else if (val2 <= 44 && val2 >= 40) {
                        val02 = 2.00f;
                    } else if (val2 <= 39 && val2 >= 0) {
                        val02 = 0.00f;
                    }


                    if (val3 <= 100 && val3 >= 80) {
                        val03 = 4.00f;
                    } else if (val3 <= 79 && val3 >= 75) {
                        val03 = 3.75f;
                    } else if (val3 <= 74 && val3 >= 70) {
                        val03 = 3.50f;
                    } else if (val3 <= 69 && val3 >= 65) {
                        val03 = 3.25f;
                    } else if (val3 <= 64 && val3 >= 60) {
                        val03 = 3.00f;
                    } else if (val3 <= 59 && val3 >= 55) {
                        val03 = 2.75f;
                    } else if (val3 <= 54 && val3 >= 50) {
                        val03 = 2.50f;
                    } else if (val3 <= 49 && val3 >= 45) {
                        val03 = 2.25f;
                    } else if (val3 <= 44 && val3 >= 40) {
                        val03 = 2.00f;
                    } else if (val3 <= 39 && val3 >= 0) {
                        val03 = 0.00f;
                    }


                    if (val4 <= 100 && val4 >= 80) {
                        val04 = 4.00f;
                    } else if (val4 <= 79 && val4 >= 75) {
                        val04 = 3.75f;
                    } else if (val4 <= 74 && val4 >= 70) {
                        val04 = 3.50f;
                    } else if (val4 <= 69 && val4 >= 65) {
                        val04 = 3.25f;
                    } else if (val4 <= 64 && val4 >= 60) {
                        val04 = 3.00f;
                    } else if (val4 <= 59 && val4 >= 55) {
                        val04 = 2.75f;
                    } else if (val4 <= 54 && val4 >= 50) {
                        val04 = 2.50f;
                    } else if (val4 <= 49 && val4 >= 45) {
                        val04 = 2.25f;
                    } else if (val4 <= 44 && val4 >= 40) {
                        val04 = 2.00f;
                    } else if (val4 <= 39 && val4 >= 0) {
                        val04 = 0.00f;
                    }

                    if (val5 <= 100 && val5 >= 80) {
                        val05 = 4.00f;
                    } else if (val5 <= 79 && val5 >= 75) {
                        val05 = 3.75f;
                    } else if (val5 <= 74 && val5 >= 70) {
                        val05 = 3.50f;
                    } else if (val5 <= 69 && val5 >= 65) {
                        val05 = 3.25f;
                    } else if (val5 <= 64 && val5 >= 60) {
                        val05 = 3.00f;
                    } else if (val5 <= 59 && val5 >= 55) {
                        val05 = 2.75f;
                    } else if (val5 <= 54 && val5 >= 50) {
                        val05 = 2.50f;
                    } else if (val5 <= 49 && val5 >= 45) {
                        val05 = 2.25f;
                    } else if (val5 <= 44 && val5 >= 40) {
                        val05 = 2.00f;
                    } else if (val5 <= 39 && val5 >= 0) {
                        val05 = 0.00f;
                    }


                    if (val6 <= 100 && val6 >= 80) {
                        val06 = 4.00f;
                    } else if (val6 <= 79 && val6 >= 75) {
                        val06 = 3.75f;
                    } else if (val6 <= 74 && val6 >= 70) {
                        val06 = 3.50f;
                    } else if (val6 <= 69 && val6 >= 65) {
                        val06 = 3.25f;
                    } else if (val6 <= 64 && val6 >= 60) {
                        val06 = 3.00f;
                    } else if (val6 <= 59 && val6 >= 55) {
                        val06 = 2.75f;
                    } else if (val6 <= 54 && val6 >= 50) {
                        val06 = 2.50f;
                    } else if (val6 <= 49 && val6 >= 45) {
                        val06 = 2.25f;
                    } else if (val6 <= 44 && val6 >= 40) {
                        val06 = 2.00f;
                    } else if (val6 <= 39 && val6 >= 0) {
                        val06 = 0.00f;
                    }

                    if (val7 <= 100 && val7 >= 80) {
                        val07 = 4.00f;
                    } else if (val7 <= 79 && val7 >= 75) {
                        val07 = 3.75f;
                    } else if (val7 <= 74 && val7 >= 70) {
                        val07 = 3.50f;
                    } else if (val7 <= 69 && val7 >= 65) {
                        val07 = 3.25f;
                    } else if (val7 <= 64 && val7 >= 60) {
                        val07 = 3.00f;
                    } else if (val7 <= 59 && val7 >= 55) {
                        val07 = 2.75f;
                    } else if (val7 <= 54 && val7 >= 50) {
                        val07 = 2.50f;
                    } else if (val7 <= 49 && val7 >= 45) {
                        val07 = 2.25f;
                    } else if (val7 <= 44 && val7 >= 40) {
                        val07 = 2.00f;
                    } else if (val7 <= 39 && val7 >= 0) {
                        val07 = 0.00f;
                    }

                    if (val8 <= 100 && val8 >= 80) {
                        val08 = 4.00f;
                    } else if (val8 <= 79 && val8 >= 75) {
                        val08 = 3.75f;
                    } else if (val8 <= 74 && val8 >= 70) {
                        val08 = 3.50f;
                    } else if (val8 <= 69 && val8 >= 65) {
                        val08 = 3.25f;
                    } else if (val8 <= 64 && val8 >= 60) {
                        val08 = 3.00f;
                    } else if (val8 <= 59 && val8 >= 55) {
                        val08 = 2.75f;
                    } else if (val8 <= 54 && val8 >= 50) {
                        val08 = 2.50f;
                    } else if (val8 <= 49 && val8 >= 45) {
                        val08 = 2.25f;
                    } else if (val8 <= 44 && val8 >= 40) {
                        val08 = 2.00f;
                    } else if (val8 <= 39 && val8 >= 0) {
                        val08 = 0.00f;
                    }

                    if (val9 <= 100 && val9 >= 80) {
                        val09 = 4.00f;
                    } else if (val9 <= 79 && val9 >= 75) {
                        val09 = 3.75f;
                    } else if (val9 <= 74 && val9 >= 70) {
                        val09 = 3.50f;
                    } else if (val9 <= 69 && val9 >= 65) {
                        val09 = 3.25f;
                    } else if (val9 <= 64 && val9 >= 60) {
                        val09 = 3.00f;
                    } else if (val9 <= 59 && val9 >= 55) {
                        val09 = 2.75f;
                    } else if (val9 <= 54 && val9 >= 50) {
                        val09 = 2.50f;
                    } else if (val9 <= 49 && val9 >= 45) {
                        val09 = 2.25f;
                    } else if (val9 <= 44 && val9 >= 40) {
                        val09 = 2.00f;
                    } else if (val9 <= 39 && val9 >= 0) {
                        val09 = 0.00f;
                    }

                    if (val10 <= 100 && val10 >= 80) {
                        val010 = 4.00f;
                    } else if (val10 <= 79 && val10 >= 75) {
                        val010 = 3.75f;
                    } else if (val10 <= 74 && val10 >= 70) {
                        val010 = 3.50f;
                    } else if (val10 <= 69 && val10 >= 65) {
                        val010 = 3.25f;
                    } else if (val10 <= 64 && val10 >= 60) {
                        val010 = 3.00f;
                    } else if (val10 <= 59 && val10 >= 55) {
                        val010 = 2.75f;
                    } else if (val10 <= 54 && val10 >= 50) {
                        val010 = 2.50f;
                    } else if (val10 <= 49 && val10 >= 45) {
                        val010 = 2.25f;
                    } else if (val10 <= 44 && val10 >= 40) {
                        val010 = 2.00f;
                    } else if (val10 <= 39 && val10 >= 0) {
                        val010 = 0.00f;
                    }


                    float totalgpa = ((credit075 * val01) + (credit3 * val02) + (credit075 * val03) + (credit3 * val04) + (credit15 * val05) + (credit3 * val06) + (credit075 * val07) + (credit3 * val08) + (credit15 * val09) + (credit3 * val010)) / ((3 * credit075) + (2 * credit15) + (5 * credit3));
                    displayGpa(totalgpa);

                } else {
                    Toast.makeText(CseY2S2CgActivity.this, "Invalid Marks Entered..Please check & Enter the correct mark", Toast.LENGTH_SHORT).show();
                }

            } else if (sdmarks == null | numerical == null | numericalLab == null | algorithms == null | algorithmsLab == null | dept == null | deptLab == null | computerArchitecture == null | assemblyLab == null | mathematics4 == null) {
                Toast.makeText(CseY2S2CgActivity.this, "Field's can't be empty", Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void displayGpa(float totalgpa) {
        String Gpa = "";
        Gpa = totalgpa + "\n" + Gpa;
        calculatedCgShow.setText(Gpa);
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

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
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
