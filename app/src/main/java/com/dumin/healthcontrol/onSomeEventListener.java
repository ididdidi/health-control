package com.dumin.healthcontrol;

/**
 * Created by operator on 04.01.2018.
 */

public interface onSomeEventListener {
    void someEvent(boolean update);     // Sends a signal about the successful adding entry
    long getLongTime();                 // Stealing time from the Activity
}
