package com.example.aust_classroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivityTeacher extends AppCompatActivity {

    private FirebaseAuth mAuth;
    // private FirebaseAuth.AuthStateListener mAuthListener;

    TextInputLayout teacherLoginEmail;
    TextInputLayout teacherloginPass;

    Button signup10;
    Button login10;
    Button forgetPass0;

    RelativeLayout rel_lay_11, rel_lay_21;

    RelativeLayout relativeLayout;
    AnimationDrawable animationDrawable;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            rel_lay_11.setVisibility(View.VISIBLE);
            rel_lay_21.setVisibility(View.VISIBLE);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_teacher);

        rel_lay_11 = (RelativeLayout) findViewById(R.id.rel_lay_11);
        rel_lay_21 = (RelativeLayout) findViewById(R.id.rel_lay_21);

        relativeLayout = findViewById(R.id.relative_layout_0);
        animationDrawable = (AnimationDrawable) relativeLayout.getBackground();

        // setting enter fade animation duration to 5 seconds
        animationDrawable.setEnterFadeDuration(5000);

        // setting exit fade animation duration to 2 seconds
        animationDrawable.setExitFadeDuration(2000);

        handler.postDelayed(runnable, 2000);

        mAuth = FirebaseAuth.getInstance();


        signup10 = findViewById(R.id.signup10);

        login10 = findViewById(R.id.login10);

        forgetPass0 = findViewById(R.id.forgotPass0);

        teacherLoginEmail = findViewById(R.id.teacherLoginEmail);
        teacherloginPass = findViewById(R.id.teacherloginPass);

        forgetPass0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (haveNetwork()) {
                    Intent forget = new Intent(LoginActivityTeacher.this, ForgetPassActivity.class);
                    startActivity(forget);
                    finish();
                }
                else if (!haveNetwork()){
                    Toast.makeText(LoginActivityTeacher.this, "Network connection unavailable!!\nCheck your 'internet' connection and try again", Toast.LENGTH_LONG).show();
                }
            }
        });

        signup10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (haveNetwork()) {
                    Intent signup = new Intent(LoginActivityTeacher.this, SignupActivityTeacher.class);
                    startActivity(signup);
                    finish();
                }
                else if (!haveNetwork()){
                    Toast.makeText(LoginActivityTeacher.this, "Network connection unavailable!!\nCheck your 'internet' connection and try again", Toast.LENGTH_LONG).show();
                }
            }
        });

        login10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (haveNetwork()) {
                    Toast.makeText(LoginActivityTeacher.this,"Please wait...",Toast.LENGTH_SHORT).show();
                    final String teacherEmail = teacherLoginEmail.getEditText().getText().toString().trim();
                    final String teacherPass = teacherloginPass.getEditText().getText().toString().trim();
                    if (teacherEmail.isEmpty()) {
                        teacherLoginEmail.setError("Fields can't be empty!");
                        return;
                    }
                    else if (teacherPass.isEmpty()) {
                        teacherloginPass.setError("Fields can't be empty!");
                        return;
                    } else {
                        mAuth.signInWithEmailAndPassword(teacherEmail, teacherPass).addOnCompleteListener(LoginActivityTeacher.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(LoginActivityTeacher.this, "Login error occurred! Please Check & Try again.", Toast.LENGTH_LONG).show();
                                } else {
                                    if (mAuth.getCurrentUser().isEmailVerified()) {

                                        Intent intent = new Intent(LoginActivityTeacher.this, HomeActivity2.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else {
                                        Toast.makeText(LoginActivityTeacher.this, "Please verify your 'email address' and try again", Toast.LENGTH_LONG).show();
                                        teacherLoginEmail.getEditText().setText("");
                                        teacherloginPass.getEditText().setText("");
                                    }

                                }
                            }
                        });
                    }
                }
                else if (!haveNetwork()){
                    Toast.makeText(LoginActivityTeacher.this, "Network connection unavailable!!\nCheck your 'internet' connection and try again", Toast.LENGTH_LONG).show();
                }
                /*mAuthListener = new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        if (firebaseAuth.getCurrentUser() != null)
                        {
                            Intent intent = new Intent(LoginActivityTeacher.this, HomeActivity2.class);
                            startActivity(intent);
                            finish();
                            return;
                        }
                    }
                };*/


            }
        });

    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (animationDrawable != null && !animationDrawable.isRunning()) {
            // start the animation
            animationDrawable.start();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (animationDrawable != null && animationDrawable.isRunning()) {
            // stop the animation
            animationDrawable.stop();
        }
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
