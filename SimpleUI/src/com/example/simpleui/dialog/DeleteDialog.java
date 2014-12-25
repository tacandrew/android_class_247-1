package com.example.simpleui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class DeleteDialog {

	
	public static AlertDialog create(Context context) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		
		builder.setTitle("Delete Message");
		builder.setMessage("Do you really want to delete message ?");
		builder.setPositiveButton("delete", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
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
