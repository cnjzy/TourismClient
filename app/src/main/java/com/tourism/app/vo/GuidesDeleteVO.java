package com.tourism.app.vo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Jzy on 16/5/12.
 */
@DatabaseTable(tableName = "guides_delete")
public class GuidesDeleteVO extends BaseDBVO {
    @DatabaseField
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
