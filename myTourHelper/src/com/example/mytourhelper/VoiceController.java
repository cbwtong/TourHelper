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
		    	Date   curDate   =   new   Date(System.currentTimeMillis());//获取当前时间     
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
						// 创建保存录音的音频文件
						mRecorder = new MediaRecorder();
						// 设置录音的声音来源
						mRecorder.setAudioSource(MediaRecorder
							.AudioSource.MIC);
						// 设置录制的声音的输出格式（必须在设置声音编码格式之前设置）
						mRecorder.setOutputFormat(MediaRecorder
							.OutputFormat.THREE_GPP);
						// 设置声音编码的格式
						mRecorder.setAudioEncoder(MediaRecorder
							.AudioEncoder.AMR_NB);
						mRecorder.setOutputFile(soundFile.getAbsolutePath());
						
						mRecorder.prepare();
						// 开始录音
						mRecorder.start();  //①
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
		    	break;
		    case MotionEvent.ACTION_UP:
		    	if (soundFile != null && soundFile.exists())
				{
					// 停止录音
					mRecorder.stop();  //②
					// 释放资源
					mRecorder.release();  //③
					mRecorder = null;
				}
		    	break;
		    }
			return true;
		}
		return false;
	}
}


