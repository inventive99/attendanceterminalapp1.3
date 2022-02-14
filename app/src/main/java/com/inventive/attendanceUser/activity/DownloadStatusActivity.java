package com.inventive.attendanceUser.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.inventive.attendanceUser.MainActivity;
import com.inventive.attendanceUser.NetworkStuff.APIClient;
import com.inventive.attendanceUser.NetworkStuff.APIInterface;
import com.inventive.attendanceUser.NetworkStuff.RegiatrationNetworkCaller;
import com.inventive.attendanceUser.NetworkStuff.ResponseCarrier;
import com.inventive.attendanceUser.R;
import com.inventive.attendanceUser.Utils.ConstantValues;
import com.inventive.attendanceUser.Utils.SessionManager;
import com.inventive.attendanceUser.database.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class DownloadStatusActivity extends AppCompatActivity implements ResponseCarrier {
    TextView txt_time;
    LinearLayout lay_back;
    private DatabaseHelper db;
    Handler h = new Handler();
    int delay =1000; //1 second=1000 milisecond, 15*1000=15seconds
    Runnable runnable;

    RegiatrationNetworkCaller networkCaller;
    APIInterface RetroFetcher;
    SessionManager sessionManager;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    Dialog dialog;

    private static final int PERMISSIONS_REQUEST = 1;
    private static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;

    private final int GetLicenseConstant = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_status);

        txt_time=findViewById(R.id.txt_time);
        lay_back=findViewById(R.id.lay_back);

        initVariable();

        if (hasPermission()) {

        } else {
            requestPermission();
        }

        ConstantValues.putSingleVisit("0","0",DownloadStatusActivity.this);
        ConstantValues.putWindowOpen("0","0",DownloadStatusActivity.this);

        lay_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenDialogue();
            }
        });

    }

    @Override
    protected void onResume() {
        h.postDelayed(runnable = new Runnable() {
            public void run() {
                h.postDelayed(runnable, delay);

                Cursor cursor = db.getEmployeeInfo();
                int totalemp=cursor.getCount();
                if (totalemp>0){
                    Intent i=new Intent(DownloadStatusActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();

                }
            }
        }, delay);
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        h.removeCallbacks(runnable);
    }

    private void initVariable() {
        sessionManager = new SessionManager(DownloadStatusActivity.this);
        networkCaller = new RegiatrationNetworkCaller(DownloadStatusActivity.this, this);
        RetroFetcher = APIClient.StringClient().create(APIInterface.class);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        db = new DatabaseHelper(this);

    }

    private void OpenDialogue() {
        dialog = new Dialog(DownloadStatusActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_confirmation);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final Button btn_yes = dialog.findViewById(R.id.btn_yes);
        final EditText edt_pass = dialog.findViewById(R.id.edt_pass);
        final ImageView img_close = dialog.findViewById(R.id.img_close);
        final TextView txt_msg = dialog.findViewById(R.id.txt_msg);

        edt_pass.setHint("Enter License Key");
        txt_msg.setText("Please Enter License Key to Proceed.");
        edt_pass.setInputType(InputType.TYPE_CLASS_NUMBER);

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(edt_pass.getText().toString())){
                    edt_pass.setFocusable(true);
                    edt_pass.setError("Enter License Key");
                }else {
                    JsonObject request = new JsonObject();
                    request.addProperty("license_key", edt_pass.getText().toString());
                    request.addProperty("device", Build.MODEL);
                    networkCaller.ValidateLicenseKey(RetroFetcher, GetLicenseConstant, request);
                }
            }
        });

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    @Override
    public void Success(Response<ResponseBody> response, int Identifier) {
        switch (Identifier) {
            case GetLicenseConstant:
                ExtractLicenseData(response);
                break;

        }
    }

    @Override
    public void Error(Throwable Response, int Identifier) {

    }

    private void ExtractLicenseData(Response<ResponseBody> response) {
        try {
            JSONObject object = new JSONObject(response.body().string());
            if (object.optString("status").equalsIgnoreCase("success")) {

                JSONObject data = new JSONObject(object.optString("data"));

                String status=data.optString("status");
                String orglocation=data.optString("orglocation");

                Toast.makeText(this, object.optString("message"), Toast.LENGTH_SHORT).show();
                ConstantValues.putlicense_keyStatus(status,orglocation, DownloadStatusActivity.this);

                dialog.dismiss();


            } else {
                Toast.makeText(DownloadStatusActivity.this, object.optString("message"), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(
            final int requestCode, final String[] permissions, final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST) {
            if (allPermissionsGranted(grantResults)) {

            } else {
                requestPermission();
            }
        }
    }

    private static boolean allPermissionsGranted(final int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private boolean hasPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(PERMISSION_CAMERA) == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (shouldShowRequestPermissionRationale(PERMISSION_CAMERA)) {
                Toast.makeText(
                        DownloadStatusActivity.this,
                        "Camera permission is required for this demo",
                        Toast.LENGTH_LONG)
                        .show();
            }
            requestPermissions(new String[] {PERMISSION_CAMERA}, PERMISSIONS_REQUEST);
        }
    }

}