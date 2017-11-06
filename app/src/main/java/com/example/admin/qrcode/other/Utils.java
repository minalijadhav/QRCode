package com.example.admin.qrcode.other;

import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.PermissionChecker;

import com.example.admin.qrcode.R;

/**
 * Created by Admin on 02/10/2017.
 */

public class Utils
{

    public static boolean isSelfPermissionGranted(String[] permissions, Context context)
    {
        boolean result = true;

        int targetSDKVersion = Build.VERSION.SDK_INT;

        try
        {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            targetSDKVersion = packageInfo.applicationInfo.targetSdkVersion;

        }
        catch(PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if(targetSDKVersion >= Build.VERSION_CODES.M)
            {
                // for targetVersion > M, we use context.checkSelfPermission
                for(String permission : permissions)
                {
                    result = context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;

                    if(! result)
                        break;
                }
            }
            else
            {
                // for targetVersion < M, we use PermissionChecker.checkSelfPermission

                for(String permission : permissions)
                {
                    result = PermissionChecker.checkSelfPermission(context, permission) == PermissionChecker.PERMISSION_GRANTED;

                    if(! result)
                        break;
                }

            }
        }

        return result;
    }

    public static AlertDialog.Builder getAlertDialog(Context context, String title, String message)
    {
        AlertDialog.Builder alertDialog = null;

        if(Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
            alertDialog = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        else
            alertDialog = new AlertDialog.Builder(context);

        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);

        return alertDialog;
    }
}
