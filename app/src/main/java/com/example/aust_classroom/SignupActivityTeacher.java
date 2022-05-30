package com.example.aust_classroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import io.grpc.Context;

public class SignupActivityTeacher extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    private static final Pattern PHONE_2 =
            Pattern.compile("(^([+]{1}[8]{2}|0088)?(01){1}[3-9]{1}[0-9]{8})$");

    private static final Pattern EMAIL_ADDRESS_2 =
            Pattern.compile("[a-zA-Z0-9._-]+@(gmail|yahoo|hotmail|aol|msn|live|outlook|mail|ymail|free|googlemail|rocketmail|aust)+\\.+(com|co.uk|fr|net|com.br|co.in|de|ru|it|es|nl|ca|com.au|co.jp|be|com.ar|com.mx|co.id|com.sg|ch|net.au|edu)+");

    private static final Pattern PASSWORD_PATTERN_2 =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{6,}" +               //at least 6 characters
                    "$");


    TextInputLayout teacherName;
    Spinner departmentSpinner;
    String item;
    TextInputLayout teacherEamil;
    TextInputLayout teacherPhone;
    TextInputLayout teacherPassword;
    TextInputLayout teacherConfirmPassword;

    ProgressDialog progressDialog;

    private Button goBackToLoginPageBtn;


    ScrollView scrollView;
    AnimationDrawable animationDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_teacher);


        teacherName = findViewById(R.id.teacherName);
        departmentSpinner = findViewById(R.id.departmentSpinner);
        teacherEamil = findViewById(R.id.textInputEmailTeacher);
        teacherPhone = findViewById(R.id.phoneTeacher);
        teacherPassword = findViewById(R.id.textInputPasswordTeacher);
        teacherConfirmPassword = findViewById(R.id.textInputPasswordConfirmTeacher);

        scrollView = findViewById(R.id.scroll_view_0);
        animationDrawable = (AnimationDrawable) scrollView.getBackground();

        // setting enter fade animation duration to 5 seconds
        animationDrawable.setEnterFadeDuration(5000);

        // setting exit fade animation duration to 2 seconds
        animationDrawable.setExitFadeDuration(2000);

        progressDialog = new ProgressDialog(this);


        goBackToLoginPageBtn = findViewById(R.id.loginTeacher);
        firebaseAuth = FirebaseAuth.getInstance();

        goBackToLoginPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (haveNetwork()) {
                    Intent login = new Intent(SignupActivityTeacher.this, LoginActivityTeacher.class);
                    startActivity(login);
                    finish();
                }
                else if (!haveNetwork()){
                    Toast.makeText(SignupActivityTeacher.this, "Network connection unavailable!!\nCheck your 'internet' connection and try again", Toast.LENGTH_LONG).show();
                }
            }
        });

        List<String> catagories = new ArrayList<>();
        catagories.add(0,"Department");
        catagories.add("CSE");
        catagories.add("EEE");
        catagories.add("ME");
        catagories.add("CE");
        catagories.add("IPE");
        catagories.add("TE");
        catagories.add("ARC");
        catagories.add("BBA");

        ArrayAdapter<String> dataAdapter;

        dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, catagories);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        departmentSpinner.setAdapter(dataAdapter);


    }


    private boolean matchPassword() {
        String inputPassword = teacherPassword.getEditText().getText().toString().trim();
        String inputConfirmPassword = teacherConfirmPassword.getEditText().getText().toString().trim();

        if (inputConfirmPassword.isEmpty()) {
            teacherConfirmPassword.setError("Field can't be empty!");
            return false;
        } else if (!inputPassword.equals(inputConfirmPassword)) {
            teacherConfirmPassword.setError("Password doesn't match!!");
            return false;
        } else {
            teacherConfirmPassword.setError(null);
            return true;
        }
    }


    private boolean validateTeacherName() {
        String inputName = teacherName.getEditText().getText().toString().trim();

        if (inputName.isEmpty()) {
            teacherName.setError("Field can't be empty!");
            return false;
        } else if (inputName.length() > 30) {
            teacherName.setError("Name too long");
            Toast.makeText(SignupActivityTeacher.this,"You made a mistake in Student Name field", Toast.LENGTH_SHORT).show();
            return false;
        } else if (inputName.length() <= 2) {
            teacherName.setError("Name too short");
            Toast.makeText(SignupActivityTeacher.this,"You made a mistake in Student Name field", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            teacherName.setError(null);
            return true;
        }
    }


    private boolean validateEmail() {
        String inputEmail = teacherEamil.getEditText().getText().toString().trim();

        if (inputEmail.isEmpty()) {
            teacherEamil.setError("Field can't be empty!");
            return false;
        } else if (!EMAIL_ADDRESS_2.matcher(inputEmail).matches()) {
            teacherEamil.setError("Please enter a valid email address!");
            Toast.makeText(SignupActivityTeacher.this,"You made a mistake in Email field", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            teacherEamil.setError(null);
            return true;
        }
    }

    private boolean validatePhone() {
        String inputPhone = teacherPhone.getEditText().getText().toString().trim();

        if (inputPhone.isEmpty()) {
            teacherPhone.setError("Field can't be empty!");
            return false;
        }
        else if (!PHONE_2.matcher(inputPhone).matches()) {
            teacherPhone.setError("Wrong Phone Number Format!");
            Toast.makeText(SignupActivityTeacher.this,"You made a mistake in Phone number field", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (inputPhone.length() < 11) {
            teacherPhone.setError("Invalid number");
            Toast.makeText(SignupActivityTeacher.this,"You made a mistake in Phone number field", Toast.LENGTH_SHORT).show();
            return false;
        } else if (inputPhone.length() > 11) {
            teacherPhone.setError("Invalid number");
            Toast.makeText(SignupActivityTeacher.this,"You made a mistake in Phone number field", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            teacherPhone.setError(null);
            return true;
        }

    }

    private boolean validatePassword() {
        String inputPassword = teacherPassword.getEditText().getText().toString().trim();

        if (inputPassword.isEmpty()) {
            teacherPassword.setError("Field can't be empty!");
            return false;
        } else if (!PASSWORD_PATTERN_2.matcher(inputPassword).matches()) {
            teacherPassword.setError("Password too weak! It should contain 6 characters; at least 1 Digit, 1 Caps required");
            Toast.makeText(SignupActivityTeacher.this,"You made a mistake in Password field", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            teacherPassword.setError(null);
            return true;
        }
    }

    public boolean validateDepartment() {

        departmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i).equals("Department"))
                {
                    Toast.makeText(SignupActivityTeacher.this, "Department is not selected!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    item = adapterView.getItemAtPosition(i).toString();
                    Toast.makeText(SignupActivityTeacher.this, "Selected Department :" +item, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });
        return true;
    }




    public void signupInputTeacher(View v) {
        if (haveNetwork()) {
            if (!validateTeacherName() | !validateEmail() | !validatePhone() | !validatePassword() | !matchPassword() | !validateDepartment()) {
                return;
            }
            else {
                Toast.makeText(SignupActivityTeacher.this, "Creating your account.. Please wait", Toast.LENGTH_LONG).show();
                final String inputEmail = teacherEamil.getEditText().getText().toString().trim();
                final String inputPassword = teacherPassword.getEditText().getText().toString().trim();
               // final String inputDepartment = departmentSpinner.toString().trim();


                Query teacherEmailQuery = FirebaseDatabase.getInstance().getReference().child("TEACHERS").orderByChild("Email").equalTo(inputEmail);
                teacherEmailQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() > 0) {
                            Toast.makeText(SignupActivityTeacher.this, "Same email can't create another account", Toast.LENGTH_LONG).show();
                        }else {
                            try {
                                firebaseAuth.createUserWithEmailAndPassword(inputEmail, inputPassword).addOnCompleteListener(SignupActivityTeacher.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {
                                            Toast.makeText(SignupActivityTeacher.this, "Sign up error occured! Try Again.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            try {
                                                String user_id = firebaseAuth.getCurrentUser().getUid();
                                                final DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("TEACHERS").child(user_id).child(item);

                                                final String inputName = teacherName.getEditText().getText().toString().trim();
                                                final String inputEmail = teacherEamil.getEditText().getText().toString().trim();
                                                final String inputPhone = teacherPhone.getEditText().getText().toString().trim();
                                                final String inputPassword = teacherPassword.getEditText().getText().toString().trim();



                                                Map newInput = new HashMap();
                                                newInput.put("TeacherName", inputName);
                                                newInput.put("Department", item);
                                                newInput.put("Email", inputEmail);
                                                newInput.put("PhoneNo", inputPhone);
                                                newInput.put("Password", inputPassword);

                                                current_user_db.setValue(newInput);


                                                progressDialog = new ProgressDialog(SignupActivityTeacher.this);
                                                progressDialog.setTitle("Uploading Information");
                                                progressDialog.setMessage("Registering User.....");
                                                progressDialog.setMax(100);
                                                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                                progressDialog.show();
                                                new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        try {
                                                            while (progressDialog.getProgress() <= progressDialog.getMax()) {
                                                                Thread.sleep(100);
                                                                handle.sendMessage(handle.obtainMessage());
                                                                try {
                                                                    if (progressDialog.getProgress() == progressDialog.getMax()) {
                                                                        progressDialog.dismiss();
                                                                    }
                                                                }catch (Exception e){
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }).start();
                                                firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        try {
                                                            if (task.isSuccessful()) {
                                                                String input = "Registered Successfully..\nPlease check your email for verification";
                                                                Toast.makeText(SignupActivityTeacher.this, input, Toast.LENGTH_LONG).show();
                                                                //startActivity(new Intent(SignupActivityTeacher.this, LoginActivityTeacher.class));
                                                                //finish();
                                                                teacherName.getEditText().setText("");
                                                                teacherEamil.getEditText().setText("");
                                                                teacherPhone.getEditText().setText("");
                                                                teacherPassword.getEditText().setText("");
                                                                teacherConfirmPassword.getEditText().setText("");
                                                            } else {
                                                                Toast.makeText(SignupActivityTeacher.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                            }
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }

                                                    }
                                                });
                                            }catch (Exception e)
                                            {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                });
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(SignupActivityTeacher.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();


                    }
                });

            }
        }
        else if (!haveNetwork()) {
            Toast.makeText(SignupActivityTeacher.this, "Network connection unavailable!!\nCheck your 'internet' connection and try again", Toast.LENGTH_LONG).show();
        }

    }
    Handler handle = new Handler()
    {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            progressDialog.incrementProgressBy(4);
        }
    };


    public void hideKeyboard1(View view) {
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

    /*@Override
    protected void onStart() {
        super.onStart();

        firebaseAuth.addAuthStateListener(firebaseAuthListener);
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
