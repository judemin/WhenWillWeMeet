package com.example.whenwillwemeet;

import android.content.Intent;
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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

import java.util.*;
import com.squareup.timessquare.*;

public class MainActivity extends AppCompatActivity {

    public static String userName;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference("message");
    private View mainLayout;
    CalendarPickerView calendarPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mainLayout = findViewById(R.id.mainlayout);
        FloatingActionButton fab = findViewById(R.id.fab);

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
                .withSelectedDate(today);
        //
    }

    @Override
    public void onResume(){
        //다른앱에 갔다와도 onResume이 호출됨 => 수정필요
        super.onResume();
        if(userName != null)
            Snackbar.make(mainLayout, "환영합니다 " + userName + "님", Snackbar.LENGTH_LONG).show();
        else
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void googleLogin(){
        Intent loginIntent = new Intent(getApplicationContext(), GoogleLogin.class);
        startActivity(loginIntent);
    }


}
