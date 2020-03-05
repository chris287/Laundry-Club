package com.example.lc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;

public class  viewuser_listview_adapter extends BaseAdapter {
    private Context context;
    ArrayList<HashMap<String, String>> array;
    ImageButton del, edit;
    Button bt;
    TextView mob;
    DataBaseHandler dbh;
    DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users");

    public viewuser_listview_adapter(Context context, ArrayList<HashMap<String, String>> array) {
        this.context = context;
        this.array = array;


    }

    @Override
    public int getCount() {
        array=dbh.getUserDetails();
        return array.size();
    }

    @Override
    public Object getItem(int position) {
        return array.get(position);
    }

    @Override
    public long getItemId(int position) {
        return array.indexOf(array.get(position));
    }

    /*static class ViewHolder{
        private TextView delete;
    }*/

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //ViewHolder mViewHolder=null;
        //HashMap<String,String> user=null;
        /*if(convertView==null) {

            //user = new HashMap<String, String>();
            //mViewHolder = new ViewHolder();
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.list_items, null);
            //mViewHolder.delete = convertView.findViewById(R.id.ItemMobNo);
            //convertView.setTag(mViewHolder);
        mob=convertView.findViewById(R.id.ItemMobNo);
            mob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,"hvgvm",Toast.LENGTH_SHORT).show();
                    Log.d("bvgvg","uvjvjhbhj");
                }
            });
        }*/
        return convertView;
    }
}


