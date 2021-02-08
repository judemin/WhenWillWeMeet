package com.example.whenwillwemeet;

import java.util.*;
import java.util.Date;

public class roomClass {
    public String firebaseKey;
    public String inviteCode; //초대코드
    public int adminIdx; //방장의 index는 항상 0번
    public List<String> members;
    public List<Date> memberDates;
    public List<String> messages;
}
/*
public String [] members = new String [101]; //들어와있는 사람들의 이름
public Date [][] memberDates = new Date[101][367]; //각 사람들의 날짜 정보
public String [] messages; // 채팅 메세지 (업데이트 예정)
 */