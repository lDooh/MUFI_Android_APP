package com.mufi.mufi;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mufi.mufi.DTO.AppUserDTO;
import com.mufi.mufi.model.MufiRest;
import com.mufi.mufi.ui.MainActivity;

public class MufiFirebaseMessagingService extends FirebaseMessagingService {
    public MufiFirebaseMessagingService() {
        super();
    }

    @Override
    public void onNewToken(@NonNull String token) {
        Log.d(MufiTag.DEBUG_TAG_FCM, "Refreshed token: " + token);
        super.onNewToken(token);

        // 토큰이 갱신되면 서버로 전송
        MufiRest mufiRest = MufiRest.getInstance();
        AppUserDTO appUserDTO = AppUserDTO.getInstance();
        String resultString = mufiRest.send(appUserDTO.getId() + "/" +  token,
                MufiTag.REST_NEW_TOKEN);
    }

    @Override       // 새로운 메시지를 받았을 때 호출되는 메소드
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Log.d(MufiTag.DEBUG_TAG_FCM, "From: " + remoteMessage);

        super.onMessageReceived(remoteMessage);

        scheduleJob();

        if (remoteMessage.getNotification() != null) {
            sendNotification(remoteMessage);
        }
    }

    private void scheduleJob() {
        
    }

    private void sendNotification(RemoteMessage remoteMessage) {
        String from = remoteMessage.getFrom();
        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();
        String value = remoteMessage.getData().get("key");      // key-value 전달 가능

        Log.i(MufiTag.DEBUG_TAG_FCM, "===============================");
        Log.i(MufiTag.DEBUG_TAG_FCM, "from: " + from);
        Log.i(MufiTag.DEBUG_TAG_FCM, "title: " + title);
        Log.i(MufiTag.DEBUG_TAG_FCM, "body: " + body);
        Log.i(MufiTag.DEBUG_TAG_FCM, "value: " + value);
        Log.i(MufiTag.DEBUG_TAG_FCM, "===============================");

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);

        Notification.Builder mBuilder = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setVibrate(new long[] { 1, 1000 });

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // 안드로이드 Oreo 버전 이상이라면 채널 생성
        String channelId = getString(R.string.default_notification_channel_id);
        String channelName = getString(R.string.default_notification_channel_name);
        String channelDescription = getString(R.string.default_notification_channel_description);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel nChannel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            nChannel.setDescription(channelDescription);
            nChannel.enableVibration(true);
            Log.d(MufiTag.DEBUG_TAG_FCM, "채널 생성");

            notificationManager.createNotificationChannel(nChannel);
        }

        notificationManager.notify(0, mBuilder.build());

        mBuilder.setContentIntent(contentIntent);
    }
}
