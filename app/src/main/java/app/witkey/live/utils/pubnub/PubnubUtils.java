package app.witkey.live.utils.pubnub;

import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;

import app.witkey.live.utils.sharedpreferences.UserSharedPreference;

/**
 * created by developer on 9/27/2017.
 */

public class PubnubUtils {

    public static PubNub pubnub = null;

    public static PubNub getPubNubInstance() {
        if (pubnub == null) {
            pubnub = new PubNub(getConfigurations());
        }
        return pubnub;
    }


    private static PNConfiguration getConfigurations() {
        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey(UserSharedPreference.readUserPubNubSubKey());
        pnConfiguration.setPublishKey(UserSharedPreference.readUserPubNubKey());

//        pnConfiguration.setSubscribeKey(Constants.PUBNUB_SUBS_KEY);
//        pnConfiguration.setPublishKey(Constants.PUBNUB_PUBLISH_KEY);
        pnConfiguration.setSecure(false);
        return pnConfiguration;
    }

}
