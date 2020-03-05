package com.example.lc;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class  viewuser_frag extends AppCompatActivity implements PullRefreshLayout.OnRefreshListener {
ListView listView;
TextView mno;
PullRefreshLayout swipe;
private ArrayList<HashMap<String,String>> userlist=new ArrayList<>();
private ArrayList<HashMap<String,String>> arrayList=new ArrayList<>();

DatabaseReference dbref;
FirebaseAuth fAuth;

public SimpleAdapter k;
public String nme,regNo,rmno,mbno,mnumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewuser_frag);


        dbref= FirebaseDatabase.getInstance().getReference().child("User");
        arrayList=getUserDetails();
        swipe=findViewById(R.id.swipeLayout);
        fAuth=FirebaseAuth.getInstance();
        listView=findViewById(R.id.viewuser_listview);
        mno=findViewById(R.id.ItemMobNo);

        //final SimpleAdapter adapter=new SimpleAdapter(viewuser_frag.this,arrayList,R.layout.list_items,new String[]{"Name","RegNo","RoomNo","MobNo"},new int[]{R.id.Itemname,R.id.Iteregno,R.id.ItemRmno,R.id.ItemMobNo});
        //listView.setAdapter(adapter);

        k=new SimpleAdapter(this,arrayList,R.layout.list_items,new String[]{"Name","RegNo","RoomNo","MobNo"},new int[]{R.id.Itemname,R.id.Iteregno,R.id.ItemRmno,R.id.ItemMobNo}){
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {

                View v=super.getView(position,convertView,parent);
                ImageButton dbt=v.findViewById(R.id.imgbtn);
                ImageButton edt=v.findViewById(R.id.editimgbtn);
                TextView phoneNumber=v.findViewById(R.id.ItemMobNo);

                final HashMap<String, String> ss= arrayList.get(position);

                dbt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        regNo=ss.get("RegNo");
                        final DatabaseReference dr=FirebaseDatabase.getInstance().getReference().child("Users");
                        dr.orderByChild("mRegNo").equalTo(regNo).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Toast.makeText(getApplicationContext(),dataSnapshot.getChildren().iterator().next().getKey(),Toast.LENGTH_SHORT).show();
                                String id=dataSnapshot.getChildren().iterator().next().getKey();
                                DatabaseReference dre= (DatabaseReference) FirebaseDatabase.getInstance().getReference("Users").child(id);
                                dre.removeValue();
                                recreate();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }
                });

                edt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        regNo=ss.get("RegNo");
                        rmno=ss.get("RoomNo");
                        mbno=ss.get("MobNo");
                        mnumber=mbno.replace("+91","");
                        nme=ss.get("Name");
                        final DatabaseReference dr=FirebaseDatabase.getInstance().getReference().child("Users");
                        dr.orderByChild("mRegNo").equalTo(regNo).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String idEdit=dataSnapshot.getChildren().iterator().next().getKey();
                                showCustomDialog(idEdit,position,nme,regNo,rmno,mnumber);


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        //Toast.makeText(getApplicationContext(),regNo,Toast.LENGTH_SHORT).show();

                        //
                    }
                });

                phoneNumber.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String pnum=ss.get("MobNo");
                        //Toast.makeText(getApplicationContext(),pnum,Toast.LENGTH_SHORT).show();
                        Intent callIntent=new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:"+pnum));
                        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(callIntent);
                    }
                });

                return v;
            }
        };


        swipe.setOnRefreshListener(this);

        swipe.post(new Runnable() {
           @Override
           public void run() {
              swipe.setRefreshing(true);
               listView.setAdapter(k);
               swipe.setRefreshing(false);
           }
         });

    }

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.logoutitem) {
            new AlertDialog.Builder(this).setTitle("Confirmation").setMessage("Are you sure you want to Logout?").setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    fAuth.signOut();
                    finish();
                    startActivity(new Intent(viewuser_frag.this,MainActivity.class));
                }
            }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).setIcon(android.R.drawable.ic_dialog_alert).show();

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onRefresh() {
        listView.setAdapter(k);
        swipe.setRefreshing(false);
    }

    protected void showCustomDialog(final String id, int position, String name, String rno, String rmno, String mno) {
        final Dialog dialog = new Dialog(viewuser_frag.this);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.customdialog);
        final EditText editNameText=dialog.findViewById(R.id.editName);
        final EditText editRegNoText=dialog.findViewById(R.id.editRegNo);
        final EditText editRoomNoText=dialog.findViewById(R.id.editRoomNo);
        final EditText editMobNoText=dialog.findViewById(R.id.editMobNo);

        editRegNoText.setEnabled(false);
        editNameText.setText(name);
        editRegNoText.setText(rno);
        editRoomNoText.setText(rmno);
        editMobNoText.setText(mno);

        Button button = (Button)dialog.findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final String newName=editNameText.getText().toString();
                final String newRegNo=editRegNoText.getText().toString();
                final String newRoomNo=editRoomNoText.getText().toString();
                final String newMobNo=editMobNoText.getText().toString();
                final String newConMobNo="+91"+newMobNo;
                DatabaseReference UpdateUserReference=FirebaseDatabase.getInstance().getReference("Users").child(id);
                UserModule um=new UserModule();
                um.setmUsername(newName);
                um.setmRegNo(newRegNo);
                um.setmRoomNo(newRoomNo);
                um.setmMobNo(newConMobNo);
                UpdateUserReference.setValue(um);
                Toast.makeText(getApplicationContext(),"User details updated successfully",Toast.LENGTH_SHORT).show();
                recreate();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void edit(String id){
        Toast.makeText(getApplicationContext(),id,Toast.LENGTH_SHORT).show();

    }


}