package com.example.mytourhelper;

import android.app.Activity;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKGeocoderAddressComponent;
import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionInfo;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;

public class MySearchListener implements MKSearchListener{
	public ArrayAdapter<String> sugAdapter = null;
	public MKGeocoderAddressComponent kk;
	private Context context;
	private MapView mMapView;
	private MKSearch mMKSearch;
	MySearchListener(Context context,MapView mapView,MKSearch mkSearch){
		this.context = context;
		this.mMapView = mapView;
		this.mMKSearch = mkSearch;
	}
	@Override
	public void onGetAddrResult(MKAddrInfo result, int arg1) {
		// TODO Auto-generated method stub
		MKGeocoderAddressComponent kk=result.addressComponents; 
	}

	@Override
	public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {
		// TODO Auto-generated method stub
		  //返回公交车详情信息搜索结果    
	}

	@Override
	public void onGetDrivingRouteResult(MKDrivingRouteResult arg0, int arg1) {
		// TODO Auto-generated method stub
		//返回驾乘路线搜索结果   
	}

	@Override
	public void onGetPoiDetailSearchResult(int type, int error) {
        if (error != 0) {
            Toast.makeText(context, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "成功，查看详情页面", Toast.LENGTH_SHORT).show();
        }
    }

	@Override
	public void onGetPoiResult(MKPoiResult res, int type, int error) {
        // 错误号可参考MKEvent中的定义
        if (error != 0 || res == null) {
            Toast.makeText(context, "抱歉，未找到结果", Toast.LENGTH_LONG).show();
            return;
        }
        // 将地图移动到第一个POI中心点
        if (res.getCurrentNumPois() > 0) {
            // 将poi结果显示到地图上
            MyPoiOverlay poiOverlay = new MyPoiOverlay(((Activity)context), mMapView, mMKSearch);
            poiOverlay.setData(res.getAllPoi());
//            mMapView.getOverlays().clear();
//            mMapView.getOverlays().add(poiOverlay);
            mMapView.refresh();
            //当ePoiType为2（公交线路）或4（地铁线路）时， poi坐标为空
            for( MKPoiInfo info : res.getAllPoi() ){
            	if ( info.pt != null ){
            		mMapView.getController().animateTo(info.pt);
            		break;
            	}
            }
        } else if (res.getCityListNum() > 0) {
        	//当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
            String strInfo = "在";
            for (int i = 0; i < res.getCityListNum(); i++) {
                strInfo += res.getCityListInfo(i).city;
                strInfo += ",";
            }
            strInfo += "找到结果";
            Toast.makeText(context, strInfo, Toast.LENGTH_LONG).show();
        }
    }

	@Override
	public void onGetShareUrlResult(MKShareUrlResult arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		 //在此处理短串请求返回结果. 
	}

	@Override
	public void onGetSuggestionResult(MKSuggestionResult res, int arg1) {
		// TODO Auto-generated method stub
		 //返回联想词信息搜索结果  
		if ( res == null || res.getAllSuggestions() == null){
    		return ;
    	}
    	sugAdapter.clear();
    	for ( MKSuggestionInfo info : res.getAllSuggestions()){
    		if ( info.key != null)
    		    sugAdapter.add(info.key);
    	}
    	sugAdapter.notifyDataSetChanged();
        
    }

	@Override
	public void onGetTransitRouteResult(MKTransitRouteResult arg0, int arg1) {
		// TODO Auto-generated method stub
		//返回公交搜索结果 
	}

	@Override
	public void onGetWalkingRouteResult(MKWalkingRouteResult arg0, int arg1) {
		// TODO Auto-generated method stub
		  //返回步行路线搜索结果  
	}

}
