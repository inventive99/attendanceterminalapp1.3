package com.inventive.attendanceUser.service;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.IBinder;
import android.util.Log;

import com.inventive.attendanceUser.R;
import com.inventive.attendanceUser.activity.MessageActivity;

public class MyService extends Service {
    static Ringtone batterylow;
    static Ringtone batteryfull;
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = getApplicationContext().registerReceiver(null, ifilter);

        /*// How are we charging?
        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;*/

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPct = level * 100 / (float) scale;

        Log.d("battery" + batteryPct, "logbattery");

        // Are we charging / charged?
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;

        Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                + "://" + getApplicationContext().getPackageName() + "/raw/batterylow");

        batterylow = RingtoneManager.getRingtone(getApplicationContext(), alarmSound);

        Uri alarmSound2 = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                + "://" + getApplicationContext().getPackageName() + "/raw/batteryfull");

        batteryfull = RingtoneManager.getRingtone(getApplicationContext(), alarmSound2);


        if (!isCharging) {
            if (batteryPct <= 30) {
                batterylow.play();
            }
        }
        else {
            batterylow.stop();
        }

        if (isCharging) {
            if (batteryPct == 100) {
                batteryfull.play();
            }
        }
        else {
            batteryfull.stop();
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        batterylow.stop();
        batteryfull.stop();
        super.onDestroy();
    }
}