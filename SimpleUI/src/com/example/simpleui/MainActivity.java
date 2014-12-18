package com.example.simpleui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	private EditText editText;
	private Button button, button3;
	private CheckBox checkBox;
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;
	
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
		setContentView(R.layout.activity_main);

		sp = getSharedPreferences("settings", Context.MODE_PRIVATE);
		editor = sp.edit();
		
		editText = (EditText) findViewById(R.id.editText1);
		button = (Button) findViewById(R.id.button1);
		button3 = (Button) findViewById(R.id.button3);
		checkBox = (CheckBox) findViewById(R.id.checkBox1);
		
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
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
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
	}

	private void send() {
		String text = editText.getText().toString();

		if (checkBox.isChecked() || text.contains("fuck")) {
			text = "*******";
		}
		
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
		editText.setText("");
		
		Intent intent = new Intent();
		intent.setClass(this, MessageActivity.class);
		intent.putExtra("text", text);
		intent.putExtra("checkbox", checkBox.isChecked());
		startActivity(intent);
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
