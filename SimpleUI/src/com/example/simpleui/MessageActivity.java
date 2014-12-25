package com.example.simpleui;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.simpleui.dialog.DeleteDialog;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MessageActivity extends Activity {

	private static final String FILE_NAME = "history.txt";
	private ListView listView;
	private List<ParseObject> messages;
	private DeleteReceiver deleteReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);

		deleteReceiver = new DeleteReceiver();
		registerReceiver(deleteReceiver, new IntentFilter(
				"com.example.simpleui.delete"));

		listView = (ListView) findViewById(R.id.listView1);
		String text = getIntent().getStringExtra("text");
		boolean isChecked = getIntent().getBooleanExtra("checkbox", false);

		// writeToFile(text);
		// setListViewData();
		// setListViewData2();

		queryDataFromParse();

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view,
					int position, long id) {
				Log.d("debug", "position:" + position);

				AlertDialog dialog = DeleteDialog.create(MessageActivity.this,
						messages.get(position));

				dialog.show();
			}
		});
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(deleteReceiver);
		super.onDestroy();
	}

	private void queryDataFromParse() {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Message");
		query.orderByDescending("createdAt");
		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> messages, ParseException e) {
				MessageActivity.this.messages = messages;

				String[] textList = new String[messages.size()];
				String[] datatimeList = new String[messages.size()];

				for (int i = 0; i < messages.size(); i++) {
					textList[i] = messages.get(i).getString("text");
					datatimeList[i] = messages.get(i).getCreatedAt().toString();

					Log.d("debug", messages.get(i).getCreatedAt().toString());
				}
				setListViewDataWithSimpleAdapter(textList, datatimeList);
			}
		});

	}

	@SuppressWarnings("unused")
	private void setListViewData() {
		String allText = readFile();
		String[] messages = allText.split("\n");

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, messages);
		listView.setAdapter(adapter);
	}

	@SuppressWarnings("unused")
	private void setListViewDataWithSimpleAdapter() {
		String allText = readFile();
		String[] messages = allText.split("\n");
		String[] messageDatetime = new String[messages.length];

		for (int i = 0; i < messageDatetime.length; i++) {
			messageDatetime[i] = new Date().toString();
		}

		setListViewDataWithSimpleAdapter(messages, messageDatetime);
	}

	private void setListViewDataWithSimpleAdapter(String[] messages,
			String[] messageDatetime) {
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		for (int i = 0; i < messages.length; i++) {
			Map<String, String> item = new HashMap<String, String>();
			item.put("message", messages[i]);
			item.put("datetime", messageDatetime[i]);
			data.add(item);
		}

		String[] from = new String[] { "message", "datetime" };
		int[] to = new int[] { R.id.messageTextView, R.id.datatimeTextView };
		SimpleAdapter adapter = new SimpleAdapter(this, data,
				R.layout.listview_item, from, to);

		/*
		 * use android's layout int[] to = new int[] { android.R.id.text1,
		 * android.R.id.text2 }; SimpleAdapter adapter = new SimpleAdapter(this,
		 * data, android.R.layout.simple_list_item_2, from, to);
		 */

		listView.setAdapter(adapter);
	}

	@SuppressWarnings("unused")
	private void writeToFile(String text) {
		try {
			text += "\n";
			FileOutputStream fos = openFileOutput(FILE_NAME,
					Context.MODE_APPEND);
			fos.write(text.getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private String readFile() {

		try {
			FileInputStream fis = openFileInput(FILE_NAME);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));

			String line = null;
			String result = "";
			while ((line = br.readLine()) != null) {
				result += line + "\n";
			}
			return result;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public class DeleteReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			Toast.makeText(context,
					"delete successfully [from DeleteReceiver inner]",
					Toast.LENGTH_SHORT).show();
			queryDataFromParse();
		}
	}

}
