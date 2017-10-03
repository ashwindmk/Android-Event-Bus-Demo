package com.example.ashwin.eventbusdemo;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by ashwin on 3/10/17.
 */

public class GlobalBus {
    private static EventBus sBus;
    public static EventBus getBus() {
        if (sBus == null) {
            sBus = EventBus.getDefault();
        }
        return sBus;
    }

}
