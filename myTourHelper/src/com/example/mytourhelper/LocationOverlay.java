package com.example.mytourhelper;

import android.view.View;
import android.widget.TextView;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.PopupOverlay;

public class LocationOverlay {
		// 定位相关
		LocationClient mLocClient;
		LocationData locData = null;
//		public MyLocationListenner myListener = new MyLocationListenner();
		//定位图层
//		locationOverlay myLocationOverlay = null;
		//弹出泡泡图层
		private PopupOverlay   pop  = null;//弹出泡泡图层，浏览节点时使用
		private TextView  popupText = null;//泡泡view
		private View viewCache = null;
		
		boolean isRequest = false;//是否手动触发请求定位
		boolean isFirstLoc = true;//是否首次定位
}
