package com.app.mobiledev.apphris.notifikasi;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.sesion.SessionManager;
//import com.google.firebase.messaging.FirebaseMessagingService;
//import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;

public class MyFCMService /*extends FirebaseMessagingService*/ {

/*
    @Override
    public void onMessageSent(String s) {
        super.onMessageSent(s);
        Log.d("TAG_ONMSG_SENT", "onMessageSent: " + s);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        SessionManager msession=new SessionManager(this);
        Log.d("TAG_REMOTE_MESSAGE_BODY", "onMessageReceived: " + remoteMessage.getMessageType());

        String title = Objects.requireNonNull(remoteMessage.getNotification()).getTitle();
        String body = Objects.requireNonNull(remoteMessage.getNotification()).getBody();
        String click_action = remoteMessage.getData().get("click_action");
        String nik = remoteMessage.getData().get("nik");
        Log.d("TAG_REMOTE_MESSAGE2", "onMessageReceived: " + nik);

        if(msession.getNik().equals(nik)){
            Log.d("TAG_REMOTE_MESSAGE3", "onMessageReceived: " + msession.getNik());
            showNotificationMessage(title, body, click_action);
        }

    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showNotificationMessage(String title, String body, String click_action) {
        Intent intent = new Intent(click_action);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationId = 1;
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);

            assert notificationManager != null;
            notificationManager.createNotificationChannel(mChannel);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MainApplication.context, channelId)
                .setSmallIcon(R.drawable.ic_hris)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.drawable.ic_hris))
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setVibrate(new long[] { 10, 10, 10, 10, 10 })
                .setColor(getResources().getColor(R.color.blue))
                .setContentIntent(pendingIntent);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(MainApplication.context);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingIntent);

        notificationManager.notify((int) System.currentTimeMillis(), mBuilder.build());


    }


 */
}