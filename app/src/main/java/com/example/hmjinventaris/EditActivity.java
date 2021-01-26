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

public class EditActivity extends AppCompatActivity {

    DBHelperSi helper;
    EditText TxNomor, TxNama, TxHarga, TxTanggal;
    Spinner SpKB;
    long id;
    TextView pilih;
    DatePickerDialog datePickerDialog;
    SimpleDateFormat dateFormatter;
    ImageView imageView;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        helper = new DBHelperSi(this);

        id = getIntent().getLongExtra(DBHelperSi.row_id, 0);

        TxNomor = (EditText)findViewById(R.id.txJumlah_Edit);
        TxNama = (EditText)findViewById(R.id.txNama_Edit);
        TxHarga = (EditText)findViewById(R.id.txHarga_Edit);
        TxTanggal = (EditText)findViewById(R.id.txTgl_Edit);
        SpKB = (Spinner)findViewById(R.id.spKB_Edit);
        imageView = (ImageView) findViewById(R.id.image_barang);
        pilih = (TextView)findViewById(R.id.ef);

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
                CropImage.startPickImageActivity(EditActivity.this);
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

            TxNomor.setText(nomor);
            TxNama.setText(nama);

            if (kb.equals("Pilih Status Barang")){
                SpKB.setSelection(0);
            }else if(kb.equals("Bagus")){
                SpKB.setSelection(1);
            }
            else if(kb.equals("Baru")){
                SpKB.setSelection(2);
            }else if(kb.equals("Usang")){
                SpKB.setSelection(3);
            }else if(kb.equals("Dalam Perbaikan")){
                SpKB.setSelection(4);
            }
            else if(kb.equals("Di Pinjam")){
                SpKB.setSelection(5);
            }else if(kb.equals("Di Buang")){
                SpKB.setSelection(6);
            }
            else if(kb.equals("Rusak")){
                SpKB.setSelection(7);
            }
            else if(kb.equals("Cacat")){
                SpKB.setSelection(8);
            }else if(kb.equals("Hilang")){
                SpKB.setSelection(9);
            }

            TxHarga.setText(harga);
            TxTanggal.setText(tanggal);

            if (foto.equals("null")){
                imageView.setImageResource(R.drawable.ic_image_24);
            }else{
                imageView.setImageURI(Uri.parse(foto));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_edit:
                String nomor = TxNomor.getText().toString().trim();
                String nama = TxNama.getText().toString().trim();
                String harga = TxHarga.getText().toString().trim();
                String tanggal = TxTanggal.getText().toString().trim();
                String kb = SpKB.getSelectedItem().toString().trim();

                ContentValues values = new ContentValues();
                values.put(DBHelperSi.row_nomor, nomor);
                values.put(DBHelperSi.row_nama, nama);
                values.put(DBHelperSi.row_harga, harga);
                values.put(DBHelperSi.row_tgl, tanggal);
                values.put(DBHelperSi.row_kb, kb);
                values.put(DBHelperSi.row_foto, String.valueOf(uri));

                if (nomor.equals("") || nama.equals("") || harga.equals("") ||tanggal.equals("")){
                    Toast.makeText(EditActivity.this, "Data Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                }else {
                    helper.updateData(values, id);
                    Toast.makeText(EditActivity.this, "Data Tersimpan", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
        switch (item.getItemId()){
            case R.id.delete_edit:
                AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                builder.setMessage("Data ini akan dihapus.");
                builder.setCancelable(true);
                builder.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        helper.deleteData(id);
                        Toast.makeText(EditActivity.this, "Data Terhapus", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

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
