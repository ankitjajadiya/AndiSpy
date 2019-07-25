package com.andispy.andispy.network;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.andispy.andispy.lib.BackgroundWork;
import com.andispy.andispy.lib.Completion;
import com.andispy.andispy.lib.Tasks;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Postdata {
    public AsyncResponse delegate;


    public Postdata(final String Url, final JSONObject json, final Context from, final Class to) throws JSONException {

        Tasks.executeInBackground(from, new BackgroundWork<HttpResponse>() {
            @Override
            public HttpResponse doInBackground() throws Exception {

                // Create a new HttpClient and Post Header
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Url);

                try {
                    // JSON data:

                    String postjson = "";

                    postjson = json.toString();
                    // Post the data:
                    StringEntity se = new StringEntity(postjson);
                    httppost.setEntity(se);
                    httppost.setHeader("Accept", "application/json");
                    httppost.setHeader("Content-type", "application/json");
                    // httppost.setHeader("json",json.toString());
                    System.setProperty("http.keepAlive", "false");
                    //httppost.getParams().setParameter("jsonpost",postjson);
                    // Execute HTTP Post Request
                    System.out.print(json);
                    return httpclient.execute(httppost);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }, new Completion<HttpResponse>() {
            @Override
            public void onSuccess(Context context, HttpResponse response) {

                try {
                    Log.println(Log.ASSERT,"http","done");

                    if (response != null) {
                        HttpResult res = new HttpResult();
                        String result = res.inputStreamToString(response.getEntity().getContent()).toString();
                        int returnedResult = res.returnParsedJsonObject(result);
                    Log.println(Log.ASSERT,"http",""+result);
                    /* res.checkemailpass(result);
	                 String Login= res.CheckLogin(result) ;
	                 if(Login.equals("login"))
	                 {

				                 String UserId= res.GetUserID(result);
				           	 	Log.d("user",UserId);

				                 SharedPreferences pref = from.getSharedPreferences("MyPref", from.MODE_PRIVATE);
				          	   Editor editor = pref.edit();
				          	 editor.putString("User_ID",UserId);  // Saving string
				          	editor.commit(); // commit changes
				          	Intent registr = new  Intent(from,to);
	                	 	registr.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	                	 	from.startActivity(registr);
	                	 		Log.d("LoginUser",String.valueOf(UserId));
				                 }
	                 else if(Login.equals("notlogin"))
	                 {
	                	Toast.makeText(from,Constants.MSG_LOGIN_ERROR,Toast.LENGTH_SHORT).show();
	                 }*/
                        if (returnedResult == 1) {
							/*	  delegate.processFinish(returnedResult);
							*/
                            Intent registr = new Intent(from, to);
                            registr.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            from.startActivity(registr);

                        }
                        if (returnedResult == 0) {
                            Log.d("jsonresult", String.valueOf(returnedResult));

                        }
                    }

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                }
            }

            @Override
            public void onError(Context context, Exception e) {

            }
        });


    }


}
