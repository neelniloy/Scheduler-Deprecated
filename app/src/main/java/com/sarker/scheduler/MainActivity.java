package com.sarker.scheduler;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private long backPressedTime;
    private Toast backtoast;
    private DrawerLayout mdrawerLayout;
    FirebaseAuth mAuth;
    private TextView name, email;
    private String user_id,routineKey,day;
    private DatabaseReference current_user_db,update;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabItem saturday,sunday,monday,tuesday,wednesday,thursday;
    public PagerAdapter pagerAdapter;

    private static int SIGN_OUT = 2500;
    private ProgressDialog progressDialog1;
    private String url;
    private ProgressDialog progressDialog;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        name = headerView.findViewById(R.id.name);
        email = headerView.findViewById(R.id.email);

        Toolbar toolbar = findViewById(R.id.tool_bar);
        //setSupportActionBar(toolbar);

        mdrawerLayout = findViewById(R.id.drawer);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mdrawerLayout, toolbar, R.string.open, R.string.close);

        mdrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();

        user_id = mAuth.getCurrentUser().getUid();
        current_user_db = FirebaseDatabase.getInstance().getReference().child("UsersData").child(user_id);
        update = FirebaseDatabase.getInstance().getReference().child("CheckUpdate");

        current_user_db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String n = snapshot.child("userName").getValue(String.class);
                String e = snapshot.child("userEmail").getValue(String.class);
                routineKey = snapshot.child("routineKey").getValue(String.class);

                name.setText(n);
                email.setText(e);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);
        saturday = findViewById(R.id.saturday);
        sunday = findViewById(R.id.sunday);
        monday = findViewById(R.id.monday);
        tuesday = findViewById(R.id.tuesday);
        wednesday = findViewById(R.id.wednesday);
        thursday = findViewById(R.id.thursday);

        pagerAdapter = new PageAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);

        Intent i = getIntent();
        day = i.getStringExtra("day");

        if(day!=null) {

            switch (day) {

                case "Saturday":
                    viewPager.setCurrentItem(0);
                    break;

                case "Sunday":
                    viewPager.setCurrentItem(1);
                    break;

                case "Monday":
                    viewPager.setCurrentItem(2);
                    break;

                case "Tuesday":
                    viewPager.setCurrentItem(3);
                    break;

                case "Wednesday":
                    viewPager.setCurrentItem(4);
                    break;
                case "Thursday":
                    viewPager.setCurrentItem(5);
                    break;

                default:
                    return;
            }

        }else {
            Calendar c = Calendar.getInstance();
            int date = c.get(Calendar.DAY_OF_WEEK);

            switch(date){

                case Calendar.SATURDAY:
                    viewPager.setCurrentItem(0);
                    break;

                case Calendar.SUNDAY:
                    viewPager.setCurrentItem(1);
                    break;

                case Calendar.MONDAY:
                    viewPager.setCurrentItem(2);
                    break;

                case Calendar.TUESDAY:
                    viewPager.setCurrentItem(3);
                    break;

                case Calendar.WEDNESDAY:
                    viewPager.setCurrentItem(4);
                    break;
                case Calendar.THURSDAY:
                    viewPager.setCurrentItem(5);
                    break;

                case Calendar.FRIDAY:
                    viewPager.setCurrentItem(0);
                    break;

                default:
                    return;

            }

        }


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());
                if(tab.getPosition()==0)
                {
                    pagerAdapter.notifyDataSetChanged();

                }
                else if(tab.getPosition()==1)
                {
                    pagerAdapter.notifyDataSetChanged();
                }
                else if(tab.getPosition()==2)
                {
                    pagerAdapter.notifyDataSetChanged();
                }
                else if(tab.getPosition()==3)
                {
                    pagerAdapter.notifyDataSetChanged();
                }
                else if(tab.getPosition()==4)
                {
                    pagerAdapter.notifyDataSetChanged();
                }
                else if(tab.getPosition()==5)
                {
                    pagerAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        Intent n;
        switch (menuItem.getItemId()) {

            case R.id.nav_add_routine : n = new Intent(this, AddRoutine.class); startActivity(n);
                break;

            case R.id.nav_manage_routine:
                n = new Intent(this, ManageRoutine.class); startActivity(n);
                break;

            case R.id.nav_import_routine : n = new Intent(this, ImportRoutine.class); startActivity(n);
                break;

            case R.id.nav_check_update:

                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Checking...");
                progressDialog.show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        update.addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                String version = dataSnapshot.child("version").getValue(String.class);
                                url = dataSnapshot.child("url").getValue(String.class);

                                String VersionName = BuildConfig.VERSION_NAME;

                                if (version.equals(VersionName)) {

                                    Toast.makeText(MainActivity.this, "Scheduler Is Up To Date", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();

                                } else{
                                    progressDialog.dismiss();

                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                    builder.setTitle("New Version Available");
                                    builder.setIcon(R.drawable.logo);
                                    builder.setCancelable(true);
                                    builder.setMessage("Update Scheduler App For Better Experience")
                                            .setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                                    intent.setData(Uri.parse(url));
                                                    startActivity(intent);

                                                    finish();

                                                }
                                            }).setNegativeButton("Not Now",new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            dialog.cancel();
                                        }

                                    });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(MainActivity.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                },SIGN_OUT);

                break;

            case R.id.nav_about :

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("About Scheduler");
                builder.setIcon(R.drawable.logo);
                builder.setCancelable(true);
                builder.setMessage("Basically this app made to manage our class routine easily. This is mainly a cloud base app. It has also offline access feature. So whenever you want to see the class schedule or added a new schedule you can do it without an internet connection.\n\nDeveloper - Niloy Kumar Sarker\n")
                        .setPositiveButton("Visit Me", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse("https://web.facebook.com/N33LNILOY/"));
                                startActivity(intent);

                                dialog.cancel();

                            }
                        }).setNegativeButton("No Thanks",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }

                });
                AlertDialog alert = builder.create();
                alert.show();

                break;

            case R.id.nav_logout:

                progressDialog1 = new ProgressDialog(MainActivity.this);
                progressDialog1.show();
                progressDialog1.setMessage("Signing Out...");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog1.dismiss();
                        finish();
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(MainActivity.this, Login.class));
                    }
                },SIGN_OUT);

                break;

            case R.id.nav_report_bug:

                String[] toemail = {"niloy64529@gmail.com"};

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, toemail);
                intent.putExtra(Intent.EXTRA_SUBJECT, "Bug In \"Scheduler\" App");
                intent.setType("message/rfc822");
                startActivity(Intent.createChooser(intent, "Choose an email client"));

                break;
            case R.id.nav_my_routine_key:

                n = new Intent(this, MyRoutineKey.class); startActivity(n);
                break;

        }
        return true;
    }


    @Override
    public void onBackPressed() {

        if (mdrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mdrawerLayout.closeDrawer(GravityCompat.START);
        } else if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backtoast.cancel();
            super.onBackPressed();
            finish();
            return;
        } else {
            backtoast = Toast.makeText(MainActivity.this, "Press Again To Exit", Toast.LENGTH_SHORT);
            backtoast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }


}