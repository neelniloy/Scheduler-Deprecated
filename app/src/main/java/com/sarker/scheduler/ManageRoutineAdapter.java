package com.sarker.scheduler;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ManageRoutineAdapter extends RecyclerView.Adapter<ManageRoutineAdapter.NewsViewHolder> {

    private Context nContext;
    private ArrayList<RoutineInfo> rList;
    private  long code;
    private static int DELAY_TIME= 1800;
    private ProgressDialog progressDialog1;
    private int lastPosition = -1;


    public ManageRoutineAdapter(Context context, ArrayList<RoutineInfo> rLists) {
        nContext = context;
        rList = rLists;
    }

    @NonNull
    @Override
    public ManageRoutineAdapter.NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(nContext).inflate(R.layout.show_manage_routine, parent, false);
        return new ManageRoutineAdapter.NewsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ManageRoutineAdapter.NewsViewHolder holder, int position) {
        final RoutineInfo info = rList.get(position);

        final String routineKey = info.getRoutineKey().toString();
        final String day = info.getDay();

        holder.courseTitle.setText(info.getCourseName());
        holder.courseCode.setText(info.getCourseCode());
        holder.roomNo.setText(info.getRoomNo());
        holder.courseTeacher.setText(info.getCourseTeacher());
        holder.classTime.setText(info.getClassTime());
        holder.daysOfweek.setText(day);

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(nContext, EditRoutine.class);
                intent.putExtra("day", day);
                intent.putExtra("key", routineKey);
                nContext.startActivity(intent);


            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog1 = new ProgressDialog(nContext);
                progressDialog1.show();
                progressDialog1.setMessage("Deleting...");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        progressDialog1.dismiss();

                        holder.databaseReference.child("Own").child(day).child(routineKey).removeValue();
                        rList.remove(holder.getAdapterPosition());
                        notifyDataSetChanged();

                    }
                },DELAY_TIME);

            }
        });


        setAnimation(holder.itemView, position);

//        Animation animation = AnimationUtils.loadAnimation(nContext,
//                (position > lastPosition) ? R.anim.item_animation_fall_down
//                        : R.anim.item_animation_fall_down);
//        holder.itemView.startAnimation(animation);
//        lastPosition = position;


    }

    @Override
    public void onViewDetachedFromWindow(@NonNull NewsViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return rList.size();
    }


    public class NewsViewHolder extends RecyclerView.ViewHolder {

        public TextView courseTitle, courseCode, courseTeacher, roomNo, classTime,daysOfweek;
        public ImageView edit,delete;
        public DatabaseReference databaseReference;
        public FirebaseAuth mAuth;
        public String current_user_id;

        public NewsViewHolder(View itemView) {
            super(itemView);

            courseTitle = itemView.findViewById(R.id.course_title);
            courseCode = itemView.findViewById(R.id.course_code);
            courseTeacher = itemView.findViewById(R.id.course_teacher);
            roomNo = itemView.findViewById(R.id.room_no);
            classTime = itemView.findViewById(R.id.class_time);
            edit = itemView.findViewById(R.id.edit);
            delete = itemView.findViewById(R.id.delete);
            daysOfweek = itemView.findViewById(R.id.day_of_week);

            mAuth = FirebaseAuth.getInstance();
            current_user_id = mAuth.getCurrentUser().getUid();


            databaseReference = FirebaseDatabase.getInstance().getReference().child("Routine").child(current_user_id.substring(7,14));

        }

    }

    private void setAnimation(View viewToAnimate, int position)
    {

        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(nContext, R.anim.item_animation_fall_down);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }else if ( position < lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(nContext, R.anim.item_animation_fall_down);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

}
