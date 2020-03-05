package com.example.lc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataBaseHandler{


    UserModule u;
    ArrayList<HashMap<String,String>> userlist=new ArrayList<>();
    public DatabaseReference dbref;



   /* private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "LaundryClub";
    private static final String TABLE_NAME_ADMIN = "Admin";
    private static final String TABLE_NAME_USER = "User";
    //ADMIN VARIABLES
    private static final String KEY_aId = "aId";
    private static final String KEY_aUSERNAME = "aUserName";
    private static final String KEY_aPassword = "aPassword";
    private static final String KEY_aRegNo = "aRegNo";
    private static final String KEY_aRoomNo = "aRoomNo";

    //USER VARIABLES
    private static final String KEY_mId = "mId";
    private static final String KEY_mUSERNAME = "mUserName";
    private static final String KEY_mRegNo = "mRegNo";
    private static final String KEY_mRoomNo = "mRoomNo";



    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            //CREATE ADMIN TABLE
            String CREATE_TABLE_ADMIN = " CREATE TABLE " + TABLE_NAME_ADMIN + " (" + KEY_aId + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_aUSERNAME + " TEXT,"
                    + KEY_aPassword + " TEXT," + KEY_aRegNo + " TEXT," + KEY_aRoomNo + " TEXT " + ")";
            db.execSQL(CREATE_TABLE_ADMIN);

            //CREATE USER TABLE
            String CREATE_TABLE_USER = " CREATE TABLE " + TABLE_NAME_USER + " (" + KEY_mId + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_mUSERNAME + " TEXT,"
                    + KEY_mRegNo + " TEXT," + KEY_mRoomNo + " TEXT " + ")";
            db.execSQL(CREATE_TABLE_USER);

        } catch (Exception e) {

        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ADMIN);
        onCreate(db);
    }
*/

    //ADD NEW ADMIN ACCOUNT



    //RETRIEVE ADMIN DETAILS
    /*public List<UserModule> getAdminDetails(String username, String password) {
        List<UserModule> adminList = new ArrayList<>();
        String selectDetails = "SELECT * FROM " + TABLE_NAME_ADMIN + " WHERE " + KEY_aUSERNAME + " = '" + username + "' AND " + KEY_aPassword + " = '" + password + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectDetails, null);
        if (cursor.moveToFirst()) {
            do {
                UserModule um = new UserModule();
                um.setaId(cursor.getString(cursor.getColumnIndex(KEY_aId)));
                um.setaUsername(cursor.getString(cursor.getColumnIndex(KEY_aUSERNAME)));
                um.setaPassword(cursor.getString(cursor.getColumnIndex(KEY_aPassword)));

                adminList.add(um);
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return adminList;
    }*/

    //CHECK FOR EXISTING REGNO IN ADMIN TABLE
    /*public int checkaRegNoexists(String regno) {
        List<UserModule> adminList = new ArrayList<>();
        Log.d("Query ", " SELECT * FROM " + TABLE_NAME_ADMIN + " WHERE " + KEY_aRegNo + " = '" + regno + "'");
        String checkQuery = " SELECT * FROM " + TABLE_NAME_ADMIN + " WHERE " + KEY_aRegNo + " = '" + regno + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(checkQuery, null);
        int count = cursor.getCount();
        db.close();
        cursor.close();
        return count;
    }*/

    //ADD NEW USER
    /*public void addtoUser(String UserName, String RegNo, String RoomNo) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contents = new ContentValues();
            contents.put(KEY_mUSERNAME, UserName);
            contents.put(KEY_mRegNo, RegNo);
            contents.put(KEY_mRoomNo, RoomNo);
            db.insert(TABLE_NAME_USER, null, contents);
            Log.d("Added", "Added to table");
            db.close();
        } catch (Exception e) {
            Log.d("Not Added", "Not Added to table");
        }
    }*/

    //CHECK FOR EXISTING REGNO IN USERTABLE
    /*public int checkmRegNoexists(String RegNo) {
        List<UserModule> userList = new ArrayList<>();
        Log.d("Query ", " SELECT * FROM " + TABLE_NAME_USER + " WHERE " + KEY_mRegNo + " = '" + RegNo + "'");
        String checkQuery = " SELECT * FROM " + TABLE_NAME_USER + " WHERE " + KEY_mRegNo + " = '" + RegNo + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(checkQuery, null);
        int count = cursor.getCount();
        db.close();
        cursor.close();
        return count;
    }*/

//RETRIEVE ALL USER DETAILS FROM USER TABLE
     /*public ArrayList<HashMap<String, String>> getUserDetails() {
        ArrayList<HashMap<String, String>> userList = new ArrayList<>();
        String selectDetails = "SELECT * FROM " + TABLE_NAME_USER + "";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectDetails, null);
        while (cursor.moveToNext()) {
            HashMap<String, String> um = new HashMap<>();
            um.put("Name", cursor.getString(cursor.getColumnIndex(KEY_mUSERNAME)));
            um.put("RegNo", cursor.getString(cursor.getColumnIndex(KEY_mRegNo)));
            um.put("RoomNo", cursor.getString(cursor.getColumnIndex(KEY_mRoomNo)));
            userList.add(um);
        }
        db.close();
        cursor.close();
        return userList;
    }*/
//DELETING THE USER DETAILS
    /*public void deleteUser(int regno){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_NAME_USER,KEY_mRegNo+" =?",new String[]{String.valueOf(regno)});
        db.close();
    }*/

    public ArrayList<HashMap<String,String>> getUserDetails(){
        dbref= FirebaseDatabase.getInstance().getReference().child("Users");

        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    HashMap<String,String>um=new HashMap<>();
                    um.put("Name",ds.child("mUsername").getValue().toString());
                    um.put("RegNo",ds.child("mRegNo").getValue().toString());
                    um.put("RoomNo",ds.child("mRoomNo").getValue().toString());
                    um.put("MobNo",ds.child("mMobNo").getValue().toString());
                    userlist.add(um);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return userlist;
    }

}



