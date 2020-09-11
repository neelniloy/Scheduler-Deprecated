package com.sarker.scheduler;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class EditRoutine extends AppCompatActivity {

    private ImageView back;
    private TextInputEditText courseName,courseCode,courseTeacher,roomNo;
    private TextInputLayout courseNameLayout, courseCodeLayout ,courseTeacherLayout ,roomNoLayout;
    private TextView startTime,endTime,dayE;
    private Button start,end,upRoutine;
    private String day,current_user_id,sTime,eTime,format,h1,m1,alarm,classTime,cName,cTeacher,cCode,rNo,ranKey;
    private FirebaseAuth mAuth;
    private DatabaseReference myRoutine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_routine);

        back = findViewById(R.id.back);
        courseName = findViewById(R.id.et_course_nameE);
        courseCode = findViewById(R.id.et_course_codeE);
        courseTeacher = findViewById(R.id.et_course_teacherE);
        roomNo = findViewById(R.id.et_room_noE);

        courseNameLayout = findViewById(R.id.editTextCourseNameE);
        courseCodeLayout = findViewById(R.id.editTextCourseCodeE);
        courseTeacherLayout = findViewById(R.id.editTextCourseTeacherNameE);
        roomNoLayout = findViewById(R.id.editTextCourseRoomNoE);

        startTime = findViewById(R.id.start_timeE);
        endTime = findViewById(R.id.end_timeE);
        start = findViewById(R.id.cv_start_timeE);
        end = findViewById(R.id.cv_end_timeE);
        upRoutine = findViewById(R.id.cv_update_routine);
        dayE = findViewById(R.id.dayE);

        Intent i = getIntent();
        day = i.getStringExtra("day");
        String key = i.getStringExtra("key");

        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();
        myRoutine = FirebaseDatabase.getInstance().getReference().child("Routine").child(current_user_id.substring(7,14)).child(day).child(key);

        myRoutine.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                 alarm = snapshot.child("alarm").getValue(String.class);
                 classTime = snapshot.child("classTime").getValue(String.class);
                cCode = snapshot.child("courseCode").getValue(String.class);
                 cTeacher = snapshot.child("courseTeacher").getValue(String.class);
                cName = snapshot.child("courseName").getValue(String.class);
                day = snapshot.child("day").getValue(String.class);
                rNo = snapshot.child("roomNo").getValue(String.class);
                ranKey = snapshot.child("randomKey").getValue(String.class);

                sTime = classTime.substring(0,8);
                eTime = classTime.substring(11,19);

                start.setText(classTime.substring(0,8));
                end.setText(classTime.substring(11,19));
                dayE.setText(day);
                courseName.setText(cName);
                courseCode.setText(cCode);
                courseTeacher.setText(cTeacher);
                roomNo.setText(rNo);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


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
                mTimePicker = new TimePickerDialog(EditRoutine.this, new TimePickerDialog.OnTimeSetListener() {
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

                        start.setText( h1 + ":" + m1+" "+format);
                        sTime = ""+h1 + ":" + ""+m1+" "+format ;
                    }

                }, hour, minute, DateFormat.is24HourFormat(EditRoutine.this));
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
                mTimePicker = new TimePickerDialog(EditRoutine.this, new TimePickerDialog.OnTimeSetListener() {
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

                        end.setText( h1 + ":" + m1+" "+format);
                        eTime = ""+h1 + ":" + ""+m1+" "+format ;

                    }

                }, hour, minute, DateFormat.is24HourFormat(EditRoutine.this));
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        upRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = courseName.getText().toString();
                String code = courseCode.getText().toString();
                String teacher = courseTeacher.getText().toString();
                String room = roomNo.getText().toString();

                if (name.isEmpty() || code.isEmpty() || teacher.isEmpty() || room.isEmpty()){

                    if (name.isEmpty()) {
                        courseNameLayout.setError("empty field");
                        courseName.requestFocus();
                    }else {
                        courseNameLayout.setErrorEnabled(false);
                    }

                    if (code.isEmpty()) {
                        courseCodeLayout.setError("empty field");
                        courseCode.requestFocus();
                    }else {
                        courseCodeLayout.setErrorEnabled(false);
                    }

                    if (teacher.isEmpty()) {
                        courseTeacherLayout.setError("empty field");
                        courseTeacher.requestFocus();
                    }
                    else {
                        courseTeacherLayout.setErrorEnabled(false);
                    }

                    if (room.isEmpty()) {
                        roomNoLayout.setError("empty field");
                        roomNo.requestFocus();
                    }
                    else {
                        roomNoLayout.setErrorEnabled(false);
                    }

                }
                else {

                    Map add = new HashMap();

                    add.put("alarm", alarm);
                    add.put("classTime", sTime+" - "+eTime);
                    add.put("courseCode", code);
                    add.put("courseTeacher", teacher);
                    add.put("courseName", name);
                    add.put("day", day);
                    add.put("roomNo", room);
                    add.put("randomKey", ranKey);


                    myRoutine.updateChildren(add);

                    Toast.makeText(EditRoutine.this, "Routine Update Successfully", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(EditRoutine.this, MainActivity.class);
                    intent.putExtra("day", day);
                    EditRoutine.this.startActivity(intent);
                }

            }
        });


    }
}