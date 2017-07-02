package com.emobi.convoy.pojo.visitor;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by sunil on 06-04-2017.
 */

public class VisitorResultPOJO implements Serializable{
    @Override
    public String toString() {
        return "VisitorResultPOJO{" +
                "visId='" + visId + '\'' +
                ", visLoginUserId='" + visLoginUserId + '\'' +
                ", visUserId='" + visUserId + '\'' +
                ", visitorTime='" + visitorTime + '\'' +
                ", visitorDate='" + visitorDate + '\'' +
                ", logId='" + logId + '\'' +
                ", logName='" + logName + '\'' +
                ", logEmail='" + logEmail + '\'' +
                ", logGen='" + logGen + '\'' +
                ", logDob='" + logDob + '\'' +
                ", logPassword='" + logPassword + '\'' +
                ", logMob='" + logMob + '\'' +
                ", logFacbook='" + logFacbook + '\'' +
                ", logTwitter='" + logTwitter + '\'' +
                ", logBio='" + logBio + '\'' +
                ", logLoc='" + logLoc + '\'' +
                ", logTag='" + logTag + '\'' +
                ", logLastLogin='" + logLastLogin + '\'' +
                ", logCreated='" + logCreated + '\'' +
                ", logPics='" + logPics + '\'' +
                ", logRoom='" + logRoom + '\'' +
                ", logCoverPhoto='" + logCoverPhoto + '\'' +
                ", logDeviceToken='" + logDeviceToken + '\'' +
                ", logEntertainment='" + logEntertainment + '\'' +
                ", logSports='" + logSports + '\'' +
                ", logTravelling='" + logTravelling + '\'' +
                ", logStudy='" + logStudy + '\'' +
                ", logGaming='" + logGaming + '\'' +
                ", logTechnology='" + logTechnology + '\'' +
                ", logAction='" + logAction + '\'' +
                ", logEverything='" + logEverything + '\'' +
                ", logFamily='" + logFamily + '\'' +
                ", logAns1='" + logAns1 + '\'' +
                ", logAns2='" + logAns2 + '\'' +
                ", logAns3='" + logAns3 + '\'' +
                ", logAns4='" + logAns4 + '\'' +
                ", logAns5='" + logAns5 + '\'' +
                ", logMessage='" + logMessage + '\'' +
                ", logStatus='" + logStatus + '\'' +
                ", visibility='" + visibility + '\'' +
                ", logVideocallStatus='" + logVideocallStatus + '\'' +
                ", logOsType='" + logOsType + '\'' +
                ", logGeoLocation='" + logGeoLocation + '\'' +
                '}';
    }

    @SerializedName("vis_id")
    private String visId;
    @SerializedName("vis_login_user_id")
    private String visLoginUserId;
    @SerializedName("vis_user_id")
    private String visUserId;
    @SerializedName("visitor_time")
    private String visitorTime;
    @SerializedName("visitor_date")
    private String visitorDate;
    @SerializedName("log_id")
    private String logId;
    @SerializedName("log_name")
    private String logName;
    @SerializedName("log_email")
    private String logEmail;
    @SerializedName("log_gen")
    private String logGen;
    @SerializedName("log_dob")
    private String logDob;
    @SerializedName("log_password")
    private String logPassword;
    @SerializedName("log_mob")
    private String logMob;
    @SerializedName("log_facbook")
    private String logFacbook;
    @SerializedName("log_twitter")
    private String logTwitter;
    @SerializedName("log_bio")
    private String logBio;
    @SerializedName("log_loc")
    private String logLoc;
    @SerializedName("log_tag")
    private String logTag;
    @SerializedName("log_last_login")
    private String logLastLogin;
    @SerializedName("log_created")
    private String logCreated;
    @SerializedName("log_pics")
    private String logPics;
    @SerializedName("log_room")
    private String logRoom;
    @SerializedName("log_cover_photo")
    private String logCoverPhoto;
    @SerializedName("log_device_token")
    private String logDeviceToken;
    @SerializedName("log_entertainment")
    private String logEntertainment;
    @SerializedName("log_sports")
    private String logSports;
    @SerializedName("log_travelling")
    private String logTravelling;
    @SerializedName("log_study")
    private String logStudy;
    @SerializedName("log_gaming")
    private String logGaming;
    @SerializedName("log_technology")
    private String logTechnology;
    @SerializedName("log_action")
    private String logAction;
    @SerializedName("log_everything")
    private String logEverything;
    @SerializedName("log_family")
    private String logFamily;
    @SerializedName("log_ans1")
    private String logAns1;
    @SerializedName("log_ans2")
    private String logAns2;
    @SerializedName("log_ans3")
    private String logAns3;
    @SerializedName("log_ans4")
    private String logAns4;
    @SerializedName("log_ans5")
    private String logAns5;
    @SerializedName("log_message")
    private String logMessage;
    @SerializedName("log_status")
    private String logStatus;
    @SerializedName("visibility")
    private String visibility;
    @SerializedName("log_videocall_status")
    private String logVideocallStatus;
    @SerializedName("log_os_type")
    private String logOsType;
    @SerializedName("log_geo_location")
    private String logGeoLocation;

    public String getVisId() {
        return visId;
    }

    public void setVisId(String visId) {
        this.visId = visId;
    }

    public String getVisLoginUserId() {
        return visLoginUserId;
    }

    public void setVisLoginUserId(String visLoginUserId) {
        this.visLoginUserId = visLoginUserId;
    }

    public String getVisUserId() {
        return visUserId;
    }

    public void setVisUserId(String visUserId) {
        this.visUserId = visUserId;
    }

    public String getVisitorTime() {
        return visitorTime;
    }

    public void setVisitorTime(String visitorTime) {
        this.visitorTime = visitorTime;
    }

    public String getVisitorDate() {
        return visitorDate;
    }

    public void setVisitorDate(String visitorDate) {
        this.visitorDate = visitorDate;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getLogName() {
        return logName;
    }

    public void setLogName(String logName) {
        this.logName = logName;
    }

    public String getLogEmail() {
        return logEmail;
    }

    public void setLogEmail(String logEmail) {
        this.logEmail = logEmail;
    }

    public String getLogGen() {
        return logGen;
    }

    public void setLogGen(String logGen) {
        this.logGen = logGen;
    }

    public String getLogDob() {
        return logDob;
    }

    public void setLogDob(String logDob) {
        this.logDob = logDob;
    }

    public String getLogPassword() {
        return logPassword;
    }

    public void setLogPassword(String logPassword) {
        this.logPassword = logPassword;
    }

    public String getLogMob() {
        return logMob;
    }

    public void setLogMob(String logMob) {
        this.logMob = logMob;
    }

    public String getLogFacbook() {
        return logFacbook;
    }

    public void setLogFacbook(String logFacbook) {
        this.logFacbook = logFacbook;
    }

    public String getLogTwitter() {
        return logTwitter;
    }

    public void setLogTwitter(String logTwitter) {
        this.logTwitter = logTwitter;
    }

    public String getLogBio() {
        return logBio;
    }

    public void setLogBio(String logBio) {
        this.logBio = logBio;
    }

    public String getLogLoc() {
        return logLoc;
    }

    public void setLogLoc(String logLoc) {
        this.logLoc = logLoc;
    }

    public String getLogTag() {
        return logTag;
    }

    public void setLogTag(String logTag) {
        this.logTag = logTag;
    }

    public String getLogLastLogin() {
        return logLastLogin;
    }

    public void setLogLastLogin(String logLastLogin) {
        this.logLastLogin = logLastLogin;
    }

    public String getLogCreated() {
        return logCreated;
    }

    public void setLogCreated(String logCreated) {
        this.logCreated = logCreated;
    }

    public String getLogPics() {
        return logPics;
    }

    public void setLogPics(String logPics) {
        this.logPics = logPics;
    }

    public String getLogRoom() {
        return logRoom;
    }

    public void setLogRoom(String logRoom) {
        this.logRoom = logRoom;
    }

    public String getLogCoverPhoto() {
        return logCoverPhoto;
    }

    public void setLogCoverPhoto(String logCoverPhoto) {
        this.logCoverPhoto = logCoverPhoto;
    }

    public String getLogDeviceToken() {
        return logDeviceToken;
    }

    public void setLogDeviceToken(String logDeviceToken) {
        this.logDeviceToken = logDeviceToken;
    }

    public String getLogEntertainment() {
        return logEntertainment;
    }

    public void setLogEntertainment(String logEntertainment) {
        this.logEntertainment = logEntertainment;
    }

    public String getLogSports() {
        return logSports;
    }

    public void setLogSports(String logSports) {
        this.logSports = logSports;
    }

    public String getLogTravelling() {
        return logTravelling;
    }

    public void setLogTravelling(String logTravelling) {
        this.logTravelling = logTravelling;
    }

    public String getLogStudy() {
        return logStudy;
    }

    public void setLogStudy(String logStudy) {
        this.logStudy = logStudy;
    }

    public String getLogGaming() {
        return logGaming;
    }

    public void setLogGaming(String logGaming) {
        this.logGaming = logGaming;
    }

    public String getLogTechnology() {
        return logTechnology;
    }

    public void setLogTechnology(String logTechnology) {
        this.logTechnology = logTechnology;
    }

    public String getLogAction() {
        return logAction;
    }

    public void setLogAction(String logAction) {
        this.logAction = logAction;
    }

    public String getLogEverything() {
        return logEverything;
    }

    public void setLogEverything(String logEverything) {
        this.logEverything = logEverything;
    }

    public String getLogFamily() {
        return logFamily;
    }

    public void setLogFamily(String logFamily) {
        this.logFamily = logFamily;
    }

    public String getLogAns1() {
        return logAns1;
    }

    public void setLogAns1(String logAns1) {
        this.logAns1 = logAns1;
    }

    public String getLogAns2() {
        return logAns2;
    }

    public void setLogAns2(String logAns2) {
        this.logAns2 = logAns2;
    }

    public String getLogAns3() {
        return logAns3;
    }

    public void setLogAns3(String logAns3) {
        this.logAns3 = logAns3;
    }

    public String getLogAns4() {
        return logAns4;
    }

    public void setLogAns4(String logAns4) {
        this.logAns4 = logAns4;
    }

    public String getLogAns5() {
        return logAns5;
    }

    public void setLogAns5(String logAns5) {
        this.logAns5 = logAns5;
    }

    public String getLogMessage() {
        return logMessage;
    }

    public void setLogMessage(String logMessage) {
        this.logMessage = logMessage;
    }

    public String getLogStatus() {
        return logStatus;
    }

    public void setLogStatus(String logStatus) {
        this.logStatus = logStatus;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getLogVideocallStatus() {
        return logVideocallStatus;
    }

    public void setLogVideocallStatus(String logVideocallStatus) {
        this.logVideocallStatus = logVideocallStatus;
    }

    public String getLogOsType() {
        return logOsType;
    }

    public void setLogOsType(String logOsType) {
        this.logOsType = logOsType;
    }

    public String getLogGeoLocation() {
        return logGeoLocation;
    }

    public void setLogGeoLocation(String logGeoLocation) {
        this.logGeoLocation = logGeoLocation;
    }
}