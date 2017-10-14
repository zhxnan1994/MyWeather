package com.myweather.app;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.myweather.app.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{
	TodayWeather todayWeather = null;
	private static final String CITYCODE = "";
	private ImageView updateBtn,cityManager,cityLocate;
	private TextView cityT,timeT,humidityT,weekT,pmDataT,pmQualityT,temperatureT,
	climateT,windT,cityNameT;
	private ImageView weatherImg,PM25Img;
	private String updateCityCode;
	private SharedPreferences pref;
    /**
     * δ������������Ϣ
     */
	private TextView day1_week,day2_week,day3_week,day1_tempture,day1_weatherstate,day1_windstate,
	day2_tempture,day2_weatherstate,day2_windstate,day3_tempture,day3_weatherstate,day3_windstate;
    /**
     * ʹ�øߵµ�ͼ�ӿڣ�ȷ������λ��
     */
    private AMapLocationClient mLocationClient = null;
    private String cityLocateText="";
    //��ȡ���ݿ��ѯ��λ���еĳ��б���
    private MyApplication myApplication;

	private Handler mHandler=new Handler(){
		public void handleMessage(Message msg){
			switch(msg.what){
			case 1:
				updateTodayWeather((TodayWeather) msg.obj);
				break;
			default:
				break;
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		updateBtn=(ImageView) findViewById(R.id.title_city_update);
		updateBtn.setOnClickListener(this);
		cityManager=(ImageView) findViewById(R.id.title_city_manager);
		cityManager.setOnClickListener(this);
		cityLocate=(ImageView) findViewById(R.id.title_city_locate) ;
        cityLocate.setOnClickListener(this);
        myApplication=(MyApplication) getApplication();

		initView();
		if(NetCheck.getNetState(this)==NetCheck.NET_NONE){
			Toast.makeText(this, "network offline", Toast.LENGTH_SHORT).show();
			Log.d("MainActivity","network offline");
		}else{
			//Toast.makeText(this, "network online", Toast.LENGTH_SHORT).show();
			Log.d("MainActivity","network ok");
		}
		pref=getSharedPreferences("CityCodePreference",Activity.MODE_PRIVATE);
		updateCityCode=pref.getString("citycode","");
		if(!updateCityCode.equals("")){
			getWeatherDatafromNet(updateCityCode);
		}else{
			getWeatherDatafromNet(CITYCODE);
		}
		/*updateCityCode=getIntent().getStringExtra("city_code");
		if(updateCityCode!="-1")
		{
			getWeatherDatafromNet(updateCityCode);
		}*/
	}
	public void getLocation(){
           mLocationClient = new AMapLocationClient(getApplicationContext());
           mLocationClient.startLocation();
           mLocationClient.setLocationListener(locationListener);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                   updateCityCode = myApplication.getCityCode(cityLocateText);
                    getWeatherDatafromNet(updateCityCode);
                }
            },2000);



    }
	void initView()  
    {  
        //title  
        cityNameT = (TextView)findViewById(R.id.title_city_name);  
  
        //today weather  
        cityT = (TextView)findViewById(R.id.todayinfo1_cityName);  
        timeT = (TextView)findViewById(R.id.todayinfo1_updateTime);  
        humidityT = (TextView)findViewById(R.id.todayinfo1_humidity);  
        weekT = (TextView)findViewById(R.id.todayinfo2_week);  
        pmDataT = (TextView)findViewById(R.id.todayinfo1_pm25);  
        pmQualityT = (TextView)findViewById(R.id.todayinfo1_pm25status);  
        temperatureT = (TextView)findViewById(R.id.todayinfo2_temperature);  
        climateT = (TextView)findViewById(R.id.todayinfo2_weatherState);  
        windT = (TextView)findViewById(R.id.todayinfo2_wind);  
  
        weatherImg = (ImageView)findViewById(R.id.todayinfo2_weatherStatusImg);  
       PM25Img = (ImageView)findViewById(R.id.todayinfo1_pm25img);  
        
       //δ��������Ϣ
       day1_week=(TextView) findViewById(R.id.day1_week);
       day2_week=(TextView) findViewById(R.id.day2_week);
       day3_week=(TextView) findViewById(R.id.day3_week);
       day1_tempture=(TextView) findViewById(R.id.day1_tempture);
       day1_weatherstate=(TextView) findViewById(R.id.day1_weatherstate);
       day1_windstate=(TextView) findViewById(R.id.day1_windstate);
       
       day2_tempture=(TextView) findViewById(R.id.day2_tempture);
       day2_weatherstate=(TextView) findViewById(R.id.day2_weatherstate);
       day2_windstate=(TextView) findViewById(R.id.day2_windstate);
       
       day3_tempture=(TextView) findViewById(R.id.day3_tempture);
       day3_weatherstate=(TextView) findViewById(R.id.day3_weatherstate);
       day3_windstate=(TextView) findViewById(R.id.day3_windstate);
       
        cityNameT.setText("N/A");  
  
        cityT.setText("N/A");  
        timeT.setText("N/A");  
        humidityT.setText("N/A");  
        weekT.setText("N/A");  
        pmDataT.setText("N/A");  
        pmQualityT.setText("N/A");  
        temperatureT.setText("N/A");  
        climateT.setText("N/A");  
        windT.setText("N/A");  
        day1_week.setText("N/A");
        day2_week.setText("N/A");
        day3_week.setText("N/A");
    }  
	private void getWeatherDatafromNet(String cityCode)  
    {  
        final String address = "http://wthrcdn.etouch.cn/WeatherApi?citykey="+cityCode;  
        Log.d("Address:",address);  
        new Thread(new Runnable() {  
            @Override  
            public void run() {  
                HttpURLConnection urlConnection = null;  
                try {  
                    URL url = new URL(address);  
                    urlConnection = (HttpURLConnection) url.openConnection();  
                    urlConnection.setRequestMethod("GET");  
                    urlConnection.setConnectTimeout(8000);  
                    urlConnection.setReadTimeout(8000);  
                    InputStream in = urlConnection.getInputStream();  
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));  
                    StringBuffer sb = new StringBuffer();  
                    String str;  
                    while((str=reader.readLine())!=null)  
                    {  
                        sb.append(str);  
                        //Log.d("date from url",str);  
                    }  
                    String response = sb.toString();  
                    //Log.d("response",response);  
                    todayWeather=parseXML(response);
                    if(todayWeather!=null){
                    	Message msg=new Message();
                    	msg.what=1;
                    	msg.obj=todayWeather;
                    	mHandler.sendMessage(msg);
                    }
                }catch (Exception e)  
                {  
                    e.printStackTrace();  
                }  
            }  
        }).start();  
    }
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		    case R.id.title_city_update:
		        if(cityLocateText==""){
			        updateCityCode=pref.getString("citycode","");}
			        if(!updateCityCode.equals("")){
				        getWeatherDatafromNet(updateCityCode);
			        }else{
				        getWeatherDatafromNet(CITYCODE);
			        }
			/*updateBtn.setImageResource(R.drawable.title_city_update_2);
			if(!updateBtn.isPressed())
				updateBtn.setImageResource(R.drawable.title_city_update);*/
			        break;
		    case R.id.title_city_manager:
			    Intent intent=new Intent(MainActivity.this,SelectCity.class);
			    startActivity(intent);
			    break;
            case R.id.title_city_locate:
                getLocation();

                break;
		default:
			break;
		}
	}  
	private TodayWeather parseXML(String xmlData)  
    {  
        int fengliCount = 0;  
        int fengxiangCount = 0;  
        int dateCount = 0;  
        int highCount = 0;  
        int lowCount = 0;  
        int typeCount = 0;  
        try {  
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();  
            XmlPullParser xmlPullParser = factory.newPullParser();  
            xmlPullParser.setInput(new StringReader(xmlData));  
  
            int eventType = xmlPullParser.getEventType();  
            //Log.d("MWeater","start parse xml");  
  
            while(eventType!=xmlPullParser.END_DOCUMENT)  
            {  
                switch (eventType)  
                {  
                    //�ĵ���ʼλ��  
                    case XmlPullParser.START_DOCUMENT:  
                        //Log.d("parse","start doc");  
                        break;  
                    //��ǩԪ�ؿ�ʼλ��  
                    case XmlPullParser.START_TAG:  
                    	if(xmlPullParser.getName().equals("resp")){
                    		todayWeather=new TodayWeather();
                    	}
                    	else if(xmlPullParser.getName().equals("city"))  
                        {  
                            eventType=xmlPullParser.next();  
                            todayWeather.setCity(xmlPullParser.getText());
                            Log.d("city",xmlPullParser.getText()); 
                          
                        }else if(xmlPullParser.getName().equals("updatetime"))  
                        {  
                            eventType=xmlPullParser.next();  
                            todayWeather.setUpdatetime(xmlPullParser.getText());
                            Log.d("updatetime",xmlPullParser.getText());  
                        }else if(xmlPullParser.getName().equals("wendu"))  
                        {  
                            eventType=xmlPullParser.next();  
                            todayWeather.setWendu(xmlPullParser.getText());
                            Log.d("wendu",xmlPullParser.getText());  
                        }else if(xmlPullParser.getName().equals("fengli") && fengliCount==0 )  
                        {  
                            eventType=xmlPullParser.next();  
                            todayWeather.setFengli(xmlPullParser.getText());
                            Log.d("fengli",xmlPullParser.getText());  
                            fengliCount++;  
                        }else if(xmlPullParser.getName().equals("shidu"))  
                        {  
                            eventType=xmlPullParser.next();  
                            todayWeather.setShidu(xmlPullParser.getText());
                            Log.d("shidu",xmlPullParser.getText());  
                        }else if(xmlPullParser.getName().equals("fengxiang") && fengxiangCount== 0)  
                        {  
                            eventType=xmlPullParser.next();  
                            todayWeather.setFengxiang(xmlPullParser.getText());
                            Log.d("fengxiang",xmlPullParser.getText());  
                            fengxiangCount++;  
                        }else if(xmlPullParser.getName().equals("pm25"))  
                        {  
                            eventType=xmlPullParser.next();  
                            todayWeather.setPm25(xmlPullParser.getText());
                            Log.d("pm25",xmlPullParser.getText());  
                        }else if(xmlPullParser.getName().equals("quality"))  
                        {  
                            eventType=xmlPullParser.next();  
                            todayWeather.setQuality(xmlPullParser.getText());
                            Log.d("quelity",xmlPullParser.getText());  
                        }else if(xmlPullParser.getName().equals("date") && dateCount==0 )  
                        {  
                            eventType=xmlPullParser.next(); 
                            todayWeather.setDate(xmlPullParser.getText());
                            Log.d("date",xmlPullParser.getText());  
                            dateCount++;  
                        }else if(xmlPullParser.getName().equals("high") && highCount==0 )  
                        {  
                            eventType=xmlPullParser.next();  
                            todayWeather.setHigh(xmlPullParser.getText());
                            Log.d("high",xmlPullParser.getText());  
                            highCount++;  
                        }else if(xmlPullParser.getName().equals("low") && lowCount==0 )  
                        {  
                            eventType=xmlPullParser.next();  
                            todayWeather.setLow(xmlPullParser.getText());
                            Log.d("low",xmlPullParser.getText());  
                            lowCount++;  
                        }else if(xmlPullParser.getName().equals("type") && typeCount==0 )  
                        {  
                            eventType=xmlPullParser.next();  
                            todayWeather.setType(xmlPullParser.getText());
                            Log.d("type",xmlPullParser.getText());  
                            typeCount++;  
                        } else if(xmlPullParser.getName().equals("date") && dateCount==1 )  
                        {  
                            eventType=xmlPullParser.next(); 
                            todayWeather.setDate1(xmlPullParser.getText());
                            Log.d("date",xmlPullParser.getText());  
                            dateCount++;  
                        }
                        else if(xmlPullParser.getName().equals("high") && highCount==1 )  
                        {  
                            eventType=xmlPullParser.next();  
                            todayWeather.setHigh1(xmlPullParser.getText());
                            Log.d("high",xmlPullParser.getText());  
                            highCount++;  
                        }else if(xmlPullParser.getName().equals("low") && lowCount==1 )  
                        {  
                            eventType=xmlPullParser.next();  
                            todayWeather.setLow1(xmlPullParser.getText());
                            Log.d("low",xmlPullParser.getText());  
                            lowCount++;  
                        }else if(xmlPullParser.getName().equals("type") && typeCount==1 )  
                        {  
                            eventType=xmlPullParser.next();  
                            todayWeather.setType1(xmlPullParser.getText());
                            Log.d("type",xmlPullParser.getText());  
                            typeCount++;  
                        }else if(xmlPullParser.getName().equals("fengxiang") && fengxiangCount== 1)  
                        {  
                            eventType=xmlPullParser.next();  
                            todayWeather.setFengxiang1(xmlPullParser.getText());
                            Log.d("fengxiang",xmlPullParser.getText());  
                            fengxiangCount++;  
                        }else if(xmlPullParser.getName().equals("date") && dateCount==2 )  
                        {  
                            eventType=xmlPullParser.next(); 
                            todayWeather.setDate2(xmlPullParser.getText());
                            Log.d("date",xmlPullParser.getText());  
                            dateCount++;  
                        }
                        else if(xmlPullParser.getName().equals("high") && highCount==2 )  
                        {  
                            eventType=xmlPullParser.next();  
                            todayWeather.setHigh2(xmlPullParser.getText());
                            Log.d("high",xmlPullParser.getText());  
                            highCount++;  
                        }else if(xmlPullParser.getName().equals("low") && lowCount==2 )  
                        {  
                            eventType=xmlPullParser.next();  
                            todayWeather.setLow2(xmlPullParser.getText());
                            Log.d("low",xmlPullParser.getText());  
                            lowCount++;  
                        }else if(xmlPullParser.getName().equals("type") && typeCount==2 )  
                        {  
                            eventType=xmlPullParser.next();  
                            todayWeather.setType2(xmlPullParser.getText());
                            Log.d("type",xmlPullParser.getText());  
                            typeCount++;  
                        }else if(xmlPullParser.getName().equals("fengxiang") && fengxiangCount== 2)  
                        {  
                            eventType=xmlPullParser.next();  
                            todayWeather.setFengxiang2(xmlPullParser.getText());
                            Log.d("fengxiang",xmlPullParser.getText());  
                            fengxiangCount++;  
                        }else if(xmlPullParser.getName().equals("date") && dateCount==3)  
                        {  
                            eventType=xmlPullParser.next(); 
                            todayWeather.setDate3(xmlPullParser.getText());
                            Log.d("date",xmlPullParser.getText());  
                            dateCount++;  
                        }
                        else if(xmlPullParser.getName().equals("high") && highCount==3 )  
                        {  
                            eventType=xmlPullParser.next();  
                            todayWeather.setHigh3(xmlPullParser.getText());
                            Log.d("high",xmlPullParser.getText());  
                            highCount++;  
                        }else if(xmlPullParser.getName().equals("low") && lowCount==3 )  
                        {  
                            eventType=xmlPullParser.next();  
                            todayWeather.setLow3(xmlPullParser.getText());
                            Log.d("low",xmlPullParser.getText());  
                            lowCount++;  
                        }else if(xmlPullParser.getName().equals("type") && typeCount==3 )  
                        {  
                            eventType=xmlPullParser.next();  
                            todayWeather.setType3(xmlPullParser.getText());
                            Log.d("type",xmlPullParser.getText());  
                            typeCount++;  
                        }else if(xmlPullParser.getName().equals("fengxiang") && fengxiangCount== 3)  
                        {  
                            eventType=xmlPullParser.next();  
                            todayWeather.setFengxiang3(xmlPullParser.getText());
                            Log.d("fengxiang",xmlPullParser.getText());  
                            fengxiangCount++;  
                        }
                        break;  
                    case XmlPullParser.END_TAG:  
                        break;  
                }  
                eventType=xmlPullParser.next();  
            }  
        }catch (Exception e)  
        {  
            e.printStackTrace();  
        } 
        return todayWeather;
    }  
	void updateTodayWeather(TodayWeather todayWeather)  
    {  
        cityNameT.setText(todayWeather.getCity()+"����");  
        cityT.setText(todayWeather.getCity());  
        timeT.setText("����ʱ��: "+todayWeather.getUpdatetime());  
        humidityT.setText("ʪ��:"+todayWeather.getShidu());  
        pmDataT.setText(todayWeather.getPm25());  
        pmQualityT.setText(todayWeather.getQuality());  
        weekT.setText(todayWeather.getDate());
        temperatureT.setText(todayWeather.getHigh()+"~"+todayWeather.getLow());  
        climateT.setText(todayWeather.getType());  
        windT.setText("����:"+todayWeather.getFengli());  
        day1_week.setText(todayWeather.getDate1());
        day1_tempture.setText(todayWeather.getLow1()+"~"+todayWeather.getHigh1());
        day1_weatherstate.setText(todayWeather.getType1());
        day1_windstate.setText(todayWeather.getFengxiang1());
        day2_week.setText(todayWeather.getDate2());
        day2_tempture.setText(todayWeather.getLow2()+"~"+todayWeather.getHigh2());
        day2_weatherstate.setText(todayWeather.getType2());
        day2_windstate.setText(todayWeather.getFengxiang2());
        day3_week.setText(todayWeather.getDate3());
        day3_tempture.setText(todayWeather.getLow3()+"~"+todayWeather.getHigh3());
        day3_weatherstate.setText(todayWeather.getType3());
        day3_windstate.setText(todayWeather.getFengxiang3());
        if(todayWeather.getPm25()!=null) {  
            int pm25 = Integer.parseInt(todayWeather.getPm25());  
            if (pm25 <= 50) {  
                PM25Img.setImageResource(R.drawable.biz_plugin_weather_0_50);  
            } else if (pm25 >= 51 && pm25 <= 100) {  
                PM25Img.setImageResource(R.drawable.biz_plugin_weather_51_100);  
            } else if (pm25 >= 101 && pm25 <= 150) {  
                PM25Img.setImageResource(R.drawable.biz_plugin_weather_101_150);  
            } else if (pm25 >= 151 && pm25 <= 200) {  
                PM25Img.setImageResource(R.drawable.biz_plugin_weather_151_200);  
            } else if (pm25 >= 201 && pm25 <= 300) {  
                PM25Img.setImageResource(R.drawable.biz_plugin_weather_201_300);  
            }  
        }  
        if(todayWeather.getType()!=null) {  
            Log.d("type", todayWeather.getType());  
            switch (todayWeather.getType()) {  
                case "��":  
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_qing);  
                    break;  
                case "��":  
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_yin);  
                    break;  
                case "��":  
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_wu);  
                    break;  
                case "����":  
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_duoyun);  
                    break;  
                case "С��":  
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_xiaoyu);  
                    break;  
                case "����":  
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhongyu);  
                    break;  
                case "����":  
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_dayu);  
                    break;  
                case "����":  
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhenyu);  
                    break;  
                case "������":  
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_leizhenyu);  
                    break;  
                case "������ӱ�":  
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_leizhenyubingbao);  
                    break;  
                case "����":  
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_baoyu);  
                    break;  
                case "����":  
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_dabaoyu);  
                    break;  
                case "�ش���":  
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_tedabaoyu);  
                    break;  
                case "��ѩ":  
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhenxue);  
                    break;  
                case "��ѩ":  
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_baoxue);  
                    break;  
                case "��ѩ":  
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_daxue);  
                    break;  
                case "Сѩ":  
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_xiaoxue);  
                    break;  
                case "���ѩ":  
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_yujiaxue);  
                    break;  
                case "��ѩ":  
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhongxue);  
                    break;  
                case "ɳ����":  
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_shachenbao);  
                    break;  
                default:  
                    break;  
            }  
        }  
        //Toast.makeText(MainActivity.this,"���³ɹ�",Toast.LENGTH_SHORT).show();  
    } 
	//������ؼ���������������˳�����  
    @Override  
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
  
        if (keyCode == KeyEvent.KEYCODE_BACK) {  
            Intent home = new Intent(Intent.ACTION_MAIN);  
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
            home.addCategory(Intent.CATEGORY_HOME);  
            startActivity(home);  
            return true;  
        }  
        return super.onKeyDown(keyCode, event);  
    }
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {

            if (null != location) {

                StringBuffer sb = new StringBuffer();
                //errCode����0����λ�ɹ���������Ϊ��λʧ�ܣ�����Ŀ��Բ��չ�����λ������˵��
                if(location.getErrorCode() == 0){
                    cityLocateText=location.getCity();
                    cityLocateText=cityLocateText.substring(0,cityLocateText.length()-1);
                    //Toast.makeText(getApplicationContext(),"��ǰ����Ϊ"+sb.toString()+location.getCityCode(),Toast.LENGTH_LONG).show();
                }
            } else {
                cityLocateText="��λʧ��";
            }
        }
    };
    protected void onDestroy() {
        super.onDestroy();
        destroyLocation();
    }

    private void destroyLocation(){
        if (null != mLocationClient) {
            /**
             * ���AMapLocationClient���ڵ�ǰActivityʵ�����ģ�
             * ��Activity��onDestroy��һ��Ҫִ��AMapLocationClient��onDestroy
             */
            mLocationClient.onDestroy();
            mLocationClient = null;

        }
    }
}