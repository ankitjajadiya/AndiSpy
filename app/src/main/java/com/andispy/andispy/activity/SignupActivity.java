package com.andispy.andispy.activity;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.andispy.andispy.BaseActivity;
import com.andispy.andispy.R;
import com.andispy.andispy.network.Postdata;
import com.andispy.andispy.util.Common;
import com.andispy.andispy.util.Constants;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class SignupActivity extends BaseActivity {

    EditText name,email,phone,pass,confpass;
    Button signup;

    int rnd1= (int) (Math.random()*1000000);
    String randomNumber = String.valueOf(rnd1);
    String regid="ASA"+randomNumber;

    JSONArray jsonregi = new JSONArray();
   JSONArray jsonname = new JSONArray();
   JSONArray jsonemail = new JSONArray();
   JSONArray jsonphon = new JSONArray();
   JSONArray jsonpass = new JSONArray();

    JSONObject objAndroid = new JSONObject();
    JSONObject objPhp = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2_signup);
        name = (EditText) findViewById(R.id.edtName);
        email = (EditText) findViewById(R.id.edtEmail);
        phone = (EditText) findViewById(R.id.edtPhone);
        pass = (EditText) findViewById(R.id.edtPass);
        confpass = (EditText) findViewById(R.id.edtConfPass);

        signup = (Button) findViewById(R.id.btnSignup);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }
    public void onClick(View v) {
        String Name = name.getText().toString();
        String Email = email.getText().toString();
        String Phone = phone.getText().toString();
        String Pass = pass.getText().toString();
        String ConfPass = confpass.getText().toString();
       // util.progressDialog.show();
        if (Common.isNetworkAvailable(getBaseContext())) {
            util.progressDialog.show();
            Common.valid_name(name);
            Common.valid_email(email);
            Common.valid_mobilenumber(phone);
            Common.valid_password(pass);
            Common.valid_repassword(confpass);

            jsonregi.put(regid);
            jsonname.put(Name);
            jsonemail.put(Email);
            jsonphon.put(Phone);
            jsonpass.put(Pass);
            //Toast.makeText(SignupActivity.this, "r="+regid+"\nn="+Name+"\ne="+Email+"\np="+Phone+"\nPaa="+Pass, Toast.LENGTH_SHORT).show();
            try {
                util.progressDialog.hide();
                objAndroid.put("Uregi", jsonregi);
                objAndroid.put("Uname", jsonname);
                objAndroid.put("Uemail", jsonemail);
                objAndroid.put("Uphone", jsonphon);
                objAndroid.put("Upass", jsonpass);
                // Toast.makeText(SignupActivity.this, "r="+jsonregi+"\nn="+jsonname+"\ne="+jsonemail+"\np="+jsonphon+"\nPaa="+jsonpass, Toast.LENGTH_SHORT).show();
                objPhp.put("req", objAndroid);

                Postdata P = new Postdata(Constants.URL_reg, objPhp, getApplicationContext(), LoginActivity.class);

                Intent i = new Intent(this,LoginActivity.class);
                startActivity(i);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            util.progressDialog.hide();
            Common.showAlertDialog(SignupActivity.this,
                    Constants.MSG_TITLE_ERROR,
                    Constants.MSG_CONNECTION_ERROR, false, true);

        }


    }


    @Override
    protected void onPostResume() {
        // TODO Auto-generated method stub

        super.onPostResume();      // getting String


    }
}
