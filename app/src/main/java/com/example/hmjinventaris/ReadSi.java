package com.example.hmjinventaris;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ReadSi extends AppCompatActivity {

    DBHelperSi helper;
    TextView TxNomor, TxNama, TxHarga, TxTanggal;
    TextView SpKB;
    long id;
    DatePickerDialog datePickerDialog;
    SimpleDateFormat dateFormatter;
    ImageView imageView;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_si);

        helper = new DBHelperSi(this);

        id = getIntent().getLongExtra(DBHelperSi.row_id, 0);

        TxNomor = (TextView)findViewById(R.id.txNomor);
        TxNama = (TextView) findViewById(R.id.txNama);
        TxHarga = (TextView) findViewById(R.id.txHarga);
        TxTanggal = (TextView) findViewById(R.id.txTgl);
        SpKB = (TextView) findViewById(R.id.txKondsi);
        imageView = (ImageView) findViewById(R.id.image_brg);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        TxTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });

        getData();
    }

    private void showDateDialog(){
        Calendar calendar = Calendar.getInstance();

        datePickerDialog =  new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                TxTanggal.setText(dateFormatter.format(newDate.getTime()));
            }
        },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void getData(){
        Cursor cursor = helper.oneData(id);
        if(cursor.moveToFirst()){
            String nomor = cursor.getString(cursor.getColumnIndex(DBHelperSi.row_nomor));
            String nama = cursor.getString(cursor.getColumnIndex(DBHelperSi.row_nama));
            String harga = cursor.getString(cursor.getColumnIndex(DBHelperSi.row_harga));
            String kb = cursor.getString(cursor.getColumnIndex(DBHelperSi.row_kb));
            String tanggal = cursor.getString(cursor.getColumnIndex(DBHelperSi.row_tgl));
            String foto = cursor.getString(cursor.getColumnIndex(DBHelperSi.row_foto));

            TxNama.setText(nama);
            if (foto.equals("null")){
                imageView.setImageResource(R.drawable.ic_image_24);
            }else{
                imageView.setImageURI(Uri.parse(foto));
            }

            TxNomor.setText(nomor);
            SpKB.setText(kb);

            TxHarga.setText(harga);
            TxTanggal.setText(tanggal);


        }
    }


}
