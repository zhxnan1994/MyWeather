package com.myweather.app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class Welcome extends Activity {
	
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcom);
		shipToGuideOrMain();
	}

	private void shipToGuideOrMain() {
		boolean firstFlag;
        SharedPreferences sharedPreferences = getSharedPreferences("flag", MODE_PRIVATE);  
        firstFlag = sharedPreferences.getBoolean("first", true);  
  
        final Intent intent = new Intent();  
        if (firstFlag){  
            intent.setClass(this,Guide.class);  
            SharedPreferences.Editor editor = sharedPreferences.edit();  
            editor.putBoolean("first", false);
            editor.apply();
        }else {  
            intent.setClass(this, MainActivity.class);  
        }  
        new Handler().postDelayed(new Runnable() { //��ʱ1.5��  
            @Override  
            public void run() {  
                startActivity(intent);  
                Welcome.this.finish();  
            }  
        },1500);  
		
	}
}
