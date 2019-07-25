package com.andispy.andispy.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class HttpResult {   

	public StringBuilder inputStreamToString(InputStream is) {
	   	 
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
	public int returnParsedJsonObject(String result){
  	 
   	JSONObject resultObject = null;
   	 String RegID="";
   	int returnedResult = 0;
  	 
   	try {
   	 
   	resultObject = new JSONObject(result);
  	 String Q= resultObject.getString("Qry");
  	 	RegID=resultObject.getString("ID");
		returnedResult = resultObject.getInt("code");


   	} catch (JSONException e) {
   	 
   	e.printStackTrace();
   	 
   	}
   	 
   	return returnedResult;
   	 
   	}
	
	public  void checkemailpass(String result){
		JSONObject resultObject = null;
		String e1="" ;
		String p1="";

		try {
			e1=resultObject.getString("loginemilid");

			p1=resultObject.getString("loginpassid");
			Log.d("emailcheck",e1);
			Log.d("passcheck",p1);

		} catch (JSONException e) {
			e.printStackTrace();
		}


	}


	public String CheckLogin(String result){
  	 
   	JSONObject resultObject = null;
   	 String RegID="";
   	 String loginche="";
   	int login=0;
   	try {
   	resultObject = new JSONObject(result);
   	loginche = resultObject.getString("Login");
 	Log.d("Login",loginche);
if(loginche.equals("login"))
{
  	 	RegID=resultObject.getString("ID");

}

   	} catch (JSONException e) {
   	 
   	e.printStackTrace();
   	}
   	 
   	return loginche;
   	 
   	}
	
	
	
	
	public String GetUserID(String result){
  	 
   	JSONObject resultObject = null;
   	 String RegID="";
   	int returnedResult = 0;
   	try {
   	 
   	resultObject = new JSONObject(result);
   	RegID=resultObject.getString("ID");

   	} catch (JSONException e) {
   	 
   	e.printStackTrace();
   	 
   	}
   	 
   	return RegID;
   	 
   	}

}
