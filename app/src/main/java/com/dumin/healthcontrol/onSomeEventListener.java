package com.dumin.healthcontrol;

/**
 * Feedback to Activity
 */

public interface onSomeEventListener {
    void someEvent(boolean update);     // Sends a signal about the successful adding entry
    long getLongTime();                 // Stealing time from the Activity
}
