package com.wikav.coromobileapp.connection;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.wikav.coromobileapp.R;
import com.wikav.coromobileapp.activity.HomeActivity;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static String NOTIFICATION_ID = "MYNOTIFIATIONID";
    public static String CHANNEL_NAME = "Coro App";
    public static String CHANNEL_DIS = "CoroApp_Dis";
    public static String tokken;
    public String intent;

    public MyFirebaseMessagingService() {

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        intent = remoteMessage.getData().get("in");
        Log.i("MSG",remoteMessage.getData().toString());

        String clickAction ="android.intent.action.home";


      genereteNotification(remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle(), intent, clickAction);
    }



    private void genereteNotification(String body, String title, String in, String clickAction) {

        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_ID,CHANNEL_NAME,NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DIS);
            channel.enableLights(true);
            channel.enableVibration(true);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
        }

        try {
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(),RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }

        NotificationCompat.Builder noti = new NotificationCompat.Builder(this,NOTIFICATION_ID)
                .setContentTitle("Coro Detect - New Person")
                .setContentText(body)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.noti_icon);

        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        PendingIntent resultPending =
                PendingIntent.getActivity(
                        this,
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        noti.setContentIntent(resultPending);

        int mNotificationId = (int) System.currentTimeMillis();
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(mNotificationId, noti.build());


    }
}
