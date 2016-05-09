package com.tourism.app.vo;

/**
 * Created by Jzy on 16/5/9.
 */

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

/**
 * 游记天节点
 */
@DatabaseTable(tableName = "guides_days")
public class GuidesDayVO extends BaseVO {
    @DatabaseField
    private String trip_date;
    @DatabaseField
    private String server_id = "0";
    @DatabaseField
    private boolean is_upload;
    @DatabaseField
    private String parent_id;

    // 游记地点集合
    private List<GuidesLocationVO> locations;

    public GuidesDayVO(){}

    public String getTrip_date() {
        return trip_date;
    }

    public void setTrip_date(String trip_date) {
        this.trip_date = trip_date;
    }

    public String getServer_id() {
        return server_id;
    }

    public void setServer_id(String server_id) {
        this.server_id = server_id;
    }

    public List<GuidesLocationVO> getLocations() {
        return locations;
    }

    public void setLocations(List<GuidesLocationVO> locations) {
        this.locations = locations;
    }

    public boolean is_upload() {
        return is_upload;
    }

    public void setIs_upload(boolean is_upload) {
        this.is_upload = is_upload;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }
}
