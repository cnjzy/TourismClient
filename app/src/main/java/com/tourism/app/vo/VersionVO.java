package com.tourism.app.vo;

/**
 * Created by Jzy on 16/6/6.
 */
public class VersionVO extends BaseVO {
    private String version;
    private String url;
    private String content;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
