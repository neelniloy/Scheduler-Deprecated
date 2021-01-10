package com.sarker.scheduler;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sarker.scheduler.mainview.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class AddTaskFragment extends Fragment {

    private Button add,time,date;
    private String format,h1,m1,sTime,sDate,current_user_id;
    private ProgressDialog progressDialog;
    private TextInputEditText title,details;
    private TextInputLayout titleLayout, detailsLayout;
    private FirebaseAuth mAuth;
    private DatabaseReference myRoutine;


    public AddTaskFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_add_task, container, false);


        add = view.findViewById(R.id.btn_add_task);
        time = view.findViewById(R.id.cv_time);
        date = view.findViewById(R.id.cv_select_date);

        title = view.findViewById(R.id.et_title);
        details = view.findViewById(R.id.et_detail);

        titleLayout = view.findViewById(R.id.editTextTitleLayout);
        detailsLayout = view.findViewById(R.id.editTextDetailLayout);

        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();
        myRoutine = FirebaseDatabase.getInstance().getReference().child("Routine").child(current_user_id.substring(7,14));





        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(android.widget.TimePicker timePicker, int hour, int selectedMinute) {
                        if (hour == 0) {
                            hour += 12;
                            format = "AM";
                        } else if (hour == 12) {
                            format = "PM";
                        } else if (hour > 12) {
                            hour -= 12;
                            format = "PM";
                        } else {
                            format = "AM";
                        }

                        if(hour <10){
                            h1 = "0"+""+hour;
                        }else {
                            h1 = ""+hour;
                        }
                        if(selectedMinute <10){
                            m1 ="0"+""+selectedMinute;
                        }else {
                            m1 = ""+selectedMinute;
                        }

                        time.setText( h1 + ":" + m1+" "+format);
                        sTime = ""+h1 + ":" + ""+m1+" "+format ;
                    }

                }, hour, minute, DateFormat.is24HourFormat(getActivity()));
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String m = "";
                                if(monthOfYear==0){
                                    m = "Jan";
                                }
                                else if(monthOfYear==1){
                                    m = "Feb";
                                }
                                else if(monthOfYear==2){
                                    m = "Mar";
                                }
                                else if(monthOfYear==3){
                                    m = "Apr";
                                }
                                else if(monthOfYear==4){
                                    m = "May";
                                }
                                else if(monthOfYear==5){
                                    m = "Jun";
                                }
                                else if(monthOfYear==6){
                                    m = "Jul";
                                }
                                else if(monthOfYear==7){
                                    m = "Aug";
                                }
                                else if(monthOfYear==8){
                                    m = "Sep";
                                }
                                else if(monthOfYear==9){
                                    m = "Oct";
                                }
                                else if(monthOfYear==10){
                                    m = "Nov";
                                }
                                else if(monthOfYear==11){
                                    m = "Dec";
                                }

                                date.setText(dayOfMonth + "-" + m + "-" + year);
                                sDate = dayOfMonth + "-" + m + "-" + year;
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();


            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String t = title.getText().toString();
                String d = details.getText().toString();


                Random random = new Random();
                int randomNumber = random.nextInt(9999-9) + 9;
                String r = String.valueOf(randomNumber);

                if(sTime==null){
                    Toast.makeText(getActivity(), "Select Task Time", Toast.LENGTH_SHORT).show();
                }else if(sDate==null){
                    Toast.makeText(getActivity(), "Select Task Date", Toast.LENGTH_SHORT).show();
                }
                else if (t.isEmpty()){

                    if (t.isEmpty()) {
                        titleLayout.setError("empty field");
                        title.requestFocus();
                    }else {
                        titleLayout.setErrorEnabled(false);
                    }

//                    if (d.isEmpty()) {
//                        detailsLayout.setError("empty field");
//                        details.requestFocus();
//                    }else {
//                        detailsLayout.setErrorEnabled(false);
//                    }

                }
                else {

                    progressDialog = new ProgressDialog(getActivity());
                    progressDialog.show();
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setMessage("Adding Task...");

                    String saveCurrentDate, saveCurrentTime,status;


                    Calendar calFordDate = Calendar.getInstance();
                    SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
                    saveCurrentDate = currentDate.format(calFordDate.getTime());

                    SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
                    saveCurrentTime = currentTime.format(calFordDate.getTime());

                    if (getDateInMillis(saveCurrentDate+ " "+saveCurrentTime) < getDateInMillis(sDate+" "+sTime)){
                        status = "Pending";
                    }else {
                        status = "Missed";
                    }


                    if (d.isEmpty()) {
                        d = " ";
                    }


                    Map add = new HashMap();

                    add.put("time", sTime);
                    add.put("date", sDate);
                    add.put("title", t);
                    add.put("details", d);
                    add.put("status", status);
                    add.put("uid", current_user_id);

                    myRoutine.child("Own").child("Task").child(""+getDateInMillis(sDate+" "+sTime)).updateChildren(add);

                    Toast.makeText(getActivity(), "Task Added Successfully", Toast.LENGTH_SHORT).show();
                    Intent n = new Intent(getActivity(),Task.class);
                    startActivity(n);


                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            progressDialog.dismiss();

                            getActivity().finish();

                        }
                    },1200);

                }



            }
        });





        return view;
    }

    public static long getDateInMillis(String srcDate) {
        SimpleDateFormat desiredFormat = new SimpleDateFormat(
                "dd-MMMM-yyyy hh:mm aa");

        long dateInMillis = 0;
        try {
            Date date = desiredFormat.parse(srcDate);
            dateInMillis = date.getTime();
            return dateInMillis;
        } catch (ParseException | java.text.ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

}