package com.sarker.scheduler;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ManageRoutine extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ManageRoutineAdapter mAdapter;
    private DatabaseReference mDatabaseRef;
    private ArrayList<RoutineInfo> mList;
    private ProgressBar mProgressCircle;
    private ImageView back;
    private TextView emptyRoutine;
    private FirebaseAuth mAuth;
    private String current_user_id;
    private TextView accessKey;
    private Button btnDelete;
    private ProgressDialog progressDialog;

    private String[] day = { "Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday" };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_routine);

        accessKey = findViewById(R.id.tv_access_key);
        btnDelete = findViewById(R.id.btn_import_delete);

        emptyRoutine = findViewById(R.id.empty_routine);
        mRecyclerView = findViewById(R.id.mrecycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mList = new ArrayList<>();
        mAdapter = new ManageRoutineAdapter(this,mList);
        mRecyclerView.setAdapter(mAdapter);


        mProgressCircle = findViewById(R.id.mprogress_circle);
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid().substring(7,14);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Routine").child(current_user_id);



        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                long count = dataSnapshot.getChildrenCount();

                if (dataSnapshot.child("Import").exists() && !dataSnapshot.child("Import").getValue().toString().equals(" ")){
                   String key = dataSnapshot.child("Import").getValue(String.class);
                   accessKey.setText("Access Key : "+key);
                    accessKey.setVisibility(View.VISIBLE);
                    btnDelete.setVisibility(View.VISIBLE);

                }
                else {
                    if(!dataSnapshot.child("Own").exists()){

                        emptyRoutine.setVisibility(View.VISIBLE);
                        mProgressCircle.setVisibility(View.INVISIBLE);

                    }else {
                        emptyRoutine.setVisibility(View.GONE);
                        mProgressCircle.setVisibility(View.INVISIBLE);
                    }


                }



                if(count>1){
                    if(dataSnapshot.exists()){

                        mList.clear();
                        mAdapter.notifyDataSetChanged();

                        for (int i=0;i<6;i++){

                            String days_of_week = day[i];

                            for (DataSnapshot postSnapshot : dataSnapshot.child("Own").child(days_of_week).getChildren()) {

                                RoutineInfo info = postSnapshot.getValue(RoutineInfo.class);
                                info.setRoutineKey(postSnapshot.getKey());
                                mList.add(info);

                            }

                        }

                        mAdapter.notifyDataSetChanged();
                        mProgressCircle.setVisibility(View.INVISIBLE);

                    }
                }
                else {

                    emptyRoutine.setVisibility(View.VISIBLE);
                    mProgressCircle.setVisibility(View.INVISIBLE);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ManageRoutine.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }

        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog = new ProgressDialog(ManageRoutine.this);
                progressDialog.show();
                progressDialog.setMessage("Removing...");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        progressDialog.dismiss();

                        accessKey.setVisibility(View.GONE);
                        btnDelete.setVisibility(View.GONE);
                        mDatabaseRef.child("Import").setValue(" ");

                        Toast.makeText(ManageRoutine.this, "Successfully Removed", Toast.LENGTH_SHORT).show();

                    }
                },1500);

            }
        });


    }
}