package com.myweather.app;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

public class MyApplication extends Application {
	private static Application mApp;
	List<City> cityList;  
    CityDB mCityDB; 
	 @Override  
	    public void onCreate() {  
	        super.onCreate();  
	  
	        Log.d("MyApplication","OnCreate");  
	        mApp=this;
	        
	        mCityDB = openCityDB();  
	        initCityList();  
	    }  
	 private boolean prepareCityList(){
		 cityList = mCityDB.getCityList();  
	        for(City city:cityList)  
	        {  
	            String cityName = city.getCity();  
	            Log.d("CityDB",cityName);  
	        }  
	        return true;  
	 }
	 
	 private void initCityList() {
		  new Thread(new Runnable() {  
	            @Override  
	            public void run() {  
	                prepareCityList();  
	            }  
	        }).start(); 
		
	}
	 

	public CityDB openCityDB() {
		String path = "/data"  
                + Environment.getDataDirectory().getAbsolutePath()  
                + File.separator + getPackageName()  
//                + File.separator + "databases"  
                + File.separator + CityDB.CITY_DB_NAME;  
        Log.d("file path",path);  
        File db = new File(path);  
        Log.d("db",db.getPath());  
  
        try {  
            InputStream is = getAssets().open("city.db");  
  
            FileOutputStream fos = new FileOutputStream(db);  
            int len = -1;  
            byte[] buffer = new byte[1024];  
            while((len = is.read(buffer))!=-1)  
            {  
                fos.write(buffer,0,len);  
                //Log.d("write buffer",buffer.toString());  
                fos.flush();  
            }  
            fos.close();  
            is.close();  
        }catch (Exception e)  
        {  
            e.printStackTrace();  
            System.exit(0);  
        }  
  
        return new CityDB(this,path); 
	}
	 List<City> getCityList()  
	    {  
	        return cityList;  
	    }  

	public static Application getInstance()  
	    {  
	        return mApp;  
	    }  
	public String getCityCode(String name){
		String code="-1";
		if(mCityDB.getCityCode(name)!=null)
			code=mCityDB.getCityCode(name);
		return code;
	}
}
