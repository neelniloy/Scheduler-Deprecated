package com.sarker.scheduler;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private Context nContext;
    private ArrayList<TaskInfo> rList;
    private  long code;
    private static int DELAY_TIME= 1800;
    private ProgressDialog progressDialog1;
    private int lastPosition = -1;
    private int expandedPosition = -1;
    private int mExpandedPosition= -1;


    public TaskAdapter(Context context, ArrayList<TaskInfo> rLists) {
        nContext = context;
        rList = rLists;
    }

    @NonNull
    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(nContext).inflate(R.layout.show_task, parent, false);
        return new TaskAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final TaskAdapter.ViewHolder holder, final int position) {
        final TaskInfo info = rList.get(position);

        final String key = info.getKey();
        String status = info.getStatus();


        holder.title.setText(info.getTitle());
        holder.details.setText(info.getDetails());
        holder.date.setText(info.getDate());

        if (status.equals("Completed")){
            holder.status.setText("● "+status);

            holder.status.setTextColor(Color.parseColor("#19A119"));
        }
        else if (status.equals("Missed")){
            holder.status.setText("● "+status);

            holder.status.setTextColor(Color.parseColor("#FF0000"));
        }else {
            holder.status.setText("● "+status);
        }


        final boolean isExpanded = position==mExpandedPosition;

        holder.llExpandArea.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.down.setBackgroundResource(isExpanded?R.drawable.ic_up:R.drawable.ic_down);
        holder.itemView.setActivated(isExpanded);

        if (isExpanded){
            expandedPosition = position;
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpandedPosition = isExpanded ? -1:position;
                notifyItemChanged(expandedPosition);
                notifyItemChanged(position);
            }
        });




//        holder.edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//                Intent intent = new Intent(nContext, EditRoutine.class);
//                intent.putExtra("key", key);
//                nContext.startActivity(intent);
//
//
//            }
//        });

//        holder.delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                progressDialog1 = new ProgressDialog(nContext);
//                progressDialog1.show();
//                progressDialog1.setMessage("Deleting...");
//
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        progressDialog1.dismiss();
//
//                        holder.databaseReference.child("Own").child("Task").child(key).removeValue();
//                        rList.remove(holder.getAdapterPosition());
//                        notifyDataSetChanged();
//
//                    }
//                },DELAY_TIME);
//
//            }
//        });


        setAnimation(holder.itemView, position);

//        Animation animation = AnimationUtils.loadAnimation(nContext,
//                (position > lastPosition) ? R.anim.item_animation_fall_down
//                        : R.anim.item_animation_fall_down);
//        holder.itemView.startAnimation(animation);
//        lastPosition = position;


    }


    @Override
    public void onViewDetachedFromWindow(@NonNull TaskAdapter.ViewHolder holder) {
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


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title,details,time,date,status;
        public DatabaseReference databaseReference;
        public FirebaseAuth mAuth;
        public String current_user_id;
        public ImageView down;
        public LinearLayout llExpandArea;

        public ViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            details = itemView.findViewById(R.id.details);
            date = itemView.findViewById(R.id.date);
            status = itemView.findViewById(R.id.status);
            down = itemView.findViewById(R.id.arrow_down);
            llExpandArea = itemView.findViewById(R.id.llExpandArea);


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
