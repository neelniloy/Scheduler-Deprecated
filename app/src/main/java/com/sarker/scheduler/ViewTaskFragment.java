package com.sarker.scheduler;

import android.app.ProgressDialog;
import android.net.ParseException;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;


public class ViewTaskFragment extends Fragment {

    public ViewTaskFragment() {
        // Required empty public constructor
    }

    private RecyclerView mRecyclerView;
    private TaskAdapter tAdapter;
    private DatabaseReference mDatabaseRef,importRef;
    private ArrayList<TaskInfo> mList;
    private ProgressBar mProgressCircle;
    private TextView emptyRoutine;
    private FirebaseAuth mAuth;
    private String current_user_id,importKey = " ";

    private ArrayList<TaskInfo> first = new ArrayList<TaskInfo>();
    private ArrayList<TaskInfo> last = new ArrayList<TaskInfo>();
    //private ArrayList<TaskInfo> impTask = new ArrayList<TaskInfo>();




    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_task, container, false);


        emptyRoutine = view.findViewById(R.id.empty_task);
        mRecyclerView = view.findViewById(R.id.trecycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        mList = new ArrayList<>();
        tAdapter = new TaskAdapter(getActivity(),mList);
        mRecyclerView.setAdapter(tAdapter);

        mProgressCircle = view.findViewById(R.id.tprogress_circle);



        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid().substring(7,14);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Routine").child(current_user_id);
        importRef = FirebaseDatabase.getInstance().getReference("Routine");


        Query query = mDatabaseRef.orderByChild("status");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {


                if(dataSnapshot.child("Import").exists()){

                    mList.clear();
                    importKey = dataSnapshot.child("Import").getValue().toString();

                    importRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot2) {

                            if(dataSnapshot2.child(importKey).child("Own").child("Task").exists()){
                                tAdapter.notifyDataSetChanged();

                                Calendar calFordDate = Calendar.getInstance();
                                SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
                                String saveCurrentDate = currentDate.format(calFordDate.getTime());

                                SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
                                String saveCurrentTime = currentTime.format(calFordDate.getTime());

                                for (DataSnapshot postSnapshot : dataSnapshot2.child(importKey).child("Own").child("Task").getChildren()) {

                                    TaskInfo info = postSnapshot.getValue(TaskInfo.class);

                                    info.setKey(postSnapshot.getKey());

                                    if (getDateInMillis(saveCurrentDate+ " "+saveCurrentTime) < getDateInMillis(info.getDate()+" "+info.getTime())){

                                        first.add(info);

                                        saveCurrentDate = info.getDate();
                                        saveCurrentTime = info.getTime();


                                    }else {
                                        last.add(info);
                                    }

                                }

                                Collections.reverse(first);

                                mList.addAll(last);
                                mList.addAll(first);

                                first.clear();
                                last.clear();

                                tAdapter.notifyDataSetChanged();
                                mProgressCircle.setVisibility(View.INVISIBLE);

                            }
                            else {

                                if(!dataSnapshot.child("Own").child("Task").exists()){
                                    emptyRoutine.setVisibility(View.VISIBLE);
                                }

                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            mProgressCircle.setVisibility(View.INVISIBLE);
                        }

                    });
                }


                    if(dataSnapshot.exists()){

                        mList.clear();
                        tAdapter.notifyDataSetChanged();

                        Calendar calFordDate = Calendar.getInstance();
                        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
                        String saveCurrentDate = currentDate.format(calFordDate.getTime());

                        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
                        String saveCurrentTime = currentTime.format(calFordDate.getTime());


                            for (DataSnapshot postSnapshot : dataSnapshot.child("Own").child("Task").getChildren()) {

                                TaskInfo info = postSnapshot.getValue(TaskInfo.class);

                                info.setKey(postSnapshot.getKey());

                                if (getDateInMillis(saveCurrentDate+ " "+saveCurrentTime) < getDateInMillis(info.getDate()+" "+info.getTime())){

                                    first.add(info);

                                    saveCurrentDate = info.getDate();
                                    saveCurrentTime = info.getTime();


                                }else {
                                    last.add(info);
                                }

                            }

                            Collections.reverse(first);

                            mList.addAll(last);
                            mList.addAll(first);


                            first.clear();
                            last.clear();


                        tAdapter.notifyDataSetChanged();
                        mProgressCircle.setVisibility(View.INVISIBLE);

                }
                else {

                    emptyRoutine.setVisibility(View.VISIBLE);
                    mProgressCircle.setVisibility(View.INVISIBLE);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
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