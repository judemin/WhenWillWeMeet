package com.example.whenwillwemeet;

import android.content.Intent;
import android.net.sip.SipSession;
import android.net.wifi.hotspot2.pps.Credential;
import android.os.Bundle;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.database.Cursor;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

import java.awt.font.NumericShaper;
import java.util.*;
import com.squareup.timessquare.*;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity {

    boolean isLogin = false;
    public static String userName;
    String inviteCode;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference("message");
    private View mainLayout;
    CalendarPickerView calendarPicker;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mainLayout = findViewById(R.id.mainlayout);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleLogin();
            }
        });

        // Calendar
        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);

        calendarPicker = (CalendarPickerView) findViewById(R.id.calendar_view);
        Date today = new Date();
        calendarPicker.init(today, nextYear.getTime())
                .inMode(CalendarPickerView.SelectionMode.MULTIPLE);
        //
        googleLogin();
    }

    @Override
    public void onResume(){
        super.onResume();
        if(userName != null && isLogin == false) {
            //로그인 성공시 실행
            isLogin = true;
            Snackbar.make(mainLayout, "환영합니다 " + userName + "님", Snackbar.LENGTH_LONG).show();
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    searchRoom();
                }
            });
            inviteCode = randomString();
            TextView tv = findViewById(R.id.textView);
            tv.setText(inviteCode);
        }
        else if(userName == null)
            Snackbar.make(mainLayout, "로그인이 필요한 서비스입니다", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //설정 눌렀을때 실행
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void googleLogin(){
        Intent newItent = new Intent(getApplicationContext(), GoogleLogin.class);
        startActivity(newItent);
    }

    public void searchRoom(){
        Intent newItent = new Intent(getApplicationContext(), SearchRoom.class);
        startActivity(newItent);
    }

    int dateCnt = 0;
    Date [] unAvailableDates = new Date[367];
    public void getCalendarInfo(View view){
        dateCnt = calendarPicker.getSelectedDates().toArray().length;
        for(int i = 0;i < dateCnt;i++) {
            unAvailableDates[i] = calendarPicker.getSelectedDates().get(i);
            Log.e("MainActivity", "" + unAvailableDates[i]);
        }
    }

    public static String randomString() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        char tempChar;
        for (int i = 0; i < 10; i++){
            tempChar = (char) (generator.nextInt(25) + 65);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

}
