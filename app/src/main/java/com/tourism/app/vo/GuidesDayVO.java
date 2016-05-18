package com.tourism.app.vo;

/**
 * Created by Jzy on 16/5/9.
 */

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.List;

/**
 * 游记天节点
 */
@DatabaseTable(tableName = "guides_days")
public class GuidesDayVO extends BaseDBVO {
    @DatabaseField
    private String trip_date;

    // 游记地点集合
    private List<GuidesLocationVO> locations;

    public GuidesDayVO(){}

    public String getTrip_date() {
        return trip_date;
    }

    public void setTrip_date(String trip_date) {
        this.trip_date = trip_date;
    }


    public List<GuidesLocationVO> getLocations() {
        if (locations == null)
            locations = new ArrayList<GuidesLocationVO>();
        return locations;
    }

    public void setLocations(List<GuidesLocationVO> locations) {
        this.locations = locations;
    }
}
