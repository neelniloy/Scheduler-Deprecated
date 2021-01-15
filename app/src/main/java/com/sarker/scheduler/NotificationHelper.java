package com.sarker.scheduler;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.sarker.scheduler.mainview.MainActivity;

import java.util.Random;

public class NotificationHelper extends ContextWrapper {

    public static final String channelID = "channelID";
    public static final String channelID2 = "channelID2";
    public static final String channelName = "Class Reminder";
    public static final String channelName2 = "Task Reminder";
    private NotificationManager mManager;
    public NotificationHelper(Context base, String type) {
        super(base);

        if (type.equals("Routine")){
            RoutineNotify();
        }
        else if (type.equals("Task")){
            TaskNotify();
        }


    }



    private void RoutineNotify() {

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);


        int notificationID = new Random().nextInt(3000);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            createChannel(notificationManager);

        }

        Intent intent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);


        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        final MediaPlayer player = MediaPlayer.create(this, notification);
        player.setLooping(true);
        player.start();
        new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    player.stop();

                }
            },7000);

            NotificationCompat.Builder notificationBuilder  = new NotificationCompat.Builder(getApplicationContext(), channelID)
                    .setContentTitle("Class Reminder!")
                    .setContentText("Your Class Will Be Started 15 Minutes Later")
                    .setSmallIcon(R.drawable.logo)
                    .setSound(notification)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                notificationBuilder.setSmallIcon(R.drawable.logo);
                notificationBuilder.setColor(getResources().getColor(R.color.colorPrimary));
            } else {
                notificationBuilder.setSmallIcon(R.drawable.logo);
            }
            notificationManager.notify(notificationID,notificationBuilder.build());

    }

    private void TaskNotify() {

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);


        int notificationID = new Random().nextInt(3000);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            createChannel2(notificationManager);

        }

        Intent intent2 = new Intent(this, Task.class);

        PendingIntent pendingIntent2 = PendingIntent.getActivity(this,0,intent2,PendingIntent.FLAG_ONE_SHOT);

            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            final MediaPlayer player = MediaPlayer.create(this, notification);
            player.setLooping(true);
            player.start();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    player.stop();

                }
            },7000);

            NotificationCompat.Builder notificationBuilder2  = new NotificationCompat.Builder(getApplicationContext(), channelID2)
                    .setContentTitle("Task Reminder!")
                    .setContentText("You Have An Incomplete Task")
                    .setSmallIcon(R.drawable.logo)
                    .setSound(notification)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent2);

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                notificationBuilder2.setSmallIcon(R.drawable.logo);
                notificationBuilder2.setColor(getResources().getColor(R.color.colorPrimary));
            } else {
                notificationBuilder2.setSmallIcon(R.drawable.logo);
            }
        notificationManager.notify(notificationID,notificationBuilder2.build());
        }



    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel(NotificationManager notificationManager) {
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);

        channel.enableLights(true);
        channel.setLightColor(Color.BLUE);
        channel.enableVibration(true);

        if(notificationManager!=null){
            notificationManager.createNotificationChannel(channel);
        }
    }




    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannel2(NotificationManager notificationManager) {
        NotificationChannel channel2 = new NotificationChannel(channelID2, channelName2, NotificationManager.IMPORTANCE_HIGH);

        channel2.enableLights(true);
        channel2.setLightColor(Color.BLUE);
        channel2.enableVibration(true);

        if(notificationManager!=null){
            notificationManager.createNotificationChannel(channel2);
        }
    }


}
