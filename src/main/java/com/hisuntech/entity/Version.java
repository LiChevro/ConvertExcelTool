package com.hisuntech.entity;

import java.util.Date;

/**
 * @Description 版本更新说明
 */
public class Version {

    private String update;                    //更新时间
    private String versionNumber;           //版本号
    private String updateAuthor;            //更新者
    private String updateDescription;       //更新描述

    public Version() {
    }

    public Version(String update, String versionNumber, String updateAuthor, String updateDescription) {
        this.update = update;
        this.versionNumber = versionNumber;
        this.updateAuthor = updateAuthor;
        this.updateDescription = updateDescription;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getUpdateAuthor() {
        return updateAuthor;
    }

    public void setUpdateAuthor(String updateAuthor) {
        this.updateAuthor = updateAuthor;
    }

    public String getUpdateDescription() {
        return updateDescription;
    }

    public void setUpdateDescription(String updateDescription) {
        this.updateDescription = updateDescription;
    }
}
