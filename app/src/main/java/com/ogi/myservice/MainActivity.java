package com.ogi.myservice;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public Button btnStartService,  btnStartIntentService, btnStartBoundService, btnStopBoundService;
    boolean mServiceBound = false;
    BoundService mBoundService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnStartService = findViewById(R.id.btn_start_service);
        btnStartService.setOnClickListener(this);

        btnStartIntentService = findViewById(R.id.btn_start_intent_service);
        btnStartIntentService.setOnClickListener(this);

        btnStartBoundService = findViewById(R.id.btn_start_bound_service);
        btnStartBoundService.setOnClickListener(this);

        btnStopBoundService = findViewById(R.id.btn_stop_bound_service);
        btnStopBoundService.setOnClickListener(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mServiceBound){
            unbindService(mServiceConnection);
        }
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
           BoundService.MyBinder myBinder = (BoundService.MyBinder) service;
           mBoundService = myBinder.getService();
           mServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServiceBound = false;
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_start_service:
                Intent mStartService = new Intent(MainActivity.this, OriginService.class );
                startService(mStartService);
            break;
            case R.id.btn_start_intent_service:
                Intent mStartIntentService = new Intent(MainActivity.this, DicodingIntentService.class);
                mStartIntentService.putExtra(DicodingIntentService.EXTRA_DURATION, 5000);
                startService(mStartIntentService);
                break;
            case R.id.btn_start_bound_service:
                Intent mBoundServiceIntent = new Intent(MainActivity.this, BoundService.class);
                bindService(mBoundServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
                break;
            case R.id.btn_stop_bound_service:
                unbindService(mServiceConnection);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }
}
