/**
 * 
 */
package com.tourism.app.vo;

/**
 * @ClassName: UserInfoVO
 * @Description: TODO
 * @author sr
 * @date 2014-12-22 上午10:44:30
 */
public class UserInfoVO extends BaseVO {

	private int userId;
	private String userName;
	private String password;
	private String mobile;
	private String email;
	private String avatar;
	private String token;
	private int vip_status;
	private String vip_name;
	private String vip_code;

	private int trips_count;
	private int activitys_count;

	/**
	 * 
	 */
	private int mid;
	private int sex;
	private int status;
	private String review;
	private String type;
	private String nickname;
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public int getMid() {
		return mid;
	}
	public void setMid(int mid) {
		this.mid = mid;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getReview() {
		return review;
	}
	public void setReview(String review) {
		this.review = review;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getVip_status() {
		return vip_status;
	}

	public void setVip_status(int vip_status) {
		this.vip_status = vip_status;
	}

	public String getVip_name() {
		return vip_name;
	}

	public void setVip_name(String vip_name) {
		this.vip_name = vip_name;
	}

	public String getVip_code() {
		return vip_code;
	}

	public void setVip_code(String vip_code) {
		this.vip_code = vip_code;
	}

	public int getTrips_count() {
		return trips_count;
	}

	public void setTrips_count(int trips_count) {
		this.trips_count = trips_count;
	}

	public int getActivitys_count() {
		return activitys_count;
	}

	public void setActivitys_count(int activitys_count) {
		this.activitys_count = activitys_count;
	}
}
