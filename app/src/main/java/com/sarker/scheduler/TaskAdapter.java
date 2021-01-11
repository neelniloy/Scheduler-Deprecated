package com.sarker.scheduler;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ParseException;
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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private Context nContext;
    private ArrayList<TaskInfo> rList;
    private  long code;
    private static int DELAY_TIME= 1500;
    private ProgressDialog progressDialog1;
    private int lastPosition = 1000;
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

        String saveCurrentDate, saveCurrentTime,status;
        final String key = info.getKey();
        status = info.getStatus();


        holder.title.setText(info.getTitle());
        holder.details.setText(info.getDetails());
        holder.date.setText(info.getDate());
        holder.time.setText(info.getTime());


        Calendar calFordDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(calFordDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        saveCurrentTime = currentTime.format(calFordDate.getTime());

        if (!status.equals("Completed") && getDateInMillis(saveCurrentDate+ " "+saveCurrentTime) > getDateInMillis(info.getDate()+" "+info.getTime())){
            holder.databaseReference.child(key).child("status").setValue("Missed");
        }

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

        holder.dTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog1 = new ProgressDialog(nContext);
                progressDialog1.show();
                progressDialog1.setMessage("Deleting...");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        progressDialog1.dismiss();

                        holder.databaseReference.child(key).removeValue();

                        if (isExpanded){
                            expandedPosition = position;
                        }
                        mExpandedPosition = isExpanded ? -1:position;
                        notifyItemChanged(expandedPosition);
                        notifyItemChanged(position);

                        rList.remove(holder.getAdapterPosition());
                        notifyDataSetChanged();



                    }
                },DELAY_TIME);

            }
        });


        setAnimation(holder.itemView, position);



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
        public CardView llExpandArea;
        public LinearLayout dTask,rTask,markTask;

        public ViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            details = itemView.findViewById(R.id.details);
            time = itemView.findViewById(R.id.time);
            date = itemView.findViewById(R.id.date);
            status = itemView.findViewById(R.id.status);
            down = itemView.findViewById(R.id.arrow_down);
            llExpandArea = itemView.findViewById(R.id.llExpandArea);
            dTask = itemView.findViewById(R.id.d_task);


            mAuth = FirebaseAuth.getInstance();
            current_user_id = mAuth.getCurrentUser().getUid();


            databaseReference = FirebaseDatabase.getInstance().getReference().child("Routine").child(current_user_id.substring(7,14)).child("Own").child("Task");

        }

    }

    private void setAnimation(View viewToAnimate, int position)
    {

        if (position < lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(nContext, R.anim.item_animation_fall_down);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }

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
