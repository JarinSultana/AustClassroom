package com.example.aust_classroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
   // private FirebaseAuth.AuthStateListener mAuthListener;

    private TextInputLayout studentLoginEmail;
    private TextInputLayout loginPass;

    CheckBox mCheckBoxRemember;
    SharedPreferences mPrefs;
    static final String PREFS_NAME = "PrefsFile";


    private Button signup;
    private Button login;
    private Button forgetPass;

    RelativeLayout rel_lay_1, rel_lay_2;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            rel_lay_1.setVisibility(View.VISIBLE);
            rel_lay_2.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        rel_lay_1 = (RelativeLayout) findViewById(R.id.rel_lay_1);
        rel_lay_2 = (RelativeLayout) findViewById(R.id.rel_lay_2);

        handler.postDelayed(runnable, 2000);

        mAuth = FirebaseAuth.getInstance();


        signup = findViewById(R.id.signup1);

        login = findViewById(R.id.login1);

        forgetPass = findViewById(R.id.forgotPass);




        studentLoginEmail = findViewById(R.id.studentLoginEmail);
        loginPass = findViewById(R.id.loginPass);

        mCheckBoxRemember = findViewById(R.id.studentLoginCheckBox);
        mPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        getPreferencesData();




        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (haveNetwork()) {
                    Intent forget = new Intent(LoginActivity.this, ForgetPassActivity.class);
                    startActivity(forget);
                    finish();
                }
                else if (!haveNetwork()){
                    Toast.makeText(LoginActivity.this, "Network connection unavailable!!\nCheck your 'internet' connection and try again", Toast.LENGTH_LONG).show();
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (haveNetwork()) {
                    Intent signup = new Intent(LoginActivity.this, SignupActivity.class);
                    startActivity(signup);
                    finish();
                }
                else if (!haveNetwork()){
                    Toast.makeText(LoginActivity.this, "Network connection unavailable!!\nCheck your 'internet' connection and try again", Toast.LENGTH_LONG).show();
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (haveNetwork()) {
                    Toast.makeText(LoginActivity.this,"Please wait...",Toast.LENGTH_SHORT).show();
                    final String studentEmail = studentLoginEmail.getEditText().getText().toString().trim();
                    final String Pass = loginPass.getEditText().getText().toString().trim();
                    if (studentEmail.isEmpty()) {
                        studentLoginEmail.setError("Fields can't be empty!");
                        return;
                    }
                    else if (Pass.isEmpty()) {
                        loginPass.setError("Fields can't be empty!");
                        return;
                    } else {
                        mAuth.signInWithEmailAndPassword(studentEmail, Pass).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, "Login error occurred! Please Check & Try again.", Toast.LENGTH_LONG).show();
                                } else {
                                    if (mAuth.getCurrentUser().isEmailVerified()) {
                                        if (mCheckBoxRemember.isChecked())
                                        {
                                            Boolean boolIsChecked = mCheckBoxRemember.isChecked();
                                            SharedPreferences.Editor editor = mPrefs.edit();
                                            editor.putString("pref_mail", studentLoginEmail.getEditText().getText().toString());
                                            editor.putString("pref_pass", loginPass.getEditText().getText().toString());
                                            editor.putBoolean("pref_check", boolIsChecked);
                                            editor.apply();
                                        }
                                        else {
                                            mPrefs.edit().clear().apply();
                                        }

                                        Intent intent = new Intent(LoginActivity.this, HomeActivity2.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else {
                                        Toast.makeText(LoginActivity.this, "Please verify your 'email address' and try again", Toast.LENGTH_LONG).show();
                                        studentLoginEmail.getEditText().setText("");
                                        loginPass.getEditText().setText("");
                                    }

                                }
                            }
                        });
                    }
                }
                else if (!haveNetwork()){
                    Toast.makeText(LoginActivity.this, "Network connection unavailable!!\nCheck your 'internet' connection and try again", Toast.LENGTH_LONG).show();
                }
                /*mAuthListener = new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        if (firebaseAuth.getCurrentUser() != null)
                        {
                            Intent intent = new Intent(LoginActivity.this, HomeActivity2.class);
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

    private void getPreferencesData()
    {
        SharedPreferences sp = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        if (sp.contains("pref_mail"))
        {
            String e = sp.getString("pref_mail", "not found");
            studentLoginEmail.getEditText().setText(e);
        }
        if (sp.contains("pref_pass"))
        {
            String p = sp.getString("pref_pass", "not found");
            loginPass.getEditText().setText(p);
        }
        if (sp.contains("pref_check"))
        {
            Boolean b = sp.getBoolean("pref_check", false);
            mCheckBoxRemember.setChecked(b);
        }
    }

    /*@Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
    }
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }*/

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