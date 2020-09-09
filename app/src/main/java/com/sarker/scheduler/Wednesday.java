package com.sarker.scheduler;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sarker.scheduler.R;
import com.sarker.scheduler.RoutineAdapter;
import com.sarker.scheduler.RoutineInfo;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Wednesday extends Fragment {

    private RecyclerView rRecyclerView;
    private RoutineAdapter rAdapter;
    private DatabaseReference rDatabaseRef;
    private ArrayList<RoutineInfo> rList;
    private ProgressBar rProgressCircle;
    private ImageView noClass;
    private FirebaseAuth mAuth;
    private String current_user_id;

    public Wednesday() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wednesday, container, false);

        noClass = view.findViewById(R.id.no_classes);
        rRecyclerView = view.findViewById(R.id.recycler_view5);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rRecyclerView.setLayoutManager(linearLayoutManager);
        rList = new ArrayList<>();
        rAdapter = new RoutineAdapter(getContext(),rList);
        rRecyclerView.setAdapter(rAdapter);


        rProgressCircle = view.findViewById(R.id.progress_circle5);


        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid().substring(7,14);
        rDatabaseRef = FirebaseDatabase.getInstance().getReference("Routine").child(current_user_id);

        rDatabaseRef.child("Wednesday").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    rList.clear();
                    rAdapter.notifyDataSetChanged();

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                        RoutineInfo info = postSnapshot.getValue(RoutineInfo.class);
                        info.setRoutineKey(postSnapshot.getKey());
                        rList.add(info);

                    }
                    rAdapter.notifyDataSetChanged();
                    rProgressCircle.setVisibility(View.INVISIBLE);

                }
                else {

                    noClass.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                rProgressCircle.setVisibility(View.INVISIBLE);
            }

        });

        return view;
    }


}