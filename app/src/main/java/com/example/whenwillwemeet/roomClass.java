package com.example.whenwillwemeet;

import java.util.Date;

public class roomClass {
    public String inviteCode; //초대코드
    public String admin; //방장
    public String [] members = new String [101]; //들어와있는 사람들의 이름
    public Date [][] memberDates = new Date[101][367]; //각 사람들의 날짜 정보
    public String [] messages; // 채팅 메세지 (업데이트 예정)
    public int myIndex; //현재 자신의 인덱스 번호
}
