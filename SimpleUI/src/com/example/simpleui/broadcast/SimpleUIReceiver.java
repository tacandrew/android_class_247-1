package com.example.simpleui.broadcast;

import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import com.example.simpleui.MainActivity;

public class SimpleUIReceiver extends BroadcastReceiver {

	private static final String TAG = "debug";

	@Override
	public void onReceive(Context context, Intent intent) {

		String action = intent.getAction();

		if (action.equals("com.example.simpleui.push")) {
			try {

				String channel = intent.getStringExtra(
						"com.parse.Channel");
				JSONObject json = new JSONObject(intent.getStringExtra(
						"com.parse.Data"));

				Log.d(TAG, "got action " + action + " on channel " + channel
						+ " with:");
				Iterator itr = json.keys();
				while (itr.hasNext()) {
					String key = (String) itr.next();
					Log.d(TAG, "..." + key + " => " + json.getString(key));
				}
			} catch (JSONException e) {
				Log.d(TAG, "JSONException: " + e.getMessage());
			}
			
			Intent newIntent = new Intent("com.example.simpleui.add");
			context.sendBroadcast(newIntent);
		}

	}
}
