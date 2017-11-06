package com.example.admin.qrcode.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.admin.qrcode.R;

public class QRCodeDetailsActivity extends AppCompatActivity
{
    private String mTAG = "QRCodeDetails";
    private TextView textView;
    private String qr_code_details = "";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_details);

        // Finding ID of a View
        textView = (TextView) findViewById(R.id.text_view_2);

        // Getting data from previous activity
        qr_code_details = getIntent().getExtras().getString("qr_code_details");
        Log.v(mTAG, "qr code details : " + qr_code_details);

        // Setting Data to the Text View
        textView.setText(qr_code_details);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent keyEvent)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            QRCodeDetailsActivity.this.finish();
        }
        return true;
    }
}
