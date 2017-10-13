package com.myweather.app;

import java.util.ArrayList;
import java.util.List;

import com.myweather.app.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

public class SelectCity extends Activity implements OnClickListener{

	private ImageView selectCityBack,searchBtn;
	private ListView cityListLv;
	private List<City> mCityList;
	private MyApplication mApplication;
	private ArrayList<String> mArrayList;
	private String updateCityCode="-1";
	private EditText searchCityName;
	private String searchName="";
	private SharedPreferences pref;
	private SharedPreferences.Editor editor;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_city);
		selectCityBack=(ImageView) findViewById(R.id.title_selectcity_back);
		selectCityBack.setOnClickListener(this);
		cityListLv=(ListView) findViewById(R.id.selectcity_lv);
		searchBtn=(ImageView) findViewById(R.id.select_btn);
		searchCityName=(EditText) findViewById(R.id.selectcity_edittext);
		pref=getSharedPreferences("CityCodePreference",Activity.MODE_PRIVATE);//存储选择的城市代码
		editor=pref.edit();
		//Log.d("SelectCity.onCreate","searchName"+searchName);
		mApplication=(MyApplication) getApplication();
		mCityList=mApplication.getCityList();
		mArrayList=new ArrayList<String>();//不使用new会指向空
		Log.d("SelectCity","mCityList"+mCityList.get(0).getCity());
		for(int i=0;i<mCityList.size();i++){
			String cityName=mCityList.get(i).getCity();
			mArrayList.add(cityName);
		}
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(SelectCity.this,android.R.layout.simple_list_item_1,mArrayList);
		cityListLv.setAdapter(adapter);
		AdapterView.OnItemClickListener itemClickListener=new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				// TODO Auto-generated method stub
				updateCityCode=mCityList.get(position).getNumber();
				editor.putString("citycode",updateCityCode);
				editor.commit();
				Log.d("SelectActivity.onItemClickListener","CityCode: "+updateCityCode); 
				Intent intent=new Intent(SelectCity.this,MainActivity.class);
				//intent.putExtra("city_code", updateCityCode);
				startActivity(intent);
			}
			
		};
		cityListLv.setOnItemClickListener(itemClickListener);
		searchBtn.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.title_selectcity_back:
			finish();
			break;
		case R.id.select_btn:
			searchName=searchCityName.getText().toString();
			Log.d("SelectActivity","SearchedName: "+searchName);
			if(searchName!=null){
				updateCityCode=mApplication.getCityCode(searchName);
				editor.putString("citycode",updateCityCode);
				editor.commit();
				Log.d("SelectActivity","updateCityCode"+updateCityCode);
				Intent intent=new Intent(SelectCity.this,MainActivity.class);
				//intent.putExtra("city_code", updateCityCode);
				startActivity(intent);
			}
		default:
			break;
		}
		
	}

}
