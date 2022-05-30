package com.example.aust_classroom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    private ViewPager nSlideViewPager;
    private LinearLayout nDotLayout;

    private SliderAdapter sliderAdapter;
    private TextView[] nDots;

    private Button nNextBtn;
    private Button nBackBtn;
    private Button nLoginBtn1;
    private Button nLoginBtn2;

    private int nCurrentPage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nSlideViewPager = findViewById(R.id.slideViewPager);
        nDotLayout = findViewById(R.id.dotsLayout);


        sliderAdapter = new SliderAdapter(this);
        nSlideViewPager.setAdapter(sliderAdapter);

        addDotsIndicator(0);

        nSlideViewPager.addOnPageChangeListener(viewListener);

        nNextBtn = findViewById(R.id.nextBtn);
        nBackBtn = findViewById(R.id.prevBtn);
        nLoginBtn1 = findViewById(R.id.loginBtn);
        nLoginBtn2 = findViewById(R.id.loginBtn2);


        nBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nSlideViewPager.setCurrentItem(nCurrentPage - 1);
            }
        });

        nNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nSlideViewPager.setCurrentItem(nCurrentPage + 1);
            }
        });

        nLoginBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (haveNetwork()) {
                    Intent login = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(login);
                }
                else if (!haveNetwork()){
                    Toast.makeText(MainActivity.this, "Network connection unavailable!!\nCheck your 'internet' connection and try again", Toast.LENGTH_LONG).show();
                }
            }
        });

        nLoginBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (haveNetwork()) {
                    Intent login2 = new Intent(MainActivity.this, LoginActivityTeacher.class);
                    startActivity(login2);
                }
                else if (!haveNetwork()){
                    Toast.makeText(MainActivity.this, "Network connection unavailable!!\nCheck your 'internet' connection and try again", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void addDotsIndicator(int position)
    {
        nDots = new TextView[3];
        nDotLayout.removeAllViews();

        for(int i=0; i<nDots.length; i++)
        {
            nDots[i] = new TextView(this);
            nDots[i].setText(Html.fromHtml("&#8226;"));
            nDots[i].setTextSize(25);
            nDots[i].setTextColor(getResources().getColor(R.color.colorTransparentWhite));

            nDotLayout.addView(nDots[i]);
        }

        if(nDots.length > 0)
        {
            nDots[position].setTextColor(getResources().getColor(R.color.colorWhite));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int i) {
            addDotsIndicator(i);

            nCurrentPage = i;

            if(i==0)
            {
                nNextBtn.setEnabled(true);
                nBackBtn.setEnabled(false);
                nLoginBtn1.setEnabled(false);
                nLoginBtn2.setEnabled(false);
                nLoginBtn1.setVisibility(View.GONE);
                nLoginBtn2.setVisibility(View.GONE);
                nBackBtn.setVisibility(View.INVISIBLE);
                nNextBtn.setText("Next");
                nBackBtn.setText("");
                nLoginBtn1.setText("");
                nLoginBtn2.setText("");
            }
            else if(i == nDots.length - 1)
            {
                nNextBtn.setEnabled(false);
                nBackBtn.setEnabled(true);
                nLoginBtn1.setEnabled(true);
                nLoginBtn2.setEnabled(true);
                nLoginBtn1.setVisibility(View.VISIBLE);
                nLoginBtn2.setVisibility(View.VISIBLE);
                nBackBtn.setVisibility(View.VISIBLE);
                nNextBtn.setText("");
                nBackBtn.setText("Back");
                nLoginBtn1.setText("Login as a Student");
                nLoginBtn2.setText("Login as a Teacher");
            }
            else
            {
                nNextBtn.setEnabled(true);
                nBackBtn.setEnabled(true);
                nLoginBtn1.setEnabled(false);
                nLoginBtn2.setEnabled(false);
                nLoginBtn1.setVisibility(View.GONE);
                nLoginBtn2.setVisibility(View.GONE);
                nBackBtn.setVisibility(View.VISIBLE);
                nNextBtn.setText("Next");
                nBackBtn.setText("Back");
                nLoginBtn1.setText("");
                nLoginBtn2.setText("");
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

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
