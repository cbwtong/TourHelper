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
	//��ʼ��MapManager
	private void initManager(Context context) {
		 if (mBMapManager == null) {
	            mBMapManager = new BMapManager(context);
	        }

	        if (!mBMapManager.init(new MyGeneralListener())) {
	            Toast.makeText(BMapApplication.getInstance().getApplicationContext(), 
	                    "BMapManager  ��ʼ������!", Toast.LENGTH_LONG).show();
	        }
	}
	private static BMapApplication getInstance(){
		return mInstance;
	}
	//����һ����̬�����̳�MKGeneralListener
	//MKGeneralListener һ���¼�֪ͨ�ӿڡ�
	//�ýӿڷ�������״̬����Ȩ��֤�Ƚ�����û���Ҫʵ�ָýӿ��Դ�����Ӧ�¼�
	static class MyGeneralListener implements MKGeneralListener{

		@Override
        public void onGetNetworkState(int iError) {
            if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
                Toast.makeText(BMapApplication.getInstance().getApplicationContext(), "���������������",
                    Toast.LENGTH_LONG).show();
            }
            else if (iError == MKEvent.ERROR_NETWORK_DATA) {
                Toast.makeText(BMapApplication.getInstance().getApplicationContext(), "������ȷ�ļ���������",
                        Toast.LENGTH_LONG).show();
            }
            // ...
        }

        @Override
        public void onGetPermissionState(int iError) {
        	//����ֵ��ʾkey��֤δͨ��
            if (iError != 0) {
                //��ȨKey����
                Toast.makeText(BMapApplication.getInstance().getApplicationContext(), 
                        "���� BMapApplication.java�ļ�������ȷ����ȨKey,������������������Ƿ�������error: "+iError, Toast.LENGTH_LONG).show();
                BMapApplication.getInstance().m_bKeyRight = false;
            }
            else{
            	BMapApplication.getInstance().m_bKeyRight = true;
            	Toast.makeText(BMapApplication.getInstance().getApplicationContext(), 
                        "key��֤�ɹ�", Toast.LENGTH_LONG).show();
            }
        }
		
	}
}
