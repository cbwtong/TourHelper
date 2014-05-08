
package com.example.mytourhelper;

import com.baidu.platform.comapi.basestruct.GeoPoint;

import android.provider.VoicemailContract;


public class MapLocation {
    private long _id;

    private double latitude;

    private double longitude;
    
    
    private int markType;

    private String name;

    private String description;
    
    private String address;
    
    public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	private String resURL;
    
    public void setResURL (String res)
    {
    	this.resURL = res;
    }
    public String getResURL()
    {
    	return resURL==null? "":this.resURL;
    }
    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    
    public int getMarkType()
    {
    	return this.markType;
    }
    public void setMarkType(int type)
    {
    	this.markType = type;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public GeoPoint getGeoPoint(){
    	GeoPoint p = new GeoPoint((int)latitude,(int)longitude);
    	return  p ;
    }
    
    
}
