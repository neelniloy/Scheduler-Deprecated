package com.sarker.scheduler;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class Login extends AppCompatActivity {

    private TextView signUp;
    private TextView forgotPass;
    private EditText userEmail, userPassword;
    private ProgressDialog progressDialog;
    private CardView btnLogin;
    private FirebaseAuth mfirebaseAuth;
    private CheckBox saveLoginCheckBox;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    private ImageView vPass;
    private boolean isShowPassword = false;
    private String useremail,userpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signUp = findViewById(R.id.tv_signup);
        mfirebaseAuth = FirebaseAuth.getInstance();
        userEmail = findViewById(R.id.et_email);
        userPassword =  findViewById(R.id.et_pass);
        forgotPass = findViewById(R.id.tv_forgotpass) ;
        btnLogin = findViewById(R.id.cv_login);
        vPass = findViewById(R.id.imgLogPass);


        saveLoginCheckBox = (CheckBox)findViewById(R.id.saveLoginCheckBox);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        saveLogin = loginPreferences.getBoolean("saveLogin", false);

        if (saveLogin == true) {
            userEmail.setText(loginPreferences.getString("useremail", ""));
            userPassword.setText(loginPreferences.getString("userpassword", ""));
            saveLoginCheckBox.setChecked(true);
        }

        FirebaseUser mFirebaseuser = mfirebaseAuth.getCurrentUser();

        if (mFirebaseuser != null) {

            startActivity(new Intent(Login.this, MainActivity.class));
            finish();
        } else {

        }


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this, Registration.class);startActivity(i) ;
                finish();
            }
        });


        vPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowPassword) {
                    userPassword.setTransformationMethod(new PasswordTransformationMethod());
                    vPass.setImageDrawable(getResources().getDrawable(R.drawable.ic_pass_blue));
                    isShowPassword = false;
                }else{
                    userPassword.setTransformationMethod(null);
                    vPass.setImageDrawable(getResources().getDrawable(R.drawable.ic_pass_visibility_blue));
                    isShowPassword = true;
                }
            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(userEmail.getWindowToken(), 0);

                useremail = userEmail.getText().toString();
                userpassword = userPassword.getText().toString();

                if (saveLoginCheckBox.isChecked()) {
                    loginPrefsEditor.putBoolean("saveLogin", true);
                    loginPrefsEditor.putString("useremail", useremail);
                    loginPrefsEditor.putString("userpassword", userpassword);
                    loginPrefsEditor.commit();
                } else {
                    loginPrefsEditor.clear();
                    loginPrefsEditor.commit();
                }



                String email = userEmail.getText().toString();
                String password = userPassword.getText().toString();

                if (email.isEmpty()) {
                    userEmail.setError("invalid email");
                    userEmail.requestFocus();
                } else if (password.isEmpty()) {
                    userPassword.setError("invalid password");
                    userPassword.requestFocus();
                } else if (email.isEmpty() && password.isEmpty()) {

                    Toast.makeText(Login.this, "Enter a valid Email & Password", Toast.LENGTH_SHORT).show();
                }else if (!(isNetworkAvaliable(Login.this))) {
                    Toast.makeText(Login.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                }
                else if (!(email.isEmpty() && password.isEmpty())) {

                    progressDialog = new ProgressDialog(Login.this);
                    progressDialog.show();
                    progressDialog.setMessage("Logging in...");
                    progressDialog.setCanceledOnTouchOutside(false);

//                    progressDialog.setContentView(R.layout.logging_progressbar);
//                    progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                    mfirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                progressDialog.dismiss();
                                Toast.makeText(Login.this, "Login Failed", Toast.LENGTH_SHORT).show();
                            } else {

                                progressDialog.dismiss();
                                Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Login.this, MainActivity.class));
                                finish();
                            }

                        }
                    });
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(Login.this, "Error Occurred!", Toast.LENGTH_SHORT).show();
                }

            }

        });


        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText input = new EditText(Login.this);
                AlertDialog dialog = new AlertDialog.Builder(Login.this)
                        .setTitle("Reset Password")
                        .setMessage("\nEnter your registered email")
                        .setView(input)
                        .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String resetemail = input.getText().toString();

                                if(resetemail.isEmpty()){
                                    Toast.makeText(Login.this, "Empty Email!", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    mfirebaseAuth.sendPasswordResetEmail(resetemail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (!task.isSuccessful()) {
                                                Toast.makeText(Login.this, "Password Reset Failed", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(Login.this, "An Email Sent To Your Registered Email", Toast.LENGTH_LONG).show();
                                            }

                                        }
                                    });
                                }
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();

            }
        });
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