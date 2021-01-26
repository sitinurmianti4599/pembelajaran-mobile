package com.example.hmjinventaris;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class CustomCursorAdapter extends CursorAdapter {

    private LayoutInflater layoutInflater;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public CustomCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View v = layoutInflater.inflate(R.layout.row_data, viewGroup, false);
        MyHolder holder = new MyHolder();
        holder.ListID = (TextView)v.findViewById(R.id.listID);
        holder.ListNama = (TextView)v.findViewById(R.id.listNama);
//       holder.ListImg = (ImageView)v.findViewById(R.id.list_img);
        holder.ListKB = (TextView)v.findViewById(R.id.list_kondisi);
        holder.ListCreated = (TextView)v.findViewById(R.id.listCreated);



        v.setTag(holder);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        MyHolder holder = (MyHolder)view.getTag();

//        holder.ListImg.setImageResource(cursor.getString(cursor.getColumnIndex(DBHelper.row_foto)));
        holder.ListKB.setText(cursor.getString(cursor.getColumnIndex(DBHelperSi.row_kb)));
        holder.ListID.setText(cursor.getString(cursor.getColumnIndex(DBHelperSi.row_id)));
        holder.ListNama.setText(cursor.getString(cursor.getColumnIndex(DBHelperSi.row_nama)));
        holder.ListCreated.setText(cursor.getString(cursor.getColumnIndex(DBHelperSi.row_created)));

    }

    class MyHolder{
        TextView ListID;
        TextView ListNama;
        //        ImageView ListImg;
        TextView ListKB;
        TextView ListCreated;
    }
}
