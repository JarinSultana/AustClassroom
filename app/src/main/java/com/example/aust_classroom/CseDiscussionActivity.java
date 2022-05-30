package com.example.aust_classroom;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContentResolverCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;

import java.util.ArrayList;

public class CseDiscussionActivity extends AppCompatActivity {

    TextView notifyPdfName;
    Button selectPdfBtn;
    Button uploadPdfBtn;
    Button showFileListBtn;
    Uri pdfUri;

    FirebaseAuth firebaseAuth;
    StorageReference storageReference;
    DatabaseReference databaseReference;

    private BoomMenuButton bmb;
    ArrayList<Integer> imageIdlist;
    ArrayList<String> tittleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cse_discussion);

        notifyPdfName = findViewById(R.id.notifyFile);
        selectPdfBtn = findViewById(R.id.selectFileBtn);
        uploadPdfBtn = findViewById(R.id.uploadFileBtn);
        showFileListBtn = findViewById(R.id.showFileListBtn);

        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("CSE Uploads");

        selectPdfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (haveNetwork()) {
                    if (ContextCompat.checkSelfPermission(CseDiscussionActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        selectPdf();
                    }
                    else if (ContextCompat.checkSelfPermission(CseDiscussionActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        selectDocx();

                    }
                    else if (ContextCompat.checkSelfPermission(CseDiscussionActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        selectZip();

                    }
                    else if (ContextCompat.checkSelfPermission(CseDiscussionActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        selectImage();

                    }
                    else {
                        ActivityCompat.requestPermissions(CseDiscussionActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
                    }
                }
                else {
                    Toast.makeText(CseDiscussionActivity.this, "Network connection unavailable!!\nCheck your 'internet' connection and try again", Toast.LENGTH_LONG).show();
                }
            }
        });

        uploadPdfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (haveNetwork()) {
                    if (pdfUri == null) {
                        Toast.makeText(CseDiscussionActivity.this, "No file selected!", Toast.LENGTH_SHORT).show();
                        //uploadFile(pdfUri);
                    }
                    else {
                        uploadFile(pdfUri);
                    }
                }
                else {
                    Toast.makeText(CseDiscussionActivity.this, "Network connection unavailable!!\nCheck your 'internet' connection and try again", Toast.LENGTH_LONG).show();
                }
            }
        });

        showFileListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (haveNetwork()) {
                    Intent showFile = new Intent(CseDiscussionActivity.this, CseShowPdfListActivity.class);
                    startActivity(showFile);
                } else {
                    Toast.makeText(CseDiscussionActivity.this, "Network connection unavailable!!\nCheck your 'internet' connection and try again", Toast.LENGTH_LONG).show();
                }
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
                                    Intent home = new Intent(CseDiscussionActivity.this, HomeActivity2.class);
                                    startActivity(home);
                                    finish();
                                }
                                else if (!haveNetwork()){
                                    Toast.makeText(CseDiscussionActivity.this, "Network connection unavailable!!\nCheck your 'internet' connection and try again", Toast.LENGTH_LONG).show();
                                }
                            }
                            if (index == 1) {
                                if (haveNetwork()) {
                                    Intent profile = new Intent(CseDiscussionActivity.this, UserProfileActivity.class);
                                    startActivity(profile);
                                }
                                else if (!haveNetwork()){
                                    Toast.makeText(CseDiscussionActivity.this, "Network connection unavailable!!\nCheck your 'internet' connection and try again", Toast.LENGTH_LONG).show();
                                }
                            }
                            if (index == 2) {
                                Toast.makeText(CseDiscussionActivity.this, "Settings will open", Toast.LENGTH_LONG).show();
                            }
                            if (index == 3) {
                                if (haveNetwork()) {
                                    firebaseAuth.signOut();
                                    finish();
                                    startActivity(new Intent(CseDiscussionActivity.this, LoginActivity.class));
                                }
                                else if (!haveNetwork()){
                                    Toast.makeText(CseDiscussionActivity.this, "Network connection unavailable!!\nCheck your 'internet' connection and try again", Toast.LENGTH_LONG).show();
                                }
                            }

                        }

                    });
            bmb.addBuilder(builder);
        }

    }

    private void uploadFile(final Uri pdfUri) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        //progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.show();


        StorageReference reference = storageReference.child("CSE Uploads/" + pdfUri.getLastPathSegment());
        reference.putFile(pdfUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                while (!uri.isComplete());
                Uri url = uri.getResult();

                uploadPDF uploadPDF = new uploadPDF(pdfUri.getLastPathSegment(), url.toString());

                databaseReference.child(databaseReference.push().getKey()).setValue(uploadPDF);

                Toast.makeText(CseDiscussionActivity.this, "File Uploaded Successfully", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                startActivity(new Intent(CseDiscussionActivity.this, CseY2S2Activity.class));
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                progressDialog.setMessage("Uploaded: "+ (int) progress+"%");
            }
        });



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 9 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            selectPdf();

        }
        else  if (requestCode == 9 && grantResults[1] == PackageManager.PERMISSION_GRANTED)
        {

            selectDocx();

        }
        else  if (requestCode == 9 && grantResults[2] == PackageManager.PERMISSION_GRANTED)
        {

            selectZip();

        }
        else  if (requestCode == 9 && grantResults[3] == PackageManager.PERMISSION_GRANTED)
        {

            selectImage();

        }
        else {
            Toast.makeText(CseDiscussionActivity.this, "Please Provide Permission",Toast.LENGTH_LONG).show();
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void selectPdf() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 86);
    }
    private void selectDocx() {
        Intent intent2 = new Intent();
        intent2.setType("docx/*");
        intent2.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent2, 87);
    }
    private void selectZip() {
        Intent intent2 = new Intent();
        intent2.setType("application/zip");
        intent2.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent2, 88);
    }
    private void selectImage() {
        Intent intent2 = new Intent();
        intent2.setType("image/*");
        intent2.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent2, 89);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 86 && resultCode == RESULT_OK && data != null && data.getData()!=null)
        {
            pdfUri=data.getData();
            notifyPdfName.setText("A file is selected: "+data.getData().getLastPathSegment());
        }
        else  if (requestCode == 87 && resultCode == RESULT_OK && data != null && data.getData()!=null)
        {
            pdfUri=data.getData();
            notifyPdfName.setText("A file is selected: "+data.getData().getLastPathSegment());
        }
        else  if (requestCode == 88 && resultCode == RESULT_OK && data != null && data.getData()!=null)
        {
            pdfUri=data.getData();
            notifyPdfName.setText("A file is selected: "+data.getData().getLastPathSegment());
        }
        else  if (requestCode == 89 && resultCode == RESULT_OK && data != null && data.getData()!=null)
        {
            pdfUri=data.getData();
            notifyPdfName.setText("A file is selected: "+data.getData().getLastPathSegment());
        }
        else {
            Toast.makeText(CseDiscussionActivity.this, "Please select a file", Toast.LENGTH_SHORT).show();
        }

        super.onActivityResult(requestCode, resultCode, data);
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

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }
}
