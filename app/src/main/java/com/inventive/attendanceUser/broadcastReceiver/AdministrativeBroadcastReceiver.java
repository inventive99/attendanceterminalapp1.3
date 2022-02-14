package com.inventive.attendanceUser.broadcastReceiver;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.inventive.attendanceUser.MainActivity;
import com.inventive.attendanceUser.R;
import com.inventive.attendanceUser.Utils.ConstantValues;
import com.inventive.attendanceUser.env.Logger;
import com.inventive.attendanceUser.service.LockScreenService;


/**
 * Created by Vrushali on 23/4/2021.
 */
public class AdministrativeBroadcastReceiver extends DeviceAdminReceiver {


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onEnabled(Context context, Intent intent) {
        super.onEnabled(context, intent);
        Logger.debug("AdministrativeBroadcastReceiver Enabled");

        ConstantValues.putUnInstallStatus("0",context);
    }

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {

        Logger.debug("AdministrativeBroadcastReceiver DisableRequested");

        String status = ConstantValues.getUninstallStatus();

//        if (status.equalsIgnoreCase("0")) {
            Intent intent1 = new Intent(context, LockScreenService.class);
            if (Build.VERSION.SDK_INT >= 26) {
                context.startForegroundService(intent1);
            } else {
                context.startService(intent1);
            }
//        }
        return super.onDisableRequested(context, intent);
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        super.onDisabled(context, intent);
        Toast.makeText(context, "AdministrativeBroadcastReceiver Disabled", Toast.LENGTH_SHORT).show();
//        App.showToast("AdministrativeBroadcastReceiver Disabled");
        Logger.debug("AdministrativeBroadcastReceiver Disabled");

//        String status = PreferenceUtils.getUninstallStatus();
//
//        if (status.equalsIgnoreCase("0")) {
            Intent intent1 = new Intent(context, LockScreenService.class);
            if (Build.VERSION.SDK_INT >= 26) {
                context.startForegroundService(intent1);
            } else {
                context.startService(intent1);
            }
//        }
    }

    @Override
    public void onPasswordChanged(Context context, Intent intent) {
        super.onPasswordChanged(context, intent);
        Logger.debug("AdministrativeBroadcastReceiver Password Changed");
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onProfileProvisioningComplete(Context context, Intent intent) {
        super.onProfileProvisioningComplete(context, intent);
        DevicePolicyManager manager =
                (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName componentName = new ComponentName(context, AdministrativeBroadcastReceiver.class);
        if (manager != null) {
            manager.setProfileEnabled(componentName);
            manager.setProfileName(componentName, context.getString(R.string.app_name));
        }

        Intent launch = new Intent(context, MainActivity.class);
        launch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(launch);
    }


}
