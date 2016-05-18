package com.tourism.app.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jzy on 16/5/10.
 */
public class StrategyVO extends BaseVO{

    private int id;
    private String name;
    private String photos_count;
    private String days_count;
    private String views_count;
    private String comments_count;
    private String likes_count;
    private int favorites_count;
    private String source;
    private String start_date = "";
    private String end_date;
    private String user_avatar;
    private String user_id;
    private int user_type;
    private String user_sex;
    private String user_nickname;
    private int user_trips_count;
    private int user_activitys_count;
    private String litpic;
    private String link;

    private List<StrategyVO> trip_days;
    private String trip_date;
    private String day;

    private List<StrategyVO> locations;
    private String location_name;
    private List<StrategyVO> notes;

    private String type;
    private String description;
    private StrategyVO photo;
    private int width;
    private int height;
    private String size;
    private int like_count;
    private String url;

    private boolean isDate = false;
    private int index = 0;

    private int current_user_favorite;
    private List<Integer> current_user_like_photos;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotos_count() {
        return photos_count;
    }

    public void setPhotos_count(String photos_count) {
        this.photos_count = photos_count;
    }

    public String getDays_count() {
        return days_count;
    }

    public void setDays_count(String days_count) {
        this.days_count = days_count;
    }

    public String getViews_count() {
        return views_count;
    }

    public void setViews_count(String views_count) {
        this.views_count = views_count;
    }

    public String getComments_count() {
        return comments_count;
    }

    public void setComments_count(String comments_count) {
        this.comments_count = comments_count;
    }

    public String getLikes_count() {
        return likes_count;
    }

    public void setLikes_count(String likes_count) {
        this.likes_count = likes_count;
    }

    public int getFavorites_count() {
        return favorites_count;
    }

    public void setFavorites_count(int favorites_count) {
        this.favorites_count = favorites_count;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getUser_avatar() {
        return user_avatar;
    }

    public void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    public String getUser_sex() {
        return user_sex;
    }

    public void setUser_sex(String user_sex) {
        this.user_sex = user_sex;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public String getLitpic() {
        return litpic;
    }

    public void setLitpic(String litpic) {
        this.litpic = litpic;
    }

    public List<StrategyVO> getTrip_days() {
        return trip_days;
    }

    public void setTrip_days(List<StrategyVO> trip_days) {
        this.trip_days = trip_days;
    }

    public String getTrip_date() {
        return trip_date;
    }

    public void setTrip_date(String trip_date) {
        this.trip_date = trip_date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public List<StrategyVO> getLocations() {
        return locations;
    }

    public void setLocations(List<StrategyVO> locations) {
        this.locations = locations;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public List<StrategyVO> getNotes() {
        return notes;
    }

    public void setNotes(List<StrategyVO> notes) {
        this.notes = notes;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public StrategyVO getPhoto() {
        return photo;
    }

    public void setPhoto(StrategyVO photo) {
        this.photo = photo;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getLike_count() {
        return like_count;
    }

    public void setLike_count(int like_count) {
        this.like_count = like_count;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isDate() {
        return isDate;
    }

    public void setIsDate(boolean isDate) {
        this.isDate = isDate;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getUser_trips_count() {
        return user_trips_count;
    }

    public void setUser_trips_count(int user_trips_count) {
        this.user_trips_count = user_trips_count;
    }

    public int getUser_activitys_count() {
        return user_activitys_count;
    }

    public void setUser_activitys_count(int user_activitys_count) {
        this.user_activitys_count = user_activitys_count;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getCurrent_user_favorite() {
        return current_user_favorite;
    }

    public void setCurrent_user_favorite(int current_user_favorite) {
        this.current_user_favorite = current_user_favorite;
    }

    public List<Integer> getCurrent_user_like_photos() {
        if (current_user_like_photos == null)
            current_user_like_photos = new ArrayList<Integer>();
        return current_user_like_photos;
    }

    public void setCurrent_user_like_photos(List<Integer> current_user_like_photos) {
        this.current_user_like_photos = current_user_like_photos;
    }
}
