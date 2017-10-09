package com.myweather.app;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CityDB {
	public static final String CITY_DB_NAME="city.db";
	private static final String CITY_TABLE_NAME="city";
	private SQLiteDatabase db;
	
	public CityDB(Context context,String path){
		db=context.openOrCreateDatabase(path, Context.MODE_PRIVATE, null);
	}
	
	public List<City> getCityList(){
		List<City> cityList=new ArrayList<City> ();
		Cursor cursor=db.rawQuery("SELECT * from "+CITY_TABLE_NAME,null);
		 while(cursor.moveToNext())  
	        {  
	            String province = cursor.getString(cursor.getColumnIndex("province"));  
	            String city = cursor.getString(cursor.getColumnIndex("city"));  
	            String number = cursor.getString(cursor.getColumnIndex("number"));  
	            String allPY = cursor.getString(cursor.getColumnIndex("allpy"));  
	            String allFirstPY = cursor.getString(cursor.getColumnIndex("allfirstpy"));  
	            String firstPY = cursor.getString(cursor.getColumnIndex("firstpy"));  
	            City item = new City(province,city,number,allPY,allFirstPY,firstPY);  
	            cityList.add(item);  
	            //Log.d("CityDB.getCityList","city: "+city);
	        }  
		return cityList;
		
	}
	
	public String getCityName(String code){
		String cityName="";
		Cursor cursor=db.rawQuery("SELECT * from"+CITY_TABLE_NAME+"where number=?", new String[]{code});
		while(cursor.moveToNext()){
			cityName=cursor.getString(cursor.getColumnIndex("city"));  
		}
		return cityName;
	}
	
	public String getCityCode(String name){
		String cityCode="";
		Cursor cursor=db.rawQuery("SELECT * from city where city=?", new String[]{name});
		while(cursor.moveToNext()){
			cityCode=cursor.getString(cursor.getColumnIndex("number"));  
			Log.d("CityDB.getCityCode","name: "+name);
		}
		return cityCode;
	}
}
