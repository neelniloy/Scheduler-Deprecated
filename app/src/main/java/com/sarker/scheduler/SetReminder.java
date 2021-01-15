package com.sarker.scheduler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;

import androidx.core.app.NotificationCompat;

public class SetReminder extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationHelper notificationHelper = new NotificationHelper(context,"Task");
//        NotificationCompat.Builder nb = notificationHelper.getChannelNotification2();
//        notificationHelper.getManager2().notify(2, nb.build());

        Vibrator v = (Vibrator)context.getSystemService(context.VIBRATOR_SERVICE);
        v.vibrate(1000);

    }
}
