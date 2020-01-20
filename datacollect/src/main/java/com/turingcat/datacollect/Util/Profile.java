package com.turingcat.datacollect.Util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Profile {

    /**
     * 情景ID
     */
    private int profileID;
    /**
     * 情景名称
     */
    private String profileName;
    /**
     * 中控ID
     */
    private int ctrolID;
    /**
     * 房间ID
     */
    private int roomID;
    /**
     * 房间类型
     */
    private int roomType;
    /**
     * 情景模板ID 1 睡眠模式 2 观影模式 3 离家模式 4 居家模式 5 手动模式
     */
    private int profileTemplateID;
    /**
     * 1表示关联 0表示 未定义 -1表示未关联
     */
    private int profileSetID;
    /**
     * 切换标记，0：单独切换 1：全家切换
     */
    private int switchFlag = 0;
    /**
     * 切换时间
     */
    private Date switchTime;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date modifyTime;

//    private List<Factor> factorList;

    public Profile() {

    }

    public Profile(JSONObject profileJson) {
        this.profileID = profileJson.getIntValue("profileID");
        this.profileName = (String) profileJson.getOrDefault("profileName", "");
        this.ctrolID = profileJson.getIntValue("ctrolID");
        this.roomID = profileJson.getIntValue("roomID");
        this.roomType = profileJson.getIntValue("roomType");
        this.profileTemplateID = profileJson.getIntValue("profileTemplateID");
        this.profileSetID = profileJson.getIntValue("profileSetID");
        this.switchFlag = (int) profileJson.getOrDefault("switchFlag", 0);
        this.createTime = profileJson.getDate("createTime");
        this.modifyTime = profileJson.getDate("modifyTime");
        this.switchTime = profileJson.getDate("switchTime");
        if (this.switchTime == null) {
            this.switchTime = new Date();
        }
        String factorListstr = profileJson.getString("factorList");
//        if (factorListstr != null) {
//            this.factorList = JSON.parseObject(factorListstr, new TypeReference<List<Factor>>() {
//            });
//        }
    }

    /**
     * @return the profileID
     */
    public int getProfileID() {
        return profileID;
    }

    /**
     * @param profileID the profileID to set
     */
    public void setProfileID(int profileID) {
        this.profileID = profileID;
    }

    /**
     * @return the profileName
     */
    public String getProfileName() {
        return profileName;
    }

    /**
     * @param profileName the profileName to set
     */
    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    /**
     * @return the ctrolID
     */
    public int getCtrolID() {
        return ctrolID;
    }

    /**
     * @param ctrolID the ctrolID to set
     */
    public void setCtrolID(int ctrolID) {
        this.ctrolID = ctrolID;
    }

    /**
     * @return the roomID
     */
    public int getRoomID() {
        return roomID;
    }

    /**
     * @param roomID the roomID to set
     */
    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    /**
     * @return the roomType
     */
    public int getRoomType() {
        return roomType;
    }

    /**
     * @param roomType the roomType to set
     */
    public void setRoomType(int roomType) {
        this.roomType = roomType;
    }

    /**
     * @return the profileTemplateID
     */
    public int getProfileTemplateID() {
        return profileTemplateID;
    }

    /**
     * @param profileTemplateID the profileTemplateID to set
     */
    public void setProfileTemplateID(int profileTemplateID) {
        this.profileTemplateID = profileTemplateID;
    }

    /**
     * @return the profileSetID
     */
    public int getProfileSetID() {
        return profileSetID;
    }

    /**
     * @param profileSetID the profileSetID to set
     */
    public void setProfileSetID(int profileSetID) {
        this.profileSetID = profileSetID;
    }

    /**
     * @return the switchFlag
     */
    public int getSwitchFlag() {
        return switchFlag;
    }

    /**
     * @param switchFlag the switchFlag to set
     */
    public void setSwitchFlag(int switchFlag) {
        this.switchFlag = switchFlag;
    }

    /**
     * @return the switchTime
     */
    public Date getSwitchTime() {
        return switchTime;
    }

    /**
     * @param switchTime the switchTime to set
     */
    public void setSwitchTime(Date switchTime) {
        this.switchTime = switchTime;
    }

    /**
     * @return the createTime
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime the createTime to set
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return the modifyTime
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * @param modifyTime the modifyTime to set
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * 将对象转成JSON
     *
     * @return
     */
    public JSONObject toJsonObj() {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JSONObject profileJson = new JSONObject();
        profileJson.put("profileID", this.profileID);
        profileJson.put("profileName", this.profileName);
        profileJson.put("ctrolID", this.ctrolID);
        profileJson.put("roomID", this.getRoomID());
        profileJson.put("roomType", this.getRoomType());
        profileJson.put("profileTemplateID", getProfileTemplateID());
        profileJson.put("profileSetID", this.profileSetID);
        profileJson.put("switchFlag", this.switchFlag);
        profileJson.put("createTime", sdf.format(getCreateTime()));
        profileJson.put("modifyTime", sdf.format(getModifyTime()));
        if (this.switchTime != null) {
            profileJson.put("switchTime", sdf.format(this.switchTime));
        }
//        profileJson.put("factorList",this.factorList);
        return profileJson;
    }

//    public List<Factor> getFactorList() {
//        return factorList;
//    }
//
//    public void setFactorList(List<Factor> factorList) {
//        this.factorList = factorList;
//    }
}
