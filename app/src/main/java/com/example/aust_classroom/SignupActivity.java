package com.example.aust_classroom;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
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
import com.hbb20.CountryCodePicker;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {


    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    private static final Pattern DEPARTMENT =
            Pattern.compile("(^(CSE|EEE|ME|CE|IPE|TE|ARC|BBA)$)");
    private static final Pattern YEAR =
            Pattern.compile("(^[1-4]{1})$");
    private static final Pattern SEMESTER =
            Pattern.compile("(^[1-2]{1})$");
    private static final Pattern PHONE =
            Pattern.compile("(^([+]{1}[8]{2}|0088)?(01){1}[3-9]{1}[0-9]{8})$");
    private static final Pattern STUDENT_ID =
           Pattern.compile("(^[1-6]{1}[0-9]{1}(0){1}[1-2]{1}(0){1}[1-9]{1}[0-2]{1}[0-9]{1}[0-9]{1})$");
    private static final Pattern EMAIL_ADDRESS =
            Pattern.compile("[a-zA-Z0-9._-]+@(gmail|yahoo|hotmail|aol|msn|live|outlook|mail|ymail|free|googlemail|rocketmail|aust)+\\.+(com|co.uk|fr|net|com.br|co.in|de|ru|it|es|nl|ca|com.au|co.jp|be|com.ar|com.mx|co.id|com.sg|ch|net.au|edu)+");
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{6,}" +               //at least 6 characters
                    "$");

    TextInputLayout studentId;
    TextInputLayout studentName;
    TextInputLayout studdepartment;

    TextInputLayout year;
    TextInputLayout semester;
    TextInputLayout email;
    TextInputLayout phone;
    TextInputLayout password;
    TextInputLayout confirmPassword;

    ProgressDialog progressDialog;

    private Button goBackToLoginPageBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        studentId = findViewById(R.id.studentID);
        studentName = findViewById(R.id.studentName);
        studdepartment = findViewById(R.id.studentDepartment);
        year = findViewById(R.id.year);
        semester = findViewById(R.id.semester);
        email = findViewById(R.id.textInputEmail);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.textInputPassword);
        confirmPassword = findViewById(R.id.textInputPasswordConfirm);

        progressDialog = new ProgressDialog(this);


        goBackToLoginPageBtn = findViewById(R.id.login);

        goBackToLoginPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (haveNetwork()) {
                    Intent login = new Intent(SignupActivity.this, LoginActivity.class);
                    startActivity(login);
                    finish();
                }
                else if (!haveNetwork()){
                    Toast.makeText(SignupActivity.this, "Network connection unavailable!!\nCheck your 'internet' connection and try again", Toast.LENGTH_LONG).show();
                }
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
       /* firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        };*/

    }

    private boolean validateDepartment(){
        String inputDepartment = studdepartment.getEditText().getText().toString().trim();

        if (inputDepartment.isEmpty()) {
            studdepartment.setError("Field can't be empty!");
            return false;
        }
        else if(!DEPARTMENT.matcher(inputDepartment).matches()){
            studdepartment.setError("Wrong department format!! All letters must be in caps.\nHint:CSE,ARC,ME,CE etc.");
            Toast.makeText(SignupActivity.this,"You made a mistake in Department field", Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            studdepartment.setError(null);
            return true;
        }
    }

    private boolean matchPassword() {
        String inputPassword = password.getEditText().getText().toString().trim();
        String inputConfirmPassword = confirmPassword.getEditText().getText().toString().trim();

        if (inputConfirmPassword.isEmpty()) {
            confirmPassword.setError("Field can't be empty!");
            return false;
        } else if (!inputPassword.equals(inputConfirmPassword)) {
            confirmPassword.setError("Password doesn't match!!");
            return false;
        } else {
            confirmPassword.setError(null);
            return true;
        }
    }

    private boolean validateStudentId() {
        String inputId = studentId.getEditText().getText().toString().trim();

        if (inputId.isEmpty()) {
            studentId.setError("Field can't be empty!");
            return false;
        } else if (!STUDENT_ID.matcher(inputId).matches()) {
            studentId.setError("Wrong Id Format!");
            Toast.makeText(SignupActivity.this,"You made a mistake in Student Id field", Toast.LENGTH_SHORT).show();
            return false;
        }
         else if (inputId.length() > 9) {
            studentId.setError("Id too long");
            return false;
        } else if (inputId.length() < 9) {
            studentId.setError("Id too short");
            Toast.makeText(SignupActivity.this,"You made a mistake in Student Id field", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            studentId.setError(null);
            return true;
        }
    }

    private boolean validateStudentName() {
        String inputName = studentName.getEditText().getText().toString().trim();

        if (inputName.isEmpty()) {
            studentName.setError("Field can't be empty!");
            return false;
        } else if (inputName.length() > 30) {
            studentName.setError("Name too long");
            Toast.makeText(SignupActivity.this,"You made a mistake in Student Name field", Toast.LENGTH_SHORT).show();
            return false;
        } else if (inputName.length() <= 2) {
            studentName.setError("Name too short");
            Toast.makeText(SignupActivity.this,"You made a mistake in Student Name field", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            studentName.setError(null);
            return true;
        }
    }


    private boolean validateYear() {
        String inputYear = year.getEditText().getText().toString().trim();

        if (inputYear.isEmpty()) {
            year.setError("Field can't be empty!");
            return false;
        }else if (!YEAR.matcher(inputYear).matches()){
            year.setError("Invalid year");
            Toast.makeText(SignupActivity.this,"You made a mistake in Year field", Toast.LENGTH_SHORT).show();
            return false;
        }

        else if (inputYear.length() > 1) {
            year.setError("Year too long");
            return false;
        }
        else {
            year.setError(null);
            return true;
        }
    }

    private boolean validateSemester() {
        String inputSemester = semester.getEditText().getText().toString().trim();

        if (inputSemester.isEmpty()) {
            semester.setError("Field can't be empty!");
            return false;
        }else if (!SEMESTER.matcher(inputSemester).matches()){
            semester.setError("Invalid semester");
            Toast.makeText(SignupActivity.this,"You made a mistake in Semester field", Toast.LENGTH_SHORT).show();
            return false;

        }
        else if (inputSemester.length() > 1) {
            semester.setError("Semester too long");
            return false;
        } else {
            semester.setError(null);
            return true;
        }
    }

    private boolean validateEmail() {
        String inputEmail = email.getEditText().getText().toString().trim();

        if (inputEmail.isEmpty()) {
            email.setError("Field can't be empty!");
            return false;
        } else if (!EMAIL_ADDRESS.matcher(inputEmail).matches()) {
            email.setError("Please enter a valid email address!");
            Toast.makeText(SignupActivity.this,"You made a mistake in Email field", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            email.setError(null);
            return true;
        }
    }

    private boolean validatePhone() {
        String inputPhone = phone.getEditText().getText().toString().trim();

        if (inputPhone.isEmpty()) {
            phone.setError("Field can't be empty!");
            return false;
        }
        else if (!PHONE.matcher(inputPhone).matches()) {
            phone.setError("Wrong Phone Number Format!");
            Toast.makeText(SignupActivity.this,"You made a mistake in Phone number field", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (inputPhone.length() < 11) {
            phone.setError("Invalid number");
            Toast.makeText(SignupActivity.this,"You made a mistake in Phone number field", Toast.LENGTH_SHORT).show();
            return false;
        } else if (inputPhone.length() > 11) {
            phone.setError("Invalid number");
            Toast.makeText(SignupActivity.this,"You made a mistake in Phone number field", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            phone.setError(null);
            return true;
        }

    }

    private boolean validatePassword() {
        String inputPassword = password.getEditText().getText().toString().trim();

        if (inputPassword.isEmpty()) {
            password.setError("Field can't be empty!");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(inputPassword).matches()) {
            password.setError("Password too weak! It should contain 6 characters; at least 1 Digit, 1 Caps required");
            Toast.makeText(SignupActivity.this,"You made a mistake in Password field", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            password.setError(null);
            return true;
        }
    }

    public void signupInput(View v) {

        if (haveNetwork()) {
                if (!validateStudentName() | !validateStudentId() | !validateDepartment() | !validateYear() | !validateSemester() | !validateEmail() | !validatePhone() | !validatePassword() | !matchPassword()) {
                    return;
                } else {
                    Toast.makeText(SignupActivity.this, "Creating your account.. Please wait", Toast.LENGTH_LONG).show();
                    final String inputId = studentId.getEditText().getText().toString().trim();
                    final String inputEmail = email.getEditText().getText().toString().trim();
                    final String inputPassword = password.getEditText().getText().toString().trim();

                    Query studentIdQuery = FirebaseDatabase.getInstance().getReference().child("AllUsers").orderByChild("StudentId").equalTo(inputId);
                    studentIdQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getChildrenCount() > 0) {
                                Toast.makeText(SignupActivity.this, "Same student id not allowed", Toast.LENGTH_LONG).show();
                            } else {
                                try {
                                    firebaseAuth.createUserWithEmailAndPassword(inputEmail, inputPassword).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (!task.isSuccessful()) {
                                                Toast.makeText(SignupActivity.this, "Sign up error occured! Try Again.", Toast.LENGTH_SHORT).show();
                                            } else {
                                                String user_id = firebaseAuth.getCurrentUser().getUid();
                                                final DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("AllUsers").child(user_id);//.child(inputId);

                                                final String inputName = studentName.getEditText().getText().toString().trim();
                                                final String inputId = studentId.getEditText().getText().toString().trim();
                                                final String inputDepartment = studdepartment.getEditText().getText().toString().trim();
                                                final String inputYear = year.getEditText().getText().toString().trim();
                                                final String inputSemester = semester.getEditText().getText().toString().trim();
                                                final String inputEmail = email.getEditText().getText().toString().trim();
                                                final String inputPhone = phone.getEditText().getText().toString().trim();
                                                final String inputPassword = password.getEditText().getText().toString().trim();

                                                Map newInput = new HashMap();
                                                newInput.put("StudentName", inputName);
                                                newInput.put("StudentId", inputId);
                                                newInput.put("Department", inputDepartment);
                                                newInput.put("Year", inputYear);
                                                newInput.put("Semester", inputSemester);
                                                newInput.put("Email", inputEmail);
                                                newInput.put("PhoneNo", inputPhone);
                                                newInput.put("Password", inputPassword);

                                                current_user_db.setValue(newInput);


                                                progressDialog = new ProgressDialog(SignupActivity.this);
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
                                                                Toast.makeText(SignupActivity.this, input, Toast.LENGTH_SHORT).show();
                                                                //startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                                                //finish();

                                                                studentId.getEditText().setText("");
                                                                studentName.getEditText().setText("");
                                                                studdepartment.getEditText().setText("");
                                                                email.getEditText().setText("");
                                                                phone.getEditText().setText("");
                                                                year.getEditText().setText("");
                                                                semester.getEditText().setText("");
                                                                password.getEditText().setText("");
                                                                confirmPassword.getEditText().setText("");

                                                            } else {
                                                                Toast.makeText(SignupActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                            }
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }

                                                    }
                                                });
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
                            Toast.makeText(SignupActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    });

                }
            }
        else if (!haveNetwork()) {
                Toast.makeText(SignupActivity.this, "Network connection unavailable!!\nCheck your 'internet' connection and try again", Toast.LENGTH_LONG).show();
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