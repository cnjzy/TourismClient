package com.tourism.app.vo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jzy on 16/5/9.
 */
@DatabaseTable(tableName = "guides")
public class GuidesVO extends BaseDBVO {


    @DatabaseField
    private String name;
    @DatabaseField
    private int photos_count;
    @DatabaseField
    private int already_photos_count;
    @DatabaseField
    private String start_date;
    @DatabaseField
    private String end_date;
    @DatabaseField
    private int privacy;
    @DatabaseField
    private int front_cover_photo_id = 0;
    @DatabaseField
    private int friend_category_id;
    @DatabaseField
    private String friend_category_name;
    @DatabaseField
    private String type;


    private GuidesNotedVO photoVO;


    public GuidesVO() {
    }

    // 游记天集合
    private List<GuidesDayVO> trip_days;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPhotos_count() {
        return photos_count;
    }

    public void setPhotos_count(int photos_count) {
        this.photos_count = photos_count;
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

    public int getPrivacy() {
        return privacy;
    }

    public void setPrivacy(int privacy) {
        this.privacy = privacy;
    }

    public int getFront_cover_photo_id() {
        return front_cover_photo_id;
    }

    public void setFront_cover_photo_id(int front_cover_photo_id) {
        this.front_cover_photo_id = front_cover_photo_id;
    }

    public int getFriend_category_id() {
        return friend_category_id;
    }

    public void setFriend_category_id(int friend_category_id) {
        this.friend_category_id = friend_category_id;
    }

    public List<GuidesDayVO> getTrip_days() {
        if (trip_days == null) {
            trip_days = new ArrayList<GuidesDayVO>();
        }
        return trip_days;
    }

    public void setTrip_days(List<GuidesDayVO> trip_days) {
        this.trip_days = trip_days;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public GuidesNotedVO getPhotoVO() {
        return photoVO;
    }

    public void setPhotoVO(GuidesNotedVO photoVO) {
        this.photoVO = photoVO;
    }

    public String getFriend_category_name() {
        return friend_category_name;
    }

    public void setFriend_category_name(String friend_category_name) {
        this.friend_category_name = friend_category_name;
    }

    public int getAlready_photos_count() {
        return already_photos_count;
    }

    public void setAlready_photos_count(int already_photos_count) {
        this.already_photos_count = already_photos_count;
    }
}
