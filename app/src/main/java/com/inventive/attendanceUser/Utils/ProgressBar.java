package com.inventive.attendanceUser.Utils;

import android.content.Context;

public class ProgressBar {

    private static com.inventive.attendanceUser.Utils.ProgressBar progress;



    public static void showProgress(Context context) {
        if (progress == null) {
//            progress = KProgressHUD.create(context).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCancellable(false).setAnimationSpeed(2).setDimAmount(0.5f).show();

        }

    }

    public static void hideProgress() {
//        if (progress != null && progress.isShowing()) {
//            progress.dismiss();
//            progress = null;
//        }
    }
}
