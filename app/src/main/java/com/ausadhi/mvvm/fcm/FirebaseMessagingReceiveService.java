package com.ausadhi.mvvm.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.ausadhi.mvvm.R;
import com.ausadhi.mvvm.data.DataManager;
import com.ausadhi.mvvm.data.network.model.UserModel;
import com.ausadhi.mvvm.data.network.services.LoginService;
import com.ausadhi.mvvm.ui.HomeActivity;
import com.ausadhi.mvvm.utils.AppConstants;
import com.ausadhi.mvvm.utils.LogUtils;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessagingReceiveService extends FirebaseMessagingService {
  String TAG ="FirebaseMessagingService";
    Context context ;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        context = this;

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            if(DataManager.getInstance().getPrefs().getUserType().equalsIgnoreCase(AppConstants.UserType.USER)){
                handleNotification(HomeActivity.getIntent(context),remoteMessage.getData().get("title"),remoteMessage.getData().get("body"));
            }

        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            if(DataManager.getInstance().getPrefs().getUserType().equalsIgnoreCase(AppConstants.UserType.USER)){
                handleNotification(HomeActivity.getIntent(context),remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getTitle());
            }
        }

    }


    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        LogUtils.networkError(s);
        DataManager.getInstance().getPrefs().setToken(s);
        if(DataManager.getInstance().getPrefs().getUser()!=null){
            UserModel model = DataManager.getInstance().getPrefs().getUser();
            model.setFirebaseToken(s);
            LoginService.getInstance().getmUserDatabase().document(model.getUserId()).set(model);
        }
    }

    private void handleNotification(Intent navigationIntent, String title, String message) {

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(navigationIntent);



        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String CHANNEL_ID = "sethji_channel_" + System.currentTimeMillis(); // The id of the channel.
            CharSequence name = "sethji"; // The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationManager.createNotificationChannel(mChannel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                            R.mipmap.ic_launcher))
                    .setContentIntent(resultPendingIntent)
                    .setChannelId(CHANNEL_ID)
                    .setContentText(message)
                    .build();
            notificationManager.notify((int) (System.currentTimeMillis() - 10000000), notification);

        } else {

            Notification.BigTextStyle bigTextStyle = new Notification.BigTextStyle();
            bigTextStyle.bigText(message);

            Notification notification = new Notification.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setAutoCancel(true)
                    .setStyle(bigTextStyle)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                            R.mipmap.ic_launcher))
                    .setPriority(Notification.PRIORITY_MAX)
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setContentIntent(resultPendingIntent)
                    .setContentText(message)
                    .build();
            notificationManager.notify((int) (System.currentTimeMillis() - 10000000), notification);

        }
    }


//            if (/* Check if data needs to be processed by long running job */ true) {
//                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
//                scheduleJob();
//            } else {
//                // Handle message within 10 seconds
//                handleNow();
//            }

}
