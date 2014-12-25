package com.example.simpleui.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class DeleteReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		Toast.makeText(context, "delete successfully [from DeleteReceiver]",
				Toast.LENGTH_SHORT).show();
	}

}
