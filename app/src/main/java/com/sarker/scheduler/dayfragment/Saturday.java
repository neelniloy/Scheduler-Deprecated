package com.sarker.scheduler.dayfragment;


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
public class Saturday extends Fragment {

    private RecyclerView rRecyclerView;
    private RoutineAdapter rAdapter;
    private DatabaseReference rDatabaseRef,importRef;
    private ArrayList<RoutineInfo> rList;
    private ProgressBar rProgressCircle;
    private ImageView noClass;
    private FirebaseAuth mAuth;
    private String current_user_id,importKey = " ";


    public Saturday() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saturday, container, false);

        noClass = view.findViewById(R.id.no_classes);
        rRecyclerView = view.findViewById(R.id.recycler_view1);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rRecyclerView.setLayoutManager(linearLayoutManager);
        rList = new ArrayList<>();
        rAdapter = new RoutineAdapter(getContext(),rList);
        rRecyclerView.setAdapter(rAdapter);


        rProgressCircle = view.findViewById(R.id.progress_circle1);


        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid().substring(7,14);
        rDatabaseRef = FirebaseDatabase.getInstance().getReference("Routine").child(current_user_id);
        importRef = FirebaseDatabase.getInstance().getReference("Routine");

        rDatabaseRef.keepSynced(true);
        importRef.keepSynced(true);

        rDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                if(dataSnapshot.child("Import").exists()){
                    importKey = dataSnapshot.child("Import").getValue().toString();

                    importRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot2) {

                            if(dataSnapshot2.child(importKey).child("Own").child("Saturday").exists()){
                                rAdapter.notifyDataSetChanged();

                                for (DataSnapshot postSnapshot : dataSnapshot2.child(importKey).child("Own").child("Saturday").getChildren()) {

                                    RoutineInfo info = postSnapshot.getValue(RoutineInfo.class);
                                    info.setRoutineKey(postSnapshot.getKey());
                                    rList.add(info);

                                }
                                rAdapter.notifyDataSetChanged();
                                rProgressCircle.setVisibility(View.INVISIBLE);

                            }
                            else {

                                if(!dataSnapshot.child("Own").child("Saturday").exists()){
                                    noClass.setVisibility(View.VISIBLE);
                                }

                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            rProgressCircle.setVisibility(View.INVISIBLE);
                        }

                    });
                }

                if(dataSnapshot.child("Own").child("Saturday").exists()){

                    rList.clear();
                    rAdapter.notifyDataSetChanged();

                    for (DataSnapshot postSnapshot : dataSnapshot.child("Own").child("Saturday").getChildren()) {

                        RoutineInfo info = postSnapshot.getValue(RoutineInfo.class);
                        info.setRoutineKey(postSnapshot.getKey());
                        rList.add(info);

                    }
                    rAdapter.notifyDataSetChanged();
                    rProgressCircle.setVisibility(View.INVISIBLE);

                }
                else {

                    if (importKey.equals(" ")){
                        rProgressCircle.setVisibility(View.INVISIBLE);
                        noClass.setVisibility(View.VISIBLE);
                    }

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
