/** 
 * Project Name:TourismAppClient 
 * File Name:WeatherVO.java 
 * Package Name:com.tourism.app.vo 
 * Date:2016年4月19日下午6:03:57 
 * Copyright (c) 2016, chenzhou1025@126.com All Rights Reserved. 
 * 
*/  
  
package com.tourism.app.vo;  

import java.util.List;

/** 
 * ClassName:WeatherVO 
 * Date:     2016年4月19日 下午6:03:57
 * @author   Jzy 
 * @version   
 * @see       
 */
public class WeatherVO extends BaseVO{

	private WeatherVO weather;
	private int code;
	private String msg;
	private WeatherVO data;
	private String city;
	private String cond;
	private String tmp;
	private String qlty;
	private String pm25;
	
	private WeatherVO vehiclelimit;
	private List<String> number;
	private String source;
	public WeatherVO getWeather() {
		return weather;
	}
	public void setWeather(WeatherVO weather) {
		this.weather = weather;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public WeatherVO getData() {
		return data;
	}
	public void setData(WeatherVO data) {
		this.data = data;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCond() {
		return cond;
	}
	public void setCond(String cond) {
		this.cond = cond;
	}
	public String getTmp() {
		return tmp;
	}
	public void setTmp(String tmp) {
		this.tmp = tmp;
	}
	public String getQlty() {
		return qlty;
	}
	public void setQlty(String qlty) {
		this.qlty = qlty;
	}
	public String getPm25() {
		return pm25;
	}
	public void setPm25(String pm25) {
		this.pm25 = pm25;
	}
	public WeatherVO getVehiclelimit() {
		return vehiclelimit;
	}
	public void setVehiclelimit(WeatherVO vehiclelimit) {
		this.vehiclelimit = vehiclelimit;
	}
	public List<String> getNumber() {
		return number;
	}
	public void setNumber(List<String> number) {
		this.number = number;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	
	
}
  