/** 
 * Project Name:TourismAppClient 
 * File Name:CategoryVO.java 
 * Package Name:com.tourism.app.vo 
 * Date:2016年4月27日上午10:08:48 
 * Copyright (c) 2016, chenzhou1025@126.com All Rights Reserved. 
 * 
*/  
  
package com.tourism.app.vo;  

import java.util.List;

/** 
 * ClassName:CategoryVO 
 * Date:     2016年4月27日 上午10:08:48
 * @author   Jzy 
 * @version   
 * @see       
 */
public class CategoryVO extends BaseVO{

	private List<NewsVO> banner;
	private List<Category> category;
	
	/**
	 * @return the banner
	 */
	public List<NewsVO> getBanner() {
		return banner;
	}



	/**
	 * @param banner the banner to set
	 */
	public void setBanner(List<NewsVO> banner) {
		this.banner = banner;
	}



	/**
	 * @return the category
	 */
	public List<Category> getCategory() {
		return category;
	}



	/**
	 * @param category the category to set
	 */
	public void setCategory(List<Category> category) {
		this.category = category;
	}



	public class Category extends BaseVO{
		private String name;
		private String litpic;
		private String banner;
		private String link_activity;
		private String link_strategy;
		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}
		/**
		 * @param name the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}
		/**
		 * @return the litpic
		 */
		public String getLitpic() {
			return litpic;
		}
		/**
		 * @param litpic the litpic to set
		 */
		public void setLitpic(String litpic) {
			this.litpic = litpic;
		}
		/**
		 * @return the banner
		 */
		public String getBanner() {
			return banner;
		}
		/**
		 * @param banner the banner to set
		 */
		public void setBanner(String banner) {
			this.banner = banner;
		}
		/**
		 * @return the link_activity
		 */
		public String getLink_activity() {
			return link_activity;
		}
		/**
		 * @param link_activity the link_activity to set
		 */
		public void setLink_activity(String link_activity) {
			this.link_activity = link_activity;
		}
		/**
		 * @return the link_strategy
		 */
		public String getLink_strategy() {
			return link_strategy;
		}
		/**
		 * @param link_strategy the link_strategy to set
		 */
		public void setLink_strategy(String link_strategy) {
			this.link_strategy = link_strategy;
		}
		
	}
}
  