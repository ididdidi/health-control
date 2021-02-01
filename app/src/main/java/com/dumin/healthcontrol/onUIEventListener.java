package com.dumin.healthcontrol;

/**
 * Feedback to Activity
 */

public interface onUIEventListener {
    void someEvent(boolean update);     // Sends a signal about the successful adding entry

    long getTimeInSeconds();            // Stealing time from the Activity

    int getOverallHealth();             // Stealing value overall health
}
