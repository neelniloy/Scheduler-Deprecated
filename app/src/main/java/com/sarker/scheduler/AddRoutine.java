package com.sarker.scheduler;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AddRoutine extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String[] users = { "Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday" };
    private ImageView back;
    private EditText courseName,courseCode,courseTeacher,roomNo;
    private TextView startTime,endTime;
    private CardView start,end,addRoutine;
    private String day,current_user_id,sTime,eTime,format,h1,m1;
    private FirebaseAuth mAuth;
    private DatabaseReference myRoutine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_routine);

        back = findViewById(R.id.back);
        courseName = findViewById(R.id.et_course_name);
        courseCode = findViewById(R.id.et_course_code);
        courseTeacher = findViewById(R.id.et_course_teacher);
        roomNo = findViewById(R.id.et_room_no);

        startTime = findViewById(R.id.start_time);
        endTime = findViewById(R.id.end_time);
        start = findViewById(R.id.cv_start_time);
        end = findViewById(R.id.cv_end_time);
        addRoutine = findViewById(R.id.cv_add_routine);

        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();
        myRoutine = FirebaseDatabase.getInstance().getReference().child("Routine").child(current_user_id.substring(7,14));

        Spinner spin = findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, users);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(this);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddRoutine.this, new TimePickerDialog.OnTimeSetListener() {
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

                        startTime.setText( h1 + ":" + m1+" "+format);
                        sTime = ""+h1 + ":" + ""+m1+" "+format ;
                    }

                }, hour, minute, DateFormat.is24HourFormat(AddRoutine.this));
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddRoutine.this, new TimePickerDialog.OnTimeSetListener() {
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

                        endTime.setText( h1 + ":" + m1+" "+format);
                       eTime = ""+h1 + ":" + ""+m1+" "+format ;

                    }

                }, hour, minute, DateFormat.is24HourFormat(AddRoutine.this));
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        addRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = courseName.getText().toString();
                String code = courseCode.getText().toString();
                String teacher = courseTeacher.getText().toString();
                String room = roomNo.getText().toString();

                Random random = new Random();
                int randomNumber = random.nextInt(9999-9) + 9;
                String r = String.valueOf(randomNumber);

                if(sTime==null){
                    Toast.makeText(AddRoutine.this, "Select Class Start Time", Toast.LENGTH_SHORT).show();
                }else if(eTime==null){
                    Toast.makeText(AddRoutine.this, "Select Class End Time", Toast.LENGTH_SHORT).show();
                }else if (name.isEmpty()) {
                    courseName.setError("empty field");
                    courseName.requestFocus();
                } else if (code.isEmpty()) {
                    courseCode.setError("empty field");
                    courseCode.requestFocus();
                }else if (teacher.isEmpty()) {
                    courseTeacher.setError("empty field");
                    courseTeacher.requestFocus();
                } else if (room.isEmpty()) {
                    roomNo.setError("empty field");
                    roomNo.requestFocus();
                }else {

                    Map add = new HashMap();

                    add.put("alarm", "false");
                    add.put("classTime", sTime+" - "+eTime);
                    add.put("courseCode", code);
                    add.put("courseTeacher", teacher);
                    add.put("courseName", name);
                    add.put("day", day);
                    add.put("roomNo", room);
                    add.put("randomKey", r);

                    myRoutine.child(day).push().updateChildren(add);

                    Toast.makeText(AddRoutine.this, "Routine Added Successfully", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(AddRoutine.this, MainActivity.class);
                    intent.putExtra("day", day);
                    AddRoutine.this.startActivity(intent);
                }

            }
        });


    }
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        day = users[position] ;
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO - Custom Code
    }

}

