package com.tourism.app.vo;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Jzy on 16/5/12.
 */
public class BaseDBVO extends BaseVO {

    /**
     * 本地主键
     */
    @DatabaseField(generatedId = true)
    private int local_id;
    /**
     * 服务器主键
     */
    @DatabaseField
    private int server_id = 0;
    /**
     * 是否上传
     * 0 未上传过
     * 1 正在上传
     * 2 上传成功
     */
    @DatabaseField(defaultValue = "0")
    private int is_upload;
    /**
     * 基类ID
     */
    @DatabaseField
    private int parent_id;

    /**
     * 是否删除
     */
    @DatabaseField(defaultValue = "0")
    private int is_delete;

    public int getLocal_id() {
        return local_id;
    }

    public void setLocal_id(int local_id) {
        this.local_id = local_id;
    }

    public int getServer_id() {
        return server_id;
    }

    public void setServer_id(int server_id) {
        this.server_id = server_id;
    }

    public int is_upload() {
        return is_upload;
    }

    public void setIs_upload(int is_upload) {
        this.is_upload = is_upload;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public int getIs_delete() {
        return is_delete;
    }

    public void setIs_delete(int is_delete) {
        this.is_delete = is_delete;
    }
}
