package com.dumin.healthcontrol;

import java.sql.Time;

/**
 * Class for storing values of blood pressure and pulse
 */

public class BloodPressure extends Entry {

    private int systolicPressure;
    private int diastolicPressure;
    private int pulse;

    public BloodPressure(Long longtime, int sPressure, int dPressure, int pulse){
        super(longtime);
        systolicPressure = sPressure;
        diastolicPressure = dPressure;
        this.pulse = pulse;
    }
    // Добавить setTime(), setPressure()
    public void setData(Long longtime, int sPressure, int dPressure, int pulse) {
        super.setData(longtime);
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
