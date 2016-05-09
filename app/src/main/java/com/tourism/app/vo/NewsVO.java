package com.tourism.app.vo;

import java.util.ArrayList;
import java.util.List;

public class NewsVO extends BaseVO{
	
	private List<NewsVO> banner;
	private NewsVO stick;
	private List<NewsVO> list;
	
	private String title;
	private String litpic;
	private int type;
	private String link;
	public List<NewsVO> getBanner() {
		if(banner == null)
			banner = new ArrayList<NewsVO>();
		return banner;
	}
	public void setBanner(List<NewsVO> banner) {
		this.banner = banner;
	}
	public NewsVO getStick() {
		return stick;
	}
	public void setStick(NewsVO stick) {
		this.stick = stick;
	}
	public List<NewsVO> getList() {
		if(list == null)
			list = new ArrayList<NewsVO>();
		return list;
	}
	public void setList(List<NewsVO> list) {
		this.list = list;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLitpic() {
		return litpic;
	}
	public void setLitpic(String litpic) {
		this.litpic = litpic;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
}
