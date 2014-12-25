package com.example.simpleui.dialog;

import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;

public class DeleteDialog {

	
	public static AlertDialog create(final Context context, final ParseObject parseObject, final DeleteCallback cb) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		
		builder.setTitle("Delete Message");
		builder.setMessage("Do you really want to delete message ?");
		builder.setPositiveButton("delete", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				parseObject.deleteInBackground(cb);
				
				context.sendBroadcast(new Intent("com.example.simpleui.delete"));
			}
		});
		
		builder.setNegativeButton("cancel", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});
		
		return builder.create();
	}
}
