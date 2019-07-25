package com.andispy.andispy.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;



import com.andispy.andispy.util.*;

public class AsyncDataClass  extends AsyncTask<List<NameValuePair>, Void, String>{

	public AsyncResponse delegate=null;
 	Context mContext;
	 String link;
	 String message;

	public AsyncDataClass(Context context,String url ,String msg) {
	      this.mContext = context;	
	      link = url;
	      message=msg;
	        
	}
	private StringBuilder inputStreamToString(InputStream is) {
   	 
	String rLine = "";
	 
	StringBuilder answer = new StringBuilder();
	 
	BufferedReader br = new BufferedReader(new InputStreamReader(is));
	 
	try {
	 
	while ((rLine = br.readLine()) != null) {
	 
	answer.append(rLine);
	 
	}
	 
	} catch (IOException e) {
	 
	// TODO Auto-generated catch block
	 
	e.printStackTrace();
	 
	}
	 
	return answer;
	 
	}
	@Override
	 
	protected void onPreExecute() {
	super.onPreExecute();
	}
	
	@Override
	 
	protected void onPostExecute(String result) {	 
	super.onPostExecute(result);	 
	System.out.println("Resulted Value: " + result);	 
	if(result.equals("") || result == null){

	return;
	}
	
	HttpResult r=new HttpResult();
 	int jsonResult = r.returnParsedJsonObject(result);
	if(jsonResult == 0 ){
		Toast.makeText(mContext, "error....", Toast.LENGTH_SHORT).show();
	return;	 
	}
	 
	if(jsonResult == 1){
		
	      //Toast.makeText(mContext, "Insert Succ....", Toast.LENGTH_SHORT).show();
		
		return;	 
	}
	
	
	}
	
	@Override
	 
	protected String doInBackground(List<NameValuePair>... params) {
		
		HttpParams httpParameters = new BasicHttpParams();
   	 
    	HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
    	 
    	HttpConnectionParams.setSoTimeout(httpParameters, 5000);
    	 
    	HttpClient httpClient = new DefaultHttpClient(httpParameters);
    	 
    	HttpPost httpPost = new HttpPost(link);
    	 
    	String jsonResult = "";
    	 
    	try {
    	
    	
    	httpPost.setEntity(new UrlEncodedFormEntity((List<? extends NameValuePair>) params[0]));
    	
    	HttpResponse response = httpClient.execute(httpPost);
    	 
    	jsonResult = inputStreamToString(response.getEntity().getContent()).toString();
    	 
    	}
    	catch (ClientProtocolException e)
    	{
    	 
    	e.printStackTrace();
    	 
    	}
    	catch (IOException e)
    	{
    	 
    	e.printStackTrace();
    	 
    	}
    	return jsonResult;
	}

}
