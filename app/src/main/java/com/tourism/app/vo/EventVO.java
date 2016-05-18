/** 
 * Project Name:TourismAppClient 
 * File Name:EventVO.java 
 * Package Name:com.tourism.app.vo 
 * Date:2016年4月27日下午3:53:58 
 * Copyright (c) 2016, jzy0106@aliyun.com All Rights Reserved. 
 * 
*/  
  
package com.tourism.app.vo;  

import java.util.ArrayList;
import java.util.List;

/** 
 * ClassName:EventVO 
 * Date:     2016年4月27日 下午3:53:58
 * @author   Jzy 
 * @version   
 * @see       
 */
public class EventVO extends BaseVO{

	private int id;
	private String title;
	private int type;
	private String deposit;
	private String start_time = "";
	private String poster;
	private String user_id;
	private String user_avatar;
	private String user_nickname;
	private String user_sex;
	private int user_type;
	private String link;
	private String slogan;
	private String link_tel;
	private List<String> tags;
	private int pv;
	private int is_review;
	private int is_bind;
	private String url_web;
	private int current_user_favorite;
	private String amount;
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}
	/**
	 * @return the deposit
	 */
	public String getDeposit() {
		return deposit;
	}
	/**
	 * @param deposit the deposit to set
	 */
	public void setDeposit(String deposit) {
		this.deposit = deposit;
	}
	/**
	 * @return the start_time
	 */
	public String getStart_time() {
		return start_time;
	}
	/**
	 * @param start_time the start_time to set
	 */
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	/**
	 * @return the poster
	 */
	public String getPoster() {
		return poster;
	}
	/**
	 * @param poster the poster to set
	 */
	public void setPoster(String poster) {
		this.poster = poster;
	}
	/**
	 * @return the user_id
	 */
	public String getUser_id() {
		return user_id;
	}
	/**
	 * @param user_id the user_id to set
	 */
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	/**
	 * @return the user_avatar
	 */
	public String getUser_avatar() {
		return user_avatar;
	}
	/**
	 * @param user_avatar the user_avatar to set
	 */
	public void setUser_avatar(String user_avatar) {
		this.user_avatar = user_avatar;
	}
	/**
	 * @return the user_nickname
	 */
	public String getUser_nickname() {
		return user_nickname;
	}
	/**
	 * @param user_nickname the user_nickname to set
	 */
	public void setUser_nickname(String user_nickname) {
		this.user_nickname = user_nickname;
	}
	/**
	 * @return the user_sex
	 */
	public String getUser_sex() {
		return user_sex;
	}
	/**
	 * @param user_sex the user_sex to set
	 */
	public void setUser_sex(String user_sex) {
		this.user_sex = user_sex;
	}
	/**
	 * @return the user_type
	 */
	public int getUser_type() {
		return user_type;
	}
	/**
	 * @param user_type the user_type to set
	 */
	public void setUser_type(int user_type) {
		this.user_type = user_type;
	}
	/**
	 * @return the link
	 */
	public String getLink() {
		return link;
	}
	/**
	 * @param link the link to set
	 */
	public void setLink(String link) {
		this.link = link;
	}
	/**
	 * @return the slogan
	 */
	public String getSlogan() {
		return slogan;
	}
	/**
	 * @param slogan the slogan to set
	 */
	public void setSlogan(String slogan) {
		this.slogan = slogan;
	}
	/**
	 * @return the link_tel
	 */
	public String getLink_tel() {
		return link_tel;
	}
	/**
	 * @param link_tel the link_tel to set
	 */
	public void setLink_tel(String link_tel) {
		this.link_tel = link_tel;
	}
	/**
	 * @return the tags
	 */
	public List<String> getTags() {
		if(tags == null){
			tags = new ArrayList<String>();
		}
		return tags;
	}
	/**
	 * @param tags the tags to set
	 */
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	/**
	 * @return the pv
	 */
	public int getPv() {
		return pv;
	}
	/**
	 * @param pv the pv to set
	 */
	public void setPv(int pv) {
		this.pv = pv;
	}
	/**
	 * @return the is_review
	 */
	public int getIs_review() {
		return is_review;
	}
	/**
	 * @param is_review the is_review to set
	 */
	public void setIs_review(int is_review) {
		this.is_review = is_review;
	}
	/**
	 * @return the is_bind
	 */
	public int getIs_bind() {
		return is_bind;
	}
	/**
	 * @param is_bind the is_bind to set
	 */
	public void setIs_bind(int is_bind) {
		this.is_bind = is_bind;
	}
	/**
	 * @return the url_web
	 */
	public String getUrl_web() {
		return url_web;
	}
	/**
	 * @param url_web the url_web to set
	 */
	public void setUrl_web(String url_web) {
		this.url_web = url_web;
	}
	public int getCurrent_user_favorite() {
		return current_user_favorite;
	}
	public void setCurrent_user_favorite(int current_user_favorite) {
		this.current_user_favorite = current_user_favorite;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
}
  