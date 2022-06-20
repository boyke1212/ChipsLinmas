package com.toelve.chipslinmas;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;

import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by boyke on 5/2/2016.
 */
public class GCMPushReceiverService extends GcmListenerService {
    int timin;
    String isi;
    //This method will be called on every new message received
    @Override
    public void onMessageReceived(String from, Bundle data) {
        //Getting the message from the bundle
        String message = data.getString("message");
        String title = data.getString("title");
        String ticker=data.getString("tickerText");
        //Displaying a notiffication with the message
        sendNotification(title,message,ticker);
       // MainActivity.isi=title+"\n"+message;


    }

    //This method is generating a notification and displaying the notification
    private void sendNotification(String title , String message, String tickertext) {
        Intent intent = new Intent(this, AdaBeritaBaru.class);
        intent.putExtra("message",message);
        intent.putExtra("title",title);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        int uniqueInt = (int) (System.currentTimeMillis() & 0xfffffff);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, uniqueInt, intent, PendingIntent.FLAG_UPDATE_CURRENT);
       // Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification notification = new NotificationCompat.Builder(this)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setSubText("Ada berita baru")
                .setTicker(tickertext)
                .setColor(Color.parseColor( "#9999ff"))
                .setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.fit))
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentIntent(pendingIntent)
                .build();

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification); //0 = ID of notification

    }

}