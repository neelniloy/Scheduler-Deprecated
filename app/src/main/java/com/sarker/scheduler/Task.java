package com.sarker.scheduler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Task extends AppCompatActivity {


    private FrameLayout frameLayout;
    private BottomNavigationView bottomNavigationView;
    private AddTaskFragment addTaskFragment;
    private ViewTaskFragment viewTaskFragment;
    private ImageView back;
    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        //Initialization

        bottomNavigationView = findViewById(R.id.bottom_nav);
        frameLayout = findViewById(R.id.main_fram);
        back = findViewById(R.id.back);
        textView = findViewById(R.id.task_text);

        addTaskFragment = new AddTaskFragment();
        viewTaskFragment = new ViewTaskFragment();


        //End

        //set default fragment
        setFragment(addTaskFragment);
        textView.setText("Add New Task");

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){

                    case R.id.nav_add_task : {
                        //bottomNavigationView.setItemBackgroundResource(R.color.colorPrimary);
                        setFragment(addTaskFragment);
                        textView.setText("Add New Task");
                        return true;
                    }
                    case R.id.nav_show_task : {
                        //bottomNavigationView.setItemBackgroundResource(R.color.colorPrimary);
                        setFragment(viewTaskFragment);
                        textView.setText("My Tasks");
                        return true;
                    }

                    default:
                        return false;

                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_fram,fragment);
        fragmentTransaction.commit();
    }


}