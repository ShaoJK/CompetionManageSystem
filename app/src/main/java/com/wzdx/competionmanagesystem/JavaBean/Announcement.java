package com.wzdx.competionmanagesystem.JavaBean;

/**
 * Created by sjk on 2017/2/20.
 */

public class Announcement extends General{
    private int bulleID;
    private String bulle_title;
    private String source;
    private String content;
    private String adjunct;
    private int release_time;

    public int getBulleID() {
        return bulleID;
    }

    public void setBulleID(int bulleID) {
        this.bulleID = bulleID;
    }

    public String getBulle_title() {
        return bulle_title;
    }

    public void setBulle_title(String bulle_title) {
        this.bulle_title = bulle_title;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAdjunct() {
        return adjunct;
    }

    public void setAdjunct(String adjunct) {
        this.adjunct = adjunct;
    }

    public int getRelease_time() {
        return release_time;
    }

    public void setRelease_time(int release_time) {
        this.release_time = release_time;
    }
}
