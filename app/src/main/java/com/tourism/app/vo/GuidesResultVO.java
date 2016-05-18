package com.tourism.app.vo;

/**
 * Created by Jzy on 16/5/16.
 */
public class GuidesResultVO extends BaseVO {

    private int local_id;
    private int server_id;
    private String type;
    private String note_type;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNote_type() {
        return note_type;
    }

    public void setNote_type(String note_type) {
        this.note_type = note_type;
    }
}
