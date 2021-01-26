package com.example.hmjinventaris;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import android.view.MenuItem;

import android.widget.DatePicker;

import android.widget.Spinner;


import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddActivity extends AppCompatActivity {


    DBHelperSi helper;
    EditText TxNomor, TxNama, TxHarga, TxTanggal;
    Spinner SpKB;
    long id;
    DatePickerDialog datePickerDialog;
    SimpleDateFormat dateFormatter;
    ImageView imageView;
    Button btnsv,btncnl;
    TextView pilih;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        helper = new DBHelperSi(this);

        id = getIntent().getLongExtra(DBHelperSi.row_id, 0);

        TxNomor = (EditText)findViewById(R.id.txNomor_Add);
        TxNama = (EditText)findViewById(R.id.txNama_Add);
        TxHarga = (EditText)findViewById(R.id.txHarga_Add);
        TxTanggal = (EditText)findViewById(R.id.txTgl_Add);
        btnsv = (Button) findViewById(R.id.save_add);
        btncnl = (Button) findViewById(R.id.cancel);
        btncnl = (Button) findViewById(R.id.cancel);
        pilih = (TextView) findViewById(R.id.pf);

        SpKB = (Spinner)findViewById(R.id.spKB_Add);
        imageView = (ImageView) findViewById(R.id.image_barang);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        TxTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });

        pilih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.startPickImageActivity(AddActivity.this);
            }
        });

        btncnl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnsv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomor = TxNomor.getText().toString().trim();
                String nama = TxNama.getText().toString().trim();
                String harga = TxHarga.getText().toString().trim();
                String tanggal = TxTanggal.getText().toString().trim();
                String kb = SpKB.getSelectedItem().toString().trim();

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat simpleDate = new SimpleDateFormat("MMM dd, yyyy");
                String created = simpleDate.format(calendar.getTime());


                ContentValues values = new ContentValues();
                values.put(DBHelperSi.row_nomor, nomor);
                values.put(DBHelperSi.row_nama, nama);
                values.put(DBHelperSi.row_kb, kb);
                values.put(DBHelperSi.row_harga, harga);
                values.put(DBHelperSi.row_tgl, tanggal);
                values.put(DBHelperSi.row_foto, String.valueOf(uri));
                values.put(DBHelperSi.row_created, created);


                if (nomor.equals("") || nama.equals("") || harga.equals("") ||tanggal.equals("") ){
                    Toast.makeText(AddActivity.this, "Data Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                }else {
                    helper.insertData(values);
                    Toast.makeText(AddActivity.this, "Data Tersimpan", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });


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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.add_menu, menu);
//        return true;
//    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE
                && resultCode == Activity.RESULT_OK) {
            Uri imageuri = CropImage.getPickImageResultUri(this, data);
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageuri)) {
                uri = imageuri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}
                        , 0);
            } else {
                startCrop(imageuri);
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageView.setImageURI(result.getUri());
                uri = result.getUri();
            }
        }
    }

    private void startCrop(Uri imageuri) {
        CropImage.activity(imageuri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(this);
        uri = imageuri;
    }
}
