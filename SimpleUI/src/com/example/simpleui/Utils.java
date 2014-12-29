package com.example.simpleui;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;

public class Utils {

	public static byte[] bitmapToBytes(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}
}
