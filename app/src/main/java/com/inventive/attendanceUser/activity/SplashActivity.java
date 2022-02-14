package com.inventive.attendanceUser.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.inventive.attendanceUser.R;
import com.inventive.attendanceUser.broadcastReceiver.AdministrativeBroadcastReceiver;

public class SplashActivity extends AppCompatActivity {
    DevicePolicyManager devicePolicyManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);


    }

    protected boolean isAllowedAdminPermission() {
        return devicePolicyManager.isAdminActive(new ComponentName(SplashActivity.this, AdministrativeBroadcastReceiver.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isAllowedAdminPermission()) {
            Log.d("aaaaaaaaa","ffffffffff");
            Intent activateDeviceAdmin = new Intent(
                    DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            activateDeviceAdmin.putExtra(
                    DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                    new ComponentName(SplashActivity.this, AdministrativeBroadcastReceiver.class));
            activateDeviceAdmin
                    .putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                            R.string.administrative_permission_message);
            startActivity(activateDeviceAdmin);
        }
    }
}