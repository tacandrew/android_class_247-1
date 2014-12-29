package com.example.simpleui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity {

	private TextView textView;
	private EditText editText;
	private Button button, button3;
	private CheckBox checkBox;
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;
	private ProgressDialog progress;
	private Spinner spinner;

	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			send();
		}
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.d("debug", "MainActivity onCreate");

		setContentView(R.layout.activity_main);

		sp = getSharedPreferences("settings", Context.MODE_PRIVATE);
		editor = sp.edit();

		progress = new ProgressDialog(this);

		editText = (EditText) findViewById(R.id.editText1);
		button = (Button) findViewById(R.id.button1);
		button3 = (Button) findViewById(R.id.button3);
		checkBox = (CheckBox) findViewById(R.id.checkBox1);
		textView = (TextView) findViewById(R.id.textView1);
		spinner = (Spinner) findViewById(R.id.spinner1);

		textView.setText("device id: " + getDeviceId());

		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				send();
			}
		});

		button3.setOnClickListener(onClickListener);

		checkBox.setChecked(sp.getBoolean("checkbox", false));
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				editor.putBoolean("checkbox", isChecked);
				editor.commit();
			}
		});

		editText.setText(sp.getString("text", ""));
		editText.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				editor.putString("text", editText.getText().toString());
				editor.commit();

				if (keyCode == KeyEvent.KEYCODE_ENTER
						&& event.getAction() == KeyEvent.ACTION_DOWN) {
					send();
					return true;
				}

				return false;
			}
		});

		loadDeviceId();
	}

	private void loadDeviceId() {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("DeviceId");
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				List<String> ids = new ArrayList<String>();
				for (ParseObject object : objects) {
					if (object.containsKey("deviceId"))
						ids.add(object.getString("deviceId"));
				}

				ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						MainActivity.this,
						android.R.layout.simple_spinner_item, ids);
				spinner.setAdapter(adapter);
			}
		});
	}

	private void send() {
		progress.setTitle("Loading ...");
		progress.show();

		final String text;

		if (checkBox.isChecked()
				|| editText.getText().toString().contains("fuck")) {
			text = "*******";
		} else {
			text = editText.getText().toString();
		}

		editText.setText("");

		JSONObject data = new JSONObject();
		try {
			data.put("action", "com.example.simpleui.push");
			data.put("message", text);
			data.put("alert", text);

			String id = (String) spinner.getSelectedItem();
			ParsePush push = new ParsePush();
			push.setChannel("device_id_" + id);
			// push.setMessage(text);
			push.setData(data);
			push.sendInBackground();

		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		ParseObject messageObject = new ParseObject("Message");
		messageObject.put("text", text);
		messageObject.put("checkbox", checkBox.isChecked());
		messageObject.saveInBackground(new SaveCallback() {

			@Override
			public void done(ParseException e) {
				progress.dismiss();
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, MessageActivity.class);
				intent.putExtra("text", text);
				intent.putExtra("checkbox", checkBox.isChecked());
				startActivity(intent);
			}
		});

		// Toast.makeText(this, text, Toast.LENGTH_SHORT).show();

	}

	private String getDeviceId() {
		return Secure.getString(getContentResolver(), Secure.ANDROID_ID);
	}

	public void onClick(View view) {
		send();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
