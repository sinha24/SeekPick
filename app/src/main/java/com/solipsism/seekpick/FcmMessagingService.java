package com.solipsism.seekpick;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.solipsism.seekpick.Dash.DashActivity;
import com.solipsism.seekpick.Login.LoginActivity;
import com.solipsism.seekpick.Search.SearchActivity;
import com.solipsism.seekpick.utils.PrefsHelper;

import java.util.Map;

/**
 * Created by ravi joshi on 5/10/2017.
 */

public class FcmMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (!(PrefsHelper.getPrefsHelper(FcmMessagingService.this).getPref(PrefsHelper.PREF_TOKEN, "token").equals("token"))){
        Map<String, String> data = remoteMessage.getData();
        String tittle= data.get("title");
        String message =data.get("body");
        String name = data.get("name");
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent intent = new Intent(this,DashActivity.class);
        intent.putExtra("tt",tittle);
        intent.putExtra("mssg",message);
        intent.putExtra("nm",name);
        intent.putExtra("flag",7);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent= PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setContentTitle(tittle);
        notificationBuilder.setContentText(message);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setSmallIcon(R.drawable.confirm);
        notificationBuilder.setSound(alarmSound);
        notificationBuilder.setContentIntent(pendingIntent);
        NotificationManager notificationmanager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationmanager.notify(0,notificationBuilder.build());
      }else{
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Intent intent = new Intent(this,LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent= PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
            notificationBuilder.setContentTitle("SeekPick");
            notificationBuilder.setContentText("Please Login to receive customer query notification");
            notificationBuilder.setAutoCancel(true);
            notificationBuilder.setSmallIcon(R.drawable.confirm);
            notificationBuilder.setSound(alarmSound);
            notificationBuilder.setContentIntent(pendingIntent);
            NotificationManager notificationmanager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationmanager.notify(0,notificationBuilder.build());
        }
    }
}
