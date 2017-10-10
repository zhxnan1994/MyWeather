package com.myweather.app;
import java.util.StringTokenizer;

public class TodayWeather {
	public String getDate1() {
		return date1;
	}
	public void setDate1(String date1) {
		this.date1 = date1;
	}
	public String getLow1() {
		return low1;
	}
	public void setLow1(String low1) {
		this.low1 = low1.substring(low1.length()-3, low1.length());
	}
	public String getHigh1() {
		return high1;
	}
	public void setHigh1(String high1) {
		this.high1 = high1.substring(high1.length()-3, high1.length());
	}
	public String getType1() {
		return type1;
	}
	public void setType1(String type1) {
		this.type1 = type1;
	}
	public String getFengxiang1() {
		return fengxiang1;
	}
	public void setFengxiang1(String fengxiang1) {
		this.fengxiang1 = fengxiang1;
	}
	public String getDate2() {
		return date2;
	}
	public void setDate2(String date2) {
		this.date2 = date2;
	}
	public String getLow2() {
		return low2;
	}
	public void setLow2(String low2) {
		this.low2 = low2.substring(low2.length()-3, low2.length());
	}
	public String getHigh2() {
		return high2;
	}
	public void setHigh2(String high2) {
		this.high2 = high2.substring(high2.length()-3, high2.length());
	}
	public String getType2() {
		return type2;
	}
	public void setType2(String type2) {
		this.type2 = type2;
	}
	public String getFengxiang2() {
		return fengxiang2;
	}
	public void setFengxiang2(String fengxiang2) {
		this.fengxiang2 = fengxiang2;
	}
	public String getDate3() {
		return date3;
	}
	public void setDate3(String date3) {
		this.date3 = date3;
	}
	public String getLow3() {
		return low3;
	}
	public void setLow3(String low3) {
		this.low3 = low3.substring(low3.length()-3, low3.length());
	}
	public String getHigh3() {
		return high3;
	}
	public void setHigh3(String high3) {
		this.high3 = high3.substring(high3.length()-3, high3.length());
	}
	public String getType3() {
		return type3;
	}
	public void setType3(String type3) {
		this.type3 = type3;
	}
	public String getFengxiang3() {
		return fengxiang3;
	}
	public void setFengxiang3(String fengxiang3) {
		this.fengxiang3 = fengxiang3;
	}

	private String city;
	private String updatetime;
	private String wendu;
	private String shidu;
	private String pm25;
	private String quality;
	private String fengxiang;
	private String fengli;
	private String date;
	private String high;
	private String low;
	private String type;
	
	private String date1;
	private String low1;
	private String high1;
	private String type1;
	private String fengxiang1;
	
	private String date2;
	private String low2;
	private String high2;
	private String type2;
	private String fengxiang2;
	
	private String date3;
	private String low3;
	private String high3;
	private String type3;
	private String fengxiang3;
	
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}
	public String getWendu() {
		return wendu;
	}
	public void setWendu(String wendu) {
		this.wendu = wendu;
	}
	public String getShidu() {
		return shidu;
	}
	public void setShidu(String shidu) {
		this.shidu = shidu;
	}
	public String getPm25() {
		return pm25;
	}
	public void setPm25(String pm25) {
		this.pm25 = pm25;
	}
	public String getQuality() {
		return quality;
	}
	public void setQuality(String quality) {
		this.quality = quality;
	}
	public String getFengxiang() {
		return fengxiang;
	}
	public void setFengxiang(String fengxiang) {
		this.fengxiang = fengxiang;
	}
	public String getFengli() {
		return fengli;
	}
	public void setFengli(String fengli) {
		this.fengli = fengli;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
			StringBuilder weekday=new StringBuilder();
			weekday.append(date.substring(0, 2));
			weekday.append(",");
			weekday.append(date.substring(date.length()-3, date.length()));
			this.date =weekday.toString() ;
	}
	public String getHigh() {
		return high;
	}
	public void setHigh(String high) {
		this.high = high;
	}
	public String getLow() {
		return low;
	}
	public void setLow(String low) {
		this.low = low;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public String toString(){
		return "today-weather{"+
				"city="+city+"\""+
				"updatetime"+updatetime+"\""+
				"wendu"+wendu+"\""+
				"shidu"+shidu+"\""+
				"pm25"+pm25+"\""+
				"quality"+quality+"\""+
				"fengxiang"+fengxiang+"\""+
				"fengli"+fengli+"\""+
				"date"+date+"\""+
				"high"+high+"\""+
				"low"+low+"\""+
				"type"+type+"\""+
				"}";		
	}
	
}