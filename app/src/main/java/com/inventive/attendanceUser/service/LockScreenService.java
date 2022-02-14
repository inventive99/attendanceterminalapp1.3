package com.inventive.attendanceUser.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.inventive.attendanceUser.MainActivity;
import com.inventive.attendanceUser.R;
import com.inventive.attendanceUser.env.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vrushali on 23/04/2021.
 */
public class LockScreenService extends Service {

    WindowManager windowManager;

    View lockScreenView;

    Context context;

    PinLockView mPinLockView;

    IndicatorDots mDotsView;

    String password="1234";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) {
            startForeground(1, displayForegroundNotification(this));
        }


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        context = this;
        LayoutInflater li = LayoutInflater.from(context);
        lockScreenView = li.inflate(R.layout.lock_screen_service, null);

        WindowManager.LayoutParams params;

        if (Build.VERSION.SDK_INT >= 26) {
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                    PixelFormat.TRANSLUCENT);

        } else {

            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        }

        //Button button = lockScreenView.findViewById(R.id.btnLockScreen);

        mPinLockView = lockScreenView.findViewById(R.id.unlockPinLockView);
        mDotsView = lockScreenView.findViewById(R.id.unlockPinDotsView);

        mPinLockView.attachIndicatorDots(mDotsView);
        mPinLockView.setPinLockListener(pinLockListener);

        mPinLockView.setPinLength(4);

        windowManager.addView(lockScreenView,params);

        return START_STICKY;
    }



    private PinLockListener pinLockListener = new PinLockListener() {
        @Override
        public void onComplete(String pin) {
//            Log.d("Uninstall pass",password);
           // Log.d("Uninstall pass",PreferenceUtils.getUser().getUninstallPassword());

            if (null!=password) {

                if (pin.matches(password)){

                    Intent intent1 = new Intent(context, LockScreenService.class);
                    context.stopService(intent1);

                    Intent intent = new Intent(context, MainActivity.class);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(context, R.string.incorrect_unstall_pin, Toast.LENGTH_SHORT).show();
//                    App.showToast(R.string.incorrect_unstall_pin);
                }
            } else {
                Toast.makeText(context, "Uninstall app password not set.", Toast.LENGTH_SHORT).show();
//                Logger.error("Uninstall app password not set.");
            }
        }

        @Override
        public void onEmpty() {

        }

        @Override
        public void onPinChange(int pinLength, String intermediatePin) {

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        windowManager.removeView(lockScreenView);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification displayForegroundNotification(Context context) {

        String CHANNEL_ID = "MTS_CHANNEL";

        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(NOTIFICATION_SERVICE);
        CharSequence name = context.getString(R.string.app_name);

        // Create the channel for the notification
        NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_MIN);
        mChannel.setLockscreenVisibility(Notification.VISIBILITY_SECRET);

        // Set the Notification Channel for the Notification Manager.
        mNotificationManager.createNotificationChannel(mChannel);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        builder
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(getString(R.string.app_name) + " is running"))
                .setChannelId(CHANNEL_ID)
                .setSound(null)
                .setAutoCancel(false);

        //pending intetnt to open DashboardActivity when tapped on notification
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        return builder.build();
    }
}
