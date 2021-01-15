package com.sarker.scheduler;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ParseException;
import android.os.Build;
import android.os.Handler;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
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
import androidx.annotation.RequiresApi;
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
    private static String mdate;

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;




    private static long currentDate() {
        Calendar calFordDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy hh:mm aa");
        String date = currentDate.format(calFordDate.getTime());
        return getDateInMillis(date);
    }

    public static String getTimeAgo(String date) {
        long time = Long.parseLong(date);
        if (time < 1000000000000L) {
            time *= 1000;
        }

        long now = currentDate();
        if (time > now || time <= 0) {

            final long diff = time - now;
            if (diff < MINUTE_MILLIS) {
                return "Just now";
            } else if (diff < 2 * MINUTE_MILLIS) {
                return "A minute to go";
            } else if (diff < 60 * MINUTE_MILLIS) {
                return diff / MINUTE_MILLIS + " minutes to go";
            } else if (diff < 2 * HOUR_MILLIS) {
                return "An hour to go";
            } else{

                Calendar calFordDate = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
                String cDate = currentDate.format(calFordDate.getTime());

                if (diff / HOUR_MILLIS >12 && diff / HOUR_MILLIS < 24) {

                    return "Tomorrow";

                }else if (diff < 24 * HOUR_MILLIS) {
                    return diff / HOUR_MILLIS + " hours to go";
                }
                else {
                    return mdate;
                }
            }
        }
        else {
            final long diff = now - time;
            if (diff < MINUTE_MILLIS) {
                return "Just now";
            } else if (diff < 2 * MINUTE_MILLIS) {
                return "A minute ago";
            } else if (diff < 60 * MINUTE_MILLIS) {
                return diff / MINUTE_MILLIS + " minutes ago";
            } else if (diff < 2 * HOUR_MILLIS) {
                return "An hour ago";
            } else if (diff < 24 * HOUR_MILLIS) {
                return diff / HOUR_MILLIS + " hours ago";
            } else if (diff < 48 * HOUR_MILLIS) {
                return "Yesterday";
            } else if (diff < 72 * HOUR_MILLIS) {
                return diff / DAY_MILLIS + " days ago";
            } else {
                return mdate;
            }
        }

    }



    public static String convertDate(String dateInMilliseconds,String dateFormat) {
        return DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString();
    }


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

        final String saveCurrentDate, saveCurrentTime,status;
        final String key = info.getKey();
        status = info.getStatus();
        mdate = info.getDate();

        if (holder.current_user_id.equals(info.getUid())){
            holder.dTask.setVisibility(View.VISIBLE);
        }else {
            holder.dTask.setVisibility(View.GONE);
        }

        if (status.equals("Missed")){
            holder.rTask.setVisibility(View.GONE);
        }else {
            holder.rTask.setVisibility(View.VISIBLE);
        }


        SharedPreferences preferences = nContext.getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();

        final String ownStatus = preferences.getString(""+position, "");
        final String reminder = preferences.getString("R"+position, "");

        if (ownStatus.equals("Completed")){
            holder.status.setText("● "+ownStatus);

            holder.status.setTextColor(Color.parseColor("#19A119"));
        }

        holder.icDone.setBackgroundResource(ownStatus.equals("Completed")?R.drawable.ic_uncheck:R.drawable.ic_check);
        holder.tvDone.setText(ownStatus.equals("Completed")?"Mark As Undone":"Mark As Done");

        holder.icReminder.setBackgroundResource(reminder.equals("ON")?R.drawable.ic_notify1:R.drawable.ic_notify0);
        holder.tvRem.setText(reminder.equals("ON")?"Reminder On":"Reminder");

        holder.title.setText(info.getTitle());
        holder.details.setText(info.getDetails());
        holder.date.setText(getTimeAgo(""+getDateInMillis(info.getDate()+" "+info.getTime())));
        holder.time.setText(info.getTime());


        Calendar calFordDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(calFordDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        saveCurrentTime = currentTime.format(calFordDate.getTime());

        if (!ownStatus.equals("Completed") && getDateInMillis(saveCurrentDate+ " "+saveCurrentTime) > getDateInMillis(info.getDate()+" "+info.getTime())){
            holder.databaseReference.child(info.getUid().substring(7,14)).child("Own").child("Task").child(key).child("status").setValue("Missed");

            if (reminder.equals("On")){
                cancelAlarm(position);
            }
            editor.putString("R"+position, "");
            editor.apply();

        }


        if (!ownStatus.equals("Completed") && status.equals("Missed")){
            holder.status.setText("● "+status);

            holder.status.setTextColor(Color.parseColor("#FF0000"));
        }else if (ownStatus.equals("Completed")){
            holder.status.setText("● "+ownStatus);
        }else {
            holder.status.setTextColor(Color.parseColor("#FF8D00"));
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





        holder.markTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ownStatus.equals("Completed")){
                    editor.putString(""+position, "");
                    editor.apply();
                }else {
                    editor.putString(""+position, "Completed");
                    editor.apply();

                    if (reminder.equals("On")){
                        cancelAlarm(position);
                    }
                    editor.putString("R"+position, "");
                    editor.apply();
                }


                notifyItemChanged(position);


            }
        });

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

                        holder.databaseReference.child(info.getUid().substring(7,14)).child("Own").child("Task").child(key).removeValue();

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

        holder.rTask.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {


                if (reminder.equals("ON")){
                    editor.putString("R"+position, "");
                    editor.apply();
                    cancelAlarm(position);
                }else {
                    editor.putString("R"+position, "ON");
                    editor.apply();
                    startAlarm(getDateInMillis(info.getDate()+" "+info.getTime()) - DAY_MILLIS,position);

                    Toast.makeText(nContext, "Reminder Activated", Toast.LENGTH_SHORT).show();
                }


                notifyItemChanged(position);


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

        public TextView title,details,time,date,status,tvDone,tvRem;
        public DatabaseReference databaseReference;
        public FirebaseAuth mAuth;
        public String current_user_id;
        public ImageView down,icDone,icReminder;
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
            markTask = itemView.findViewById(R.id.mark_task);
            icDone = itemView.findViewById(R.id.ic_done);
            icReminder = itemView.findViewById(R.id.ic_reminder);
            tvDone = itemView.findViewById(R.id.tv_done);
            rTask = itemView.findViewById(R.id.r_task);
            tvRem = itemView.findViewById(R.id.tv_reminder);


            mAuth = FirebaseAuth.getInstance();
            current_user_id = mAuth.getCurrentUser().getUid();


            databaseReference = FirebaseDatabase.getInstance().getReference().child("Routine");

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void startAlarm(long c, int key) {
        AlarmManager alarmManager = (AlarmManager) nContext.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(nContext, SetReminder.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(nContext, key, intent,PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c,AlarmManager.INTERVAL_DAY/2, pendingIntent);
    }



    private void cancelAlarm(int key) {
        AlarmManager alarmManager = (AlarmManager) nContext.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(nContext, SetReminder.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(nContext, key, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
        Toast.makeText(nContext, "Reminder Canceled", Toast.LENGTH_SHORT).show();
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
