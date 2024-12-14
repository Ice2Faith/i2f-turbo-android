package com.ugex.savelar.hikaridatasourceutils;

import java.util.Date;

public class NoteT {
    private Integer id;
    private String title;
    private String viceTitle;
    private String headImg;
    private String attachFile;
    private String content;
    private Date createDate;
    private Date modifyDate;
    private Integer updateCount;
    private Integer viewCount;

    public NoteT(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getViceTitle() {
        return viceTitle;
    }

    public void setViceTitle(String viceTitle) {
        this.viceTitle = viceTitle;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getAttachFile() {
        return attachFile;
    }

    public void setAttachFile(String attachFile) {
        this.attachFile = attachFile;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Integer getUpdateCount() {
        return updateCount;
    }

    public void setUpdateCount(Integer updateCount) {
        this.updateCount = updateCount;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    @Override
    public String toString() {
        return "NoteT{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", viceTitle='" + viceTitle + '\'' +
                ", headImg='" + headImg + '\'' +
                ", attachFile='" + attachFile + '\'' +
                ", content='" + content + '\'' +
                ", createDate=" + createDate +
                ", modifyDate=" + modifyDate +
                ", updateCount=" + updateCount +
                ", viewCount=" + viewCount +
                '}';
    }
}
