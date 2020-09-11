package com.sarker.scheduler;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class Registration extends AppCompatActivity {

    private TextView logIn;
    private TextInputEditText userName, userEmail, userPass;
    private TextInputLayout nameEditTextLayout, emailEditTextLayout, passEditTextLayout;
    private Button btnSignup;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private String user_id;
    private DatabaseReference current_user_db,myRoutine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        logIn = findViewById(R.id.tv_login);
        btnSignup = findViewById(R.id.cv_signup);
        userEmail = findViewById(R.id.et_remail);
        userName = findViewById(R.id.et_name);
        userPass = findViewById(R.id.et_rpass);
        nameEditTextLayout = findViewById(R.id.editTextNameLayout);
        emailEditTextLayout = findViewById(R.id.editTextSignUpEmailLayout);
        passEditTextLayout = findViewById(R.id.editTextSignUpPassLayout);


        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Registration.this, Login.class);startActivity(i) ;
                finish();
            }
        });



        mAuth = FirebaseAuth.getInstance();


        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String email = userEmail.getText().toString();
                String password = userPass.getText().toString();
                String fname = userName.getText().toString();

                 if (email.isEmpty() && password.isEmpty()) {

                     if (fname.isEmpty()) {
                         nameEditTextLayout.setError("*Name required");
                         userName.requestFocus();

                     } else{
                         nameEditTextLayout.setErrorEnabled(false);
                     }

                     if (email.isEmpty()) {
                         emailEditTextLayout.setError("*Email required");
                         userEmail.requestFocus();
                     } else{
                         emailEditTextLayout.setErrorEnabled(false);
                     }

                     if (password.isEmpty()) {
                         passEditTextLayout.setError("*Password required");
                         userPass.requestFocus();
                     } else{
                         passEditTextLayout.setErrorEnabled(false);
                     }

                }else if (!(isNetworkAvaliable(Registration.this))) {
                    Toast.makeText(Registration.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                }
                else if (!(email.isEmpty() && password.isEmpty())) {

                    progressDialog = new ProgressDialog(Registration.this);
                    progressDialog.show();
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setTitle("Creating Account");
                    progressDialog.setMessage("Please wait a moment. We are creating your account");

                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(Registration.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                progressDialog.dismiss();
                                Toast.makeText(Registration.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                            } else {

                                sendUserData();

                            }
                        }
                    });
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(Registration.this, "Error Occurred!", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    private void sendUserData() {

        user_id = mAuth.getCurrentUser().getUid();
        current_user_db = FirebaseDatabase.getInstance().getReference().child("UsersData").child(user_id);
        myRoutine = FirebaseDatabase.getInstance().getReference().child("Routine");


        String fname = userName.getText().toString();
        String email = userEmail.getText().toString();
        String routineKey = user_id.substring(7,14);
        String saveCurrentDate, saveCurrentTime;


        Calendar calFordDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(calFordDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        saveCurrentTime = currentTime.format(calFordDate.getTime());

        Map reg = new HashMap();

        reg.put("userName", fname);
        reg.put("userEmail", email);
        reg.put("routineKey", routineKey);
        reg.put("userUID", user_id);
        reg.put("userMembershipTime",saveCurrentTime);
        reg.put("userMembershipDate",saveCurrentDate);

        current_user_db.updateChildren(reg);

        Map key = new HashMap();

        key.put("isKey", "true");

        myRoutine.child(routineKey).updateChildren(key);


        progressDialog.dismiss();
        Toast.makeText(Registration.this, "Registration Successful", Toast.LENGTH_SHORT).show();

        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(Registration.this, Login.class));
        finish();


    }

    public static boolean isNetworkAvaliable(Context ctx) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if ((connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null && connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED)
                || (connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null && connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState() == NetworkInfo.State.CONNECTED)) {
            return true;
        } else {
            return false;
        }
    }
}