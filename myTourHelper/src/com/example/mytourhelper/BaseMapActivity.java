package com.example.mytourhelper;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.map.MKMapTouchListener;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.MyLocationOverlay.LocationMode;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.mapapi.map.RouteOverlay;
import com.baidu.mapapi.map.TransitOverlay;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPlanNode;
import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKRoute;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionInfo;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;

public class BaseMapActivity extends Activity {
	final static String TAG = "MainActivity";
	/**
	 *  MapView �ǵ�ͼ���ؼ�
	 */
	private MapView mMapView = null;
	/**
	 *  ��MapController��ɵ�ͼ���� 
	 */
	private MapController mMapController = null;
	

	/**
	 *  MKMapViewListener ���ڴ����ͼ�¼��ص�
	 */
	MKMapViewListener mMapListener = null;
	
//	DBHelper helper = new DBHelper(getApplicationContext());
	private int markType;
	private GeoPoint lastClick;
	private GeoPoint myPoint;
	private ToggleButton locationBtn;
	private ListView mListForMarker;
	private Button beginSearch,beginDirection;
	private Button searchBtn;
	private Button placeBtn;
	DialogAddPlace dialogAddPlace ;
	//�������
	private MKSearch mMKSearch = null;
//	private MySearchListener mySearchListener = new MySearchListener(this,mMapView,mMKSearch);
	private AutoCompleteTextView keyWorldsView ;
	private ArrayAdapter<String> sugAdapter = null;
	private EditText place;
	private String city = "֣��";
	private String address;
	// ��λ���
		LocationClient mLocClient;
		LocationData locData = null;
		public MyLocationListenner myListener = new MyLocationListenner();
	//��λͼ��
		public locationOverlay myLocationOverlay = null;
//		//��������ͼ��
//		private PopupOverlay   pop  = null;//��������ͼ�㣬����ڵ�ʱʹ��
//		private TextView  popupText = null;//����view
//		private View viewCache = null;
//		boolean isRequest = false;//�Ƿ��ֶ���������λ
		boolean isFirstLoc = true;//�Ƿ��״ζ�λ
	//·�����
		MKRoute route = null;//����ݳ�/����·�����ݵı�����������ڵ�ʱʹ��
		TransitOverlay transitOverlay = null;//���湫��·��ͼ�����ݵı�����������ڵ�ʱʹ��
		RouteOverlay routeOverlay = null; 
		private Button btnNext,btnPre;
		private AutoCompleteTextView startView,endView;
		private Button routeSearchBtn;
		Button mBtnPre,mBtnNext;
		private int type = 0;
		private LinearLayout routeLayout;
		private Button driveBtn,busBtn,walkBtn;
		private View viewCache = null;
		int nodeIndex = -2;//�ڵ�����,������ڵ�ʱʹ��
		int searchType = -1;//��¼���������ͣ����ּݳ�/���к͹���
		private PopupOverlay   pop  = null;//��������ͼ�㣬����ڵ�ʱʹ��
		private TextView  popupText = null;//����view
		
	private 	LinearLayout menuLayout ;
	private		LinearLayout directionLayout ;
	private		LinearLayout searchLayout ;
	
	private MyMarkerOverlay markerOverlay ;
	private List<OverlayItem> markerList = new ArrayList<OverlayItem>();
	private List<MapLocation> mapLocationList = new ArrayList<MapLocation>();
	public void openNewActivity(int type)
    {
    	switch(type)
    	{
    	case CONSTANT.OPEN_CAMERA:
          Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

          startActivityForResult(intent, CONSTANT.OPEN_CAMERA);
    		break;
    	case CONSTANT.OPEN_LOCAL_PICTURE:
		    Intent intent2 = new Intent();
		    intent2.setType("image/*");
		    intent2.putExtra("return-data", true);
		    intent2.setAction(Intent.ACTION_GET_CONTENT);
		    startActivityForResult(intent2, CONSTANT.OPEN_LOCAL_PICTURE);
    		break;
    		
    	}
    }
	
	private void showDailog(GeoPoint point)
	{
		GeoPoint geoPoint = point;
		dialogAddPlace = new DialogAddPlace(this, true, markType);
		mMKSearch.reverseGeocode(point);
		dialogAddPlace.setGeoPoint(geoPoint);
        dialogAddPlace.setAddress(address);
        dialogAddPlace.setMarkerType(markType);
        dialogAddPlace.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                DialogAddPlace d = (DialogAddPlace) dialog;
                if(dialogAddPlace.isCancel() == false){
                	Log.i("Test",dialogAddPlace.isCancel()+"");
                	add_new_marker(markType);
                }
            }
        });
        dialogAddPlace.show();
	}
	
	private void add_new_marker(int type)
	{
		String titleString = dialogAddPlace.getTitle();
		String disString = dialogAddPlace.getDiscription();
		if(markerOverlay == null){
			markerOverlay = new MyMarkerOverlay(getResources().getDrawable(R.drawable.ic_launcher), mMapView, this);
		}
		boolean hasOverlay = mMapView.getOverlays().contains(markerOverlay);
		OverlayItem currentItem;
		switch (type) {
		case CONSTANT.VOICE_MARKER:
//			 newMarker =  mMap.addMarker(new MarkerOptions()
//             .position(lastClick)
//             .title(titleString)
//             .draggable(true)
//             .icon(BitmapDescriptorFactory.fromResource(R.drawable.voice6)));
//			 vMarker.add(newMarker);
			currentItem = new OverlayItem(lastClick,titleString,disString);
			currentItem.setMarker(this.getResources().getDrawable(R.drawable.voice6));
			markerOverlay.addItem(currentItem);
			if(hasOverlay == false){
				mMapView.getOverlays().add(markerOverlay);
			}
			break;
		case CONSTANT.TEXT_MARKER:
//			 newMarker = mMap.addMarker(new MarkerOptions()
//             .position(lastClick)
//             .title(titleString)
//             .draggable(true)
//             .icon(BitmapDescriptorFactory.fromResource(R.drawable.textt)));
//			 vMarker.add(newMarker);
			currentItem = new OverlayItem(lastClick,titleString,disString);
			currentItem.setMarker(this.getResources().getDrawable(R.drawable.textt));
			markerOverlay.addItem(currentItem);
			markerList.add(currentItem);
			if(hasOverlay == false){
				mMapView.getOverlays().add(markerOverlay);
			}
			mMapView.refresh();
			break;	
		case CONSTANT.PIC_MARKER:
//			 newMarker = mMap.addMarker(new MarkerOptions()
//             .position(lastClick)
//             .title(titleString)
//             .draggable(true)
//             .icon(BitmapDescriptorFactory.fromResource(R.drawable.picture)));
//			 vMarker.add(newMarker);
			currentItem = new OverlayItem(lastClick,titleString,disString);
			currentItem.setMarker(this.getResources().getDrawable(R.drawable.picture));
			markerOverlay.addItem(currentItem);
			markerList.add(currentItem);
			if(hasOverlay == false){
				mMapView.getOverlays().add(markerOverlay);
			}
			break;
		default:
			break;
		}
		mMapView.refresh();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 /**
         * ʹ�õ�ͼsdkǰ���ȳ�ʼ��BMapManager.
         * BMapManager��ȫ�ֵģ���Ϊ���MapView���ã�����Ҫ��ͼģ�鴴��ǰ������
         * ���ڵ�ͼ��ͼģ�����ٺ����٣�ֻҪ���е�ͼģ����ʹ�ã�BMapManager�Ͳ�Ӧ������
         */
		BMapApplication app = (BMapApplication)this.getApplication();//����APP
        if (app.mBMapManager == null) {
            app.mBMapManager = new BMapManager(getApplicationContext());
            /**
             * ���BMapManagerû�г�ʼ�����ʼ��BMapManager
             */
            app.mBMapManager.init(new BMapApplication.MyGeneralListener());
        }
        
        /**
         * ����MapView��setContentView()�г�ʼ��,��������Ҫ��BMapManager��ʼ��֮��
         */
       setContentView(R.layout.base_map);
    // ��ʼ������ģ�飬ע�������¼�����
       mMKSearch = new MKSearch();  
//       mMKSearch.init(app.mBMapManager, mySearchListener);//ע�⣬MKSearchListenerֻ֧��һ���������һ������Ϊ׼  
       mMKSearch.init(app.mBMapManager, new MKSearchListener(){

    	 //�ڴ˴�������ҳ���
           @Override
           public void onGetPoiDetailSearchResult(int type, int error) {
               if (error != 0) {
                   Toast.makeText(BaseMapActivity.this, "��Ǹ��δ�ҵ����", Toast.LENGTH_SHORT).show();
               }
               else {
                   Toast.makeText(BaseMapActivity.this, "�ɹ����鿴����ҳ��", Toast.LENGTH_SHORT).show();
               }
           }
           /**
            * �ڴ˴���poi�������
            */
           public void onGetPoiResult(MKPoiResult res, int type, int error) {
               // ����ſɲο�MKEvent�еĶ���
               if (error != 0 || res == null) {
                   Toast.makeText(BaseMapActivity.this, "��Ǹ��δ�ҵ����", Toast.LENGTH_LONG).show();
                   return;
               }
               // ����ͼ�ƶ�����һ��POI���ĵ�
               if (res.getCurrentNumPois() > 0) {
                   // ��poi�����ʾ����ͼ��
                   MyPoiOverlay poiOverlay = new MyPoiOverlay(BaseMapActivity.this, mMapView, mMKSearch);
                   poiOverlay.setData(res.getAllPoi());
                   mMapView.getOverlays().clear();
                   mMapView.getOverlays().add(poiOverlay);
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
                   Toast.makeText(BaseMapActivity.this, strInfo, Toast.LENGTH_LONG).show();
               }
           }
           public void onGetDrivingRouteResult(MKDrivingRouteResult res,
					int error) {
				//�����յ������壬��Ҫѡ�����ĳ����б���ַ�б�
				if (error == MKEvent.ERROR_ROUTE_ADDR){
					//�������е�ַ
//					ArrayList<MKPoiInfo> stPois = res.getAddrResult().mStartPoiList;
//					ArrayList<MKPoiInfo> enPois = res.getAddrResult().mEndPoiList;
//					ArrayList<MKCityListInfo> stCities = res.getAddrResult().mStartCityList;
//					ArrayList<MKCityListInfo> enCities = res.getAddrResult().mEndCityList;
					return;
				}
				// ����ſɲο�MKEvent�еĶ���
				if (error != 0 || res == null) {
					Toast.makeText(BaseMapActivity.this, "��Ǹ��δ�ҵ����", Toast.LENGTH_SHORT).show();
					return;
				}
			
				searchType = 0;
			    routeOverlay = new RouteOverlay(BaseMapActivity.this, mMapView);
			    // �˴���չʾһ��������Ϊʾ��
			    routeOverlay.setData(res.getPlan(0).getRoute(0));
			    //�������ͼ��
			    mMapView.getOverlays().clear();
			    //���·��ͼ��
			    mMapView.getOverlays().add(routeOverlay);
			    //ִ��ˢ��ʹ��Ч
			    mMapView.refresh();
			    // ʹ��zoomToSpan()���ŵ�ͼ��ʹ·������ȫ��ʾ�ڵ�ͼ��
			    mMapView.getController().zoomToSpan(routeOverlay.getLatSpanE6(), routeOverlay.getLonSpanE6());
			    //�ƶ���ͼ�����
			    mMapView.getController().animateTo(res.getStart().pt);
			    //��·�����ݱ����ȫ�ֱ���
			    route = res.getPlan(0).getRoute(0);
			    //����·�߽ڵ��������ڵ����ʱʹ��
			    nodeIndex = -1;
			    mBtnPre.setVisibility(View.VISIBLE);
				mBtnNext.setVisibility(View.VISIBLE);
			}
           public void onGetTransitRouteResult(MKTransitRouteResult res,
					int error) {
				//�����յ������壬��Ҫѡ�����ĳ����б���ַ�б�
				if (error == MKEvent.ERROR_ROUTE_ADDR){
					//�������е�ַ
//					ArrayList<MKPoiInfo> stPois = res.getAddrResult().mStartPoiList;
//					ArrayList<MKPoiInfo> enPois = res.getAddrResult().mEndPoiList;
//					ArrayList<MKCityListInfo> stCities = res.getAddrResult().mStartCityList;
//					ArrayList<MKCityListInfo> enCities = res.getAddrResult().mEndCityList;
					return;
				}
				if (error != 0 || res == null) {
					Toast.makeText(BaseMapActivity.this, "��Ǹ��δ�ҵ����", Toast.LENGTH_SHORT).show();
					return;
				}
				
				searchType = 1;
				transitOverlay = new TransitOverlay (BaseMapActivity.this, mMapView);
			    // �˴���չʾһ��������Ϊʾ��
			    transitOverlay.setData(res.getPlan(0));
			  //�������ͼ��
			    mMapView.getOverlays().clear();
			  //���·��ͼ��
			    mMapView.getOverlays().add(transitOverlay);
			  //ִ��ˢ��ʹ��Ч
			    mMapView.refresh();
			    // ʹ��zoomToSpan()���ŵ�ͼ��ʹ·������ȫ��ʾ�ڵ�ͼ��
			    mMapView.getController().zoomToSpan(transitOverlay.getLatSpanE6(), transitOverlay.getLonSpanE6());
			  //�ƶ���ͼ�����
			    mMapView.getController().animateTo(res.getStart().pt);
			  //����·�߽ڵ��������ڵ����ʱʹ��
			    nodeIndex = 0;
			    mBtnPre.setVisibility(View.VISIBLE);
				mBtnNext.setVisibility(View.VISIBLE);
			}
           public void onGetWalkingRouteResult(MKWalkingRouteResult res,
					int error) {
				//�����յ������壬��Ҫѡ�����ĳ����б���ַ�б�
				if (error == MKEvent.ERROR_ROUTE_ADDR){
					//�������е�ַ
//					ArrayList<MKPoiInfo> stPois = res.getAddrResult().mStartPoiList;
//					ArrayList<MKPoiInfo> enPois = res.getAddrResult().mEndPoiList;
//					ArrayList<MKCityListInfo> stCities = res.getAddrResult().mStartCityList;
//					ArrayList<MKCityListInfo> enCities = res.getAddrResult().mEndCityList;
					return;
				}
				if (error != 0 || res == null) {
					Toast.makeText(BaseMapActivity.this, "��Ǹ��δ�ҵ����", Toast.LENGTH_SHORT).show();
					return;
				}

				searchType = 2;
				routeOverlay = new RouteOverlay(BaseMapActivity.this, mMapView);
			    // �˴���չʾһ��������Ϊʾ��
				routeOverlay.setData(res.getPlan(0).getRoute(0));
				//�������ͼ��
			    mMapView.getOverlays().clear();
			  //���·��ͼ��
			    mMapView.getOverlays().add(routeOverlay);
			  //ִ��ˢ��ʹ��Ч
			    mMapView.refresh();
			    // ʹ��zoomToSpan()���ŵ�ͼ��ʹ·������ȫ��ʾ�ڵ�ͼ��
			    mMapView.getController().zoomToSpan(routeOverlay.getLatSpanE6(), routeOverlay.getLonSpanE6());
			  //�ƶ���ͼ�����
			    mMapView.getController().animateTo(res.getStart().pt);
			    //��·�����ݱ����ȫ�ֱ���
			    route = res.getPlan(0).getRoute(0);
			    //����·�߽ڵ��������ڵ����ʱʹ��
			    nodeIndex = -1;
			    mBtnPre.setVisibility(View.VISIBLE);
				mBtnNext.setVisibility(View.VISIBLE);
			    
			}
           public void onGetAddrResult(MKAddrInfo res, int error) {
        	   address = res.strAddr;
           }
           public void onGetBusDetailResult(MKBusLineResult result, int iError) {
           }
           /**
            * ���½����б�
            */
           @Override
           public void onGetSuggestionResult(MKSuggestionResult res, int arg1) {
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
			public void onGetShareUrlResult(MKShareUrlResult result, int type,
					int error) {
				// TODO Auto-generated method stub
				
			}
    	   
       });
       //��ʼ��UI
       initUI();
       sugAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line);
       keyWorldsView.setAdapter(sugAdapter);
       //��UI���ü�����
       setListener();
	}
	
	//��UI���ü�����
	private void setListener() {
		locationBtn.setOnCheckedChangeListener(new OnCheckedChangeListener (){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if(isChecked){
					//��λ��ʼ��
			        mLocClient = new LocationClient( BaseMapActivity.this );
			        locData = new LocationData();
			        mLocClient.registerLocationListener( myListener );
			        LocationClientOption option = new LocationClientOption();
			        option.setOpenGps(true);//��gps
			        option.setCoorType("bd09ll");     //������������
			        option.setScanSpan(1000);//ˢ�¼��
			        mLocClient.setLocOption(option);
			        mLocClient.start();
			        //��λͼ���ʼ��
					myLocationOverlay = new locationOverlay(mMapView);
					//���ö�λ����
				    myLocationOverlay.setData(locData);
				    //��Ӷ�λͼ��
					mMapView.getOverlays().add(myLocationOverlay);
					myLocationOverlay.enableCompass();
					//�޸Ķ�λ���ݺ�ˢ��ͼ����Ч
					mMapView.refresh();
				}
			} 
	   });
       
       mListForMarker.setOnItemClickListener(new OnItemClickListener() {
       	
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {
				// TODO Auto-generated method stub
				//add_new_marker(position);
				markType = position;
				showDailog(lastClick);
				
				Log.v("test",Integer.toString(position));
				mListForMarker.setVisibility(View.GONE);
			}
		});
       
       beginDirection.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				directionLayout.setVisibility(View.VISIBLE);
				searchLayout.setVisibility(View.GONE);
				mBtnNext.setVisibility(View.GONE);
				mBtnPre.setVisibility(View.GONE);
				routeLayout.setVisibility(View.GONE);
			}
		});
       
		beginSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				searchLayout.setVisibility(View.VISIBLE);
				directionLayout.setVisibility(View.GONE);
				mBtnNext.setVisibility(View.GONE);
				mBtnPre.setVisibility(View.GONE);
				routeLayout.setVisibility(View.GONE);
			}
		});
		
		placeBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				mMKSearch.poiSearchNearBy(place.getText().toString().trim(), myPoint, 5000);  
			}
			
		});
		
		searchBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
			}
			
		});
		
		mMapView.regMapTouchListner(new MKMapTouchListener(){

			@Override
			public void onMapClick(GeoPoint point) {
			  //�ڴ˴����ͼ����¼� 
			  //����pop
			  if ( pop != null ){
				  pop.hidePop();
			  }
			}

			@Override
			public void onMapDoubleClick(GeoPoint arg0) {
				
			}

			@Override
			public void onMapLongClick(GeoPoint point) {
				lastClick = point;
				Log.v("test","long click");
//				 Marker marker = mMap.addMarker(new MarkerOptions()
//			     .position(point)
//		         .title("Melbourne")
//		         .snippet("Population: 4,137,400")
//		         .draggable(true));
				mListForMarker.setVisibility(View.VISIBLE);
			}
			
		});
		
		keyWorldsView.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable arg0) {
				
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				
			}
			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				 if ( cs.length() <=0 ){
					 return ;
				 }
				 /**
				  * ʹ�ý������������ȡ�����б������onSuggestionResult()�и���
				  */
//				 city = mySearchListener.kk.city;
                 mMKSearch.suggestionSearch(cs.toString(), city);				
			}
        });
		startView.addTextChangedListener(new TextWatcher(){
			@Override
			public void afterTextChanged(Editable arg0) {
				
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				
			}
			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				 if ( cs.length() <=0 ){
					 return ;
				 }
				 /**
				  * ʹ�ý������������ȡ�����б������onSuggestionResult()�и���
				  */
//				 city = mySearchListener.kk.city;
                 mMKSearch.suggestionSearch(cs.toString(), city);				
			}
		});
		endView.addTextChangedListener(new TextWatcher(){
			@Override
			public void afterTextChanged(Editable arg0) {
				
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				
			}
			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				 if ( cs.length() <=0 ){
					 return ;
				 }
				 /**
				  * ʹ�ý������������ȡ�����б������onSuggestionResult()�и���
				  */
//				 city = mySearchListener.kk.city;
                 mMKSearch.suggestionSearch(cs.toString(), city);				
			}
		});
		OnClickListener clickListener = new OnClickListener(){
			public void onClick(View v) {
				//��������
				SearchButtonProcess(v);
			}
        };
        routeSearchBtn.setOnClickListener(clickListener);
        driveBtn.setOnClickListener(clickListener);
        busBtn.setOnClickListener(clickListener);
        walkBtn.setOnClickListener(clickListener);
        
        OnClickListener nodeClickListener = new OnClickListener(){
			public void onClick(View v) {
				//���·�߽ڵ�
				nodeClick(v);
			}
        };
        mBtnNext.setOnClickListener(nodeClickListener);
        mBtnPre.setOnClickListener(nodeClickListener);
        createPaopao();
	}
	
	void SearchButtonProcess(View v) {
		//��������ڵ��·������
		route = null;
		routeOverlay = null;
		transitOverlay = null; 
		routeLayout.setVisibility(View.VISIBLE);
		mBtnPre.setVisibility(View.VISIBLE);
		mBtnNext.setVisibility(View.VISIBLE);
		// ����������ť��Ӧ
		EditText editSt = (EditText)findViewById(R.id.input_start);
		EditText editEn = (EditText)findViewById(R.id.input_end);
		
		// ������յ��name���и�ֵ��Ҳ����ֱ�Ӷ����긳ֵ����ֵ�����򽫸��������������
		MKPlanNode stNode = new MKPlanNode();
		stNode.name = editSt.getText().toString();
		MKPlanNode enNode = new MKPlanNode();
		enNode.name = editEn.getText().toString();

		// ʵ��ʹ�����������յ���н�����ȷ���趨
		if (driveBtn.equals(v)) {
			mMKSearch.drivingSearch(city, stNode, city, enNode);
		} else if (busBtn.equals(v)) {
			mMKSearch.transitSearch(city, stNode, enNode);
		} else if (walkBtn.equals(v)) {
			mMKSearch.walkingSearch(city, stNode, city, enNode);
		} else{
			mMKSearch.transitSearch(city, stNode, enNode);
		}
	}
	
	
	public void nodeClick(View v){
		viewCache = getLayoutInflater().inflate(R.layout.custom_text_view, null);
        popupText =(TextView) viewCache.findViewById(R.id.textcache);
        Log.i("Test",""+route);
		if (searchType == 0 || searchType == 2){
			//�ݳ�������ʹ�õ����ݽṹ��ͬ���������Ϊ�ݳ����У��ڵ����������ͬ
			if (nodeIndex < -1 || route == null || nodeIndex >= route.getNumSteps())
				return;
			
			//��һ���ڵ�
			if (mBtnPre.equals(v) && nodeIndex > 0){
				//������
				nodeIndex--;
				//�ƶ���ָ������������
				mMapView.getController().animateTo(route.getStep(nodeIndex).getPoint());
				//��������
				popupText.setBackgroundResource(R.drawable.popup);
				popupText.setText(route.getStep(nodeIndex).getContent());
				pop.showPopup(BMapUtil.getBitmapFromView(popupText),
						route.getStep(nodeIndex).getPoint(),
						5);
			}
			//��һ���ڵ�
			if (mBtnNext.equals(v) && nodeIndex < (route.getNumSteps()-1)){
				//������
				nodeIndex++;
				//�ƶ���ָ������������
				mMapView.getController().animateTo(route.getStep(nodeIndex).getPoint());
				//��������
				popupText.setBackgroundResource(R.drawable.popup);
				popupText.setText(route.getStep(nodeIndex).getContent());
				pop.showPopup(BMapUtil.getBitmapFromView(popupText),
						route.getStep(nodeIndex).getPoint(),
						5);
			}
		}
		if (searchType == 1){
			//��������ʹ�õ����ݽṹ��������ͬ����˵�������ڵ����
			if (nodeIndex < -1 || transitOverlay == null || nodeIndex >= transitOverlay.getAllItem().size())
				return;
			
			//��һ���ڵ�
			if (mBtnPre.equals(v) && nodeIndex > 1){
				//������
				nodeIndex--;
				//�ƶ���ָ������������
				mMapView.getController().animateTo(transitOverlay.getItem(nodeIndex).getPoint());
				//��������
				popupText.setBackgroundResource(R.drawable.popup);
				popupText.setText(transitOverlay.getItem(nodeIndex).getTitle());
				pop.showPopup(BMapUtil.getBitmapFromView(popupText),
						transitOverlay.getItem(nodeIndex).getPoint(),
						5);
			}
			//��һ���ڵ�
			if (mBtnNext.equals(v) && nodeIndex < (transitOverlay.getAllItem().size()-2)){
				//������
				nodeIndex++;
				//�ƶ���ָ������������
				mMapView.getController().animateTo(transitOverlay.getItem(nodeIndex).getPoint());
				//��������
				popupText.setBackgroundResource(R.drawable.popup);
				popupText.setText(transitOverlay.getItem(nodeIndex).getTitle());
				pop.showPopup(BMapUtil.getBitmapFromView(popupText),
						transitOverlay.getItem(nodeIndex).getPoint(),
						5);
			}
		}
		
	}
	
	/**
	 * ������������ͼ��
	 */
	public void createPaopao(){
		
        //���ݵ����Ӧ�ص�
        PopupClickListener popListener = new PopupClickListener(){
			@Override
			public void onClickedPopup(int index) {
				Log.v("click", "clickapoapo");
			}
        };
        pop = new PopupOverlay(mMapView,popListener);
	}
	private void initUI() {
		mMapView = (MapView)findViewById(R.id.bmapsView);
		mListForMarker = (ListView)findViewById(R.id.markOption);
		locationBtn = (ToggleButton)findViewById(R.id.locationbtn);
		
		menuLayout = (LinearLayout)findViewById(R.id.menu_control);
		directionLayout = (LinearLayout)findViewById(R.id.direction);
		searchLayout = (LinearLayout)findViewById(R.id.search);
		
		 beginSearch = (Button)findViewById(R.id.mysearch_btn);
		 beginDirection = (Button)findViewById(R.id.mydirection_btn);
		 
		 //λ��
		 searchBtn = (Button)findViewById(R.id.start_direction_btn);
		 placeBtn = (Button)findViewById(R.id.place_btn);
		 keyWorldsView = (AutoCompleteTextView)findViewById(R.id.input_address);
		 place = (EditText)findViewById(R.id.input_address);
		 
		 //·��
		 startView = (AutoCompleteTextView)findViewById(R.id.input_start);
		 endView = (AutoCompleteTextView)findViewById(R.id.input_end);
		 mBtnNext = (Button)findViewById(R.id.next);
		 mBtnPre = (Button)findViewById(R.id.pre);
		 routeSearchBtn = (Button)findViewById(R.id.start_direction_btn);
		 routeLayout = (LinearLayout)findViewById(R.id.route);
		 driveBtn = (Button)findViewById(R.id.drive);
		 busBtn = (Button)findViewById(R.id.bus);
		 walkBtn = (Button)findViewById(R.id.walk);
		 
		 
		mListForMarker.setVisibility(View.GONE);
		directionLayout.setVisibility(View.GONE);
		searchLayout.setVisibility(View.GONE);
		mBtnNext.setVisibility(View.GONE);
		mBtnPre.setVisibility(View.GONE);
		routeLayout.setVisibility(View.GONE);
		   /**
		    * ��ȡ��ͼ������
		    */
		   mMapController = mMapView.getController();
		   /**
		    *  ���õ�ͼ�Ƿ���Ӧ����¼�  .
		    */
		   mMapController.enableClick(true);
		   /**
		    * ���õ�ͼ���ż���
		    */
		   mMapController.setZoom(12);
		   mMapView.setBuiltInZoomControls(true);
		
	}
	
	
	//�̳�MyLocationOverlay��дdispatchTapʵ�ֵ������
  	public class locationOverlay extends MyLocationOverlay{

  		public locationOverlay(MapView mapView) {
  			super(mapView);
  			// TODO Auto-generated constructor stub
  		}
//  		@Override
//  		protected boolean dispatchTap() {
//  			// TODO Auto-generated method stub
//  			//�������¼�,��������
//			popupText.setText("�ҵ�λ��");
//			pop.showPopup(BMapUtil.getBitmapFromView(popupText),
//					new GeoPoint((int)(locData.latitude*1e6), (int)(locData.longitude*1e6)),
//					8);
//  			return true;
//  		}
  		
  	}
  	
  	
  	//ʵ��BDLocationListener�ӿڣ������ж�λ
  	public class MyLocationListenner implements BDLocationListener {
    	
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null)
                return ;
            //���ֶ�����������״ζ�λʱ���ƶ�����λ��
            if (locationBtn.isChecked()){
            	 locData.latitude = location.getLatitude();
                 locData.longitude = location.getLongitude();
                 //�������ʾ��λ����Ȧ����accuracy��ֵΪ0����
                 locData.accuracy = location.getRadius();
                 // �˴��������� locData�ķ�����Ϣ, �����λ SDK δ���ط�����Ϣ���û������Լ�ʵ�����̹�����ӷ�����Ϣ��
                 locData.direction = location.getDerect();
                 //���¶�λ����
                 myLocationOverlay.setData(locData);
                 //����ͼ������ִ��ˢ�º���Ч
                 mMapView.refresh();
                 
            	//�ƶ���ͼ����λ��
            	Log.d("LocationOverlay", "receive location, animate to it");
                if(isFirstLoc == true){
                	mMapController.animateTo(new GeoPoint((int)(locData.latitude* 1e6), (int)(locData.longitude *  1e6)));
                	isFirstLoc = false;
                }
                myPoint = mMapView.getMapCenter();
                myLocationOverlay.setLocationMode(LocationMode.NORMAL);
            }
        }
        
        public void onReceivePoi(BDLocation poiLocation) {
            if (poiLocation == null){
                return ;
            }
        }
    }
	@Override
	protected void onDestroy() {
		//�˳�ʱ���ٶ�λ
	    if (mLocClient != null)
	        mLocClient.stop();
	    mMapView.destroy();
	    mMKSearch.destory();
	    super.onDestroy();
	}
	 @Override
	    protected void onPause() {
	        mMapView.onPause();
	        super.onPause();
	    }
	    
	    @Override
	    protected void onResume() {
	        mMapView.onResume();
	        super.onResume();
	    }
	    @Override
	    protected void onSaveInstanceState(Bundle outState) {
	    	super.onSaveInstanceState(outState);
	    	mMapView.onSaveInstanceState(outState);
	    	
	    }
	    
	    @Override
	    protected void onRestoreInstanceState(Bundle savedInstanceState) {
	    	super.onRestoreInstanceState(savedInstanceState);
	    	mMapView.onRestoreInstanceState(savedInstanceState);
	    }
	    
	    //�˵���
	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	    	menu.add(menu.NONE,1,menu.NONE,"���ǵ�ͼ");
	    	menu.add(menu.NONE,2,menu.NONE,"�ֵ���ͼ");
	    	menu.add(menu.NONE,3,menu.NONE,"�˳�");
	    	return super.onCreateOptionsMenu(menu);
	    }
	    @Override
		public boolean onOptionsItemSelected(MenuItem item) {
	    	switch(item.getItemId()){
	    	case 1:
	    		mMapView.setSatellite(true);
	    		break;
	    	case 2:
	    		mMapView.setSatellite(false);
	    		break;
	    	case 3:
	    		this.finish();
	    	}
	    	return super.onOptionsItemSelected(item);
		}

}
