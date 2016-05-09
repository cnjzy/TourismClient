/** 
 * Project Name:TourismAppClient 
 * File Name:BrandInfoVO.java 
 * Package Name:com.tourism.app.vo 
 * Date:2016年4月20日上午11:29:18 
 * Copyright (c) 2016, chenzhou1025@126.com All Rights Reserved. 
 * 
*/  
  
package com.tourism.app.vo;  

import java.util.List;

/** 
 * ClassName:BrandInfoVO 
 * Date:     2016年4月20日 上午11:29:18
 * @author   Jzy 
 * @version   
 * @see       
 */
public class BrandInfoVO extends BaseVO{

	private int id;
	private String title;
	private String logo;
	private String index_url;
	private String tmall_url;
	private String home_url;
	private String wechat;
	private String hotline;
	private String dealers;
	private String maintain;
	private String testdrive;
	private String ad_pic;
	private String brand_pic;
	private List<String> product_pic;
	private String barcode_pic;
	private int status;
	private int rank;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getIndex_url() {
		return index_url;
	}
	public void setIndex_url(String index_url) {
		this.index_url = index_url;
	}
	public String getTmall_url() {
		return tmall_url;
	}
	public void setTmall_url(String tmall_url) {
		this.tmall_url = tmall_url;
	}
	public String getHome_url() {
		return home_url;
	}
	public void setHome_url(String home_url) {
		this.home_url = home_url;
	}
	public String getWechat() {
		return wechat;
	}
	public void setWechat(String wechat) {
		this.wechat = wechat;
	}
	public String getHotline() {
		return hotline;
	}
	public void setHotline(String hotline) {
		this.hotline = hotline;
	}
	public String getDealers() {
		return dealers;
	}
	public void setDealers(String dealers) {
		this.dealers = dealers;
	}
	public String getMaintain() {
		return maintain;
	}
	public void setMaintain(String maintain) {
		this.maintain = maintain;
	}
	public String getTestdrive() {
		return testdrive;
	}
	public void setTestdrive(String testdrive) {
		this.testdrive = testdrive;
	}
	public String getAd_pic() {
		return ad_pic;
	}
	public void setAd_pic(String ad_pic) {
		this.ad_pic = ad_pic;
	}
	public String getBrand_pic() {
		return brand_pic;
	}
	public void setBrand_pic(String brand_pic) {
		this.brand_pic = brand_pic;
	}
	public List<String> getProduct_pic() {
		return product_pic;
	}
	public void setProduct_pic(List<String> product_pic) {
		this.product_pic = product_pic;
	}
	public String getBarcode_pic() {
		return barcode_pic;
	}
	public void setBarcode_pic(String barcode_pic) {
		this.barcode_pic = barcode_pic;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
}
  