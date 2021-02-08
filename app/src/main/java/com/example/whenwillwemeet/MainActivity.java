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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

import java.awt.font.NumericShaper;
import java.util.*;
import com.squareup.timessquare.*;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity {

    roomClass nowRoom = new roomClass();
    private ArrayList<String> members;
    private ArrayList<Date> memberDates;
    private ArrayList<String> messages;

    boolean isLogin = false;
    public static String userName = null;
    String inviteCode = null;

    // availableDate와 memberDates 수정 필요
    int dateCnt;
    public Date [] availableDate = new Date[367];
    public int [][] availableDatesInt = new int[14][33]; // 1월~12월, 1일~31일로 저장됨

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference;
    private ChildEventListener mChildEventListener;

    private View mainLayout;
    CalendarPickerView calendarPicker;
    FloatingActionButton fab;
    TextView logTextView;

    // 생명주기 관련 함수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        members = new ArrayList<String>();
        messages = new ArrayList<String>();
        memberDates = new ArrayList<Date>();

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
    public void onDestroy(){
        super.onDestroy();

        if(mChildEventListener != null) {
            DatabaseReference mPostReference = FirebaseDatabase.getInstance().getReference()
                    .child("" + nowRoom.inviteCode).child(nowRoom.firebaseKey);
            mPostReference.removeValue();
            databaseReference.removeEventListener(mChildEventListener);
        }
    }

    // 사용자 초기 로그인 관련 함수 (카카오톡 등 추가 예정)

    public void googleLogin(){
        Intent newItent = new Intent(getApplicationContext(), GoogleLogin.class);
        startActivity(newItent);
    }

    // 메뉴 선택 관련 함수

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

    // 방을 만들기, 입장하기 관련 함수

    public void searchRoom(){
        Intent newIntent = new Intent(getApplicationContext(), SearchRoom.class);
        startActivity(newIntent);
    }

    public void createRoom(View view){
        inviteCode = randomString();

        nowRoom.inviteCode = this.inviteCode;
        nowRoom.adminIdx = 0;

        try {
            initFirebaseDatabase();
            ((Button) findViewById(R.id.createRoomButton)).setEnabled(false);
        }catch(Exception e){
            addToLoc("오류가 발생하였습니다. 네트워크를 확인해주세요",true);
            Log.e("createRoom","" + e);
        }
    }

    // 데이터베이스 관련 함수

    private void initFirebaseDatabase() {
        if(inviteCode == null){
            addToLoc("방을 만들거나 방에 입장해주세요.",true);
            return;
        }else if(userName == null){
            addToLoc("로그인이 필요한 서비스입니다.",true);
            return;
        }

        databaseReference = firebaseDatabase.getReference("" + inviteCode);
        mChildEventListener = new ChildEventListener() {

            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(nowRoom.firebaseKey != null)
                    return;

                nowRoom.firebaseKey = dataSnapshot.getKey();
                databaseReference.child(nowRoom.firebaseKey).child("firebaseKey").setValue(nowRoom.firebaseKey);

                databaseReference.child(nowRoom.firebaseKey).push().child("members");
                databaseReference.child(nowRoom.firebaseKey).push().child("memberDates");
                databaseReference.child(nowRoom.firebaseKey).push().child("messages");

                //int len = members.size();
                databaseReference.child(nowRoom.firebaseKey).child("members").child("0").setValue(""+userName);
                members.add(userName);

                insertMsg("방을 성공적으로 생성하였습니다. InviteCode : " + inviteCode);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                roomClass tmp = (roomClass) dataSnapshot.getValue(roomClass.class);

                if(tmp.messages != null) {
                    int st = messages.size();
                    int ed = tmp.messages.size();
                    for (int i = st; i < ed; i++) {
                        messages.add(tmp.messages.get(i));
                        addToLoc(tmp.messages.get(i), false);
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                inviteCode = null;
                addToLoc("방장이 방을 닫았습니다.",true);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        databaseReference.addChildEventListener(mChildEventListener);
        databaseReference.push().setValue(nowRoom);
    }

    // 캘린더 뷰, 처리 관련 함수

    public void getCalendarInfo(View view){
        dateCnt = calendarPicker.getSelectedDates().toArray().length;
        if(dateCnt == 0){
            addToLoc("가능한 날짜를 선택해 주세요.",false);
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
        addToLoc(text,false);
        initCalendar();
    }

    public void initCalendar(){
        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        Date today = new Date();

        calendarPicker.init(today, nextYear.getTime())
                .inMode(CalendarPickerView.SelectionMode.MULTIPLE);
    }

    // 텍스트뷰 메세지 기록 관련 함수

    public void insertMsg(String tmp){
        tmp = "[" + userName + "]\n" + tmp + "\n";
        int len = messages.size();
        databaseReference.child(nowRoom.firebaseKey).child("messages").child(""+len).setValue(""+tmp);
    }

    public void addToLoc(String tmp,boolean isLog){
        if(isLog == true){
            tmp = "[ log ]\n" + tmp + "\n";
            logTextView.append(tmp);
        }
        else
            logTextView.append(tmp);
    }

    // 기타 함수

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
