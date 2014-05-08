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
	 *  MapView 是地图主控件
	 */
	private MapView mMapView = null;
	/**
	 *  用MapController完成地图控制 
	 */
	private MapController mMapController = null;
	

	/**
	 *  MKMapViewListener 用于处理地图事件回调
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
	//搜索相关
	private MKSearch mMKSearch = null;
//	private MySearchListener mySearchListener = new MySearchListener(this,mMapView,mMKSearch);
	private AutoCompleteTextView keyWorldsView ;
	private ArrayAdapter<String> sugAdapter = null;
	private EditText place;
	private String city = "郑州";
	private String address;
	// 定位相关
		LocationClient mLocClient;
		LocationData locData = null;
		public MyLocationListenner myListener = new MyLocationListenner();
	//定位图层
		public locationOverlay myLocationOverlay = null;
//		//弹出泡泡图层
//		private PopupOverlay   pop  = null;//弹出泡泡图层，浏览节点时使用
//		private TextView  popupText = null;//泡泡view
//		private View viewCache = null;
//		boolean isRequest = false;//是否手动触发请求定位
		boolean isFirstLoc = true;//是否首次定位
	//路径相关
		MKRoute route = null;//保存驾车/步行路线数据的变量，供浏览节点时使用
		TransitOverlay transitOverlay = null;//保存公交路线图层数据的变量，供浏览节点时使用
		RouteOverlay routeOverlay = null; 
		private Button btnNext,btnPre;
		private AutoCompleteTextView startView,endView;
		private Button routeSearchBtn;
		Button mBtnPre,mBtnNext;
		private int type = 0;
		private LinearLayout routeLayout;
		private Button driveBtn,busBtn,walkBtn;
		private View viewCache = null;
		int nodeIndex = -2;//节点索引,供浏览节点时使用
		int searchType = -1;//记录搜索的类型，区分驾车/步行和公交
		private PopupOverlay   pop  = null;//弹出泡泡图层，浏览节点时使用
		private TextView  popupText = null;//泡泡view
		
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
         * 使用地图sdk前需先初始化BMapManager.
         * BMapManager是全局的，可为多个MapView共用，它需要地图模块创建前创建，
         * 并在地图地图模块销毁后销毁，只要还有地图模块在使用，BMapManager就不应该销毁
         */
		BMapApplication app = (BMapApplication)this.getApplication();//生成APP
        if (app.mBMapManager == null) {
            app.mBMapManager = new BMapManager(getApplicationContext());
            /**
             * 如果BMapManager没有初始化则初始化BMapManager
             */
            app.mBMapManager.init(new BMapApplication.MyGeneralListener());
        }
        
        /**
         * 由于MapView在setContentView()中初始化,所以它需要在BMapManager初始化之后
         */
       setContentView(R.layout.base_map);
    // 初始化搜索模块，注册搜索事件监听
       mMKSearch = new MKSearch();  
//       mMKSearch.init(app.mBMapManager, mySearchListener);//注意，MKSearchListener只支持一个，以最后一次设置为准  
       mMKSearch.init(app.mBMapManager, new MKSearchListener(){

    	 //在此处理详情页结果
           @Override
           public void onGetPoiDetailSearchResult(int type, int error) {
               if (error != 0) {
                   Toast.makeText(BaseMapActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
               }
               else {
                   Toast.makeText(BaseMapActivity.this, "成功，查看详情页面", Toast.LENGTH_SHORT).show();
               }
           }
           /**
            * 在此处理poi搜索结果
            */
           public void onGetPoiResult(MKPoiResult res, int type, int error) {
               // 错误号可参考MKEvent中的定义
               if (error != 0 || res == null) {
                   Toast.makeText(BaseMapActivity.this, "抱歉，未找到结果", Toast.LENGTH_LONG).show();
                   return;
               }
               // 将地图移动到第一个POI中心点
               if (res.getCurrentNumPois() > 0) {
                   // 将poi结果显示到地图上
                   MyPoiOverlay poiOverlay = new MyPoiOverlay(BaseMapActivity.this, mMapView, mMKSearch);
                   poiOverlay.setData(res.getAllPoi());
                   mMapView.getOverlays().clear();
                   mMapView.getOverlays().add(poiOverlay);
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
                   Toast.makeText(BaseMapActivity.this, strInfo, Toast.LENGTH_LONG).show();
               }
           }
           public void onGetDrivingRouteResult(MKDrivingRouteResult res,
					int error) {
				//起点或终点有歧义，需要选择具体的城市列表或地址列表
				if (error == MKEvent.ERROR_ROUTE_ADDR){
					//遍历所有地址
//					ArrayList<MKPoiInfo> stPois = res.getAddrResult().mStartPoiList;
//					ArrayList<MKPoiInfo> enPois = res.getAddrResult().mEndPoiList;
//					ArrayList<MKCityListInfo> stCities = res.getAddrResult().mStartCityList;
//					ArrayList<MKCityListInfo> enCities = res.getAddrResult().mEndCityList;
					return;
				}
				// 错误号可参考MKEvent中的定义
				if (error != 0 || res == null) {
					Toast.makeText(BaseMapActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
					return;
				}
			
				searchType = 0;
			    routeOverlay = new RouteOverlay(BaseMapActivity.this, mMapView);
			    // 此处仅展示一个方案作为示例
			    routeOverlay.setData(res.getPlan(0).getRoute(0));
			    //清除其他图层
			    mMapView.getOverlays().clear();
			    //添加路线图层
			    mMapView.getOverlays().add(routeOverlay);
			    //执行刷新使生效
			    mMapView.refresh();
			    // 使用zoomToSpan()绽放地图，使路线能完全显示在地图上
			    mMapView.getController().zoomToSpan(routeOverlay.getLatSpanE6(), routeOverlay.getLonSpanE6());
			    //移动地图到起点
			    mMapView.getController().animateTo(res.getStart().pt);
			    //将路线数据保存给全局变量
			    route = res.getPlan(0).getRoute(0);
			    //重置路线节点索引，节点浏览时使用
			    nodeIndex = -1;
			    mBtnPre.setVisibility(View.VISIBLE);
				mBtnNext.setVisibility(View.VISIBLE);
			}
           public void onGetTransitRouteResult(MKTransitRouteResult res,
					int error) {
				//起点或终点有歧义，需要选择具体的城市列表或地址列表
				if (error == MKEvent.ERROR_ROUTE_ADDR){
					//遍历所有地址
//					ArrayList<MKPoiInfo> stPois = res.getAddrResult().mStartPoiList;
//					ArrayList<MKPoiInfo> enPois = res.getAddrResult().mEndPoiList;
//					ArrayList<MKCityListInfo> stCities = res.getAddrResult().mStartCityList;
//					ArrayList<MKCityListInfo> enCities = res.getAddrResult().mEndCityList;
					return;
				}
				if (error != 0 || res == null) {
					Toast.makeText(BaseMapActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
					return;
				}
				
				searchType = 1;
				transitOverlay = new TransitOverlay (BaseMapActivity.this, mMapView);
			    // 此处仅展示一个方案作为示例
			    transitOverlay.setData(res.getPlan(0));
			  //清除其他图层
			    mMapView.getOverlays().clear();
			  //添加路线图层
			    mMapView.getOverlays().add(transitOverlay);
			  //执行刷新使生效
			    mMapView.refresh();
			    // 使用zoomToSpan()绽放地图，使路线能完全显示在地图上
			    mMapView.getController().zoomToSpan(transitOverlay.getLatSpanE6(), transitOverlay.getLonSpanE6());
			  //移动地图到起点
			    mMapView.getController().animateTo(res.getStart().pt);
			  //重置路线节点索引，节点浏览时使用
			    nodeIndex = 0;
			    mBtnPre.setVisibility(View.VISIBLE);
				mBtnNext.setVisibility(View.VISIBLE);
			}
           public void onGetWalkingRouteResult(MKWalkingRouteResult res,
					int error) {
				//起点或终点有歧义，需要选择具体的城市列表或地址列表
				if (error == MKEvent.ERROR_ROUTE_ADDR){
					//遍历所有地址
//					ArrayList<MKPoiInfo> stPois = res.getAddrResult().mStartPoiList;
//					ArrayList<MKPoiInfo> enPois = res.getAddrResult().mEndPoiList;
//					ArrayList<MKCityListInfo> stCities = res.getAddrResult().mStartCityList;
//					ArrayList<MKCityListInfo> enCities = res.getAddrResult().mEndCityList;
					return;
				}
				if (error != 0 || res == null) {
					Toast.makeText(BaseMapActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
					return;
				}

				searchType = 2;
				routeOverlay = new RouteOverlay(BaseMapActivity.this, mMapView);
			    // 此处仅展示一个方案作为示例
				routeOverlay.setData(res.getPlan(0).getRoute(0));
				//清除其他图层
			    mMapView.getOverlays().clear();
			  //添加路线图层
			    mMapView.getOverlays().add(routeOverlay);
			  //执行刷新使生效
			    mMapView.refresh();
			    // 使用zoomToSpan()绽放地图，使路线能完全显示在地图上
			    mMapView.getController().zoomToSpan(routeOverlay.getLatSpanE6(), routeOverlay.getLonSpanE6());
			  //移动地图到起点
			    mMapView.getController().animateTo(res.getStart().pt);
			    //将路线数据保存给全局变量
			    route = res.getPlan(0).getRoute(0);
			    //重置路线节点索引，节点浏览时使用
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
            * 更新建议列表
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
       //初始化UI
       initUI();
       sugAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line);
       keyWorldsView.setAdapter(sugAdapter);
       //给UI设置监听器
       setListener();
	}
	
	//给UI设置监听器
	private void setListener() {
		locationBtn.setOnCheckedChangeListener(new OnCheckedChangeListener (){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if(isChecked){
					//定位初始化
			        mLocClient = new LocationClient( BaseMapActivity.this );
			        locData = new LocationData();
			        mLocClient.registerLocationListener( myListener );
			        LocationClientOption option = new LocationClientOption();
			        option.setOpenGps(true);//打开gps
			        option.setCoorType("bd09ll");     //设置坐标类型
			        option.setScanSpan(1000);//刷新间隔
			        mLocClient.setLocOption(option);
			        mLocClient.start();
			        //定位图层初始化
					myLocationOverlay = new locationOverlay(mMapView);
					//设置定位数据
				    myLocationOverlay.setData(locData);
				    //添加定位图层
					mMapView.getOverlays().add(myLocationOverlay);
					myLocationOverlay.enableCompass();
					//修改定位数据后刷新图层生效
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
			  //在此处理地图点击事件 
			  //消隐pop
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
				  * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
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
				  * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
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
				  * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
				  */
//				 city = mySearchListener.kk.city;
                 mMKSearch.suggestionSearch(cs.toString(), city);				
			}
		});
		OnClickListener clickListener = new OnClickListener(){
			public void onClick(View v) {
				//发起搜索
				SearchButtonProcess(v);
			}
        };
        routeSearchBtn.setOnClickListener(clickListener);
        driveBtn.setOnClickListener(clickListener);
        busBtn.setOnClickListener(clickListener);
        walkBtn.setOnClickListener(clickListener);
        
        OnClickListener nodeClickListener = new OnClickListener(){
			public void onClick(View v) {
				//浏览路线节点
				nodeClick(v);
			}
        };
        mBtnNext.setOnClickListener(nodeClickListener);
        mBtnPre.setOnClickListener(nodeClickListener);
        createPaopao();
	}
	
	void SearchButtonProcess(View v) {
		//重置浏览节点的路线数据
		route = null;
		routeOverlay = null;
		transitOverlay = null; 
		routeLayout.setVisibility(View.VISIBLE);
		mBtnPre.setVisibility(View.VISIBLE);
		mBtnNext.setVisibility(View.VISIBLE);
		// 处理搜索按钮响应
		EditText editSt = (EditText)findViewById(R.id.input_start);
		EditText editEn = (EditText)findViewById(R.id.input_end);
		
		// 对起点终点的name进行赋值，也可以直接对坐标赋值，赋值坐标则将根据坐标进行搜索
		MKPlanNode stNode = new MKPlanNode();
		stNode.name = editSt.getText().toString();
		MKPlanNode enNode = new MKPlanNode();
		enNode.name = editEn.getText().toString();

		// 实际使用中请对起点终点城市进行正确的设定
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
			//驾车、步行使用的数据结构相同，因此类型为驾车或步行，节点浏览方法相同
			if (nodeIndex < -1 || route == null || nodeIndex >= route.getNumSteps())
				return;
			
			//上一个节点
			if (mBtnPre.equals(v) && nodeIndex > 0){
				//索引减
				nodeIndex--;
				//移动到指定索引的坐标
				mMapView.getController().animateTo(route.getStep(nodeIndex).getPoint());
				//弹出泡泡
				popupText.setBackgroundResource(R.drawable.popup);
				popupText.setText(route.getStep(nodeIndex).getContent());
				pop.showPopup(BMapUtil.getBitmapFromView(popupText),
						route.getStep(nodeIndex).getPoint(),
						5);
			}
			//下一个节点
			if (mBtnNext.equals(v) && nodeIndex < (route.getNumSteps()-1)){
				//索引加
				nodeIndex++;
				//移动到指定索引的坐标
				mMapView.getController().animateTo(route.getStep(nodeIndex).getPoint());
				//弹出泡泡
				popupText.setBackgroundResource(R.drawable.popup);
				popupText.setText(route.getStep(nodeIndex).getContent());
				pop.showPopup(BMapUtil.getBitmapFromView(popupText),
						route.getStep(nodeIndex).getPoint(),
						5);
			}
		}
		if (searchType == 1){
			//公交换乘使用的数据结构与其他不同，因此单独处理节点浏览
			if (nodeIndex < -1 || transitOverlay == null || nodeIndex >= transitOverlay.getAllItem().size())
				return;
			
			//上一个节点
			if (mBtnPre.equals(v) && nodeIndex > 1){
				//索引减
				nodeIndex--;
				//移动到指定索引的坐标
				mMapView.getController().animateTo(transitOverlay.getItem(nodeIndex).getPoint());
				//弹出泡泡
				popupText.setBackgroundResource(R.drawable.popup);
				popupText.setText(transitOverlay.getItem(nodeIndex).getTitle());
				pop.showPopup(BMapUtil.getBitmapFromView(popupText),
						transitOverlay.getItem(nodeIndex).getPoint(),
						5);
			}
			//下一个节点
			if (mBtnNext.equals(v) && nodeIndex < (transitOverlay.getAllItem().size()-2)){
				//索引加
				nodeIndex++;
				//移动到指定索引的坐标
				mMapView.getController().animateTo(transitOverlay.getItem(nodeIndex).getPoint());
				//弹出泡泡
				popupText.setBackgroundResource(R.drawable.popup);
				popupText.setText(transitOverlay.getItem(nodeIndex).getTitle());
				pop.showPopup(BMapUtil.getBitmapFromView(popupText),
						transitOverlay.getItem(nodeIndex).getPoint(),
						5);
			}
		}
		
	}
	
	/**
	 * 创建弹出泡泡图层
	 */
	public void createPaopao(){
		
        //泡泡点击响应回调
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
		 
		 //位置
		 searchBtn = (Button)findViewById(R.id.start_direction_btn);
		 placeBtn = (Button)findViewById(R.id.place_btn);
		 keyWorldsView = (AutoCompleteTextView)findViewById(R.id.input_address);
		 place = (EditText)findViewById(R.id.input_address);
		 
		 //路径
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
		    * 获取地图控制器
		    */
		   mMapController = mMapView.getController();
		   /**
		    *  设置地图是否响应点击事件  .
		    */
		   mMapController.enableClick(true);
		   /**
		    * 设置地图缩放级别
		    */
		   mMapController.setZoom(12);
		   mMapView.setBuiltInZoomControls(true);
		
	}
	
	
	//继承MyLocationOverlay重写dispatchTap实现点击处理
  	public class locationOverlay extends MyLocationOverlay{

  		public locationOverlay(MapView mapView) {
  			super(mapView);
  			// TODO Auto-generated constructor stub
  		}
//  		@Override
//  		protected boolean dispatchTap() {
//  			// TODO Auto-generated method stub
//  			//处理点击事件,弹出泡泡
//			popupText.setText("我的位置");
//			pop.showPopup(BMapUtil.getBitmapFromView(popupText),
//					new GeoPoint((int)(locData.latitude*1e6), (int)(locData.longitude*1e6)),
//					8);
//  			return true;
//  		}
  		
  	}
  	
  	
  	//实现BDLocationListener接口，来进行定位
  	public class MyLocationListenner implements BDLocationListener {
    	
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null)
                return ;
            //是手动触发请求或首次定位时，移动到定位点
            if (locationBtn.isChecked()){
            	 locData.latitude = location.getLatitude();
                 locData.longitude = location.getLongitude();
                 //如果不显示定位精度圈，将accuracy赋值为0即可
                 locData.accuracy = location.getRadius();
                 // 此处可以设置 locData的方向信息, 如果定位 SDK 未返回方向信息，用户可以自己实现罗盘功能添加方向信息。
                 locData.direction = location.getDerect();
                 //更新定位数据
                 myLocationOverlay.setData(locData);
                 //更新图层数据执行刷新后生效
                 mMapView.refresh();
                 
            	//移动地图到定位点
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
		//退出时销毁定位
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
	    
	    //菜单栏
	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	    	menu.add(menu.NONE,1,menu.NONE,"卫星地图");
	    	menu.add(menu.NONE,2,menu.NONE,"街道地图");
	    	menu.add(menu.NONE,3,menu.NONE,"退出");
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
