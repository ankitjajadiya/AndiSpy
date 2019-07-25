package com.andispy.andispy;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.andispy.andispy.util.Util;

public class BaseActivity extends AppCompatActivity {
    public SharedPreferences pref;
    public Util util;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        util=new Util(this);
        pref = getSharedPreferences("MyPref", MODE_PRIVATE);
    }


    public void logout()
    {
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("Login",false );  // Saving string
        editor.commit(); // commit changes
    }
}
