package com.andispy.andispy.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.andispy.andispy.BaseActivity;
import com.andispy.andispy.R;
import com.andispy.andispy.util.Common;
import com.andispy.andispy.util.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginActivity extends BaseActivity {

    Button log;
    Button sign;
    EditText email;
    EditText pass;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        log = (Button) findViewById(R.id.login);
        sign = (Button) findViewById(R.id.login1);

        email = (EditText) findViewById(R.id.Email);
        pass = (EditText) findViewById(R.id.Password);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);

            }
        });

        if(pref.getBoolean("Login",false))
        {
            goto_home();
        }

    }


    public void onClick(View v) {
        String Email = email.getText().toString();
        String Pass = pass.getText().toString();
        if (Common.isNetworkAvailable(getBaseContext())) {
            if (email.getText().toString() != "") {

                Common.valid_email(email);
                Common.valid_password(pass);
                // TODO Auto-generated method stub

                HashMap<String, String> data = new HashMap<>();
                data.put("loginemail", Email);
                data.put("loginpass", Pass);
                util.progressDialog.show();
                util.http.Login(data, new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {
                        util.progressDialog.hide();
                        try {
                            Log.println(Log.ASSERT, "data", "" + util.getJson(response));
                            JSONObject jsonObject = util.getJson(response);
                            int code = 0;

                            code = jsonObject.getInt("code");


                            if (code == 1) {
                                JSONObject data = jsonObject.getJSONObject("data");
                                String UserId = data.getString("UserId");
                                Log.d("user", UserId);


                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("User_ID", UserId);  // Saving string
                                editor.putBoolean("Login",true );  // Saving string
                                editor.commit(); // commit changes
                               goto_home();

                            } else {
                                Toast.makeText(LoginActivity.this, "Login error", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void failure(RetrofitError error) {
                        util.progressDialog.hide();
                        Log.println(Log.ASSERT,"reto error",""+error);
                    }
                });


//                        try {
//
//                            objAndroid.put("loginemail", jsonemail);
//                            objAndroid.put("loginpass", jsonpass);
//
//                            //Toast.makeText(this, "email="+jsonemail+"\npass="+jsonpass, Toast.LENGTH_LONG).show();
//
//                            objPhp.put("req", objAndroid);
//
//                            Postdata P1= new Postdata(Constants.URL_login, objPhp,getApplicationContext(),WelcomeActivity.class);
//
//
//                        } catch (JSONException e)
//                        {
//
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//
//                        }

            } else {
                Toast.makeText(getApplicationContext(), "Blank Not Allowed", Toast.LENGTH_LONG).show();

            }

        } else {
            Common.showAlertDialog(LoginActivity.this,
                    Constants.MSG_TITLE_ERROR,
                    Constants.MSG_CONNECTION_ERROR, false, true);

        }
    }

    private void goto_home() {
        Intent registr = new Intent(LoginActivity.this, WelcomeActivity.class);
        registr.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(registr);
        finish();
    }


    @Override
    protected void onPostResume() {
        // TODO Auto-generated method stub

        super.onPostResume();      // getting String


    }

}
