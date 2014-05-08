
package com.example.mytourhelper;

public interface IDBValues {
    String TABLE_LOCATIONS = "locations";
    
    String TABLE_MAP = "map";
    
    String TABLE_CAMERA = "camera";
    
    String COL_ID = "_id";
    
    
    String COL_LAT = "latitude";

    String COL_LONG = "longitude";
    
    String COL_TYPE = "marktype";
    
    String COL_NAME = "loc_name";

    String COL_DESCRIPTION = "loc_description";

    String COL_RES = "resource";
    
    String COL_ADD = "address";
    
    String CREATE_TABLE_LOCATIONS = "create table " + TABLE_LOCATIONS + "(" + COL_ID
            + " integer primary key autoincrement," + COL_LAT + " text, " + COL_LONG + " text,"+COL_ADD+" text, "+ COL_TYPE + " text,"
            + COL_NAME + " text," + COL_RES + " text," + COL_DESCRIPTION + " text);";
}
