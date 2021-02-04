package com.example.whenwillwemeet;

import android.content.Intent;
import android.graphics.Color;
import android.net.sip.SipSession;
import android.net.wifi.hotspot2.pps.Credential;
import android.os.Bundle;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.ContactsContract;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.database.Cursor;
import android.widget.Button;
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

    roomClass newRoom = new roomClass();

    boolean isLogin = false;
    public static String userName;
    String inviteCode;

    String [] roomMsg = new String [100001];
    int nowMsgCnt = 0;

    int dateCnt;
    public Date [] availableDate = new Date[367];
    public int [][] availableDatesInt = new int[14][33]; // 1월~12월, 1일~31일로 저장됨

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference;
    private View mainLayout;
    CalendarPickerView calendarPicker;
    FloatingActionButton fab;
    TextView logTextView;

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

        logTextView = findViewById(R.id.textView);
        logTextView.setMovementMethod(new ScrollingMovementMethod());
        logTextView.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollBottom(logTextView);
                logTextView.postDelayed(this, 100);
            }
        }, 100);

        // Calendar
        calendarPicker = (CalendarPickerView) findViewById(R.id.calendar_view);
        initCalendar();
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
        Intent newIntent = new Intent(getApplicationContext(), SearchRoom.class);
        startActivity(newIntent);
    }

    public void createRoom(View view){
        inviteCode = randomString();
        addMsg("방을 성공적으로 생성하였습니다. InviteCode : " + inviteCode);
        ((Button) findViewById(R.id.createRoomButton)).setEnabled(false);
    }

    public void getCalendarInfo(View view){
        dateCnt = calendarPicker.getSelectedDates().toArray().length;
        if(dateCnt == 0){
            addMsg("가능한 날짜를 선택해 주세요.");
            return;
        }

        String text = "";
        for(int i = 0;i < 13;i++)
            for(int j = 0;j < 32;j++)
                availableDatesInt[i][j] = 0;
        for(int i = 0;i < dateCnt;i++) {
            Date tmp = calendarPicker.getSelectedDates().get(i);
            availableDate[i] = tmp;
            int year = ((tmp.getYear()) % 100) + 2000;
            int month = tmp.getMonth() + 1;
            int date = tmp.getDate();

            availableDatesInt[month][date] = 1;

            text += year + "." + month  + "." + date;
            if(i != dateCnt - 1)
                text += " / ";
        }
        text += "\n총 " + dateCnt + "개의 날짜가 선택되었습니다.";
        addMsg(text);
        initCalendar();
    }

    public void initCalendar(){
        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        Date today = new Date();

        calendarPicker.init(today, nextYear.getTime())
                .inMode(CalendarPickerView.SelectionMode.MULTIPLE);
    }

    public void addMsg(String tmp){
        tmp = "[" + userName + "]\n" + tmp + "\n";
        roomMsg[nowMsgCnt++] = tmp;
        logTextView.append(tmp);
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

    private void scrollBottom(TextView textView) {
        int lineTop =  textView.getLayout().getLineTop(textView.getLineCount()) ;
        int scrollY = lineTop - textView.getHeight();
        if (scrollY > 0) {
            textView.scrollTo(0, scrollY);
        } else {
            textView.scrollTo(0, 0);
        }
    }
}
