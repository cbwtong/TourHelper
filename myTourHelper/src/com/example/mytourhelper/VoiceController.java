package com.example.mytourhelper;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;


public class VoiceController implements OnClickListener, OnTouchListener {
	private File soundFile;
	private MediaRecorder mRecorder;
	private MediaPlayer mPlayer;
	private String resURL;
	
	public VoiceController()
	{
		
	}
	public VoiceController(String URL)
	{
		this.resURL = URL;
		soundFile = new File(URL);
	}
	public String getResURL()
	{
		return this.resURL == null? "":this.resURL;
	}
	@Override
	public void onClick(View arg0) {
		if (soundFile != null && soundFile.exists())
		{
			mPlayer = new MediaPlayer();
			mPlayer.setOnCompletionListener(new OnCompletionListener() {
				
				@Override
				public void onCompletion(MediaPlayer mp) {
					// TODO Auto-generated method stub				
					mPlayer.release();
					mPlayer = null;
				}
			});
			String pathString = resURL;
			try {
				mPlayer.setDataSource(pathString);
				mPlayer.prepare();
				mPlayer.start();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}	
		
	}
	@Override
	public boolean onTouch(View yourButton, MotionEvent theMotion) {
		// TODO Auto-generated method stub
		Log.i("onTouchTest","touch");
		if (Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED))
		{
			switch ( theMotion.getAction() ) {
		    case MotionEvent.ACTION_DOWN: 
		    	SimpleDateFormat   formatter   =   new   SimpleDateFormat   ("yyyy_MM_dd_HH_mm_ss");     
		    	Date   curDate   =   new   Date(System.currentTimeMillis());//��ȡ��ǰʱ��     
		    	String   strCurDate   =   formatter.format(curDate); 
				try {
					soundFile = new File(Environment
							.getExternalStorageDirectory().getCanonicalFile()
							+ "/xinwang/"+strCurDate+".amr");
					soundFile.createNewFile();
					resURL = soundFile.getPath();
					Log.i("resURL",resURL);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					try
					{
						// ��������¼������Ƶ�ļ�
						mRecorder = new MediaRecorder();
						// ����¼����������Դ
						mRecorder.setAudioSource(MediaRecorder
							.AudioSource.MIC);
						// ����¼�Ƶ������������ʽ���������������������ʽ֮ǰ���ã�
						mRecorder.setOutputFormat(MediaRecorder
							.OutputFormat.THREE_GPP);
						// ������������ĸ�ʽ
						mRecorder.setAudioEncoder(MediaRecorder
							.AudioEncoder.AMR_NB);
						mRecorder.setOutputFile(soundFile.getAbsolutePath());
						
						mRecorder.prepare();
						// ��ʼ¼��
						mRecorder.start();  //��
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
		    	break;
		    case MotionEvent.ACTION_UP:
		    	if (soundFile != null && soundFile.exists())
				{
					// ֹͣ¼��
					mRecorder.stop();  //��
					// �ͷ���Դ
					mRecorder.release();  //��
					mRecorder = null;
				}
		    	break;
		    }
			return true;
		}
		return false;
	}
}


