package com.prft.cif.test.metadata;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;


/**
 * The Root Schema
 * <p>
 *
 *
 */
//@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "startTime",
        "endTime",
        "timeZone",
        "seconds",
        "minutes",
        "hours",
        "monthDays",
        "month",
        "weekDays",
        "year"
})
public class CIFSchedule implements Serializable {

    /**
     * The Starttime Schema
     * <p>
     *
     * (Required)
     *
     */
    @JsonProperty("startTime")
    private String startTime = "";
    /**
     * The Endtime Schema
     * <p>
     *
     * (Required)
     *
     */
    @JsonProperty("endTime")
    private String endTime = "";
    /**
     * The Timezone Schema
     * <p>
     *
     * (Required)
     *
     */
    @JsonProperty("timeZone")
    private String timeZone = "America/New_York";
    /**
     * The Seconds Schema
     * <p>
     *
     * (Required)
     *
     */
    @JsonProperty("seconds")
    private String seconds = "0";
    /**
     * The Minutes Schema
     * <p>
     *
     * (Required)
     *
     */
    @JsonProperty("minutes")
    private String minutes = "";
    /**
     * The Hours Schema
     * <p>
     *
     * (Required)
     *
     */
    @JsonProperty("hours")
    private String hours = "";
    /**
     * The Monthdays Schema
     * <p>
     *
     * (Required)
     *
     */
    @JsonProperty("monthDays")
    private String monthDays = "";
    /**
     * The Month Schema
     * <p>
     *
     * (Required)
     *
     */
    @JsonProperty("month")
    private String month = "";
    /**
     * The Weekdays Schema
     * <p>
     *
     * (Required)
     *
     */
    @JsonProperty("weekDays")
    private String weekDays = "";
    /**
     * The Year Schema
     * <p>
     *
     * (Required)
     *
     */
    @JsonProperty("year")
    private String year = "";

    /**
     * No args constructor for use in serialization
     *
     */
    public CIFSchedule() {
    }

    /**
     *
     * @param startTime
     * @param monthDays
     * @param minutes
     * @param seconds
     * @param hours
     * @param month
     * @param year
     * @param timeZone
     * @param endTime
     * @param weekDays
     */
    public CIFSchedule(String startTime, String endTime, String timeZone, String seconds, String minutes, String hours, String monthDays, String month, String weekDays, String year) {
        //super();
        this.startTime = startTime;
        this.endTime = endTime;
        this.timeZone = timeZone;
        this.seconds = seconds;
        this.minutes = minutes;
        this.hours = hours;
        this.monthDays = monthDays;
        this.month = month;
        this.weekDays = weekDays;
        this.year = year;
    }

    /**
     * The Starttime Schema
     * <p>
     *
     * (Required)
     *
     */
    @JsonProperty("startTime")
    public String getStartTime() {
        return startTime;
    }

    /**
     * The Starttime Schema
     * <p>
     *
     * (Required)
     *
     */
    @JsonProperty("startTime")
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * The Endtime Schema
     * <p>
     *
     * (Required)
     *
     */
    @JsonProperty("endTime")
    public String getEndTime() {
        return endTime;
    }

    /**
     * The Endtime Schema
     * <p>
     *
     * (Required)
     *
     */
    @JsonProperty("endTime")
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     * The Timezone Schema
     * <p>
     *
     * (Required)
     *
     */
    @JsonProperty("timeZone")
    public String getTimeZone() {
        return timeZone;
    }

    /**
     * The Timezone Schema
     * <p>
     *
     * (Required)
     *
     */
    @JsonProperty("timeZone")
    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    /**
     * The Seconds Schema
     * <p>
     *
     * (Required)
     *
     */
    @JsonProperty("seconds")
    public String getSeconds() {
        return seconds;
    }

    /**
     * The Seconds Schema
     * <p>
     *
     * (Required)
     *
     */
    @JsonProperty("seconds")
    public void setSeconds(String seconds) {
        this.seconds = seconds;
    }

    /**
     * The Minutes Schema
     * <p>
     *
     * (Required)
     *
     */
    @JsonProperty("minutes")
    public String getMinutes() {
        return minutes;
    }

    /**
     * The Minutes Schema
     * <p>
     *
     * (Required)
     *
     */
    @JsonProperty("minutes")
    public void setMinutes(String minutes) {
        this.minutes = minutes;
    }

    /**
     * The Hours Schema
     * <p>
     *
     * (Required)
     *
     */
    @JsonProperty("hours")
    public String getHours() {
        return hours;
    }

    /**
     * The Hours Schema
     * <p>
     *
     * (Required)
     *
     */
    @JsonProperty("hours")
    public void setHours(String hours) {
        this.hours = hours;
    }

    /**
     * The Monthdays Schema
     * <p>
     *
     * (Required)
     *
     */
    @JsonProperty("monthDays")
    public String getMonthDays() {
        return monthDays;
    }

    /**
     * The Monthdays Schema
     * <p>
     *
     * (Required)
     *
     */
    @JsonProperty("monthDays")
    public void setMonthDays(String monthDays) {
        this.monthDays = monthDays;
    }

    /**
     * The Month Schema
     * <p>
     *
     * (Required)
     *
     */
    @JsonProperty("month")
    public String getMonth() {
        return month;
    }

    /**
     * The Month Schema
     * <p>
     *
     * (Required)
     *
     */
    @JsonProperty("month")
    public void setMonth(String month) {
        this.month = month;
    }

    /**
     * The Weekdays Schema
     * <p>
     *
     * (Required)
     *
     */
    @JsonProperty("weekDays")
    public String getWeekDays() {
        return weekDays;
    }

    /**
     * The Weekdays Schema
     * <p>
     *
     * (Required)
     *
     */
    @JsonProperty("weekDays")
    public void setWeekDays(String weekDays) {
        this.weekDays = weekDays;
    }

    /**
     * The Year Schema
     * <p>
     *
     * (Required)
     *
     */
    @JsonProperty("year")
    public String getYear() {
        return year;
    }

    /**
     * The Year Schema
     * <p>
     *
     * (Required)
     *
     */
    @JsonProperty("year")
    public void setYear(String year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "{" +
                "startTime:" + startTime +
                ", endTime=" + endTime  +
                ", timeZone=" + timeZone  +
                ", seconds=" + seconds +
                ", minutes=" + minutes +
                ", hours=" + hours +
                ", month=" + month +
                ", monthDays=" + monthDays +
                ", weekDays=" + weekDays +
                ", year=" + year  +
                '}';
    }

    @JsonIgnore
    public String toJSONSchema() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json  = mapper.writeValueAsString(this);
        return json;
    }

    @JsonIgnore
    public String getOozieFrequency(){
        return minutes+" "+hours+" "+monthDays+" "+month+" "+weekDays;
    }

}