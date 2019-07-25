package com.andispy.andispy.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.EditText;

public class Common {

	public static void showAlertDialog(final Activity act, String title,
			String msg, final boolean needToClose, boolean isCancelable) {
		final AlertDialog alert = new AlertDialog.Builder(act)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						if (needToClose) {
							act.finish();
						}

					}
				}).create();
		if (title != null)
			alert.setTitle(title);
		if (msg != null)
			alert.setMessage(msg);
		alert.show();
		alert.setCancelable(isCancelable);
	}

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.getState() == NetworkInfo.State.CONNECTED
				|| connectivityManager.getNetworkInfo(
						ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
			return true;
		} else {
			return false;
		}
	}
	public static boolean valid_name(EditText name)
	{
		if(name.getText().toString().length()==0){	
			name.setError(Constants.MSG_VALID_NAME);
			name.requestFocus();
			return false;
		}
		return false;
	}
	public static void valid_mobilenumber(EditText mobileno)
	{
		if(mobileno.getText().toString().length()==0){	
			mobileno.setError(Constants.MSG_VALID_PHONE_NO);
			mobileno.requestFocus();		
		}
	}
	public static void valid_email(EditText email)
	{
		if(email.getText().toString().length()==0){	
			email.setError(Constants.MSG_VALID_EMAIL);
			email.requestFocus();		
		}
	}
	public static void valid_password(EditText password)
	{
		if(password.getText().toString().length()==0){	
			password.setError(Constants.MSG_VALID_PASSWORD);
			password.requestFocus();		
		}
	}
	public static void valid_repassword(EditText repassword)
	{
		if(repassword.getText().toString().length()==0){	
			repassword.setError(Constants.MSG_VALID_REPASSWORD);
			repassword.requestFocus();		
		}
		
	}
}
	

