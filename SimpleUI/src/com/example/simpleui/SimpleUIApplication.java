package com.example.simpleui;

import java.util.List;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.PushService;

import android.app.Application;
import android.provider.Settings.Secure;
import android.util.Log;

public class SimpleUIApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		Parse.enableLocalDatastore(this);
		Parse.initialize(this, "xFjWt1sp1ewmSVX0xEr1yODF7Q81xYELghV0GXwN",
				"kzL2psN9bd3CGmZLHPwjcwrSuoM5APCXnjcw0w1p");
		PushService.setDefaultPushCallback(this, MainActivity.class);
		ParsePush.subscribeInBackground("all");
		ParsePush.subscribeInBackground("device_id_" + getDeviceId());
		Log.d("debug", "app onCreate");

		register();
	}

	private void register() {
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("DeviceId");
		query.whereEqualTo("deviceId", getDeviceId());
		query.findInBackground(new FindCallback<ParseObject>() {
			
			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				if (objects.size() == 0) {
					ParseObject object = new ParseObject("DeviceId");
					object.put("deviceId", getDeviceId());
					object.saveInBackground();
					
					ParsePush push = new ParsePush();
					push.setChannel("all");
					push.setMessage("new device coming");
					push.sendInBackground();
				}
			}
		});
	}

	private String getDeviceId() {
		return Secure.getString(getContentResolver(), Secure.ANDROID_ID);
	}
}
