package com.example.mytourhelper;



import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;

public class BMapApplication extends Application{
	private static BMapApplication mInstance = null;
    public boolean m_bKeyRight = true;
    BMapManager mBMapManager = null;
	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		initManager(this);
	}
	//初始化MapManager
	private void initManager(Context context) {
		 if (mBMapManager == null) {
	            mBMapManager = new BMapManager(context);
	        }

	        if (!mBMapManager.init(new MyGeneralListener())) {
	            Toast.makeText(BMapApplication.getInstance().getApplicationContext(), 
	                    "BMapManager  初始化错误!", Toast.LENGTH_LONG).show();
	        }
	}
	private static BMapApplication getInstance(){
		return mInstance;
	}
	//建立一个静态类来继承MKGeneralListener
	//MKGeneralListener 一般事件通知接口。
	//该接口返回网络状态，授权验证等结果，用户需要实现该接口以处理相应事件
	static class MyGeneralListener implements MKGeneralListener{

		@Override
        public void onGetNetworkState(int iError) {
            if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
                Toast.makeText(BMapApplication.getInstance().getApplicationContext(), "您的网络出错啦！",
                    Toast.LENGTH_LONG).show();
            }
            else if (iError == MKEvent.ERROR_NETWORK_DATA) {
                Toast.makeText(BMapApplication.getInstance().getApplicationContext(), "输入正确的检索条件！",
                        Toast.LENGTH_LONG).show();
            }
            // ...
        }

        @Override
        public void onGetPermissionState(int iError) {
        	//非零值表示key验证未通过
            if (iError != 0) {
                //授权Key错误：
                Toast.makeText(BMapApplication.getInstance().getApplicationContext(), 
                        "请在 BMapApplication.java文件输入正确的授权Key,并检查您的网络连接是否正常！error: "+iError, Toast.LENGTH_LONG).show();
                BMapApplication.getInstance().m_bKeyRight = false;
            }
            else{
            	BMapApplication.getInstance().m_bKeyRight = true;
            	Toast.makeText(BMapApplication.getInstance().getApplicationContext(), 
                        "key认证成功", Toast.LENGTH_LONG).show();
            }
        }
		
	}
}
