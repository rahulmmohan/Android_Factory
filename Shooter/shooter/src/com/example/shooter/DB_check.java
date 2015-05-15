package com.example.shooter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.util.Log;

public class DB_check {
	public static String DB_NAME = "game.db";
	public static String DB_PATH;
	Context context;
	public DB_check(String packagename, Context context) {
		DB_PATH = "/data/data/" + packagename + "/databases/";
		this.context=context;
	// TODO Auto-generated constructor stub
	}

	
	
	// ############### IS DB EXIST ############### //
	public boolean isdatabaseexist() {
		// TODO Auto-generated method stub
		try {
			File db_file = new File(DB_PATH + DB_NAME);
			return db_file.exists();
			
		} catch (Exception e) {
			Log.e("Sbi_po:isdatabaseexist()", e.toString());
		}
		return false;
	}
	// ########################################### //
	
	// #################### COPY DATABASE ################### //
	public void copydatabase() {
		try {
			InputStream myInput = context.getAssets().open(
					DB_NAME);
			OutputStream myOutput = new FileOutputStream(DB_PATH + DB_NAME);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = myInput.read(buffer)) > 0) {
				myOutput.write(buffer, 0, length);
			}
			myOutput.flush();
			myOutput.close();
			myInput.close();

		} catch (Exception e) {
			Log.e("Sbi_po:copydatabase()", e.toString());
		}
	}
	//######################################################## //
	
	// ############ CREATE FOLDER ########### //
	public void createfolder() {
		// TODO Auto-generated method stub
		try {
			File file = new File(DB_PATH);
			if (!file.exists()) {
				file.mkdirs();
			}
		} catch (Exception e) {
			Log.e("Sbi_po:createfolder()", e.toString());
		}
	}
	// ##################################### //

}
