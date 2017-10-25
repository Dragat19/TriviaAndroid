package com.playtown.triviaplay.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.playtown.triviaplay.R;

import static android.R.style.Theme_Material_Light_Dialog_Alert;
import static android.content.Context.CONNECTIVITY_SERVICE;

public class Utils {
    private static final String PREF_NAME = "com.newset.showTour";
    private static final String URL_WAP = "com.newset.urlWap";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirst";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context _context;
    private int PRIVATE_MODE = 0;

    public Utils(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }


    public void setUrlWap(String urlWap) {
        editor.putString(URL_WAP, urlWap);
        editor.commit();
    }

    public String getUrlWap() {
        return pref.getString(URL_WAP, null);
    }

    public static Boolean hasInternet(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static void shareIntent(Activity activity, String text) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text + "\n" + activity.getString(R.string.app_link));
        sendIntent.setType("text/plain");
        activity.startActivity(sendIntent);
    }


    public void showInfoDialog(Context context, String strTitle, String msg) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(strTitle);
        dialog.setMessage(msg);
        dialog.setCancelable(true);
        dialog.create().show();
    }

    public static Boolean checkEnabledInternet(Context context) {
        boolean isConnected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
        for (NetworkInfo networkInfo : networkInfos) {
            if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                isConnected = true;
                break;
            }
        }
        return isConnected;
    }

    public void showDialogoffInternet(Context context) {
        final AlertDialog.Builder networkDialog = new AlertDialog.Builder(context, Theme_Material_Light_Dialog_Alert);
        networkDialog.setTitle("Not Connected");
        networkDialog.setMessage("Please connect to internet to proceed");
        networkDialog.setCancelable(false);
        networkDialog.setPositiveButton("Okay!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                networkDialog.setCancelable(true);
            }
        });
        networkDialog.create().show();
    }


}
