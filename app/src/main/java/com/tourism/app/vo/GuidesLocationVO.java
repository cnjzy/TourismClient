package com.tourism.app.vo;

/**
 * Created by Jzy on 16/5/9.
 */

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

/**
 * 游记地点节点
 */
@DatabaseTable(tableName = "guides_locations")
public class GuidesLocationVO extends BaseVO {
    @DatabaseField
    private String server_id = "0";
    @DatabaseField
    private String location_name;
    @DatabaseField
    int rank;
    @DatabaseField
    private boolean is_upload;
    @DatabaseField
    private String parent_id;
    // 游记内容集合
    private List<GuidesNotedVO> notes;

    public GuidesLocationVO(){}


    public String getServer_id() {
        return server_id;
    }

    public void setServer_id(String server_id) {
        this.server_id = server_id;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public List<GuidesNotedVO> getNotes() {
        return notes;
    }

    public void setNotes(List<GuidesNotedVO> notes) {
        this.notes = notes;
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
