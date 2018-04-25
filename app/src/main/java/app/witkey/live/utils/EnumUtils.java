package app.witkey.live.utils;


import android.support.v4.app.Fragment;
import android.util.Log;

import app.witkey.live.Constants;
import app.witkey.live.R;
import app.witkey.live.activities.GoLiveActivity;
import app.witkey.live.fragments.dashboard.stream.GoLiveStartPageFragment;
import app.witkey.live.fragments.dashboard.stream.GoLiveStreamerStartFragment;
import app.witkey.live.stream.GoLiveStreamActivity;

/**
 * created by developer on 8/10/2015.
 */
public class EnumUtils {

    public static enum AppDomain {
        LIVE, QA, DEV
    }

    public static class HomeScreenType {
        public static final String FEATURED = "0";
        public static final String HOTTEST = "1";
        public static final String NEW = "2";
        public static final String GODDESS = "3";
        public static final String MACHO = "4";
    }

    public static class ConversationType {
        public static final int CHAT_MESSAGE = 0;
        public static final int LIVE_STATUS = 1;
        public static final int GIFT_SENT = 2;
        public static final int FIRST_MESSAGE = 3;
        public static final int LIVE_STATUS_REMOVE = 4;

        //PRIVATE
        public static final int CHAT_MESSAGE_ME = 0;
        public static final int CHAT_MESSAGE_OTHER = 1;
    }

    public static class Notification {//20: allow, 10: not allow
        public static final int ALLOW = 20;
        public static final int DISALLOW = 10;
    }

    public static class UserLevelTypes {
        public static final int EARTH_FROM = 1;
        public static final int EARTH_TO = 9;

        public static final int FIRE_FROM = 10;
        public static final int FIRE_TO = 29;

        public static final int WATER_FROM = 30;
        public static final int WATER_TO = 49;

        public static final int WOOD_FROM = 50;
        public static final int WOOD_TO = 99;

        public static final int GOLD_FROM = 100;
        public static final int GOLD_TO = 149;

        public static final int PRESTIGE_FROM = 150;
        public static final int PRESTIGE_TO = 199;

        public static final int VIP_FROM = 200;
        public static final int VIP_TO = 500;

        public static final String TYPE_EARTH = "EARTH";
        public static final String TYPE_FIRE = "FIRE";
        public static final String TYPE_WATER = "WATER";
        public static final String TYPE_WOOD = "WOOD";
        public static final String TYPE_GOLD = "GOLD";
        public static final String TYPE_PRESTIGE = "PRESTIGE";
        public static final String TYPE_VIP = "VIP";

    }

    public static class BroadcasterLevelTypes {
        public static final int PEAR_FROM = 1;
        public static final int PEAR_TO = 29;

        public static final int AMETHYST_FROM = 30;
        public static final int AMETHYST_TO = 59;

        public static final int RUBY_FROM = 60;
        public static final int RUBY_TO = 89;

        public static final int SAPPHIRE_FROM = 90;
        public static final int SAPPHIRE_TO = 119;

        public static final int EMERALD_FROM = 120;
        public static final int EMERALD_TO = 149;

        public static final int DIAMOND_FROM = 150;
        public static final int DIAMOND_TO = 189;

        public static final int STAR_DIAMOND_FROM = 190;
        public static final int STAR_DIAMOND_TO = 220;

        public static final String TYPE_PEAR = "PEAR";
        public static final String TYPE_AMETHYST = "AMETHYST";
        public static final String TYPE_RUBY = "RUBY";
        public static final String TYPE_SAPPHIRE = "SAPPHIRE";
        public static final String TYPE_EMERALD = "EMERALD";
        public static final String TYPE_DIAMOND = "DIAMOND";
        public static final String TYPE_STAR_DIAMOND = "STAR DIAMOND";

    }

    public static class FullScreenAnimationType {
        public static final int HEART_ANIMAION = 1;
    }

    public static class IsFollow {
        public static final String FOLLOWING = "1";
        public static final String NOT_FOLLOWING = "0";
    }

    public static class AboutUs_Type {
        public static final String PRIVACY_POLICY = "1";
        public static final String USER_AGREEMENT = "2";
        public static final String ABOUT_OUR_COMPANY = "3";
        public static final String COMMUNITY_CONVENTION = "4";
    }

    public static enum SeasonType {
        AUTUMN(0), SUMMER(1), WINTER(2), SPRING(3), Other(4);

        private int numVal;

        SeasonType(int numVal) {
            this.numVal = numVal;
        }

        public int getNumVal() {
            return numVal;
        }

        public static String getLabel(int position) {
            switch (position) {
                case 0:
                    return "Autumn";
                case 1:
                    return "Summer";
                case 2:
                    return "Winter";
                case 3:
                    return "Spring";
                case 4:
                    return "Other";
                default:
                    return "Other";
            }
        }

        public static SeasonType getSeason(int type) {

            if (type == 0) {
                return AUTUMN;
            } else if (type == 1) {
                return SUMMER;
            } else if (type == 2) {
                return WINTER;
            } else if (type == 3) {
                return SPRING;
            } else if (type == 4) {
                return Other;
            }
            return Other;
        }

        public static int getSeasonIndex(String strTomatch) {

            if (strTomatch.equals("Autumn")) {
                return 0;
            } else if (strTomatch.equals("Summer")) {
                return 1;
            } else if (strTomatch.equals("Winter")) {
                return 2;
            } else if (strTomatch.equals("Spring")) {
                return 3;
            } else if (strTomatch.equals("Other")) {
                return 4;
            }

            return 4;
        }
    }

    public static enum GiftID {
        LIPS(0), HAND(1), SHAMPAIN(2), STAR(3), TROPHY(4), GUITAR(5), RING(6), CAR(7),
        lollipop(8), popcorn(9), hotairballoon(10), gold_coins(11), piano(12), goldaward(13),
        lightsticks(14), icecream(15);

        private int numVal;

        GiftID(int numVal) {
            this.numVal = numVal;
        }

        public int getNumVal() {
            return numVal;
        }

        public static GiftID getGift(int type) {

            if (type == 0) {
                return LIPS;
            } else if (type == 1) {
                return HAND;
            } else if (type == 2) {
                return SHAMPAIN;
            } else if (type == 3) {
                return STAR;
            } else if (type == 4) {
                return TROPHY;
            } else if (type == 5) {
                return GUITAR;
            } else if (type == 6) {
                return RING;
            } else if (type == 7) {
                return CAR;
            } else if (type == 8) {
                return lollipop;
            } else if (type == 9) {
                return popcorn;
            } else if (type == 10) {
                return hotairballoon;
            } else if (type == 11) {
                return gold_coins;
            } else if (type == 12) {
                return piano;
            } else if (type == 17) {
                return goldaward;
            } else if (type == 14) {
                return lightsticks;
            } else if (type == 15) {
                return icecream;
            }
            return LIPS;
        }
    }

    public static enum ServiceName {
        ExternalService,
        AuthenticateUser,
        register,
        login,
        streams,
        streams_create,
        end_stream,
        all_streams,
        user,
        streams_id,
        user_streams,
        forgot_password,
        change_password,
        streamsactions,
        follow_user,
        unfollow_user,
        artist,
        my_artist_streams,
        cms_pages,
        app_setting,
        add_gift,
        add_transaction,
        add_moments,
        all_moments,
        moment_action,
        add_comment,
        all_comment,
        all_like,
        my_artists_moment,
        top_broadcaster,
        top_spender,
        top_fan,
        get_packages,
        my_fan,
        get_blocked_user,
        block_user,
        unblock_user,
        get_banner,
        add_chat,
        get_chat,
        download_gifts,
        social_login,
        feed_back,
        get_notifications,
        allow_notification,
        is_friend,
        add_promotions_transaction,
        artist_stream,
        withdraw_wd,
        logout
    }

    public static enum RequestMethod {
        GET, POST
    }

    public static enum Authentications {
        TWITTER(0), FACEBOOK(1), GMAIL(2);

        private int numVal;

        Authentications(int numVal) {
            this.numVal = numVal;
        }

        public int getNumVal() {
            return numVal;
        }


        public static Authentications getAuthentication(int type) {

            if (type == 0) {
                return TWITTER;
            } else if (type == 1) {
                return FACEBOOK;
            } else if (type == 2) {
                return GMAIL;
            }
            return TWITTER;
        }
    }

    public static enum ServiceResponseMessage {
        InvalidResponse, NetworkError, ServerNotReachable, ConnectionTimeOut
    }

    public static class FRIEND_STATUSES {
        public static final String NON_FRIEND = "30";
        public static final String REQUEST_PENDING = "0";
        public static final String FRIEND = "10";
    }

    public static class FOLLOW_STATUSES {
        public static final String NOT_FOLLOW = "30";
        public static final String FOLLOWED = "20";
    }

    public static class NOTIFICATION_TYPES {
        public static final String TYPE_FOLLOW = "11";
        public static final String TYPE_STREAM_CREATED = "22";
    }

    public static class LIKE_DISLIKE_STATUSES {
        public static final String TYPE_BLOCK = "0";
        public static final String TYPE_REPORT = "10";
        public static final String TYPE_FAVORITE = "20";
        public static final String TYPE_WATCH_LATER = "30";
        public static final String TYPE_SAVE = "40";
        public static final String SHARE = "50";
        public static final String LIKE = "60";
        public static final String DISLIKE = "70";
        public static final String TYPE_VIEW = "80";
        public static final String INVITE_FRIEND = "33";
    }

    public static class GiftLevel {
        public static final int ENTRY_LEVEL = 1;
        public static final int JUNIOR_LEVEL = 2;
        public static final int MIDDLE_LEVEL = 3;
        public static final int HIGH_LEVEL = 4;
        public static final int PRO_LEVEL = 5;
        public static final int UPGRADED_LEVEL = 6;
        public static final int VIP_LEVEL = 7;
        public static final int MISC_LEVEL = 8;

        public static final int GIFT_STATUS_LOCK = 0;
        public static final int GIFT_STATUS_UNLOCK = 1;

        public static final String GIFT_UPDATE_STATUS_VERSION = "0.1";

    }

    public static class GiftResById {

        public static int getGiftRes(int giftID) {
            switch (giftID) {
                case 1000:
                    return R.drawable.lollipop;
                case 1001:
                    return R.drawable.icecream;
                case 1002:
                    return R.drawable.popcorn;
                case 1003:
                    return R.drawable.rose;
                case 1004:
                    return R.drawable.lips;
                case 1005:
                    return R.drawable.hearts;
                case 1006:
                    return R.drawable.hand;
                case 1007:
                    return R.drawable.skateboard;
                case 1008:
                    return R.drawable.champagne;
                case 1009:
                    return R.drawable.headphone;
                case 1010:
                    return R.drawable.hotairballoon;
                case 1011:
                    return R.drawable.guiter;
                case 1012:
                    return R.drawable.gold_coins;
                case 1013:
                    return R.drawable.gold_ring;
                case 1014:
                    return R.drawable.stars;
                case 1015:
                    return R.drawable.goldbar;
                case 1016:
                    return R.drawable.trophy;
                case 1017:
                    return R.drawable.treehouse;
                case 1018:
                    return R.drawable.piano;
                case 1019:
                    return R.drawable.car;
                case 1020:
                    return R.drawable.goldaward;
                case 1021:
                    return R.drawable.snowglobe;
                case 1022:
                    return R.drawable.lightsticks;
                default:
                    return R.drawable.lollipop;
            }
        }

        public static int getCandyRes(int candyID) {
            switch (candyID) {
                case 1:
                    return R.drawable.car;
                case 2:
                    return R.drawable.car;
                case 3:
                    return R.drawable.car;
                case 4:
                    return R.drawable.car;
                case 5:
                    return R.drawable.car;
                case 6:
                    return R.drawable.car;
                case 7:
                    return R.drawable.car;
                case 8:
                    return R.drawable.car;
                case 9:
                    return R.drawable.car;
                case 10:
                    return R.drawable.car;
                case 11:
                    return R.drawable.car;
                default:
                    return R.drawable.car;
            }
        }
    }

    public static Fragment getCurrentGoLiveFlow() {
        Fragment fragment = null;

        if (Constants.CURRENT_GO_LIVE_WITH == Constants.GO_LIVE_WITH_WOWZA) {
            fragment = new GoLiveStartPageFragment();
        } else if (Constants.CURRENT_GO_LIVE_WITH == Constants.GO_LIVE_WITH_KSY) {
            fragment = new GoLiveStreamerStartFragment();
        } else if (Constants.CURRENT_GO_LIVE_WITH == Constants.GO_LIVE_WITH_TOK) {
            //URL: rtc
//            fragment = new GoLiveTokStartFragment();

        }

        Log.d("JKS", fragment.getClass().getSimpleName());

        return fragment;
    }

    public static Class getCurrentGoLiveViewType() {
        Class currentClass = null;

        if (Constants.CURRENT_GO_LIVE_WITH == Constants.GO_LIVE_WITH_WOWZA) {
            currentClass = GoLiveActivity.class;
        } else if (Constants.CURRENT_GO_LIVE_WITH == Constants.GO_LIVE_WITH_KSY) {
            currentClass = GoLiveStreamActivity.class;
        } else if (Constants.CURRENT_GO_LIVE_WITH == Constants.GO_LIVE_WITH_TOK) {
            //URL: rtc
//            currentClass = GoLiveTokActivity.class;
        }

        Log.d("JKS", currentClass.getSimpleName());
        return currentClass;
    }

}
