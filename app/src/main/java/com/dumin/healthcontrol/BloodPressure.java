package com.dumin.healthcontrol;

import java.sql.Time;

/**
 * Class for storing values of blood pressure and pulse
 */

public class BloodPressure extends Entry {

    private int systolicPressure;
    private int diastolicPressure;
    private int pulse;

    public BloodPressure(Time time, int sPressure, int dPressure, int pulse){
        super(time);
        systolicPressure = sPressure;
        diastolicPressure = dPressure;
        this.pulse = pulse;
    }
    // Добавить setTime(), setPressure()
    public void setData(Time time, int sPressure, int dPressure, int pulse) {
        super.setData(time);
        systolicPressure = sPressure;
        diastolicPressure = dPressure;
        this.pulse = pulse;
    }

    @Override
    public Time getTime() {
        return super.getTime();
    }

    public int getSystolicPressure() {
        return systolicPressure;
    }

    public int getDiastolicPressure() {
        return diastolicPressure;
    }

    public int getPulse() {
        return pulse;
    }
}
