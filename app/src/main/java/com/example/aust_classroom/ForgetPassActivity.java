package com.example.aust_classroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class ForgetPassActivity extends AppCompatActivity {

    private static final Pattern EMAIL_ADDRESS =
            Pattern.compile("[a-zA-Z0-9._-]+@(gmail|yahoo|hotmail|aol|msn|live|outlook|mail|ymail|free|googlemail|rocketmail|aust)+\\.+(com|co.uk|fr|net|com.br|co.in|de|ru|it|es|nl|ca|com.au|co.jp|be|com.ar|com.mx|co.id|com.sg|ch|net.au|edu)+");


    private FirebaseAuth firebaseAuth;

    private TextInputLayout recoveryEmail;

    private Button recoveryBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);

        firebaseAuth = FirebaseAuth.getInstance();

        recoveryEmail = findViewById(R.id.forgetPassEmail);
        recoveryBtn = findViewById(R.id.forgetPassRecoverBtn);



        recoveryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = recoveryEmail.getEditText().getText().toString().trim();
                if (haveNetwork()){
                    if (mail.isEmpty()){
                        recoveryEmail.setError("Field can't be empty!");
                        return;
                    }
                    else if (!EMAIL_ADDRESS.matcher(mail).matches()) {
                        recoveryEmail.setError("Please enter a valid email address!");
                        return;
                    }
                    else {
                        firebaseAuth.sendPasswordResetEmail(recoveryEmail.getEditText().getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(ForgetPassActivity.this, "Password recovery link sent. Check your mail.", Toast.LENGTH_LONG).show();
                                    recoveryEmail.getEditText().setText("");
                                    recoveryEmail.setError(null);
                                    finish();
                                    startActivity(new Intent(ForgetPassActivity.this, LoginActivity.class));
                                }
                                else {
                                    Toast.makeText(ForgetPassActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    recoveryEmail.setError(null);
                                }
                            }
                        });
                    }
                }
                else if (!haveNetwork()){
                    Toast.makeText(ForgetPassActivity.this, "Network connection unavailable!!\nCheck your 'internet' connection and try again", Toast.LENGTH_LONG).show();
                }
            }
        });

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

    public void hideKeyboard2(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }
}
