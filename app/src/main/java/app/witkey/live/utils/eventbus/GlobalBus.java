package app.witkey.live.utils.eventbus;

import org.greenrobot.eventbus.EventBus;

/**
 * created by developer on 10/03/2017.
 */

public class GlobalBus {
    private static EventBus sBus;

    public static EventBus getBus() {
        if (sBus == null)
            sBus = EventBus.getDefault();
        return sBus;
    }
}
