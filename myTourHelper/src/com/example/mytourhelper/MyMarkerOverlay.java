package com.example.mytourhelper;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;

public class MyMarkerOverlay extends ItemizedOverlay<OverlayItem> {
	private Drawable drawable;
	private DialogAddPlace dialogAddPlace ;
	private Context mContext;
	public MyMarkerOverlay(Drawable drawable, MapView arg1,Context context) {
		super(drawable, arg1);
		this.drawable = drawable;
		this.mContext =context;
	}
	@Override
	public boolean onTap(GeoPoint arg0, MapView arg1) {
		// TODO Auto-generated method stub
		return super.onTap(arg0, arg1);
	}
	@Override
	protected boolean onTap(int arg0) {
		OverlayItem item = getItem(arg0);
		DBHelper helper = new DBHelper(mContext.getApplicationContext());
        helper.open();
        MapLocation location = helper.getLocationByName(item.getTitle());
        helper.close();
        if(location !=null){
        	dialogAddPlace = new DialogAddPlace(mContext,false,location);
        	dialogAddPlace.displayLocationInfo(location);
        	dialogAddPlace.show();
        }
		return super.onTap(arg0);
	}

}
