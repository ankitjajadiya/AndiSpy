package com.andispy.andispy.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.andispy.andispy.service.MyService;

public class AlarmReceiver extends BroadcastReceiver {
    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context,MyService.class));
    }
}
