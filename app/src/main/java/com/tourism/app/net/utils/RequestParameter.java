package com.tourism.app.net.utils;
 

public class RequestParameter  implements java.io.Serializable, Comparable {

	private static final long serialVersionUID = 1274906854152052510L;

	private String name;
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	private String value;
	
	public RequestParameter(String name,String value){
		this.name = name;
		this.value = value;
	}
	public RequestParameter(String name,int value){
		this.name = name;
		this.value = value+"";
	}
	
	public RequestParameter(String name,String value,boolean isFile){
		this.name = name;
		this.value = value;
		this.isFile=isFile;
	}
	public RequestParameter(String name,int value,boolean isFile){
		this.name = name;
		this.value = value+"";
		this.isFile=isFile;
	}
	
	private boolean isFile;
	
	public boolean isFile() {
		return isFile;
	}

	public void setFile(boolean isFile) {
		this.isFile = isFile;
	}

	public boolean equals(Object o) {
		if(null == o){
			return false;
		}
		
		if(this == o){
			return true;
		}
		
		if(o instanceof  RequestParameter){
			RequestParameter parameter = (RequestParameter)o;
			return name.equals(parameter.name)&&value.equals(parameter.value);
		}
		
		return false;
	}
	
	
	public int compareTo(Object another) {
		int compared;
		/**
		 * 值比较
		 */
		RequestParameter parameter = (RequestParameter)another;
		compared = name.compareTo(parameter.name);
		if(compared == 0){
			compared = value.compareTo(parameter.value);
		}
		return compared;
	}
}
