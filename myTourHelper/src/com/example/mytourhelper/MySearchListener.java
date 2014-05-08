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
		  //���ع�����������Ϣ�������    
	}

	@Override
	public void onGetDrivingRouteResult(MKDrivingRouteResult arg0, int arg1) {
		// TODO Auto-generated method stub
		//���ؼݳ�·���������   
	}

	@Override
	public void onGetPoiDetailSearchResult(int type, int error) {
        if (error != 0) {
            Toast.makeText(context, "��Ǹ��δ�ҵ����", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "�ɹ����鿴����ҳ��", Toast.LENGTH_SHORT).show();
        }
    }

	@Override
	public void onGetPoiResult(MKPoiResult res, int type, int error) {
        // ����ſɲο�MKEvent�еĶ���
        if (error != 0 || res == null) {
            Toast.makeText(context, "��Ǹ��δ�ҵ����", Toast.LENGTH_LONG).show();
            return;
        }
        // ����ͼ�ƶ�����һ��POI���ĵ�
        if (res.getCurrentNumPois() > 0) {
            // ��poi�����ʾ����ͼ��
            MyPoiOverlay poiOverlay = new MyPoiOverlay(((Activity)context), mMapView, mMKSearch);
            poiOverlay.setData(res.getAllPoi());
//            mMapView.getOverlays().clear();
//            mMapView.getOverlays().add(poiOverlay);
            mMapView.refresh();
            //��ePoiTypeΪ2��������·����4��������·��ʱ�� poi����Ϊ��
            for( MKPoiInfo info : res.getAllPoi() ){
            	if ( info.pt != null ){
            		mMapView.getController().animateTo(info.pt);
            		break;
            	}
            }
        } else if (res.getCityListNum() > 0) {
        	//������ؼ����ڱ���û���ҵ����������������ҵ�ʱ�����ذ����ùؼ�����Ϣ�ĳ����б�
            String strInfo = "��";
            for (int i = 0; i < res.getCityListNum(); i++) {
                strInfo += res.getCityListInfo(i).city;
                strInfo += ",";
            }
            strInfo += "�ҵ����";
            Toast.makeText(context, strInfo, Toast.LENGTH_LONG).show();
        }
    }

	@Override
	public void onGetShareUrlResult(MKShareUrlResult arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		 //�ڴ˴���̴����󷵻ؽ��. 
	}

	@Override
	public void onGetSuggestionResult(MKSuggestionResult res, int arg1) {
		// TODO Auto-generated method stub
		 //�����������Ϣ�������  
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
		//���ع���������� 
	}

	@Override
	public void onGetWalkingRouteResult(MKWalkingRouteResult arg0, int arg1) {
		// TODO Auto-generated method stub
		  //���ز���·���������  
	}

}
