/**
 * 
 */
package com.tourism.app.vo;

/**
 * @ClassName: VersionUpdateVO 
 * @Description: TODO
 * @author sr
 * @date 2015-1-5 上午11:49:27 
 */
public class VersionUpdateVO extends BaseVO {

	private int last_version_code;
	private String last_version_name;
	private String download_url;
	private String version_desc;
	private String last_city_time;
	private boolean version_compulsory;
	
	
	public int getLast_version_code() {
		return last_version_code;
	}
	public void setLast_version_code(int last_version_code) {
		this.last_version_code = last_version_code;
	}
	public String getLast_version_name() {
		return last_version_name;
	}
	public void setLast_version_name(String last_version_name) {
		this.last_version_name = last_version_name;
	}
	public String getDownload_url() {
		return download_url;
	}
	public void setDownload_url(String download_url) {
		this.download_url = download_url;
	}
	public String getVersion_desc() {
		return version_desc;
	}
	public void setVersion_desc(String version_desc) {
		this.version_desc = version_desc;
	}
	public String getLast_city_time() {
		return last_city_time;
	}
	public void setLast_city_time(String last_city_time) {
		this.last_city_time = last_city_time;
	}
    public boolean isVersion_compulsory() {
        return version_compulsory;
    }
    public void setVersion_compulsory(boolean version_compulsory) {
        this.version_compulsory = version_compulsory;
    }
	
	
	
}
