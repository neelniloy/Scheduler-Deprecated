package com.sarker.scheduler;

import android.app.ProgressDialog;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ViewTaskFragment extends Fragment {

    public ViewTaskFragment() {
        // Required empty public constructor
    }

    private RecyclerView mRecyclerView;
    private TaskAdapter tAdapter;
    private DatabaseReference mDatabaseRef;
    private ArrayList<TaskInfo> mList;
    private ProgressBar mProgressCircle;
    private TextView emptyRoutine;
    private FirebaseAuth mAuth;
    private String current_user_id;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_task, container, false);


        emptyRoutine = view.findViewById(R.id.empty_task);
        mRecyclerView = view.findViewById(R.id.trecycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mList = new ArrayList<>();
        tAdapter = new TaskAdapter(getActivity(),mList);
        mRecyclerView.setAdapter(tAdapter);

        mProgressCircle = view.findViewById(R.id.tprogress_circle);



        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid().substring(7,14);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Routine").child(current_user_id).child("Own").child("Task");



        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                    if(dataSnapshot.exists()){

                        mList.clear();
                        tAdapter.notifyDataSetChanged();


                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                                TaskInfo info = postSnapshot.getValue(TaskInfo.class);

                                info.setKey(postSnapshot.getKey());

//                                info.setTitle(dataSnapshot.child(key).child("title").getValue().toString());
//                                info.setDetails(dataSnapshot.child(key).child("details").getValue().toString());
//                                info.setDate(dataSnapshot.child(key).child("date").getValue().toString());
//                                info.setStatus(dataSnapshot.child(key).child("status").getValue().toString());

                                mList.add(info);

                            }


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
}