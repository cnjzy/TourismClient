package com.tourism.app.common;

public class Constants {

    /************************************ Common ******************************/
    // 版本信息
    public static final String VERSION_NAME = "远行家V";
    /**
     * 民瑞PC：http://10.8.9.140:8080
     * 测试：http://newapi1.etaoshi.com
     * 线上：http://newapi.etaoshi.com
     */
    public static final String BASE_URL = "http://api.yxj.76iw.com/";
    public static final String PLATFORM = "Android";
    public static final String CHANNEL = "yxj";

    /************************************ Net ******************************/
    public static boolean isGzip = true;
    // 链接次数
    public static final int CONNECTION_COUNT = 3;
    // 链接失败提示
    public static final String ERROR_MESSAGE = "请求失败，请重试";
    // 链接失败提示
    public static final String ERROR_NO_NETWORK_MESSAGE = "网络异常，请检查网络连接是否正常";
    // 加载提示
    public static String LOADING_CONTENTS = "加载中，请稍候...";
    // 链接超时设置
    public static final int CONNECTION_SHORT_TIMEOUT = 10000;// 连接超时 5s
    public static final int READ_SHORT_TIMEOUT = 10000;// 连接超时 5s
    // 网络请求方式
    public static final String HTTP_POST = "POST";
    public static final String HTTP_GET = "GET";
    public static final String HTTP_FILE = "FILE";

    /******************************** Location *******************************/
    public static final int LOCATION_SCAN_SPAN = 30 * 1000;

    /******************************** API ************************************/
    // 登陆
    public static final String URL_ACCOUNT_LOGIN = BASE_URL + "v1/user/login";
    
    // 注册
    public static final String URL_ACCOUNT_REGISTER = BASE_URL + "v1/user/regist";

    // 发送手机验证码
    public static final String URL_GET_SMS_AUTH_CODE = BASE_URL + "v1/user/regCheckCode";

    // 获取用户信息
    public static final String URL_GET_USER_INFO = BASE_URL + "v1/user/getUserInfo";
    
    // 修改用户信息
    public static final String URL_SET_USER_INFO = BASE_URL + "v1/user/setUserInfo";
    
    // 用户认证
    public static final String URL_USER_REVIEW = BASE_URL + "v1/user/setUserReview";
    
    // 图片上传
    public static final String URL_UPLOAD_FILE = BASE_URL + "v1/index/uploadfile";
    
    // 服务首页
    public static final String URL_NEWS_LIST = BASE_URL + "v1/service/lists";
    
    // 服务首页
    public static final String URL_WEATHER_LIST = BASE_URL + "v1/service/tripinfos";
    
    // 服务首页
    public static final String URL_BRAND_LIST = BASE_URL + "v1/brand/lists";
    
    // 服务详情
    public static final String URL_BRAND_INFO = BASE_URL + "/v1/brand/details";
    
    // 在线咨询
    public static final String URL_BRAND_FEEDBACK = BASE_URL + "v1/brand/consulting";
    
    // 首页接口
    public static final String URL_HOME = BASE_URL + "v1/friend";
    
    // 预约
    public static final String URL_CATEGORY_MAKE = BASE_URL + "v1/friend/activityReserve";
    
    // 报名
    public static final String URL_CATEGORY_SIGNED_UP = BASE_URL + "v1/friend/activityApply";
}
