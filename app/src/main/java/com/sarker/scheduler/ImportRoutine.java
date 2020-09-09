package com.sarker.scheduler;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class ImportRoutine extends AppCompatActivity {
    private ImageView back;
    private EditText key;
    private CardView btnImport;
    private FirebaseAuth mAuth;
    private DatabaseReference user,sender;
    private String current_user_id;
    private static int DELAY_TIME= 2100;
    private ProgressDialog progressDialog1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_routine);

        back = findViewById(R.id.back);
        key = findViewById(R.id.et_key);
        btnImport = findViewById(R.id.cv_import);

        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();
        user = FirebaseDatabase.getInstance().getReference().child("Routine").child(current_user_id.substring(7,14));
        sender = FirebaseDatabase.getInstance().getReference().child("Routine");


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(key.getWindowToken(), 0);

                final String rKey = key.getText().toString();

                if(rKey.isEmpty()) {
                    key.setError("empty key");
                    key.requestFocus();
                    key.setHint("");
                }else {

                    progressDialog1 = new ProgressDialog(ImportRoutine.this);
                    progressDialog1.show();
                    progressDialog1.setMessage("Importing Routine...");

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            progressDialog1.dismiss();

                            sender.child(rKey).addListenerForSingleValueEvent(new ValueEventListener()  {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()){

                                        user.setValue(dataSnapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                Toast.makeText(ImportRoutine.this, "Import Successfully", Toast.LENGTH_SHORT).show();

                                                Intent n = new Intent(ImportRoutine.this, MainActivity.class); startActivity(n);
                                            }
                                        });
                                    }
                                    else {
                                        Toast.makeText(ImportRoutine.this, "Invalid Routine Key", Toast.LENGTH_SHORT).show();
                                    }

                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {}
                            });
                        }
                    },DELAY_TIME);

                }
            }
        });
    }
}