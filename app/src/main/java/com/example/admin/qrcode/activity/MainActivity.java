package com.example.admin.qrcode.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.admin.qrcode.R;
import com.example.admin.qrcode.other.Constants;
import com.example.admin.qrcode.other.Utils;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler
{
    private String mTAG = "QRCode";
    private int current_api_version = 0;
    private ZXingScannerView scannerView;
    private AlertDialog.Builder alertDialog;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = MainActivity.this;

        current_api_version = Build.VERSION.SDK_INT;
        Log.v(mTAG, "current api version : " + current_api_version);

        // Finding ID of a Scanner View
        scannerView = (ZXingScannerView) findViewById(R.id.scanner_view);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        // Checking Permissions
        checkPermissions();
    }

    @Override
    public void onPause()
    {
        super.onPause();

        // Stop camera on pause
        scannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result)
    {
        Log.v(mTAG, "result : " + result.getText());

        // Calling QR Code Details Activity
        Intent intent = new Intent(MainActivity.this, QRCodeDetailsActivity.class);
        intent.putExtra("qr_code_details", result.getText());
        startActivity(intent);
        finish();

        /*
        Toast.makeText(context, "QR Code scanned. Result : " + result.getText(), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                finish();
            }
        }, 200);
        */
    }

    private void startCamera()
    {
        // Register ourselves as a handler for scan results
        scannerView.setResultHandler(this);

        // Start Camera on resume
        scannerView.startCamera();
    }

    private void checkPermissions()
    {
        Log.v(mTAG, "00000** checkPermissions() method called");

        if (current_api_version >= 23)
        {

            if (!Utils.isSelfPermissionGranted(new String[]
                    {
                            android.Manifest.permission.CAMERA,
                    }, MainActivity.this))
            {

                ActivityCompat.requestPermissions(
                        MainActivity.this,
                        new String[]
                                {
                                        android.Manifest.permission.CAMERA,
                                },
                        Constants.PERMISSION_REQUEST_CODE
                );
            }
            else
            {
                startCamera();
            }
        }
        else
        {
            startCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        switch (requestCode)
        {
            case Constants.PERMISSION_REQUEST_CODE:
            {
                try
                {
                    for(int i = 0; i < grantResults.length; i++)
                    {
                        if(grantResults.length > 0 && grantResults[i] == PackageManager.PERMISSION_GRANTED)
                        {
                            startCamera();
                        }
                        else
                        {
                            boolean showRationale = shouldShowRequestPermissionRationale(permissions[i]);

                            if(!showRationale)
                            {
                                alertDialog = Utils.getAlertDialog(context, "", "Please allow the permissions to continue.");
                                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(final DialogInterface dialog, int which) {
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                dialog.dismiss();

                                                Intent intent = new Intent();
                                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                                intent.setData(uri);
                                                startActivityForResult(intent, Constants.PERMISSION_REQUEST_CODE);

                                            }
                                        }, 100);
                                    }
                                });
                                alertDialog.show();
                            }
                            else
                            {
                                alertDialog = Utils.getAlertDialog(context, "", "Please select \"ALLOW\" to continue.");
                                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(final DialogInterface dialog, int which) {
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                dialog.dismiss();

                                                // Checking Permissions
                                                checkPermissions();
                                            }
                                        }, 100);
                                    }
                                });
                                alertDialog.show();
                            }

                        }

                        return;
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode)
        {
            case Constants.PERMISSION_REQUEST_CODE:
                // Checking Permissions
                checkPermissions();
                break;

            default:
                break;
        }
    }
}
