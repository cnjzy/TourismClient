package com.tourism.app.vo;

/**
 * Created by Jzy on 16/5/9.
 */

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.List;

/**
 * 游记地点节点
 */
@DatabaseTable(tableName = "guides_locations")
public class GuidesLocationVO extends BaseDBVO {
    /**
     * 地理位置名称
     */
    @DatabaseField
    private String location_name;
    @DatabaseField
    private int rank;

    private String location_name_en;
    private int type;

    // 游记内容集合
    private List<GuidesNotedVO> notes;

    private String day;
    private String dateStr;
    private boolean isDate;


    public GuidesLocationVO() {
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
        if (notes == null)
            notes = new ArrayList<GuidesNotedVO>();
        return notes;
    }

    public void setNotes(List<GuidesNotedVO> notes) {
        this.notes = notes;
    }

    public String getLocation_name_en() {
        return location_name_en;
    }

    public void setLocation_name_en(String location_name_en) {
        this.location_name_en = location_name_en;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public boolean isDate() {
        return isDate;
    }

    public void setIsDate(boolean isDate) {
        this.isDate = isDate;
    }
}
