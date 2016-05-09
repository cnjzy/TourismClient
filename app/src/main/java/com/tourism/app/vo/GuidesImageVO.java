package com.tourism.app.vo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Jzy on 16/5/9.
 */
@DatabaseTable(tableName = "guides_images")
public class GuidesImageVO extends BaseVO {
    @DatabaseField
    private String name;
    @DatabaseField
    private String server_id;
    @DatabaseField
    private String path;
    @DatabaseField
    private boolean is_upload;
    @DatabaseField
    private String parent_id;

    public GuidesImageVO(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServer_id() {
        return server_id;
    }

    public void setServer_id(String server_id) {
        this.server_id = server_id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public boolean is_upload() {
        return is_upload;
    }

    public void setIs_upload(boolean is_upload) {
        this.is_upload = is_upload;
    }
}
