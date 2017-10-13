package com.myweather.app;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.*;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.support.v7.app.ActionBarActivity;

public class Guide extends Activity {
	 	private ViewPager vPager;  
	    private List<View> vList;
		private View v1,v2,v3;
	    private ImageView[] pointImgViews;
	    private ViewGroup pointGroup;
        private Button startBtn;
	    @Override  
	    protected void onCreate(Bundle savedInstanceState) {  
	        super.onCreate(savedInstanceState);  
	        setContentView(R.layout.activity_navigation);  
	        initView();  
	        setViewPager();  
	        addPoints();
	        setAdapterForViewPager();
			startBtn=(Button)findViewById(R.id.startAppBtn);

	    }  
	  
	    private void initView(){  
	        vPager = (ViewPager)findViewById(R.id.navigation_vp);  
	        pointGroup = (ViewGroup)findViewById(R.id.viewPoints);

            //getSupportActionBar().hide();
	    }  
	  
	    //ʵ�ֻ�������
	    private void setViewPager(){  
	        LayoutInflater inflater = getLayoutInflater();  
	        vList = new ArrayList<View>();  
			v1=inflater.inflate(R.layout.guide_01,null);
			v2=inflater.inflate(R.layout.guide_02,null);
			v3=inflater.inflate(R.layout.guide_03,null);
	        vList.add(v1);
	        vList.add(v2);
	        vList.add(v3);

	  
	        PagerAdapter myAdapter = new PagerAdapter() {  
	            @Override  
	            public int getCount() {  
	                return vList.size();  
	            }  
	  
	            @Override  
	            public boolean isViewFromObject(View view, Object o) {  
	                return view==o;  
	            }  
	  
	            @Override  
	            public void destroyItem(ViewGroup container, int position, Object object) {  
	                container.removeView(vList.get(position));  
	            }  
	  
	            @Override  
	            public Object instantiateItem(ViewGroup container, int position) {  
	                container.addView(vList.get(position));  
	                return vList.get(position);  
	            }  
	        };  
	  
	        vPager.setAdapter(myAdapter);

	    }  
	  
	    private void addPoints(){  
	        ImageView pointImgView;  
	        pointImgViews = new ImageView[vList.size()]; //ȷ��СԲ��ĸ���  
	  
	        //��̬���СԲ��  
	        for(int i=0; i<vList.size(); i++){  
	            pointImgView = new ImageView(Guide.this);  
	            pointImgView.setLayoutParams(new ViewGroup.LayoutParams(25,25)); //����Բ���С  
	            pointImgView.setPadding(5, 0, 5, 0);  
	            pointImgViews[i] = pointImgView;  
	  
	            // Ĭ��ѡ�е��ǵ�һ��ͼƬ����ʱ��һ��СԲ����ѡ��״̬����������  
	            if (i == 0)  
	                pointImgViews[i].setImageDrawable(getResources().getDrawable(  
	                        R.drawable.page_indicator_focused));  
	            else  
	                pointImgViews[i].setImageDrawable(getResources().getDrawable(  
	                        R.drawable.page_indicator_unfocused));  
	            // ��imageviews��ӵ�СԲ����ͼ��  
	            pointGroup.addView(pointImgViews[i]);  
	        }  
	    }  
	  
	    //��Ӽ�����������Ӧҳ���СԲ������Ϊѡ��״̬  
	    private void setAdapterForViewPager(){  
	        vPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
	            @Override  
	            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {  
	  
	            }  
	  
	            @Override  
	            public void onPageSelected(int position) {
	               for (int i = 0; i < pointImgViews.length; i++) {
	                    // ��ǰview������СԲ��Ϊδѡ��״̬  
	                    pointImgViews[i].setImageDrawable(getResources().getDrawable(  
	                            R.drawable.page_indicator_unfocused));  
	                    //����СԲ��Ϊѡ��״̬  
	                    if(position == i)  
	                    pointImgViews[i].setImageDrawable(getResources().getDrawable(  
	                            R.drawable.page_indicator_focused));  
	                }
	            }  
	  
	            @Override  
	            public void onPageScrollStateChanged(int state) {  
	  
	            }  
	        });  
	    }  
	  
	    //���button����ã���ת�������棬�������ò���  
	    public void shipToMain(View v){  
	        Intent intent = new Intent(this, MainActivity.class);  
	        startActivity(intent);  
	        this.finish();  
	    } 
}
