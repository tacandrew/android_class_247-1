package com.example.simpleui;

import com.parse.Parse;
import com.parse.ParsePush;
import com.parse.PushService;

import android.app.Application;
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
		Log.d("debug", "app onCreate");
	}
}
