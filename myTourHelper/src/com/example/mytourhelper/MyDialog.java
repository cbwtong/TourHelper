package com.example.mytourhelper;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;

public class MyDialog {
	public void createMessageDialogHelp(Context context){
		AlertDialog.Builder builder = new Builder(context);
		builder.setTitle("帮助");
		builder.setPositiveButton("确定",new android.content.DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		builder.setMessage("这是一个地图应用，包括了地图的基本功能。你可以用它来进行定位和路线查询。并且做出标记\n并且可以用来录音和分享音频");
		builder.create().show();
	}
	public void createMessageDialoAbout(Context context){
		AlertDialog.Builder builder = new Builder(context);
		builder.setTitle("关于");
		builder.setPositiveButton("确定",new android.content.DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		builder.setMessage("本应用由小助依据百度地图API开发");
		builder.create().show();
	}
}
