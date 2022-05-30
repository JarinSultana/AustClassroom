/*
package com.example.aust_classroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser user;

    private TextView headerName;
    private TextView headerEmail;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    private Button eduNotice;
    private Button genNotice;
    private Button disForum;
    private Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



       // headerName =  findViewById(R.id.header_name);
      //  headerEmail = findViewById(R.id.header_email);

        firebaseAuth = FirebaseAuth.getInstance();
      //  firebaseDatabase = FirebaseDatabase.getInstance();
      //  user = firebaseAuth.getCurrentUser();


      //  final DatabaseReference databaseReference = firebaseDatabase.getReference().child(user.getUid());

       */
/* databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String username = dataSnapshot.child("Student Name").getValue().toString();
                String userEmail = dataSnapshot.child("Email").getValue().toString();

                headerName.setText(username);
                headerEmail.setText(userEmail);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HomeActivity.this, databaseError.getCode(), Toast.LENGTH_LONG).show();

            }
        });*//*



        eduNotice = findViewById(R.id.educationalNoticeBtn);
        genNotice = findViewById(R.id.generalNoticeBtn);
        disForum = findViewById(R.id.discussionForumBtn);
        logout = findViewById(R.id.logoutBtn);

        eduNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, EduNoticeActivity.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

   */
/* @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.logoutMenu:
            {
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            }
        }
        return super.onOptionsItemSelected(item);
    }*//*

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (mToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        switch (item.getItemId())
        {
            case R.id.logoutMenu2:
            {
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
*/
