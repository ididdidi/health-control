package com.dumin.healthcontrol;

import java.sql.Time;

/**
 * Abstract class for entry retention
 */

public abstract class Entry {
    private Time time;

    protected Entry(Time time){
        this.time = time;
    }
    protected Entry(Long longTime){
        this.time = new Time(longTime);
    }

    public void setData(Long longtime) {
        this.time = new Time(longtime);
    }
    public Time getTime(){
        return time;
    }
}
