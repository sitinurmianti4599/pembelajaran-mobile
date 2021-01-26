package com.example.hmjinventaris.ui.dashboard;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.hmjinventaris.Adapter.AlertDialogManager;
import com.example.hmjinventaris.MainActivity;
import com.example.hmjinventaris.R;
import com.example.hmjinventaris.database.DatabaseHelper;
import com.example.hmjinventaris.session.SessionManager;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private Button btnLogout;
    AlertDialogManager alert = new AlertDialogManager();
    SessionManager session;
    DatabaseHelper help;
    TextView Txuser;
    String id;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
//        final TextView textView = root.findViewById(R.id.text_dashboard);
//        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle saveInstanceState){
        super.onViewCreated(view, saveInstanceState);

//        help = new DatabaseHelper(getContext());
//        id = getArguments().getString(DatabaseHelper.COL_USERNAME);
//        Txuser = view.findViewById(R.id.txtUserprofil);

        session = new SessionManager(getContext());
        session.checkLogin();

        btnLogout = view.findViewById(R.id.out);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setTitle("Anda yakin ingin keluar ?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //finish();
                                session.logoutUser();
                            }
                        })
                        .setNegativeButton("Tidak", null)
                        .create();
                dialog.show();
            }
        });

        //getData();
    }


//    private void getData(){
//        Cursor cursor = help.oneData(id);
//        if(cursor.moveToFirst()){
//
//            String nama = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_USERNAME));
//
//            Txuser.setText(nama);
//
//
//        }
//    }

    @Override
    public void onActivityCreated(@NonNull Bundle saveInstanceState) {
        super.onActivityCreated(saveInstanceState);




    }
}