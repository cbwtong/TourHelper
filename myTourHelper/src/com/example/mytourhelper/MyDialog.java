package com.example.mytourhelper;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;

public class MyDialog {
	public void createMessageDialogHelp(Context context){
		AlertDialog.Builder builder = new Builder(context);
		builder.setTitle("����");
		builder.setPositiveButton("ȷ��",new android.content.DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		builder.setMessage("����һ����ͼӦ�ã������˵�ͼ�Ļ������ܡ���������������ж�λ��·�߲�ѯ�������������\n���ҿ�������¼���ͷ�����Ƶ");
		builder.create().show();
	}
	public void createMessageDialoAbout(Context context){
		AlertDialog.Builder builder = new Builder(context);
		builder.setTitle("����");
		builder.setPositiveButton("ȷ��",new android.content.DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		builder.setMessage("��Ӧ����С�����ݰٶȵ�ͼAPI����");
		builder.create().show();
	}
}
