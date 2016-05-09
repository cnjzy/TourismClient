package com.tourism.app.vo;

/**
 * Created by Jzy on 16/5/9.
 */

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 游记内容节点
 */
@DatabaseTable(tableName = "guides_noteds")
public class GuidesNotedVO extends BaseVO {
    @DatabaseField
    private String server_id;
    @DatabaseField
    private int rank;
    @DatabaseField
    private String type;
    @DatabaseField
    private String description;
    @DatabaseField
    private boolean is_upload;
    @DatabaseField
    private String parent_id;

    private GuidesImageVO imgVO;

    public GuidesNotedVO(){}

    public String getServer_id() {
        return server_id;
    }

    public void setServer_id(String server_id) {
        this.server_id = server_id;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
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

    public GuidesImageVO getImgVO() {
        return imgVO;
    }

    public void setImgVO(GuidesImageVO imgVO) {
        this.imgVO = imgVO;
    }
}
