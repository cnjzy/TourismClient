package com.tourism.app.vo;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

public class BaseVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@DatabaseField(generatedId = true)
	private int local_id;

	public int getLocal_id() {
		return local_id;
	}

	public void setLocal_id(int local_id) {
		this.local_id = local_id;
	}
}
