package com.example.mytourhelper;

import android.view.View;
import android.widget.TextView;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.PopupOverlay;

public class LocationOverlay {
		// ��λ���
		LocationClient mLocClient;
		LocationData locData = null;
//		public MyLocationListenner myListener = new MyLocationListenner();
		//��λͼ��
//		locationOverlay myLocationOverlay = null;
		//��������ͼ��
		private PopupOverlay   pop  = null;//��������ͼ�㣬����ڵ�ʱʹ��
		private TextView  popupText = null;//����view
		private View viewCache = null;
		
		boolean isRequest = false;//�Ƿ��ֶ���������λ
		boolean isFirstLoc = true;//�Ƿ��״ζ�λ
}
