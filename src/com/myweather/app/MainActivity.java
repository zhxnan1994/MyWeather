package com.myweather.app;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{
	TodayWeather todayWeather = null;
	private static final String CITYCODE = "101170101";
	private ImageView updateBtn,cityManager;
	private TextView cityT,timeT,humidityT,weekT,pmDataT,pmQualityT,temperatureT,
	climateT,windT,cityNameT;
	private ImageView weatherImg,PM25Img;
	private String updateCityCode;
	private boolean updateCityMessage=false;//判断是否要更新城市信息
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
		initView();
		if(NetCheck.getNetState(this)==NetCheck.NET_NONE){
			Toast.makeText(this, "network offline", Toast.LENGTH_SHORT).show();
			Log.d("MainActivity","network offline");
		}else{
			//Toast.makeText(this, "network online", Toast.LENGTH_SHORT).show();
			Log.d("MainActivity","network ok");
		}
		updateCityCode=getIntent().getStringExtra("city_code");
		if(updateCityCode!="-1")
		{
			getWeatherDatafromNet(updateCityCode);
			updateCityMessage=true;
		}
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
                        Log.d("date from url",str);  
                    }  
                    String response = sb.toString();  
                    Log.d("response",response);  
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
				getWeatherDatafromNet(CITYCODE);
			/*updateBtn.setImageResource(R.drawable.title_city_update_2);
			if(!updateBtn.isPressed())
				updateBtn.setImageResource(R.drawable.title_city_update);*/
			break;
		case R.id.title_city_manager:
			Intent intent=new Intent(this,SelectCity.class);
			startActivity(intent);
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
            Log.d("MWeater","start parse xml");  
  
            while(eventType!=xmlPullParser.END_DOCUMENT)  
            {  
                switch (eventType)  
                {  
                    //文档开始位置  
                    case XmlPullParser.START_DOCUMENT:  
                        Log.d("parse","start doc");  
                        break;  
                    //标签元素开始位置  
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
        cityNameT.setText(todayWeather.getCity()+"天气");  
        cityT.setText(todayWeather.getCity());  
        timeT.setText("发布时间: "+todayWeather.getUpdatetime());  
        humidityT.setText("湿度:"+todayWeather.getShidu());  
        pmDataT.setText(todayWeather.getPm25());  
        pmQualityT.setText(todayWeather.getQuality());  
        weekT.setText(todayWeather.getDate());
        temperatureT.setText(todayWeather.getHigh()+"~"+todayWeather.getLow());  
        climateT.setText(todayWeather.getType());  
        windT.setText("风力:"+todayWeather.getFengli());  
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
                case "晴":  
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_qing);  
                    break;  
                case "阴":  
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_yin);  
                    break;  
                case "雾":  
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_wu);  
                    break;  
                case "多云":  
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_duoyun);  
                    break;  
                case "小雨":  
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_xiaoyu);  
                    break;  
                case "中雨":  
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhongyu);  
                    break;  
                case "大雨":  
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_dayu);  
                    break;  
                case "阵雨":  
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhenyu);  
                    break;  
                case "雷阵雨":  
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_leizhenyu);  
                    break;  
                case "雷阵雨加暴":  
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_leizhenyubingbao);  
                    break;  
                case "暴雨":  
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_baoyu);  
                    break;  
                case "大暴雨":  
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_dabaoyu);  
                    break;  
                case "特大暴雨":  
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_tedabaoyu);  
                    break;  
                case "阵雪":  
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhenxue);  
                    break;  
                case "暴雪":  
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_baoxue);  
                    break;  
                case "大雪":  
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_daxue);  
                    break;  
                case "小雪":  
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_xiaoxue);  
                    break;  
                case "雨夹雪":  
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_yujiaxue);  
                    break;  
                case "中雪":  
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhongxue);  
                    break;  
                case "沙尘暴":  
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_shachenbao);  
                    break;  
                default:  
                    break;  
            }  
        }  
        //Toast.makeText(MainActivity.this,"更新成功",Toast.LENGTH_SHORT).show();  
    } 
	
}