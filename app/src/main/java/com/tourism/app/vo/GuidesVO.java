package com.tourism.app.vo;

import java.util.List;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Jzy on 16/5/9.
 */
@DatabaseTable(tableName = "guides")
public class GuidesVO extends BaseVO {


    @DatabaseField
    private String server_id;
    @DatabaseField
    private String name;
    @DatabaseField
    private int photos_count;
    @DatabaseField
    private String start_date;
    @DatabaseField
    private String end_date;
    @DatabaseField
    private int privacy;
    @DatabaseField
    private int front_cover_photo_id;
    @DatabaseField
    private int friend_category_id;
    @DatabaseField
    private boolean isUpload;

    public GuidesVO(){}

    // 游记天集合
    private List<GuidesDayVO> trip_days;

    public String getServer_id() {
        return server_id;
    }

    public void setServer_id(String server_id) {
        this.server_id = server_id;
    }

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
        return trip_days;
    }

    public void setTrip_days(List<GuidesDayVO> trip_days) {
        this.trip_days = trip_days;
    }

    public boolean isUpload() {
        return isUpload;
    }

    public void setIsUpload(boolean isUpload) {
        this.isUpload = isUpload;
    }








}
